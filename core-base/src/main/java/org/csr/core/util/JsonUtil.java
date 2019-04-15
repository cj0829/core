/**
 * Project Name:core
 * File Name:JsonUtil.java
 * Package Name:org.csr.core.util
 * Date:2014年3月31日下午9:33:24
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.csr.core.exception.Exceptions;
import org.csr.core.util.json.ExcludePropertyPreFilter;
import org.csr.core.util.support.SetJsonObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * ClassName:JsonUtil.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014年3月31日下午9:33:24 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class JsonUtil{
	public final static String Default_DATE_PATTERN = "yyyy-MM-dd HH:mm";  
	public final static String Default_TIMe_PATTERN = "yyyy-MM-dd HH:mm";  
	
	public static String excludeJson(Object obj,String...params){
		if(ObjUtil.isNotEmpty(obj)&&!ObjUtil.isString(obj)){
			ExcludePropertyPreFilter filter = new ExcludePropertyPreFilter(obj.getClass(), params);
			JSONSerializer  serializer = new JSONSerializer(); 
			serializer.addFilter(filter);
			SerializeConfig config = new SerializeConfig(); 
			config.put(Date.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			config.put(Time.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			return JSON.toJSONString(obj, config, filter, SerializerFeature.DisableCircularReferenceDetect);
		}
		if(ObjUtil.isString(obj)){
			return obj.toString();
		}
		return "";
	}

	public static String excludeJson(Object obj,ExcludePropertyPreFilter[] filters){
		if(ObjUtil.isNotEmpty(obj)&&!ObjUtil.isString(obj)){
			SerializeConfig config = new SerializeConfig(); 
			config.put(Date.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			config.put(Time.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			return JSON.toJSONString(obj, config, filters, SerializerFeature.DisableCircularReferenceDetect);
		}
		if(ObjUtil.isString(obj)){
			return obj.toString();
		}
		return "";
	}

	
	public static String excludeNullJson(Object obj,String...params){
		if(ObjUtil.isNotEmpty(obj)&&!ObjUtil.isString(obj)){
//			 jsonConfig=new JsonConfig();
//			jsonConfig.setExcludes(params);
//			jsonConfig.setJsonPropertyFilter(new NotNullPropertyFilter());
//			return JSONSerializer.toJSON(obj,jsonConfig).toString();
		}
		if(ObjUtil.isNotEmpty(obj)&&!ObjUtil.isString(obj)){
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(obj.getClass(), params);
			JSONSerializer  serializer = new JSONSerializer(); 
			serializer.addFilter(filter);
			SerializeConfig config = new SerializeConfig(); 
			config.put(Date.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			config.put(Time.class, new SimpleDateFormatSerializer(Default_DATE_PATTERN));  
			return JSON.toJSONString(obj, config, filter, SerializerFeature.DisableCircularReferenceDetect);
		}
		if(ObjUtil.isString(obj)){
			return obj.toString();
		}
		return "";
	}
	
	public static <O> List<O> asArray(JSONArray array,SetJsonObject<O> setJson){
		if(ObjUtil.isEmpty(setJson)){
			Exceptions.dao("", "setJson不能为空！");
		}
		List<O> list=new ArrayList<O>(); 
		if(ObjUtil.isEmpty(array)){
			return list;
		}
		Iterator<Object> it=array.iterator();
		while(it.hasNext()){
			JSONObject value=(JSONObject) it.next();
			list.add(setJson.setBean(value));
		}
		return list;
	}
	
	public static Map<String, String[]> toMap(String files){
		Map<String, String[]> map=new LinkedHashMap<String,String[]>();
		JSONObject jsonMap=JSONObject.parseObject(files);
		Iterator<?> it=jsonMap.keySet().iterator();
		while(it.hasNext()){  
	        String key=(String)it.next();  
	        String value=(String)jsonMap.get(key);
	        map.put(key, value.split(","));  
	    }
		return map;
	}
	
	public static Map<String, String> jsonToMap(String files){
		Map<String, String> map=new LinkedHashMap<String,String>();
		JSONObject jsonMap=JSONObject.parseObject(files);
		Iterator<?> it=jsonMap.keySet().iterator();
		while(it.hasNext()){  
	        String key=(String)it.next();  
	        String value=(String)jsonMap.get(key);
	        map.put(key,value);  
	    }
		return map;
	}
	
	public static void main(String[] args) {
		JSONObject jsonMap=JSONObject.parseObject("{'基础数据.xlsx':'系统参数,初始数据,系统字典数据','功能点.xlsx':'01基础数据,02系统管理,04运维管理,18个人空间,20内容管理','菜单.xlsx':'菜单'}");
	
		Iterator<?> it=jsonMap.keySet().iterator();
		while(it.hasNext()){  
	        String key=(String)it.next();  
	        String value=(String)jsonMap.get(key);
	        System.out.println(value);
//	        map.put(key, value.split(","));  
	    }
	}
}
