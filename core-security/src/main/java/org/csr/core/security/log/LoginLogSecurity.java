/**
 * Project Name:core
 * File Name:LoginLogSecurity.java
 * Package Name:org.csr.core.security.log
 * Date:2014-2-20下午3:32:36
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.security.log;

import org.csr.core.UserSession;

/**
 * ClassName:LoginLogSecurity.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014-2-20下午3:32:36 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface LoginLogSecurity {
	/**
	 * loginOperation: 返回登录日志的id <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Long saveLoginOperation(UserSession userSession);
}

