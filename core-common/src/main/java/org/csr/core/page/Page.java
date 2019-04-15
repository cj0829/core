package org.csr.core.page;

import java.io.Serializable;
import java.util.List;

import org.csr.core.Param;


/**
 * 分页请求对象
 * @author caijin
 *
 */
public interface Page extends Serializable{

	/**
	 * 当前页码
	 * @return
	 */
	int getPageNumber();

	/**
	 * 当页显示条数
	 * @return
	 */
	int getPageSize();

	/**
	 * 查询起始页
	 * @return
	 */
	int getOffset();


	/**
	 * addQueryPropertyList: 描述方法的作用 <br/>
	 * @author caijin
	 * @param filter
	 * @since JDK 1.7
	 */
	void addQueryPropertyList(QueryProperty filter);
	/**
	 * getQueryString: 页面封装过来的查询字符串 value:$u.name:$likeS|$value:$u.name:$likeS <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	String getQueryString();
	
	/**
	 * hasParam: 是否存在有对应的key值的参数 <br/>
	 * @author caijin
	 * @param key
	 * @return
	 * @since JDK 1.7
	 */
	public Param hasParam(String key);
	
	/**
	 * removeParam: 如果存在kye，就删除Param <br/>
	 * @author caijin
	 * @param key
	 * @return
	 * @since JDK 1.7
	 */
	public int removeParam(String key);
	/**
	 * 返回Param参数的方式，进行查询
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	List<Param> toParam();
	
	/**
	 * 排序
	 * @return
	 */
	Sort getSort();

}
