package org.csr.core.tree;

import java.io.Serializable;
import java.util.List;

public interface TreeNode<PK extends Serializable> extends Serializable {
	
	PK getId();
	
	void setParentId(PK parentId);
	
	PK getParentId();
	
	List<? extends TreeNode> getChildren();

	void setChildren(List<TreeNode> arrayList);
}
