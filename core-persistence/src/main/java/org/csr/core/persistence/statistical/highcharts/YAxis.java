package org.csr.core.persistence.statistical.highcharts;

/**
 * y轴
 */
public class YAxis{
	Integer min; //y轴最小值
	Integer max; //y轴最大值
	Title title;
	Labels labels;
	/**y轴，在左边或者右边显示*/
	Boolean opposite;
	/**y轴十分显示小数*/
	Boolean allowDecimals;
	public YAxis(){
		
	}
	
	public YAxis(String title){
		this.title=new Title(title);
	}
	
	public YAxis(String title,Integer min){
		this.title=new Title(title);
		this.min=min;
	}

	public Integer getMin(){
		return min;
	}

	public void setMin(Integer min){
		this.min=min;
	}
	
	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Title getTitle(){
		return title;
	}

	public void setTitle(Title title){
		this.title=title;
	}

	public Labels getLabels(){
		return labels;
	}

	public void setLabels(Labels labels){
		this.labels=labels;
	}

	public Boolean getOpposite(){
		return opposite;
	}

	public void setOpposite(Boolean opposite){
		this.opposite=opposite;
	}

	/**
	 * allowDecimals.
	 *
	 * @return  the allowDecimals
	 * @since   JDK 1.7
	 */
	public Boolean getAllowDecimals() {
		return allowDecimals;
	}

	/**
	 * allowDecimals. *
	 * @param  allowDecimals the allowDecimals to set
	 * @since  JDK 1.7
	 */
	public void setAllowDecimals(Boolean allowDecimals) {
		this.allowDecimals = allowDecimals;
	}
	
	
}