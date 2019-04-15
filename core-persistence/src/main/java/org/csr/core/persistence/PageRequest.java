package org.csr.core.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.csr.core.Param;
import org.csr.core.page.Direction;
import org.csr.core.page.Page;
import org.csr.core.page.QueryProperty;
import org.csr.core.page.Sort;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:PageRequest.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-20上午11:02:02 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class PageRequest implements Page, Serializable {

	private static final long serialVersionUID = 8280485938848398236L;
	/**
	 * 页码
	 */
	private final int page;
	/**
	 * 每页显示的条数
	 */
	private final int size;
	/**
	 * 排序
	 */
	private Sort sort;

	/** 页面封装过来的查询字符串 value:$u.name:$likeS|$value:$u.name:$likeS **/
	private String queryString;

	ArrayList<Param> paramList = new ArrayList<Param>();

	public PageRequest() {
		this(1, 1, null);
	}
	
	public PageRequest(int page, int size) {
		this(page, size, null);
	}

	public PageRequest(int page, int size, String queryProperty) {
		this(page, size, null, queryProperty);
	}

	public PageRequest(int page, int size, Direction direction,String... properties) {
		this(page, size, new Sort(direction, properties), null);
	}

	public PageRequest(int page, int size, Sort sort, String queryProperty) {
		if (0 > page) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		if (0 >= size) {
			throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
		}
		this.page = page;
		this.size = size;
		this.sort = sort;
		this.queryString = queryProperty;
	}

	public int getPageSize() {
		return size;
	}

	public int getPageNumber() {

		return page;
	}

	public int getOffset() {
		if (page == 0) {
			return 0;
		}
		return (page - 1) * size;
	}

	public Sort getSort() {
		return sort;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PageRequest)) {
			return false;
		}
		PageRequest that = (PageRequest) obj;
		boolean pageEqual = this.page == that.page;
		boolean sizeEqual = this.size == that.size;
		boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);
		return pageEqual && sizeEqual && sortEqual;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + page;
		result = 31 * result + size;
		result = 31 * result + (null == sort ? 0 : sort.hashCode());
		return result;
	}

	/**
	 * 获取查询条件字符串
	 * 
	 * @see org.csr.core.persistence.Page#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return queryString;
	}

	/**
	 * 封装查询条件
	 * 
	 * @see org.csr.core.persistence.Page#addQueryPropertyList(org.csr.core.persistence.QueryProperty)
	 */
	public void addQueryPropertyList(QueryProperty propertyFilter) {
		this.paramList.add(propertyFilter.toParam());
	}

	@Override
	public Param hasParam(String key) {
		if (ObjUtil.isNotEmpty(paramList)) {
			Iterator<Param> it = paramList.iterator();
			while (it.hasNext()) {
				Param param = it.next();
				if (param.getKey().equals(key)) {
					return param;
				}
			}
		}
		return null;
	}

	@Override
	public int removeParam(String key) {
		int size = 0;
		if (ObjUtil.isNotEmpty(paramList)) {
			Iterator<Param> it = paramList.iterator();
			while (it.hasNext()) {
				if (it.next().getKey().equals(key)) {
					it.remove();
					size++;
				}
			}
		}
		return size;
	}

	@Override
	public List<Param> toParam() {
		return paramList;
	}

}
