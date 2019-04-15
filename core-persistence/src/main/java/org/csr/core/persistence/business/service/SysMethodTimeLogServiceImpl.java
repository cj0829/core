package org.csr.core.persistence.business.service;


import javax.annotation.Resource;

import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.dao.SysMethodTimeLogDao;
import org.csr.core.persistence.business.domain.SysMethodTimeLog;
import org.csr.core.persistence.service.SimpleBasisService;
import org.csr.core.queue.Message;
import org.csr.core.util.ObjUtil;
import org.springframework.stereotype.Service;

/**
 * ClassName:SysMethodTimeLog.java <br/>
 * Date:     Thu Mar 15 12:13:51 CST 2018
 * @author   caijin-ThinkPad-X260 <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 系统方法日志 service实现
 */
@Service("sysMethodTimeLogService")
public class SysMethodTimeLogServiceImpl extends SimpleBasisService<SysMethodTimeLog, Long> implements SysMethodTimeLogService {
    @Resource
    private SysMethodTimeLogDao sysMethodTimeLogDao;
    
    @Override
	public BaseDao<SysMethodTimeLog, Long> getDao() {
		return sysMethodTimeLogDao;
	}

	@Override
	public void processMessages(Message<SysMethodTimeLog> messages) {
		if(ObjUtil.isNotEmpty(messages)&& ObjUtil.isNotEmpty(messages.body())) {
			sysMethodTimeLogDao.save(messages.body());
		}
	}
	
}
