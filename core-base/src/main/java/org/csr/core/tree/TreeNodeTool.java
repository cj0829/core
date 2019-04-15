package org.csr.core.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNodeTool {
	
	
	public static List<TreeNode> treeToList(TreeNode treeNode){
		List<TreeNode> gridRows=new ArrayList<TreeNode>();
		gridRows.add(treeNode);
		@SuppressWarnings("unchecked")
		List<TreeNode> childs=(List<TreeNode>) treeNode.getChildren();
		for (int i = 0; i < childs.size(); i++) {
			TreeNode cTn=childs.get(i);
			gridRows.addAll(treeToList(cTn));
		}
		return gridRows;
	}
	
	
	public static List<TreeNode> toList(List<? extends TreeNode> treeNodes){
		List<TreeNode> gridRows=new ArrayList<TreeNode>();
		for (int i = 0; i < treeNodes.size(); i++) {
			gridRows.addAll(treeToList(treeNodes.get(i)));
		}
		return gridRows;
	}
	
	
	public static <V,N,PK> List<N> toResult(List<V> datas,TreeHandle<V,N,PK> handle){
		List<N> gridRows=new ArrayList<N>();
		if(datas==null){
			return gridRows;
		}
		//缓存对象
		Map<PK,V> mapNode = new HashMap<PK,V>();
		for (int i = 0;i < datas.size(); i++) {
			V value=datas.get(i);
			N gridNode= handle.newTreeNode(value);
			mapNode.put(handle.getId(gridNode),value);
			gridRows.add(gridNode);
		}
		//利用map构建tree数据结构
		Map<PK,N> map=asMap(handle,gridRows);
		
		ArrayList<N> result=new ArrayList<N>();
		for (int i = 0; i < gridRows.size(); i++) {
			N gridNode =  gridRows.get(i);
			if(handle.isRoot(gridNode)){
				if(handle.setBean(mapNode.get(handle.getId(gridNode)),gridNode,map.get(handle.getParentId(gridNode)))){
					result.add(gridNode);
				}
			}else{
				N parent=map.get(handle.getParentId(gridNode));
				if(parent!=null){
					if(handle.setBean(mapNode.get(handle.getId(gridNode)),gridNode,map.get(handle.getParentId(gridNode)))){
						handle.children(parent).add(gridNode);
					}
				}
			}
			
		}
		return result;
	}
	
	/**
	 * 更改到 接口：{@code org.csr.core.tree.TreeHandle} 的 isRoot()方法中
	 * compareRootId: 比较parentId，与 rootId 是否相同，如果相同，那么能把当前作为，最高等级node节点<br/>
	 * 就是为了区分出第一级节点。一般在实际使用中，采用更改parentId的返回值，来作为第一级节点使用
	 * @author caijin 
	 * @param parentId
	 * @param rootId
	 * @return
	 * @since JDK 1.7
	 */
	/*private static <PK> boolean compareRootId(PK parentId,PK rootId){
		if(parentId==null){
			if(rootId==null){
				return true;
			}else{
				return false;
			}
		}else{
			return parentId.equals(rootId);
		}
	}*/

	private static<V,N,PK> Map<PK,N> asMap(TreeHandle<V,N,PK> handle,List<N> nodes) {
		Map<PK,N> map = new HashMap<PK,N>();
		if (nodes!=null) {
			for (int i = 0; i < nodes.size(); i++) {
				N n =  nodes.get(i);
				map.put(handle.getId(n), n);
			}
		}
		return map;
	}
}
