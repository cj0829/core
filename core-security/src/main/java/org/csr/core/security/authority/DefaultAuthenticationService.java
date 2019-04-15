package org.csr.core.security.authority;

import org.csr.core.UserSession;
import org.csr.core.userdetails.SecurityUser;
import org.csr.core.userdetails.UserSessionBasics;

/**
 * 默认的策略
 * @author caijin
 *
 */
public final class DefaultAuthenticationService implements AuthenticationService{
	@Override
	public SecurityUser findByLoginName(final String username) {
		return new SimpleSecurityUser();
	}

	@Override
	public UserSession setUserSession(SecurityUser suser) {
		return new UserSessionBasics(suser.getLoginName());
	}

}
