/**
 * Project Name:core
 * File Name:IpUtil.java
 * Package Name:org.csr.core.util
 * Date:2016年7月21日上午11:56:07
 * Copyright (c) 2016, 版权所有 ,All rights reserved 
 */

package org.csr.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: IpUtil.java <br/>
 * System Name： 在线学习系统<br/>
 * Date: 2016年7月21日上午11:56:07 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public abstract class IpUtil {
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

}
