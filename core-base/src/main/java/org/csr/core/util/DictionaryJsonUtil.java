package org.csr.core.util;

import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * ClassName:MapUtil.java <br/>
 * System Name：    csr <br/>
 * Date:     2014-2-28上午9:21:54 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  map工具类<br/>
 * 公用方法描述：  <br/>
 */
public class DictionaryJsonUtil {
	/**
	 * 将Map转换为JSON格式字符串
	 * {"3":"禁用","2":"启用","1":"待审核"}
	 * {'1':'待审核','2':'启用','3':'禁用'}
	 */
	public static String getJsonString(Map<String, String> map) {
		JSONObject obj = new JSONObject();
		if (ObjUtil.isNotEmpty(map)) {
			Iterator<String> it = map.keySet().iterator();
			String key = null;
			while (it.hasNext()) {
				key = it.next();
				String value = map.get(key);
				obj.put(key, value);
			}
		}
		return obj.toString();
	}
	
	
	public static String getAllJsonString(Map<String,Map<String, String>> map) {
		JSONObject obj = new JSONObject();
		if (ObjUtil.isNotEmpty(map)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key=it.next();
				Map<String, String> value=map.get(key);
				obj.put(key, value);
			}
		}
		return obj.toString();
	}
}
