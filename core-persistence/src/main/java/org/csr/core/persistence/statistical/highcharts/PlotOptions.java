/**
 * Project Name:common
 * File Name:PlotOptions.java
 * Package Name:org.csr.core.persistence.statistical.highcharts
 * Date:2014年3月28日上午11:56:31
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical.highcharts;
/**
 * ClassName: PlotOptions.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月28日上午11:56:31 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class PlotOptions{
	Pie pie;
	Column column;

	
	public PlotOptions(){
		
	}
	
	public PlotOptions(Float pointPadding,Integer borderWidth){
		this.column=new Column(pointPadding,borderWidth);
	}
	
	public PlotOptions addPie(Boolean allowPointSelect,String cursor,DataLabels dataLabels){
		pie=new Pie(allowPointSelect, cursor, dataLabels);
		return this;
	}
	
	public Pie getPie() {
		return pie;
	}

	public void setPie(Pie pie) {
		this.pie = pie;
	}

	public Column getColumn(){
		return column;
	}

	public void setColumn(Column column){
		this.column=column;
	}
}

