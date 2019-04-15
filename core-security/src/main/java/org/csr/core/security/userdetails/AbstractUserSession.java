/**
 * Project Name:core
 * File Name:aUserSession.java
 * Package Name:org.csr.core.security.userdetails
 * Date:2016年7月21日下午8:19:36
 * Copyright (c) 2016, 版权所有 ,All rights reserved 
*/

package org.csr.core.security.userdetails;

import org.csr.core.UserSession;
import org.csr.core.util.ObjUtil;

/**
 * ClassName: aUserSession.java <br/>
 * System Name：    在线学习系统<br/>
 * Date:     2016年7月21日下午8:19:36 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public abstract class AbstractUserSession implements UserSession{

	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -3055405030470107564L;

	@Override
	public String toString() {
		return ObjUtil.toString(getUserId());
	}
	
	@Override
	public int hashCode() {
		return ObjUtil.toInt(getUserId());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}
	
}

