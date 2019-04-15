package org.csr.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.csr.core.Persistable;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

public class PagedInfoImpl<T> implements PagedInfo<T>, Serializable {

	private static final long serialVersionUID = 867755909294344406L;

	private final List<T> rows = new ArrayList<T>();
	private final Page page;
	private long total;
	private Object data;
	
	public PagedInfoImpl(List<T> rows, Page page, long total) {
		if (null != rows) {
			this.rows.addAll(rows);
		}
		this.total = total;
		this.page = page;
	}

	public PagedInfoImpl(List<T> content) {
		this(content, null, (null == content) ? 0 : content.size());
	}
	public int getPageNumber() {
		return page == null ? 0 : page.getPageNumber();
	}

	public int getPageSize() {
		return page == null ? 0 : page.getPageSize();
	}
	
	public int getTotalPage() {
		return getPageSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getPageSize());
	}
	
	public long getTotal() {
		return total;
	}

	public boolean hasPreviousPage() {
		return getPageNumber() > 0;
	}

	public boolean isFirstPage() {

		return !hasPreviousPage();
	}

	public boolean hasNextPage() {

		return ((getPageNumber() + 1) * getPageSize()) < total;
	}

	public boolean isLastPage() {
		return !hasNextPage();
	}

	public Iterator<T> iterator() {
		return rows.iterator();
	}

	public List<T> getRows() {
//		return Collections.unmodifiableList(rows);
		return rows;
	}

	public boolean hasRows() {
		return ObjUtil.isNotEmpty(rows);
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {

		String contentType = "UNKNOWN";
		if (rows.size() > 0) {
			contentType = rows.get(0).getClass().getName();
		}
		return String.format("Page %s of %d containing %s instances", getPageNumber(), getTotalPage(), contentType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PagedInfoImpl<?>)) {
			return false;
		}
		PagedInfoImpl<?> that = (PagedInfoImpl<?>) obj;
		boolean totalEqual = this.total == that.total;
		boolean contentEqual = this.rows.equals(that.rows);
		boolean pagingEqual = this.page == null ? that.page == null : this.page.equals(that.page);
		return totalEqual && contentEqual && pagingEqual;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (page == null ? 0 : page.hashCode());
		result = 31 * result + rows.hashCode();
		return result;
	}

	@Override
	public int getStatus() {
		return VOBase.SUCCESS;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public T hasRow(Persistable value) {
		if(ObjUtil.isEmpty(rows) || value == null){
			return null;
		}
	    Iterator<T> it=rows.iterator();
	    while (it.hasNext()) {
			T type = it.next();
		    if (type instanceof Persistable){
		    	Object id=value.getId();
		    	if(ObjUtil.isNotEmpty(id) && id.equals(((Persistable) type).getId())){
		    		return type;
		    	}
		    }
		}
		return null;
	}
	@Override
	public void addRow(int index,T value) {
		if(ObjUtil.isEmpty(rows) || value == null){
			return;
		}
		if(!rows.equals(value)){
			rows.add(index, value);
			total=total+1;
		}
	}
}
