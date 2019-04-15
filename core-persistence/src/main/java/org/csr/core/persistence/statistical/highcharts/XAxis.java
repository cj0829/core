package org.csr.core.persistence.statistical.highcharts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.csr.core.util.ObjUtil;

/**
 * x轴
 */
public class XAxis{
	/**x轴显示内容*/
	List<String> categories;
	/**y轴十分显示小数*/
	Boolean allowDecimals;
	
	public XAxis(){
		
	}
	public XAxis(String...categories){
		if(ObjUtil.isNotEmpty(categories)){
			for(String cate:categories){
				this.getCategories().add(cate);
			}
		}
	
	}
	
	public XAxis addCategories(String categorie){
		getCategories().add(categorie);
		return this;
	}
	
	public XAxis addCategories(Collection<String> categories){
		if(ObjUtil.isNotEmpty(categories)){
			getCategories().addAll(categories);
		}
		return this;
	}
	public List<String> getCategories(){
		if(ObjUtil.isEmpty(categories)){
			categories=new ArrayList<String>();
		}
		return categories;
	}

	public void setCategories(List<String> categories){
		this.categories=categories;
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