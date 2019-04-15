package org.csr.core.security.resource;

import java.util.List;

import org.csr.core.SecurityResource;
import org.csr.core.UserSession;

/**
 * 
 * @author caijin
 * 
 */
public interface SecurityService {

	/**
	 * 查询权限
	 * @param user
	 * @return
	 */
	List<? extends SecurityResource> findResourcesByUser(UserSession user);
	
	
	/**
	 * findResourcesByDefault: 查询默认权限 <br/>
	 * @author caijin
	 * @param user
	 * @return
	 * @since JDK 1.7
	 */
	List<? extends SecurityResource> findResourcesByDefault();
	
	
	/**
	 * 查询匿名访问的权限
	 * @param user
	 * @return
	 */
	List<? extends SecurityResource> findResourcesByAnonymous(UserSession user);
}
