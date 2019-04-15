package org.csr.core.persistence.business.bean;

import org.csr.core.web.bean.VOBase;

/**
 * ClassName:OrganizationParameterResult.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class OrganizationParameterResult extends VOBase<Long>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Long organizationId;
	private String parameterValue;
	private Byte parameterType;
	private String remark;

	public OrganizationParameterResult(Long id, String name,
			Long organizationId, String parameterValue, Byte parameterType,
			String remark) {
		this.id = id;
		this.name = name;
		this.organizationId = organizationId;
		this.parameterValue = parameterValue;
		this.parameterType = parameterType;
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Byte getParameterType() {
		return parameterType;
	}

	public void setParameterType(Byte parameterType) {
		this.parameterType = parameterType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
