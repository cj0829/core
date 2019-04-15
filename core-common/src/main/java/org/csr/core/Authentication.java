package org.csr.core;

import java.io.Serializable;
import java.security.Principal;

/**
 * 认证对象,只需要保存,证书,和登录名,<br>
 * 权限需要作为实时查询,这样才能确保,用户的安全性.<br>
 * 作为,权限的缓存,则需要坐在服务中.<br>
 * 
 * @author caijin
 * 
 */
public interface Authentication extends Principal, Serializable {
	// ~ Methods
	// ========================================================================================================

	/**
	 * 用户
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	UserSession getUserSession();

	/**
	 * 用户登录名
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Object getPrincipal();
	/**
	 * 系统配置
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Object getSystemConfig();

	/**
	 * 请求配置
	 * @return
	 */
	Object getRequestConfig();

	/**
	 * 响应配置
	 * @return
	 */
	Object getResponseResult();
	
	/**
	 * sessin配置
	 * @return
	 */
	Object getSessionConfig();

}
