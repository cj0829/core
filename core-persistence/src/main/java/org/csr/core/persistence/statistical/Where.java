/**
 * Project Name:common
 * File Name:Where.java
 * Package Name:org.csr.core.persistence.statistical
 * Date:2014年3月27日下午4:30:46
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.persistence.query.Compare;
import org.csr.core.util.ObjUtil;

/**
 * ClassName: Where.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月27日下午4:30:46 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class Where{
	/**字段名称*/
	String fieldName;
	/**比较方式*/
	String compare;
	/**字段值*/
	String fieldValue;
	/**AND 和 OR */
	String andOr=Compare.AND;
	
	List<Where> child;
	
	public Where(String fieldName,String compare,String fieldValue,String andOr){
		this.fieldName=fieldName;
		this.compare=compare;
		this.fieldValue=fieldValue;
		this.andOr=andOr;
	}

	public String getFieldName(){
		return fieldName;
	}

	public void setFieldName(String fieldName){
		this.fieldName=fieldName;
	}

	public String getCompare(){
		return compare;
	}

	public void setCompare(String compare){
		this.compare=compare;
	}

	public String getFieldValue(){
		return fieldValue;
	}

	public void setFieldValue(String fieldValue){
		this.fieldValue=fieldValue;
	}

	public String getAndOr(){
		return andOr;
	}

	public void setAndOr(String andOr){
		this.andOr=andOr;
	}

	public Where addWheres(Where where){
		if(ObjUtil.isEmpty(child)){
			child=new ArrayList<Where>();
		}
		this.child.add(where);
		return this;
	}

	public List<Where> getWheres(){
		return this.child;
	}
	
}

