package org.csr.core.security.resource;

import java.util.List;

import org.csr.core.SecurityResource;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

/**
 * ClassName:FunctionPointNode.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014-1-27 上午9:31:56 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class SecurityResourceBean extends VOBase<Long> implements
		SecurityResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3614667370025562971L;
	private Long id;
	private String name;
	private String pName;
	private String code;
	private String forwardUrl;
	private String urlRule;
	private Byte functionPointType;
	private String remark;
	private List<SecurityResource> children;
	private Byte browseLogLevel;
	private Byte operationLogLevel;

	public SecurityResourceBean(){}
	
	public SecurityResourceBean(String forwardUrl){
		this.forwardUrl=forwardUrl;
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

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getUrlRule() {
		return urlRule;
	}

	public void setUrlRule(String urlRule) {
		this.urlRule = urlRule;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setChildren(List<SecurityResource> children) {
		this.children = children;
	}

	public List<SecurityResource> getChildren() {
		return children;
	}

	public Byte getFunctionPointType() {
		return functionPointType;
	}

	public void setFunctionPointType(Byte functionPointType) {
		this.functionPointType = functionPointType;
	}

	public Byte getBrowseLogLevel() {
		return browseLogLevel;
	}

	public void setBrowseLogLevel(Byte browseLogLevel) {
		this.browseLogLevel = browseLogLevel;
	}

	public Byte getOperationLogLevel() {
		return operationLogLevel;
	}

	public void setOperationLogLevel(Byte operationLogLevel) {
		this.operationLogLevel = operationLogLevel;
	}

	public static SecurityResourceBean toNode(SecurityResource value) {
		if(ObjUtil.isEmpty(value)){
			return null;
		}
		SecurityResourceBean node = new SecurityResourceBean();
		node.setId(value.getId());
		node.setName(value.getName());
		node.setCode(value.getCode());
		node.setForwardUrl(value.getForwardUrl());
		node.setUrlRule(value.getUrlRule());
		node.setBrowseLogLevel(value.getBrowseLogLevel());
		node.setOperationLogLevel(value.getOperationLogLevel());
		return node;
	}

}
