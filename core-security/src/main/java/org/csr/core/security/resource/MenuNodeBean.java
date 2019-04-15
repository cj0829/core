/**
 * Project Name:core
 * File Name:MenuNodeBean.java
 * Package Name:org.csr.core.security.resource
 * Date:2014年9月16日下午7:01:59
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.security.resource;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import org.csr.core.AutoSetProperty;
import org.csr.core.MenuNode;
import org.csr.core.SecurityResource;
import org.csr.core.tree.TreeNode;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

/**
 * ClassName:MenuNodeBean.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014年9月16日下午7:01:59 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class MenuNodeBean extends VOBase<Long> implements MenuNode,TreeNode<Long> {

	/**
	 * (用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	@AutoSetProperty(message="父节点",required=false)
	private MenuNode parent;
	private Long parentId;
	private String name;
	private String icon;
	private String defIcon;
	@AutoSetProperty(message="功能点",required=false)
	private SecurityResource functionPoint;
	private Byte display;
	private Integer rank;
	/**
	 * 子总数
	 */
	private int childCount;
	/** 描述 */
	protected String remark;
	/**
	 * 子菜单
	 */
	private List<MenuNode> children = new ArrayList<MenuNode>(0);

	public MenuNodeBean() {
	}
	public MenuNodeBean(Long id,String name) {
		this.id=id;
		this.name=name;
	}
	public MenuNodeBean(MenuNode doMain) {
		this.setId(doMain.getId());
		this.setIcon(doMain.getIcon());
		
		if (ObjUtil.isNotEmpty(doMain.getParent())) {
			this.setParentId(doMain.getParent().getId());
//			this.setParent(new MenuNodeBean(doMain.getParent()));
		}
		
		this.setDisplay(doMain.getDisplay());
		this.setName(doMain.getName());
		this.setSecurityResource(doMain.getSecurityResource());
		this.setDefIcon(doMain.getDefIcon());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public MenuNode getParent() {
		return this.parent;
	}

	public void setParent(MenuNode parent) {
		this.parent = parent;
	}

	@Override
	public Byte getDisplay() {
		return display;
	}

	public void setDisplay(Byte display) {
		this.display = display;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String getDefIcon() {
		return defIcon;
	}

	public void setDefIcon(String defIcon) {
		this.defIcon = defIcon;
	}

	/**
	 * 获取子
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getChildren() {
		return this.children;
	}
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setChildren(List arrayList) {
		this.children=arrayList;
	}
	@Transient
	public Integer getChildCount() {
		return childCount;
	}

	public void setSecurityResource(SecurityResource functionPoint) {
		this.functionPoint = functionPoint;
	}

	@Override
	public SecurityResource getSecurityResource() {
		return functionPoint;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}



}
