/**
 * Project Name:common
 * File Name:AndIsParam.java
 * Package Name:org.csr.core.service.impl
 * Date:2014-2-11下午4:49:28
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.param;

import org.csr.core.persistence.query.Compare;
import org.csr.core.util.ObjUtil;


/**
 * ClassName:BasisServiceImpl <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class OrNotIsValue extends OrParam {
	public OrNotIsValue(String key, Object value) {
		super(key, value);
		compare=Compare.NotIs;
	}
	
	public OrNotIsValue(String key, Object value,Class<?> property) {
		super(key,value,property);
		compare=Compare.NotIs;
	}

	@Override
	public String hql() {
		if (ObjUtil.isEmpty(value)) {
			return "NULL";
		} else {
			return ObjUtil.toString(value);
		}
	}
}

