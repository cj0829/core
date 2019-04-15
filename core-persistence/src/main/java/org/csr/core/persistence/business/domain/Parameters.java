package org.csr.core.persistence.business.domain;

// Generated Oct 29, 2013 10:37:52 AM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.csr.core.Comment;
import org.csr.core.persistence.domain.RootDomain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 系统参数，存放于系统部署相关的可变参数，<br>
 * 如果初始化时缓存该数据，在页面修改后，要更新缓存；
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "c_Parameters")
@Comment(en="pmt_core_parameters",ch="系统参数表，存放部署参数，机构参数，用户参数")
public class Parameters extends RootDomain<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4147210364049710238L;
	private Long id;
	private String name;
	private String parameterValue;
	private Byte parameterType;
	private String remark;

	public Parameters() {
	}

	public Parameters(Long id) {
		this.id = id;
	}

	public Parameters(Long id, String name, String parameterValue,
			Byte parameterType, String remark) {
		this.id = id;
		this.name = name;
		this.parameterValue = parameterValue;
		this.parameterType = parameterType;
		this.remark = remark;
	}

	@Id
	@GeneratedValue(generator = "globalGenerator")  
	@GenericGenerator(name = "globalGenerator", strategy = "org.csr.core.persistence.generator5.GlobalGenerator")
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name", length = 128)
	@Comment(ch="系统参数名称")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "parameterValue", length = 512)
	@Comment(ch="参数值")
	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Column(name = "parameterType",length=1)
	@Comment(ch="参数类型：1. 部署参数; 2.机构参数;3.用户参数;")
	public Byte getParameterType() {
		return this.parameterType;
	}

	public void setParameterType(Byte parameterType) {
		this.parameterType = parameterType;
	}
	@Column(name = "remark",length=255)
	@Comment(ch="备注")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
