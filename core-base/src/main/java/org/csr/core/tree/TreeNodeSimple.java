/**
 * Project Name:core
 * File Name:TreeNodeSimple.java
 * Package Name:org.csr.core.tree
 * Date:2014年11月13日下午5:47:17
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:TreeNodeSimple.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2014年11月13日下午5:47:17 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public abstract class TreeNodeSimple implements TreeNode<Long> {
	
	/**
	 * (用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 5815204796601865246L;
	protected List<TreeNode> children;
	public List<TreeNode> getChildren() {
		if (children == null) {
			children = new ArrayList<TreeNode>();
		}
		return children;
	}
	/**
	 * @param children  the children to set
	 */
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

}
