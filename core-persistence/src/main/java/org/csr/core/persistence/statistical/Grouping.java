/**
 * Project Name:common
 * File Name:XCoordinate.java
 * Package Name:org.csr.supplies.result
 * Date:2014年3月26日下午3:43:31
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical;

import java.util.Arrays;
import java.util.List;

import org.csr.core.util.ObjUtil;


/**
 * ClassName: XCoordinate.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月26日下午3:43:31 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 * 分组字段
 */
public class Grouping{
	String field;
	List<String> fieldValue;
	
	public Grouping(String field){
		this.field=field;
	}
	
	public String getFiled(){
		return field;
	}
	
	public List<String> getFieldValue(){
		return fieldValue;
	}
	
	public Grouping addFieldName(String...string){
		if(ObjUtil.isNotEmpty(fieldValue)){
			if(ObjUtil.isEmpty(fieldValue)){
				fieldValue=Arrays.asList(string);
			}else{
				fieldValue.addAll(Arrays.asList(string));
			}
		}
		return this;
	}
}

