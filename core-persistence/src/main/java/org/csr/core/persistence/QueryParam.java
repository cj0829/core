package org.csr.core.persistence;

import java.util.List;

import org.csr.core.Param;

public interface QueryParam extends Param{

	Object getValue();
	
	QueryParam add(QueryParam param);

	List<QueryParam> getChildParam();

	String compare();

	void setKeyPrefix(String prefix);
	
	/**
	 * 获取占位符，有:+prefix+key 组成
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	String getPlaceholder();
	
	/**
	 * 获取hql in带有 () is value+Placeholder
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	String hql();
}
