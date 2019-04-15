package org.csr.core.page;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.csr.core.Persistable;

/**
 * ClassName:PagedInfo.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014年3月17日上午10:29:26 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface PagedInfo<T> extends Iterable<T>,Serializable {
	
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
	 * getTotalPage: 总页数 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	int getTotalPage();

	long getTotal();

	boolean hasPreviousPage();

	boolean isFirstPage();

	boolean hasNextPage();

	boolean isLastPage();

	Iterator<T> iterator();
	
	List<T> getRows();

	boolean hasRows();
	
	@SuppressWarnings("rawtypes")
	T hasRow(Persistable value);
	
	void addRow(int index, T value);
	
	int getStatus();
	
	void setData(Object value);
	
	Object getData();

}