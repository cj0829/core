package org.csr.core.tree;

import java.util.ArrayList;
import java.util.List;





public abstract class TreeNodeHandle<V> extends TreeHandle<V,TreeNode,Object>{


	@Override
	public Long rootId() {
		return null;
	}
	@Override
	public Object getId(TreeNode node) {
		return node.getId();
	}

	@Override
	public Object getParentId(TreeNode node) {
		return node.getParentId();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TreeNode> children(TreeNode parent){
		if(parent==null){
			throw new NullPointerException();
		}
		if(parent.getChildren()==null){
			parent.setChildren(new ArrayList<TreeNode>());
		}
		return (List<TreeNode>) parent.getChildren();
	}


}