/**
 * Project Name:core
 * File Name:MenuShwo.java
 * Package Name:org.csr.core.web.bean
 * Date:2014-3-8上午10:32:00
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.web.bean;


/**
 * ClassName:MenuShwo.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014-3-8上午10:32:00 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class MenuShow extends VOBase<Long> {

	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -412607290541878680L;
	private Long userId;
	private Long menuId;
	private boolean show;

	public MenuShow(){}
	
	public MenuShow(Long userId, Long menuId, boolean show) {
		this.userId = userId;
		this.menuId = menuId;
		this.show = show;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
