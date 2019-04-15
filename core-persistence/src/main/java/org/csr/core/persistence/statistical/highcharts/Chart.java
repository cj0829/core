package org.csr.core.persistence.statistical.highcharts;

/**
 * 显示图形的类型：<br>
 * 类型有
 */
public class Chart {

	String zoomType; // 变焦类型
	Integer spacingRight; // 右间距
	Integer spacingBottom; // 间距底部
	String type; // 类型
	Boolean plotShadow; // 阴影
	Boolean inverted; // 颠倒

	public Chart() {
	}

	public Chart(String type) {
		this.type = type;
	}

	public String getZoomType() {
		return zoomType;
	}

	public void setZoomType(String zoomType) {
		this.zoomType = zoomType;
	}

	public Integer getSpacingRight() {
		return spacingRight;
	}

	public void setSpacingRight(Integer spacingRight) {
		this.spacingRight = spacingRight;
	}

	public Integer getSpacingBottom() {
		return spacingBottom;
	}

	public void setSpacingBottom(Integer spacingBottom) {
		this.spacingBottom = spacingBottom;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean isPlotShadow() {
		return plotShadow;
	}

	public void setPlotShadow(Boolean plotShadow) {
		this.plotShadow = plotShadow;
	}

	public Boolean isInverted() {
		return inverted;
	}

	public void setInverted(Boolean inverted) {
		this.inverted = inverted;
	}
}