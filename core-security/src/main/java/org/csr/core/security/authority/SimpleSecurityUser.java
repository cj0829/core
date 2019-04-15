package org.csr.core.security.authority;

import org.csr.core.userdetails.SecurityUser;
import org.csr.core.util.Md5PwdEncoder;

/**
 * 系统提供的默认安全用户,登录名,密码为admin
 * 
 * @author caijin
 *
 */
public class SimpleSecurityUser implements SecurityUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3392850933827662580L;
	//
	final String username="admin";
	public SimpleSecurityUser(){
	}
	@Override
	public Long getId() {
		return 0l;
	}

	@Override
	public String getLoginName() {
		return Md5PwdEncoder.encodePassword(username);
	}

	@Override
	public String getPassword() {
		return Md5PwdEncoder.encodePassword(username);
	}
	
	@Override
	public Long getPrimaryOrgId() {
		
		return 0l;
	}
	@Override
	public Long getRoot() {
		
		// Auto-generated method stub
		return null;
	}

}
