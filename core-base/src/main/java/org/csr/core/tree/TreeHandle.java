package org.csr.core.tree;

import java.util.List;

/**
 * ClassName:TreeHandle.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午10:07:32 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public abstract class TreeHandle<V,N,PK> {
	
	public boolean isRoot(N node){
		if(getParentId(node)==null){
			if(rootId()==null){
				return true;
			}else{
				return false;
			}
		}else{
			return getParentId(node).equals(rootId());
		}
	}
	
	public abstract  boolean setBean(V value, N node,N parent);
	
	public abstract N newTreeNode(V value) throws IllegalArgumentException;
	
	public abstract PK getId(N node);
	
	public abstract PK rootId();
	
	public abstract PK getParentId(N node);
	
	public abstract List<N> children(N parent);

}