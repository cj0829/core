/**
 * Project Name:common
 * File Name:Column.java
 * Package Name:org.csr.core.persistence.statistical.highcharts
 * Date:2014年3月28日上午11:56:54
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.statistical.highcharts;

/**
 * 显示图形的类型：<br>
 * 类型有
 */
public class Column{
	Float pointPadding;
	Integer borderWidth;
	
	
	public Column(){
		
	}
	
	public Column(Float pointPadding,Integer borderWidth){
		this.pointPadding=pointPadding;
		this.borderWidth=borderWidth;
	}
	public Float getPointPadding(){
		return pointPadding;
	}
	public void setPointPadding(Float pointPadding){
		this.pointPadding=pointPadding;
	}
	public Integer getBorderWidth(){
		return borderWidth;
	}
	public void setBorderWidth(Integer borderWidth){
		this.borderWidth=borderWidth;
	}
}
