/**
 * Project Name:core
 * File Name:SecurityContextFactory.java
 * Package Name:org.csr.core.security.context
 * Date:2014-2-21上午10:00:19
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.context;

/**
 * ClassName:SecurityContextFactory.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014-2-21上午10:00:19 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class SecurityContextFactory {

	private static SecurityContextRepository securityContextRepository;

	synchronized public static SecurityContextRepository getInstance() {
		if (securityContextRepository == null) {
			securityContextRepository = new HttpSessionSecurityContextRepository();
		}
		return securityContextRepository;
	}
}
