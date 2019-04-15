package org.csr.core.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by liuxin on 16/9/10.
 * <p>
 * <p>
 * 1, execution
 * (modifier-pattern?
 * ret-type-pattern declaring-type-pattern?
 * name-pattern(param-pattern) throws-pattern?)
 * pattern分别表示
 * 修饰符匹配（modifier-pattern?）、
 * 返回值匹配（ret-type-pattern）、
 * 类路径匹配（declaring-type-pattern?）、
 * 方法名匹配（name-pattern）、
 * 参数匹配（(param-pattern)）、
 * 异常类型匹配（throws-pattern?），
 * 其中后面跟着“?”的是可选项。
 * <p>
 * 2,  注解
 * @Before         前置通知：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
 * @After          后通知：当某连接点退出的时候执行的通知 （不论是正常返回还是异常退出）。
 * @AfterReturning 返回后通知 ：在某连接点正常完成后执行的通知，不包括抛出异常的情况。
 * @Around         环绕通知 ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为，也可以选择不执行。
 * @AfterThrowing  抛出异常后通知：在方法抛出异常退出时执行的通知。
 */
public class MonitorLogAspect {

    //@Pointcut("execution(* com.qufenqi.controller.TestController.getData(java.lang.Integer)) && args(i)")
    //@Pointcut("execution(* com.qufenqi.controller.TestController..*(..))")
    @Pointcut("@annotation(org.csr.core.log.MonitorLog)")
    public void pointcut() {

    }

    @Before("pointcut() && @annotation(monitorLog)")
    public void doBefore(JoinPoint point, MonitorLog monitorLog) {
        System.out.println("*********startMonitorLog*********");

        String[] param = monitorLog.param();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String[] args = {};
        for (String key : param) {
            Object attribute = session.getAttribute(key);

        }
        System.out.println(monitorLog);
    }

    @AfterThrowing(pointcut = "pointcut() && @annotation(monitorLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, MonitorLog monitorLog, Throwable e) {
        System.out.println("*********捕获异常开始*********");
        e.printStackTrace();
        System.out.println("*********捕获异常结束*********");
    }

    @AfterReturning(value = "pointcut() && @annotation(monitorLog)", returning = "returnValue")
    public void doAfterReturning(JoinPoint jp, MonitorLog monitorLog, Object returnValue) {
        System.out.println("*********endMonitorLog*********");

    }

}