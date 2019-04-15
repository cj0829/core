package org.csr.core.tree;

import java.util.ArrayList;
import java.util.List;

public class ETreeNode implements TreeNode<Long> {

	/** 绑定节点的标识值。 */
	Long id;

	Long parentId;
	String name;
	/** 显示的节点文本。 */
	String text;
	/** 显示的节点图标CSS类ID。 */
	String iconCls;
	/** 该节点是否被选中。 */
	boolean checked;
	/** 节点状态，'open' 或 'closed'。 */
	String state;
	/** 绑定该节点的自定义属性。 */
	String attributes;
	/** 目标DOM对象。 */
	String target;

	List children=new ArrayList();;
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentId(Long parentId) {
		this.parentId=parentId;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public List<? extends TreeNode> getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	public void setChildren(List<TreeNode> arrayList) {
		this.children=arrayList;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
