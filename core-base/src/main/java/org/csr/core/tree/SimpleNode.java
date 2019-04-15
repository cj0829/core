package org.csr.core.tree;

import java.util.List;

/**
 * ClassName:SimpleNode.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午10:07:22 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class SimpleNode extends NodeBean {
	Long pId;
	@SuppressWarnings("rawtypes")
	private List children;
	public SimpleNode(Long pId, Long id, String name) {
		super(id, name);
		this.pId = pId;
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 * @param name
	 * @param type
	 */
	public SimpleNode(Long pId, Long id, String name, String type) {
		super(id, name, type);
		this.pId = pId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void setParentId(Long parentId) {
		this.pId = parentId;
		
	}

	public Long getParentId() {
		return pId;
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> arrayList) {
		this.children=arrayList;
	}
}
