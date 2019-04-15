package org.csr.core.security.authority;

import org.csr.core.HttpAuthentication;

public class UsernamePasswordToken extends HttpAuthentication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Object principal;
	
	
	public UsernamePasswordToken(Object principal) {
		this.principal = principal;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getSystemConfig() {
		return new Object();
	}

	@Override
	public Object getRequestConfig() {
		return new Object();
	}

	@Override
	public Object getResponseResult() {
		return new Object();
	}

	@Override
	public Object getSessionConfig() {
		return new Object();
	}

}
