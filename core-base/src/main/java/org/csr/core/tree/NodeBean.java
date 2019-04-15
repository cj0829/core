package org.csr.core.tree;



public abstract class NodeBean implements TreeNode<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -640562428941137351L;
	/**
	 * 
	 */
	private Long id;
	/** 节点名称。 */
	private String name = "";

	/** 
	 * 设置节点的 checkbox / radio 是否禁用 
	 *  默认值：false
	 * */
	private boolean chkDisabled = false;
	/** 当显示了checkBox时是否进行选中 */
	private boolean checkBox = false;

	/**
	 * true 表示节点的输入框 强行设置为半勾选<br>
	 * false 表示节点的输入框 根据 zTree 的规则自动计算半勾选状态<br>
	 * 默认值：false
	 * */
	private boolean halfCheck = false;
	/** 
	 * 记录 treeNode 节点的 展开 / 折叠 状态
	 * true 表示节点为 展开 状态
	 * false 表示节点为 折叠 状态
	 */
	private boolean open = false;
	/** 
	 * 记录 treeNode 节点是否为父节点
	 * 初始化节点数据时，如果设定 treeNode.isParent = true，即使无子节点数据，也会设置为父节点
	 * false 表示节点为 折叠 状态
	 */
	private boolean isParent = false;

	/** 
	 * 设置节点是否隐藏
	 * true 表示此节点不显示 checkbox / radio，不影响勾选的关联关系，不影响父节点的半选状态。
	 * false 表示节点具有正常的勾选功能
	 */
	private boolean nocheck = false;

	/**
	 * 父节点自定义展开时图标的 URL 路径。
	 * 1、此属性只针对父节点有效
	 * 2、此属性必须与 iconClose 同时使用

	 */
	private String iconOpen = "";
	/**
	 * 父节点自定义折叠时图标的 URL 路径。
	 * 1、此属性只针对父节点有效
	 * 2、此属性必须与 iconOpen 同时使用
	 */
	private String iconClose = "";
	/**
	 * 节点自定义图标的 URL 路径。
	 * 
	 */
	private String icon = "";
	/**
	 * 节点链接的目标 URL
	 */
	private String url = "";
	/**
	 * 设置点击节点后在何处打开 url。[treeNode.url 存在时有效]
	 */
	private String target = "";
	
	
	private String rootId;

	private String type;
	
	private String status;
	
	public NodeBean(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @param id
	 * @param name
	 * @param type
	 */
	public NodeBean(Long id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** 节点名称。 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** 
	 * 设置节点的 checkbox / radio 是否禁用 
	 *  默认值：false
	 * */
	public boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	/** 当显示了checkBox时是否进行选中 */
	public boolean getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

	/**
	 * true 表示节点的输入框 强行设置为半勾选<br>
	 * false 表示节点的输入框 根据 zTree 的规则自动计算半勾选状态<br>
	 * 默认值：false
	 * */
	public boolean getHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	/** 
	 * 记录 treeNode 节点的 展开 / 折叠 状态
	 * true 表示节点为 展开 状态
	 * false 表示节点为 折叠 状态
	 */
	public boolean getOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	/** 
	 * 记录 treeNode 节点是否为父节点
	 * 初始化节点数据时，如果设定 treeNode.isParent = true，即使无子节点数据，也会设置为父节点
	 * false 表示节点为 折叠 状态
	 */
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	/** 
	 * 设置节点是否隐藏
	 * true 表示此节点不显示 checkbox / radio，不影响勾选的关联关系，不影响父节点的半选状态。
	 * false 表示节点具有正常的勾选功能
	 */
	public boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	/**
	 * 父节点自定义折叠时图标的 URL 路径。
	 * 1、此属性只针对父节点有效
	 * 2、此属性必须与 iconOpen 同时使用
	 */
	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the rootId
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * @param rootId the rootId to set
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
