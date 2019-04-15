/**
 * Project Name:core
 * File Name:JSONObjectsss.java
 * Package Name:org.csr.core.util
 * Date:2015-11-6下午12:17:45
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
 */

package org.csr.core.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * ClassName:JSONObjectsss.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2015-11-6下午12:17:45 <br/>
 * 
 * @author Administrator <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class JSONArrayDeco {
	final JSONArray array;

	public JSONArrayDeco(JSONArray array) {
		this.array = array;
	}

	public Long getLong(int index) {
		try {
			return array.getLong(index);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer getInt(int index) {
		try {
			return array.getInteger(index);
		} catch (Exception e) {
			return null;
		}
	}

	public String getString(int index) {
		try {
			return array.getString(index);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean getBoolean(int index) {
		try {
			return array.getBoolean(index);
		} catch (Exception e) {
			return false;
		}
	}

	public double getDouble(int index) {
		try {
			return array.getDouble(index);
		} catch (Exception e) {
			return 0;
		}
	}

	public JSONArray getJSONArray(int index) {
		try {
			return array.getJSONArray(index);
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject getJSONObject(int index) {
		try {
			return array.getJSONObject(index);
		} catch (Exception e) {
			return null;
		}
	}

	public Object get(int index) {
		try {
			return array.get(index);
		} catch (Exception e) {
			return null;
		}
	}

}
