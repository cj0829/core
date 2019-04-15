/**
 * Project Name:core
 * File Name:SqlTimeFormat.java
 * Package Name:org.csr.core.util
 * Date:Oct 30, 201411:11:50 AM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.format;

import java.text.ParseException;
import java.util.Locale;

import org.csr.core.AutoSetProperty;
import org.csr.core.Persistable;
import org.csr.core.util.ObjUtil;
import org.springframework.format.Formatter;

/**
 * ClassName:SqlTimeFormat.java <br/>
 * System Name： 基础框架 <br/>
 * Date: Oct 30, 201411:11:50 AM <br/>
 * 
 * @author yjY <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
@SuppressWarnings("rawtypes")
public class SetBeanFormat implements Formatter<Persistable> {
	private final Class<?> clazz;
//	private final AutoSetProperty property;

	public SetBeanFormat(AutoSetProperty property, Class<?> clazz) {
//		this.property = property;
		this.clazz = clazz;
	}

	@Override
	public String print(Persistable time, Locale locale) {
		if (time == null) {
			return "";
		}
		return "";
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Persistable parse(String text, Locale locale) throws ParseException {
		Persistable persistable=null;
		try {
			persistable = (Persistable) clazz.newInstance();
		} catch (Exception e) { throw new ParseException("转换对象异常", 0);}
		if (ObjUtil.isNotBlank(text)) {
			try {
				persistable.setId(Long.parseLong(text));
			} catch (Exception e) { throw new ParseException("数据格式导致转换对象异常", 0);}
		}
		return persistable;
	}

}
