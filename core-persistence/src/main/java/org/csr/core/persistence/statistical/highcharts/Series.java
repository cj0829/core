package org.csr.core.persistence.statistical.highcharts;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.util.ObjUtil;

public class Series{
	String name;
	String color;
	String type;
    Integer yAxis;
    List<?> data;
    Tooltip tooltip;
    
    public Series(){
		
	}
    
	public Series(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getColor(){
		return color;
	}
	public void setColor(String color){
		this.color=color;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	public Integer getyAxis(){
		return yAxis;
	}
	public void setyAxis(Integer yAxis){
		this.yAxis=yAxis;
	}
	@SuppressWarnings("unchecked")
	public List<Object> getData(){
		if(ObjUtil.isEmpty(data)){
			data=new ArrayList<Object>();
		}
		return (List<Object>)data;
	}
	public void setData(List<?> data){
		this.data=data;
	}
	public Tooltip getTooltip(){
		return tooltip;
	}
	public void setTooltip(Tooltip tooltip){
		this.tooltip=tooltip;
	}

	public Series addData(Object object){
		getData().add(object);
		return this;
	}
}