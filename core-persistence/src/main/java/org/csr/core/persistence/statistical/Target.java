/**
 * Project Name:common
 * File Name:Target.java
 * Package Name:org.csr.core.persistence.statistical
 * Date:2014年3月26日下午4:01:58
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical;
/**
 * ClassName: Target.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月26日下午4:01:58 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 * 统计指标
 */
public class Target{
	/**统计字段名称*/
	String fieldName;
	/**统计方式*/
	int statisticalMethods;
	
	public Target(String fieldName,int statisticalMethods){
		this.fieldName=fieldName;
		this.statisticalMethods=statisticalMethods;
	}
	public String getFieldName(){
		return fieldName;
	}
	public void setFieldName(String fieldName){
		this.fieldName=fieldName;
	}
	public int getStatisticalMethods(){
		return statisticalMethods;
	}
	public void setStatisticalMethods(int statisticalMethods){
		this.statisticalMethods=statisticalMethods;
	}
	
}

