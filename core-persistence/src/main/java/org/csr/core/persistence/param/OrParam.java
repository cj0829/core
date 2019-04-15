/**
 * Project Name:core
 * File Name:OrParam.java
 * Package Name:org.csr.core.persistence.param
 * Date:2015年6月23日下午4:59:17
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.param;

import org.csr.core.Param;

/**
 * ClassName:OrParam.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2015年6月23日下午4:59:17 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public abstract class OrParam extends BaseParam implements Param {

	public OrParam(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public OrParam(String key, Object value, Class<?> property) {
		this(key, value);
		this.property = property;
	}
}
