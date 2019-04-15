package org.csr.core.persistence.statistical.highcharts;

/**
 * 图主标题
 */
public class Title{
	String text;
	Style style;
	
	public Title(String text){
		this.text=text;
	}
	public String getText(){
		return text;
	}
	public void setText(String text){
		this.text=text;
	}
	public Style getStyle(){
		return style;
	}
	public void setStyle(Style style){
		this.style=style;
	}
}