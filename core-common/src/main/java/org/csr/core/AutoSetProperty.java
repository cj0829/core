/**
 * Project Name:core
 * File Name:IsNotFind.java
 * Package Name:org.csr.core.persistence
 * Date:2014年11月3日下午12:23:12
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:IsNotFind.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年11月3日下午12:23:12 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})  
@Retention(RetentionPolicy.RUNTIME) 
public @interface AutoSetProperty {
	
	boolean required() default true;
	
	String message() default "";
}

