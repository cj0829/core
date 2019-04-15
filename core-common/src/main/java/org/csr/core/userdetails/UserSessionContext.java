package org.csr.core.userdetails;

import org.csr.core.Authentication;
import org.csr.core.UserSession;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.util.ObjUtil;


/**
 * 用户会话的个人资料信息.
 * @author caijin
 *
 */

public class UserSessionContext{
	/**
	 * getUserSession: 获取UserSession <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public static UserSessionBasics getUserSession(){
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		if(ObjUtil.isNotEmpty(authentication)){
			UserSession us = authentication.getUserSession();
			if(ObjUtil.isNotEmpty(us)){
				return (UserSessionBasics) us;
			}
		}
		return null;
	}
}
