package org.csr.core.persistence.business.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.csr.core.Comment;
import org.csr.core.persistence.domain.RootDomain;
import org.csr.core.queue.Message;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "c_sysmethodtimelog")
@DynamicInsert(true)
@DynamicUpdate(true)
@Comment(ch = "系统方法执行日志", en = "c_sysmethodtimelog")
public class SysMethodTimeLog extends RootDomain<Long> implements Message<Object> {

	/***/
	private static final long serialVersionUID = 1L;
	/***/
	Long id;
	/** 日志创建时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/** 日志类名 */
	private String className;
	/** 方法名 */
	private String methodName;
	/** 方法执行时间 */
	private Integer methodTime;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "globalGenerator")
	@GenericGenerator(name = "globalGenerator", strategy = "org.csr.core.persistence.generator5.GlobalGenerator")
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "createTime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "className",length=128)
	@Length(min = 0, max = 128)
	@Comment(ch = "记录的对象", en = "className", vtype = "required:true", len = 40, search = true)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "methodName",length=64)
	@Length(min = 0, max = 64)
	@Comment(ch = "方法名称", en = "methodName", vtype = "required:true", len = 40, search = true)
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "methodTime")
	@Comment(ch = "方法执行时间", en = "methodTime", vtype = "required:true", search = true)
	public Integer getMethodTime() {
		return methodTime;
	}

	public void setMethodTime(Integer methodTime) {
		this.methodTime = methodTime;
	}

	@Override
	public Object body() {
		return this;
	}
}
