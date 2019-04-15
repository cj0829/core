/**
 * Project Name:core
 * File Name:SqlTimeFormatter.java
 * Package Name:org.csr.core.util
 * Date:Oct 30, 201410:50:48 AM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:SqlTimeFormatter.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     Oct 30, 201410:50:48 AM <br/>
 * @author   yjY <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})  
@Retention(RetentionPolicy.RUNTIME) 
public @interface SqlTimeFormatter {
}

