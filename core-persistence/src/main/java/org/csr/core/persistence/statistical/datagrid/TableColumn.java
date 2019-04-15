/**
 * Project Name:common
 * File Name:Column.java
 * Package Name:org.csr.core.persistence.statistical.highcharts
 * Date:2014年3月28日上午11:56:54
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.statistical.datagrid;

/**
 * 显示图形的类型：<br>
 * 类型有
 */
public class TableColumn {
	String title;
	String field;
	Integer width;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}
