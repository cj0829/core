package org.csr.core.persistence.message;

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
 * 功能描述：操作消息
 */
public class OperLogMessage implements Message<UserSession> {

	UserSession log;
	Byte logType;
//	String oldContent;
//	String newContent;
	
	public OperLogMessage(UserSession log) {
		this.log=log;
	}

	@Override
	public UserSession body() {
		return log;
	}

	public Byte getLogType() {
		return logType;
	}

	public void setLogType(Byte logType) {
		this.logType = logType;
	}

//	public String getOldContent() {
//		return oldContent;
//	}
//
//	public void setOldContent(String oldContent) {
//		this.oldContent = oldContent;
//	}
//
//	public String getNewContent() {
//		return newContent;
//	}
//
//	public void setNewContent(String newContent) {
//		this.newContent = newContent;
//	}


}
