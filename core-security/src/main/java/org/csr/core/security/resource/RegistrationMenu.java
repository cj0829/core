/**
 * Project Name:core
 * File Name:RegistrationMenu.java
 * Package Name:org.csr.core.security.menu
 * Date:2014年9月15日上午10:42:40
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.security.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.MenuNode;
import org.csr.core.SecurityResource;
import org.csr.core.constant.MenuDisplay;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:RegistrationMenu.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014年9月15日上午10:42:40 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public abstract class RegistrationMenu {
	protected final Log logger = LogFactory.getLog(getClass());

	public abstract String registration(HttpServletRequest request,List<SecurityResource> securityResources,Long rootId);

	// 存放功能点
	protected List<MenuNodeBean> filterMenu(List<MenuNodeBean> menus,
			List<SecurityResource> securityResources, Long menuType) {
		if (ObjUtil.isEmpty(menus)) {
			return menus;
		}  else {
			// 创建新的对象并封装结构（封装新的node节点对象，及返回新的list）
			List<MenuNodeBean> treeMenus = wrapTree(menus, menuType);
			// 过滤权限,如果是超级管理员，则不需要过滤权限
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
			if (!Constants.USER_SUPER.equals(authentication.getPrincipal())) {
				verifyNode(treeMenus, securityResources);
			}
			return treeMenus;
		}
	}

	private void verifyNode(List<MenuNodeBean> treeMenus,
			List<SecurityResource> securityResources) {
		for (Iterator<MenuNodeBean> it = treeMenus.iterator(); it.hasNext();) {
			MenuNodeBean node = it.next();
			@SuppressWarnings("unchecked")
			List<MenuNodeBean> child = node.getChildren();
			if (ObjUtil.isNotEmpty(child)) {
				verifyNode(child, securityResources);
				if (ObjUtil.isEmpty(node.getChildren()) && ObjUtil.isEmpty(node.getSecurityResource())) {
					it.remove();
				}
			} else {
				if(ObjUtil.isNotEmpty(node.getSecurityResource())){
				}
				if (!verifyResource(securityResources,node.getSecurityResource())) {
					it.remove();
				}
			}
		}
	}

	private boolean verifyResource(List<SecurityResource> securityResources, SecurityResource sr) {
		if (ObjUtil.isNotEmpty(sr)) {
//			System.out.println(sr.getName());
			for (SecurityResource resource : securityResources) {
				if (sr.getId().equals(resource.getId())) {
					logger.debug("不删除"+resource.getName());
					return true;
				}
			}
		}
		return false;
	}
	
	
	protected boolean filterParent(MenuNode menu,MenuNode comparison){
		while(menu!=null){
			if(menu.getId().equals(comparison.getId())){
				return true;
			}
			menu=menu.getParent();
		}
		return false;
		
	}

	/**
	 * 返回一个新的数组，及全新的node节点对象
	 * @param menus
	 * @param menuType
	 * @return
	 */
	protected List<MenuNodeBean> wrapTree(List<MenuNodeBean> menus, Long menuType) {

		List<MenuNodeBean> gridRows = new ArrayList<MenuNodeBean>();
		if (menus == null) {
			return gridRows;
		}
		// 创建新对象
		List<MenuNodeBean> newMenus = new ArrayList<MenuNodeBean>();
		Map<Long, MenuNodeBean> mapNode = new TreeMap<Long, MenuNodeBean>();
		for (int i = 0; i < menus.size(); i++) {
			MenuNode value = menus.get(i);
			if(MenuDisplay.NO.equals(value.getDisplay())){
				continue;
			}
			MenuNodeBean bean = new MenuNodeBean(value);
			if(ObjUtil.isNotEmpty(bean.getSecurityResource())){
			}
			newMenus.add(bean);
			mapNode.put(bean.getId(), bean);
		}

		for (int i = 0; i < newMenus.size(); i++) {
			MenuNodeBean gridNode = newMenus.get(i);
			if (ObjUtil.isEmpty(gridNode.getParentId())) {
				continue;
			}
			MenuNodeBean parent = mapNode.get(gridNode.getParentId());
			gridNode.setParent(parent);
			if (menuType.equals(gridNode.getParentId())) {
				gridRows.add(gridNode);
			}
			gridNode.getParent().getChildren().add(gridNode);
		}
		return gridRows;
	}

}
