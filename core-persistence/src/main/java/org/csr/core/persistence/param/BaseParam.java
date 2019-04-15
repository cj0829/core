/**
 * Project Name:core
 * File Name:BaseParam.java
 * Package Name:org.csr.core.persistence.param
 * Date:2016年1月13日下午5:48:09
 * Copyright (c) 2016, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.param;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.csr.core.persistence.QueryParam;
import org.csr.core.util.DateUtil;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:BaseParam.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2016年1月13日下午5:48:09 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class BaseParam implements QueryParam {
	protected String key;
	protected String prefix;
	protected Object value;
	protected String compare;
	protected Class<?> property;
	protected List<QueryParam> childParam = new ArrayList<QueryParam>();

	@Override
	public Object getValue() {
		if (ObjUtil.isEmpty(property)) {
			return value;
		} else if (property == Date.class) {
			return DateUtil.parseDate(ObjUtil.toString(value));
		} else if (property == Time.class) {
			return DateUtil.parseDateTimeToMin(ObjUtil.toString(value));
		} else if (property == Integer.class) {
			return ObjUtil.toInteger(value);
		} else if (property == Long.class) {
			return ObjUtil.toLong(value);
		} else if (property == Float.class) {
			return ObjUtil.toFloat(value);
		} else if (property == Double.class) {
			return ObjUtil.toDouble(value);
		} else if (property == String.class) {
			return ObjUtil.toString(value);
		} else if (property == Byte.class) {
			return ObjUtil.toByte(value);
		} else {
			return value;
		}
	}

	public String compare() {
		return compare;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public QueryParam add(QueryParam param) {
		this.childParam.add(param);
		return this;
	}

	@Override
	public List<QueryParam> getChildParam() {
		return this.childParam;
	}

	@Override
	public void setKeyPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String hql(){
		return ":"+getPlaceholder();
	};
	
	@Override
	public String getPlaceholder() {
		if (ObjUtil.isNotBlank(prefix)) {
			return prefix + "_" + ObjUtil.underlineSeparated(key);
		} else {
			return ObjUtil.underlineSeparated(key);
		}
	}
}
