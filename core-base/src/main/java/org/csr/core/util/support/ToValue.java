/**
 * Project Name:core
 * File Name:ToValue.java
 * Package Name:org.csr.core.util
 * Date:2014年4月7日下午4:08:55
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.util.support;
/**
 * ClassName:ToValue.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年4月7日下午4:08:55 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface ToValue<T,ID> {
	ID getValue(T obj);
}

