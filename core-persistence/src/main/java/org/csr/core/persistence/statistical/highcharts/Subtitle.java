package org.csr.core.persistence.statistical.highcharts;

/**
 * 图主子标题
 */
public class Subtitle{
	String text;

	public Subtitle(String text){
		this.text=text;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text=text;
	}
}