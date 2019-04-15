/**
 * Project Name:core
 * File Name:SqlTimeFormatAnnotationFormatterFactory.java
 * Package Name:org.csr.core.util
 * Date:Oct 30, 201410:55:56 AM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.format;

import java.util.HashSet;
import java.util.Set;

import org.csr.core.AutoSetProperty;
import org.csr.core.Persistable;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * ClassName:SqlTimeFormatAnnotationFormatterFactory.java <br/>
 * System Name： 基础框架 <br/>
 * Date: Oct 30, 201410:55:56 AM <br/>
 * @author yjY <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class SetBeanAnnotationFormatterFactory implements
		AnnotationFormatterFactory<AutoSetProperty> {
	private final Set<Class<?>> fieldTypes;

	public SetBeanAnnotationFormatterFactory() {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(Persistable.class);
		this.fieldTypes = set;
	}

	@Override
	public Set<Class<?>> getFieldTypes() {
		return fieldTypes;
	}

	@Override
	public Parser<?> getParser(AutoSetProperty arg0, Class<?> arg1) {
		return new SetBeanFormat(arg0,arg1);
	}

	@Override
	public Printer<?> getPrinter(AutoSetProperty arg0, Class<?> arg1) {
		
		return new SetBeanFormat(arg0,arg1);
	}

}
