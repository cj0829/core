package org.csr.core.persistence.statistical.highcharts;

public class Tooltip{
	String valueSuffix;
	Boolean shared;
	
	public String getValueSuffix(){
		return valueSuffix;
	}
	public void setValueSuffix(String valueSuffix){
		this.valueSuffix=valueSuffix;
	}
	public Boolean getShared(){
		return shared;
	}
	public void setShared(Boolean shared){
		this.shared=shared;
	}
}