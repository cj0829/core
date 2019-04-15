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
public class JSONObjectDeco {
	final JSONObject object;

	public JSONObjectDeco(JSONObject object) {
		this.object = object;
	}

	public Long getLong(String key) {
		try {
			return object.getLong(key);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer getInt(String key) {
		try {
			return object.getInteger(key);
		} catch (Exception e) {
			return null;
		}
	}

	public String getString(String key) {
		try {
			return object.getString(key);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean getBoolean(String key) {
		try {
			return object.getBoolean(key);
		} catch (Exception e) {
			return false;
		}
	}

	public double getDouble(String key) {
		try {
			return object.getDouble(key);
		} catch (Exception e) {
			return 0;
		}
	}

	public JSONArray getJSONArray(String key) {
		try {
			return object.getJSONArray(key);
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject getJSONObject(String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			return null;
		}
	}

	public Object get(String key) {
		try {
			return object.get(key);
		} catch (Exception e) {
			return null;
		}
	}

	public Object get(Object key) {
		try {
			return object.get(key);
		} catch (Exception e) {
			return null;
		}
	}
}
