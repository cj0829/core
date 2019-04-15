package org.csr.core.listener;
 
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.csr.core.Constants;
import org.csr.core.cloudsession.CloudSessionManager;
 
/**
 * ClassName:ServletContextLoaderListener.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午4:39:27 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class ServletContextLoaderListener implements ServletContextListener {
	public ServletContextLoaderListener() {
		CloudSessionManager.getInstance().addApplicationEventListener(this);
	}
	

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("ServletContextEvent 开始");
		//注册新监听
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("结束==");
		servletContextEvent.getServletContext().removeAttribute(Constants.SECURITY_CONTEXT_KEY);
	}


}
