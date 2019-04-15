package org.csr.core.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class SimpleDomain<PK extends Serializable> extends RootDomain<PK>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5254176183930998342L;

	
	protected Long createdBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	protected Date createTime;
	protected Long updateBy;
	protected Long updateTime;
	/** 是否删除 */
	protected boolean isDelete=false;
	private int version;
	
	@Column(name = "createdBy")
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "createTime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "updateBy")
	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(final Long updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "updateTime")
	public Long getUpdateTime() {
		return null == updateTime ? null : updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "isDelete")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Version
	public int getVersion() {
		return version;
	}

	@SuppressWarnings("unused")
	private void setVersion(int version) {
		this.version = version;
	}
	
	public boolean equals(final Object obj) {
		if (!(obj instanceof SimpleDomain)) {
			return false;
		}
		Object myKey = this.getId();
		Object outKey = ((SimpleDomain<?>) obj).getId();
		if ((myKey == null) || (outKey == null)) {
			return (this == obj);
		} else {
			return (myKey.equals(outKey));
		}
	}
}
