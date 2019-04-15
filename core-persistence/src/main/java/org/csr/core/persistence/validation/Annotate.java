package org.csr.core.persistence.validation;

import org.apache.commons.lang.StringUtils;

public class Annotate {
	
	private String ch;                //中文名称
	private String en;                //英文名称
	private String uppercaseEn;       //首字母大写英文名称
	private String vtype; 		      //校验规则
	private int len;               	  //长度
	private boolean search;           //是否当做查询条件
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getEn() {
		return en;
	}
	public void setEn(String en) {
		this.setUppercaseEn(toUpperCaseFirstOne(en));
		this.en = en;
	}
	public String getVtype() {
		return vtype;
	}
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		
		this.len = len;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	public String getUppercaseEn() {
		return uppercaseEn;
	}
	public void setUppercaseEn(String uppercaseEn) {
		this.uppercaseEn = uppercaseEn;
	}

	/**
	 * toUpperCaseFirstOne:首字母更换大写 <br/>
	 * @author caijin
	 * @param name
	 * @return
	 * @since JDK 1.7
	 */
	public static String toUpperCaseFirstOne(String name) {
		
		if(StringUtils.isNotBlank(name)){
			char[] ch = name.toCharArray();
			ch[0] = Character.toUpperCase(ch[0]);
			StringBuffer a = new StringBuffer();
			a.append(ch);
			return a.toString();
		}
		return "";
	}
}
