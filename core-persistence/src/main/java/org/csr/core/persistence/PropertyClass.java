/**
 * Project Name:core
 * File Name:PropertyType.java
 * Package Name:org.csr.core.persistence
 * Date:2015年5月22日上午11:33:38
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence;

import java.util.Date;

import org.csr.core.exception.Exceptions;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:PropertyType.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2015年5月22日上午11:33:38 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public abstract class PropertyClass {
	/**时间类型*/
	public final static String DATE="Date";
	/**整型*/
	public final static String BYTE="B";
	/**整型*/
	public final static String INT="I";
	/**字符串型*/
	public final static String STRING="S";
	/**Long型*/
	public final static String LONG="L";
	/**float型*/
	public final static String FLOAT="F";
	/**double型*/
	public final static String DOUBLE="D";
	/**任意类型，以字符串为基准，sql语句采用比较符合，不采用站位符*/
	public final static String ANY="A";
	
	
	/**
	 * 默认，给String类型
	 * @author caijin
	 * @param className
	 * @return
	 * @since JDK 1.7
	 */
	public static Class<?> getClassType(String className){
		if(ObjUtil.isBlank(className)){
			Exceptions.service("", "查询的类型不存在");
		}else if (DATE.toLowerCase().equals(className.toLowerCase())) {
			return Date.class;
		}else if (INT.toLowerCase().equals(className.toLowerCase())) {
			return Integer.class;
		}else if (BYTE.toLowerCase().equals(className.toLowerCase())) {
			return Byte.class;
		}else if (STRING.toLowerCase().equals(className.toLowerCase())) {
			return String.class;
		}else if (LONG.toLowerCase().equals(className.toLowerCase())) {
			return Long.class;
		}else if (FLOAT.toLowerCase().equals(className.toLowerCase())) {
			return Float.class;
		}else if (DOUBLE.toLowerCase().equals(className.toLowerCase())) {
			return Double.class;
		}else if (ANY.toLowerCase().equals(className.toLowerCase())) {
			return AnyType.class;
		}else{
			Exceptions.service("", "[" + className + "]数据类型没有按规则编写。");
		}
		
		return null;
	}
}

