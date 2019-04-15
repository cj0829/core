package org.csr.core.persistence.log;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.csr.core.persistence.business.domain.SysMethodTimeLog;
import org.csr.core.queue.QueueService;
import org.csr.core.util.ObjUtil;

//声明这是一个组件
// 声明这是一个切面Bean
public class ServiceMethodTimeAspect {

	private final static Logger log = Logger.getLogger(ServiceMethodTimeAspect.class);

	private QueueService queueService;

	public ServiceMethodTimeAspect() {
		System.out.println("ServiceAspect()");
	}

	// 声明环绕通知
	@Around("pointCutMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Long begin = System.currentTimeMillis();
		Object obj = pjp.proceed();
		log.info(pjp.getTarget().getClass().getName() + "=" + pjp.getSignature().getName() + "|" + (System.currentTimeMillis() - begin));
		if(ObjUtil.isNotEmpty(queueService)) {
			SysMethodTimeLog sysMethodTimeLog=new SysMethodTimeLog();
			sysMethodTimeLog.setClassName(pjp.getTarget().getClass().getName());
			sysMethodTimeLog.setMethodName(pjp.getSignature().getName());
			Long tm=System.currentTimeMillis() - begin;
			sysMethodTimeLog.setMethodTime(tm.intValue());
			sysMethodTimeLog.setCreateTime(new Date());
			queueService.sendMessage(sysMethodTimeLog);
		}
		
		return obj;
	}

	public QueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(QueueService queueService) {
		this.queueService = queueService;
	}

}