package org.csr.core.persistence.business.bean;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.web.bean.VOBase;

/**
 * ClassName:DictionaryResult.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午10:07:09 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class DictionaryResult extends VOBase<Integer>{

	/**
	 * (用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -7571879488830377938L;
	private Integer key;
	private String value;
	
	private List<DictionaryResult> childDict = new ArrayList<DictionaryResult>();

	
	// 构造方法区
	public DictionaryResult() {
		super();
	}

	public DictionaryResult(Integer key, String value,
			List<DictionaryResult> childDict) { 
		this.key = key;
		this.value = value;
		this.childDict = childDict;
	}
	

	// getter \ setter 方法区
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<DictionaryResult> getChildDict() {
		return childDict;
	}

	public void setChildDict(List<DictionaryResult> childDict) {
		this.childDict = childDict;
	}
	
}
