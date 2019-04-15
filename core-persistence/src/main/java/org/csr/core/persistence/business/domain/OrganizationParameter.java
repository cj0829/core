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
 * 组织机构参数由系统管理员设置，来控制机构的数据范围，<br>
 * 比如最多允许创建100个用户等；如果没有设置，默认从系统参数中取值；
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "c_OrganizationParameter")
@DynamicInsert(true)
@DynamicUpdate(true)
@Comment(en="c_OrganizationParameter",ch="组织机构参数由系统管理员设置，来控制机构的数据范围，比如最多允许创建100个用户等；如果没有设置，默认从系统参数中取值；")
public class OrganizationParameter extends RootDomain<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992518048058164924L;
	private Long id;
	private String name;
	private String value;
	private Long orgId;
	
	public OrganizationParameter() {
	}

	public OrganizationParameter(Long id) {
		this.id = id;
	}

	public OrganizationParameter(Long orgId,String name, String value) {
		this.orgId = orgId;
		this.name = name;
		this.value = value;
	}
	
	public OrganizationParameter(Long id, String name, String value,
			Long orgId) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.orgId = orgId;
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
	@Comment(ch="机构参数名称")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "vlaue", length = 512)
	@Comment(ch="机构参数值")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	@Column(name = "orgId")
	@Comment(ch="域id",en="orgId")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	
}
