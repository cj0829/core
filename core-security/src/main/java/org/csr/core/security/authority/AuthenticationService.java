package org.csr.core.security.authority;

import org.csr.core.UserSession;
import org.csr.core.userdetails.SecurityUser;

/**
 * 认证服务
 * @author caijin
 *
 */
public interface AuthenticationService {

	/**
	 * findByLoginName: 获取安全用户<br>
	 * @author caijin
	 * @param loginName
	 * @return
	 * @since JDK 1.7
	 */
	SecurityUser findByLoginName(String loginName);

	/**
	 * 填充UserSession对象值
	 * @param suser
	 * @return
	 */
	UserSession setUserSession(SecurityUser suser);
}
