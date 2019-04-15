package org.csr.core.web.bean;

import java.io.Serializable;

import org.csr.core.Persistable;

/**
 * ClassName:BeanBase.java <br/>
 * System Name： 核心框架<br/>
 * Date: 2019年4月3日下午1:45:17 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public abstract class VOBase<ID extends Serializable> implements
		Persistable<ID> {

	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -6121045037474073031L;
	/** 成功 */
	public final static int SUCCESS = 1;
	/** 失败 */
	public final static int FAIL = 0;
	/**
	 * 请求状态返回状态
	 */
	protected String code = "";
	/**
	 * 请求状态返回状态
	 */
	protected Integer status = SUCCESS;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public ID getId() {
		return null;
	}

	@Override
	public void setId(ID id) {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!Persistable.class.isAssignableFrom(obj.getClass()))
			return false;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Persistable localBaseEntity = (Persistable<ID>) obj;
		return getId() != null ? getId().equals(localBaseEntity.getId()): false;
	}

	public int hashCode() {
		int i = 17;
		i += (getId() == null ? 0 : getId().hashCode() * 31);
		return i;
	}
}
