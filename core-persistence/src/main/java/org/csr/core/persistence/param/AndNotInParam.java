/**
 * Project Name:training
 * File Name:AndLikeParam.java
 * Package Name:org.csr.core.service.impl
 * Date:2014年9月20日下午2:47:40
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.param;

import org.csr.core.persistence.query.Compare;

/**
 * ClassName: AndLikeParam.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014年9月20日下午2:47:40 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class AndNotInParam extends AndParam {
	public AndNotInParam(String key, Object value) {
		super(key, value);
		compare = Compare.NotIn;
	}

	public AndNotInParam(String key, Object value, Class<?> property) {
		super(key, value, property);
		compare = Compare.NotIn;
	}

	@Override
	public String hql() {
		return "(:" + getPlaceholder() + ")";
	}
}
