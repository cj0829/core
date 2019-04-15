package org.csr.core.web.bean;

import org.csr.core.util.ObjUtil;

/**
 * 返回数据类型
 * 
 * @author caijin
 * 
 */
public class UUIDBean extends VOBase<Long> {
	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -147803416909577393L;
	private Integer size = 0;
	private String[] uuids;

	public UUIDBean() {
		this.size=0;
		this.uuids=new String[0];
		status=SUCCESS;
	}

	public UUIDBean(String[] uuids) {
		if(ObjUtil.isEmpty(uuids)){
			this.size=0;
			this.uuids=new String[0];
		}else{
			this.uuids=uuids;
			this.size=uuids.length;
		}
		status=SUCCESS;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String[] getUuids() {
		return uuids;
	}

	public void setUuids(String[] uuids) {
		this.uuids = uuids;
	}

}
