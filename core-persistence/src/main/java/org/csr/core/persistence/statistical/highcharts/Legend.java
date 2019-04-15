package org.csr.core.persistence.statistical.highcharts;

/**
 * 图的说明
 */
public class Legend{
	String layout;
	String align;
	Integer x;
	String verticalAlign;
	Integer y;
	Boolean floating;
	String backgroundColor;
	public String getLayout(){
		return layout;
	}
	public void setLayout(String layout){
		this.layout=layout;
	}
	public String getAlign(){
		return align;
	}
	public void setAlign(String align){
		this.align=align;
	}
	public Integer getX(){
		return x;
	}
	public void setX(Integer x){
		this.x=x;
	}
	public String getVerticalAlign(){
		return verticalAlign;
	}
	public void setVerticalAlign(String verticalAlign){
		this.verticalAlign=verticalAlign;
	}
	public Integer getY(){
		return y;
	}
	public void setY(Integer y){
		this.y=y;
	}
	public Boolean getFloating(){
		return floating;
	}
	public void setFloating(Boolean floating){
		this.floating=floating;
	}
	public String getBackgroundColor(){
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor){
		this.backgroundColor=backgroundColor;
	}
}