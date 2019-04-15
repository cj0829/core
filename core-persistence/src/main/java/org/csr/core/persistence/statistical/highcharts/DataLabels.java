/**
 * Project Name:core
 * File Name:DataLabels.java
 * Package Name:org.csr.core.persistence.statistical.highcharts
 * Date:2014年4月8日下午3:30:44
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.statistical.highcharts;

/**
 * ClassName:DataLabels.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014年4月8日下午3:30:44 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class DataLabels {

	Boolean enabled;
	String color;
	String connectorColor;
	String format;

	public DataLabels() {
	}

	public DataLabels(Boolean enabled, String color, String connectorColor,
			String format) {
		this.enabled = enabled;
		this.color = color;
		this.connectorColor = connectorColor;
		this.format = format;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getConnectorColor() {
		return connectorColor;
	}

	public void setConnectorColor(String connectorColor) {
		this.connectorColor = connectorColor;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
