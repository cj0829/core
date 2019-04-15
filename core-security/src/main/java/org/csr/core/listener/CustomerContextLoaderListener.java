package org.csr.core.listener;

import javax.servlet.ServletContextEvent;

import org.csr.core.cloudsession.CloudSessionManager;
import org.springframework.web.context.ContextLoaderListener;

/**
 * ClassName:CustomerContextLoaderListener.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:19:58 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class CustomerContextLoaderListener extends ContextLoaderListener {
	public CustomerContextLoaderListener() {
		CloudSessionManager.getInstance().addApplicationEventListener(this);
	}
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		//注册新监听
		System.out.println("注册新的");
	}

}
