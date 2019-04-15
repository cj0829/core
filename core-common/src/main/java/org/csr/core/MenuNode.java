/**
 * Project Name:core
 * File Name:MenuNode.java
 * Package Name:org.csr.core.security.resource
 * Date:2014-2-18上午10:13:28
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:MenuNode.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-18上午10:35:04 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface MenuNode extends Serializable{

	Long getId();
	
	MenuNode getParent();
	
	String getName();
	
	SecurityResource getSecurityResource();

	List<MenuNode> getChildren();

	String getIcon();

	String getDefIcon();

	Byte getDisplay();
}

