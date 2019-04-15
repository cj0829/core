/**
 * Project Name:common
 * File Name:Where.java
 * Package Name:org.csr.core.persistence.statistical
 * Date:2014年3月27日下午4:30:46
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.statistical;

import java.util.ArrayList;

import org.csr.core.util.ObjUtil;

/**
 * ClassName: Where.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014年3月27日下午4:30:46 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class BracketsWhere extends Where {

	public BracketsWhere(String fieldName, String compare, String fieldValue,
			String andOr) {
		super(fieldName, compare, fieldValue, andOr);
	}

	public BracketsWhere(String andOr) {
		super(null, null, null, andOr);
	}

	public BracketsWhere addWheres(BracketsWhere where) {
		if (ObjUtil.isEmpty(child)) {
			child = new ArrayList<Where>();
		}
		if (ObjUtil.isNotEmpty(where)) {
			this.child.add(where);
		}
		return this;
	}

}
