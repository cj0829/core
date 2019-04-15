/**
 * Project Name:common
 * File Name:Highcharts.java
 * Package Name:org.csr.core.persistence.statistical
 * Date:2014年3月28日上午10:35:40
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical.highcharts;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

/**
 * ClassName: Highcharts.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月28日上午10:35:40 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class HighchartsChart extends VOBase{
	private static final long serialVersionUID = -237347756043012598L;
	Chart chart;
	Title title;
	Subtitle subtitle;
	List<XAxis> xAxis;
	List<YAxis> yAxis;
	PlotOptions plotOptions;
	Tooltip tooltip;
	Legend legend;
	List<Series> series;
	Object dataItems;
	List<Object> columns;
	
	
	public HighchartsChart(){
	}
	
	public Chart getChart(){
		return chart;
	}
	public void setChart(Chart chart){
		this.chart=chart;
	}
	public Title getTitle(){
		return title;
	}
	public void setTitle(Title title){
		this.title=title;
	}
	public Subtitle getSubtitle(){
		return subtitle;
	}
	public void setSubtitle(Subtitle subtitle){
		this.subtitle=subtitle;
	}
	public List<XAxis> getxAxis(){
		return xAxis;
	}
	public void setxAxis(List<XAxis> xAxis){
		this.xAxis=xAxis;
	}
	public List<YAxis> getyAxis(){
		return yAxis;
	}
	public void setyAxis(List<YAxis> yAxis){
		this.yAxis=yAxis;
	}
	public PlotOptions getPlotOptions(){
		return plotOptions;
	}
	public void setPlotOptions(PlotOptions plotOptions){
		this.plotOptions=plotOptions;
	}
	public Tooltip getTooltip(){
		return tooltip;
	}
	public void setTooltip(Tooltip tooltip){
		this.tooltip=tooltip;
	}
	public Legend getLegend(){
		return legend;
	}
	public void setLegend(Legend legend){
		this.legend=legend;
	}
	public List<Series> getSeries(){
		return series;
	}
	public void setSeries(List<Series> series){
		this.series=series;
	}
	public void addChart(Chart chart){
		this.chart=chart;
	}
	public void addTitle(Title title){
		this.title=title;
	}
	public void addSubtitle(Subtitle subtitle){
		this.subtitle=subtitle;
	}
	public void addXAxis(XAxis axis){
		if(ObjUtil.isEmpty(xAxis)){
			this.xAxis=new ArrayList<XAxis>();
		}
		this.xAxis.add(axis);
	}
	public void addYAxis(YAxis axis){
		if(ObjUtil.isEmpty(yAxis)){
			this.yAxis=new ArrayList<YAxis>();
		}
		this.yAxis.add(axis);
		
	}
	public void addTooltip(Tooltip tooltip){
		this.tooltip=tooltip;
	}
	
	public void addpPlotOptions(PlotOptions plotOptions){
		this.plotOptions=plotOptions;
	}
	
	public void addSeries(List<Series> series){
		this.series=series;
	}
	
	public Object getDataItems(){
		return dataItems;
	}
	public void setDataItems(Object dataItems){
		this.dataItems=dataItems;
	}

	public List<Object> getColumns() {
		return columns;
	}

	public void setColumns(List<Object> columns) {
		this.columns = columns;
	}
}

