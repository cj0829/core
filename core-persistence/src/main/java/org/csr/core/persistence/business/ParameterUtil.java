/**
 * Project Name:common
 * File Name:Paramen.java
 * Package Name:org.csr.core.support
 * Date:2014-3-5下午11:14:19
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.business;

import java.util.HashMap;
import java.util.Map;

import org.csr.core.persistence.business.service.OrganizationParameterService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ObjUtil;

/**
 * ClassName: Paramen.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-3-5下午11:14:19 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class ParameterUtil {
	private static Map<String, String> paramObjectMap = new HashMap<String, String>();
	
	/**
	 * getParamValue: 获取系统参数。 <br/>
	 * 先查询机构参数，如果机构参数没有值，那么直接去查询系统参数
	 * @author caijin
	 * @param name
	 * @return 
	 * @since JDK 1.7
	 */
	public static String getParamValue(Long root,String name){
		String value=paramObjectMap.get(name);
		if(!ObjUtil.isNotBlank(value)){
			OrganizationParameterService paramService=((OrganizationParameterService) ClassBeanFactory.getBean("organizationParameterService"));
			if(ObjUtil.isNotEmpty(root)){
				value = paramService.findParameterValue(root , name);
				if(ObjUtil.isNotBlank(value)){
					paramObjectMap.put(name, value);
				}
			}else{
				value = paramService.findParameterValue(null , name);
				if(ObjUtil.isNotBlank(value)){
					paramObjectMap.put(name, value);
				}
			}
		}
		return value;
	}
	/**
	 * getParamValue: 获取系统参数。 <br/>
	 * 先查询机构参数，如果机构参数没有值，那么直接去查询系统参数
	 * @author caijin
	 * @param name
	 * @return 
	 * @since JDK 1.7
	 */
	public static String getParamValue(String name){
		String value=paramObjectMap.get(name);
		if(!ObjUtil.isNotBlank(value)){
		OrganizationParameterService paramService=((OrganizationParameterService) ClassBeanFactory.getBean("organizationParameterService"));
			value = paramService.findParameterValue(null , name);
			if(ObjUtil.isNotBlank(value)){
				paramObjectMap.put(name, value);
			}
		}
		return value;
	}
	
	public static synchronized void clear(){ 
		paramObjectMap.clear();
	}
}

