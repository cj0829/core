package org.csr.core.persistence.business.bean;


import java.util.List;

import org.csr.core.persistence.business.domain.Dictionary;
import org.csr.core.tree.TreeNode;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

/**
 * ClassName:DictionaryNode.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2019年4月5日下午2:35:55 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class DictionaryNode extends VOBase<Long> implements TreeNode<Long> {

	private static final long serialVersionUID = 5537174725798414935L;
	private Long id;
	private Long parentId;
	private String dictType;
	private String dictValue;
	private Byte hasChild;
	private Integer childCount;
	private Long rank;
	private String remark;
	@SuppressWarnings("rawtypes")
	private List<TreeNode> children;
//	private String state = "open";
	
	public DictionaryNode() {
	}

	public DictionaryNode(Long id) {
		this.id = id;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDictType() {
		return this.dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictValue() {
		return this.dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public Byte getHasChild() {
		return this.hasChild;
	}

	public void setHasChild(Byte hasChild) {
		this.hasChild = hasChild;
	}

	public Long getRank() {
		return this.rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}


	public Long getParentId() {
		return parentId;
	}

	
	@Override
	public void setChildren(@SuppressWarnings("rawtypes") List<TreeNode> children) {
		this.children = children;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TreeNode> getChildren() {
		return children;
	}

	@Override
	public void setParentId(Long parentId) {
		this.parentId=parentId;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		if(children==null){
			return "open";
		}else{
			if(children.size()>0){
				return "closed";
			}else{
				return "open";
			}
		}
	}

//	/**
//	 * @param state the state to set
//	 */
//	public void setState(String state) {
//		this.state = state;
//	}
	
	public static DictionaryNode toNode(Dictionary value){
		DictionaryNode node = new DictionaryNode();
		node.setId(value.getId());
		node.setDictType(value.getDictType());
		node.setDictValue(value.getDictValue());
		if(ObjUtil.isNotEmpty(value.getDictionary())){
			node.setParentId(value.getDictionary().getId());
		}
		node.setChildCount(value.getChildCount());
		node.setRemark(value.getRemark());
		node.setHasChild(value.getHasChild());
		node.setRank(value.getRank());
		return node;
	}
}
