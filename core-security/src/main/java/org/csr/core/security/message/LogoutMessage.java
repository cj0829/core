package org.csr.core.security.message;

import org.csr.core.UserSession;
import org.csr.core.queue.Message;

/**
 * ClassName:LogoutMessage.java <br/>
 * System Name：    csr <br/>
 * Date:     2014-2-20下午3:15:23 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述： 用户登出消息
 */
public class LogoutMessage implements Message {

	UserSession log;
	public LogoutMessage(UserSession log) {
		this.log=log;
	}

	@Override
	public UserSession body() {
		return log;
	}

}
