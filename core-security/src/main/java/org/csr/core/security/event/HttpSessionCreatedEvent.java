package org.csr.core.security.event;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationEvent;

/**
 * ClassName:HttpSessionCreatedEvent.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:21:22 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class HttpSessionCreatedEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 5028328471771117833L;

	public HttpSessionCreatedEvent(HttpSession httpSession) {
		super(httpSession);
    }
	 
	public HttpSession getSession() {
		return (HttpSession) getSource();
	}
}
