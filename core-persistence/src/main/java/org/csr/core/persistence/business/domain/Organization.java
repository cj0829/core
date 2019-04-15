package org.csr.core.persistence.business.domain;

// Generated Oct 29, 2013 10:37:52 AM by Hibernate Tools 3.2.0.b9

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.csr.core.Comment;
import org.csr.core.persistence.domain.RootDomain;
import org.csr.core.tree.TreeNode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

/**
 * 机构为多级树状结构，每一级的含义固定
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "c_Organization")
@Comment(ch="机构为多级树状结构，每一级的含义固定",en="pmt_core_organization")
public class Organization extends RootDomain<Long> implements TreeNode<Long> {

	/**
	 * 全局的根目录
	 */
	public static final Long global = 1l;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5512503303364481990L;
	private Long id;
	private Long parentId;
	private String name;
	private String aliases;
	private Long adminUserId;
	private Long adminRoleId;
	private Long safeResourceCollectionId;
	private Integer organizationLevel;
	private Integer leader;
	private Byte organizationStatus;
	protected boolean isDelete=false;
	protected String remark;
	
	public Organization() {
	}

	public Organization(Long id) {
		this.id = id;
	}

	public Organization(Long id, Long parentId, String name,
			Integer organizationLevel, Integer leader, String remark) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.organizationLevel = organizationLevel;
		this.leader = leader;
		this.remark = remark;
	}
	@Id
	@GeneratedValue(generator = "globalGenerator")  
	@GenericGenerator(name = "globalGenerator", strategy = "org.csr.core.persistence.generator5.GlobalGenerator")
	@Column(name = "id", unique = true, nullable = false)
	@Comment(ch="编号",en="id")
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "parentId")
	@Comment(ch="父域",en="parentId")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "name", length = 30)
	@Length(min=1,max=30)
	@Comment(ch="域名称",en="name",len=30,search=true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "aliases",length=128)
	@Length(min=0,max=128)
	@Comment(ch="别名（英文）",en="aliases")
	public String getAliases() {
		return aliases;
	}
	
	@Comment(ch="机构管理员",en="adminUserId")
	public Long getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(Long adminUserId) {
		this.adminUserId = adminUserId;
	}
	@Column(name = "adminRoleId")
	@Comment(ch="机构系统角色",en="adminRoleId")
	//用来存在，当前系统的功能的
	public Long getAdminRoleId() {
		return adminRoleId;
	}

	public void setAdminRoleId(Long adminRoleId) {
		this.adminRoleId = adminRoleId;
	}

	@Column(name = "safeResourceCollectionId")
	@Comment(ch="安全资源库",en="safeResourceCollectionId")
	public Long getSafeResourceCollectionId() {
		return safeResourceCollectionId;
	}
	public void setSafeResourceCollectionId(Long safeResourceCollectionId) {
		this.safeResourceCollectionId = safeResourceCollectionId;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	@Column(name = "organizationLevel",length=3)
	public Integer getOrganizationLevel() {
		return this.organizationLevel;
	}

	public void setOrganizationLevel(Integer organizationLevel) {
		this.organizationLevel = organizationLevel;
	}

	@Column(name = "leader",length=1)
	public Integer getLeader() {
		return this.leader;
	}

	public void setLeader(Integer leader) {
		this.leader = leader;
	}

	//机构状态：1.激活状态；2.冻结状态;
	@Column(name = "organizationStatus",length=1)
	public Byte getOrganizationStatus() {
		return organizationStatus;
	}

	public void setOrganizationStatus(Byte organizationStatus) {
		this.organizationStatus = organizationStatus;
	}
	
	@Column(name = "isDelete")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	//备注
	@Column(name = "remark",length=128)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	////////////////一下字段不作为数据库字段
	@SuppressWarnings("rawtypes")
	private List<TreeNode> children;
	@SuppressWarnings("rawtypes")
	@Transient
	public void setChildren(List<TreeNode> children) {
		this.children=children;
	}
	@SuppressWarnings("rawtypes")
	@Transient
	public List<TreeNode> getChildren() {
		return this.children;
	}
	/**是否存在子*/
	private Integer childCount;
	@Transient
	public Integer getChildCount() {
		return childCount;
	}
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	/**管理员名称*/
	private String adminUserName;
	@Transient
	public String getAdminUserName() {
		return adminUserName;
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	
}
