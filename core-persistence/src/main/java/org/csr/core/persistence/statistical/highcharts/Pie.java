/**
 * Project Name:core
 * File Name:Pie.java
 * Package Name:org.csr.core.persistence.statistical.highcharts
 * Date:2014年4月8日下午3:29:23
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical.highcharts;
/**
 * ClassName:Pie.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年4月8日下午3:29:23 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class Pie {
	Boolean allowPointSelect;
    String cursor;
    DataLabels dataLabels;
    Boolean showInLegend = false;
    
    
 	public Pie() {
 	}
    
	public Pie(Boolean allowPointSelect, String cursor, DataLabels dataLabels) {
		this.allowPointSelect = allowPointSelect;
		this.cursor = cursor;
		this.dataLabels = dataLabels;
	}
	public Boolean getAllowPointSelect() {
		return allowPointSelect;
	}
	public void setAllowPointSelect(Boolean allowPointSelect) {
		this.allowPointSelect = allowPointSelect;
	}
	public String getCursor() {
		return cursor;
	}
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	public DataLabels getDataLabels() {
		return dataLabels;
	}
	public void setDataLabels(DataLabels dataLabels) {
		this.dataLabels = dataLabels;
	}
	public Boolean getShowInLegend() {
		return showInLegend;
	}
	public void setShowInLegend(Boolean showInLegend) {
		this.showInLegend = showInLegend;
	}
    
}

