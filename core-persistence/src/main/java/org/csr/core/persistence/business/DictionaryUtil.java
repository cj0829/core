/**
 * Project Name:common
 * File Name:Snippet.java
 * Package Name:org.csr.core.support
 * Date:2014年4月24日下午11:21:36
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.csr.core.persistence.business.domain.Dictionary;
import org.csr.core.persistence.business.service.DictionaryService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.DictionaryJsonUtil;
import org.csr.core.util.ObjUtil;

/**
 * ClassName: Snippet.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年4月24日下午11:21:36 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */

public class DictionaryUtil {
	
	/**
	 * @description:获取参数值
	 * @param: name-参数名字
	 * @return: String 
	 */
	public static List<Dictionary> initDictList(DictionaryService service,String name){
		return service.findList(name);
		
	}
	
	/**
	 * @description:获取参数值
	 * @param: name-参数名字
	 * @return: String 
	 */
	public static List<Dictionary> findDictList(String name){
		DictionaryService dictionaryService=((DictionaryService) ClassBeanFactory.getBean("dictionaryService"));
		if(ObjUtil.isNotEmpty(dictionaryService)){
			return dictionaryService.findList(name);
		}else{
			return new ArrayList<Dictionary>();
		}
		
	}
	
	/**
	 * @description:获取数据字典信息以json格式返回（适合action中直接调用）
	 * @param: name：数据字典类型
	 * @return: String 
	 */
	public static String getDictToJson(String name){
		DictionaryService dictionaryService=((DictionaryService) ClassBeanFactory.getBean("dictionaryService"));
		Map<String, String> map = dictionaryService.findDictMap(name);
		if(ObjUtil.isNotEmpty(dictionaryService)){
			return  DictionaryJsonUtil.getJsonString(map);
		}else{
			return "";
		}
	}
	
	/**
	 * @description:获取数据字典信息以json格式返回（适合action中直接调用）
	 * @param: name：数据字典类型
	 * @return: String 
	 */
	public static String getDictValue(String name){
		DictionaryService dictionaryService=((DictionaryService) ClassBeanFactory.getBean("dictionaryService"));
		Map<String, String> map = dictionaryService.findDictMap(name);
		if(ObjUtil.isNotEmpty(dictionaryService)){
			return  DictionaryJsonUtil.getJsonString(map);
		}else{
			return "";
		}
	}
	
	/**
	 * @description:获取数据字典信息以json格式返回（适合action中直接调用）
	 * @param: name：数据字典类型
	 * @return: String 
	 */
	public static String getDictName(String name,String value){
		DictionaryService dictionaryService=((DictionaryService) ClassBeanFactory.getBean("dictionaryService"));
		Map<String, String> map = dictionaryService.findDictMap(name);
		return map.get(value);
	}
}

