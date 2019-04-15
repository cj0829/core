package org.csr.core;

import java.security.Principal;

/**
 * 认证对象
 * @author caijin
 *
 */
public abstract class HttpAuthentication implements Authentication{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1632733469032747549L;
	protected UserSession userSession;
	
	public void setUserSession(UserSession userSession) {
		this.userSession=userSession;
	}

	@Override
	public UserSession getUserSession() {
		return userSession;
	}
	@Override
	public String getName() {
		if (getPrincipal() instanceof Principal) {
			return ((Principal) getPrincipal()).getName();
		}
		return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
	}
    
}
