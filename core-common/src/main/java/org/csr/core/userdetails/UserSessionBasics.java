package org.csr.core.userdetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.csr.core.MenuNode;
import org.csr.core.SecurityResource;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.MenuShow;

/**
 * 用户会话的个人资料信息.
 * 
 * @author caijin
 * 
 */

public class UserSessionBasics extends AbstractUserSession {
	private static final long serialVersionUID = 1L;
	/** 用户id */
	private Long userId;
	/** 用户名字 */
	private String userName;
	/** 登录登录名 */
	private String loginName;
	/** 个人头像地址 */
	private String photoUrl;
	/** 用户所选的菜单状态 */
	private Map<Long, MenuShow> menuShow = new HashMap<Long, MenuShow>();
	/** 用户所选的菜单 */
	private MenuNode selectMenu;
	/** 当前的功能点 当前运行的功能点 */
	private SecurityResource securityResource;
	/** 当前用户的全部访问资源 */
	List<SecurityResource> resources;
	/** 当期访问的url */
	private String url;
	/** 全部的功能点编码 */
	private String allFunCode;
	/** 所属根机构别名 */
	private String aliases;
	/** 主域 */
	private Long primaryOrgId;
	/** 机构id */
	private Long agenciesId;
	/** 机构id */
	private String agenciesName;
	/** 登录日志id */
	private Long loginLogId;
	/** 用户登录时间 */
	private long loginTime;
	/** ip地址 */
	private String clientAddr;
	/** ip客户端信息 */
	private String clientInfo;
	// ======================================================
	/** 样式名称 */
	private String skinName = "default";
	/** 菜单样式 */
	private String menuStyle;
	/** 主屏幕表格显示条数 */
	private Integer mainShowNum = 10;
	/** 弹出表格显示条数 */
	private Integer winNum = 10;
	/** 默认跳转功能点id */
	private Long dufHome;
	/** 默认跳转功能点Url */
	private String forwardUrl;
	/** 区域设置语言 */
	private Long areaLanguagesId;
	/** 区域设置通知方法 */
	private Long notificationMethodId;
	/** 位置 */
	private Long locationId;
	/** 语言 */
	private Long languagesId;
	/**需要显示的消息个数*/
	private Integer messageNum;
	/**用户小头像uuid*/
	private String avatarUrl;
	/**用户中头像uuid*/
	private String middleheadUrl;
	/**用户大头像uuid*/
	private String headUrl;
	/**积分*/
	private Integer points1;
	/**说说*/
	private String saysome;
	
	/**说说*/
	private String apptype;

	public UserSessionBasics(String loginName) {
		this.loginName = loginName;
		this.loginTime = System.currentTimeMillis();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getSkinName() {
		if (ObjUtil.isNotEmpty(skinName)) {
			return skinName;
		}
		return "dark";
	}


	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	@Override
	public SecurityResource getSecurityResource() {
		return securityResource;
	}

	@Override
	public void setSecurityResource(SecurityResource securityResource) {
		this.securityResource = securityResource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAllFunCode() {
		return allFunCode;
	}

	/**
	 * 用户的主域<br>
	 * 
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public Long getPrimaryOrgId() {
		return primaryOrgId;
	}

	public void setPrimaryOrgId(Long primaryOrgId) {
		this.primaryOrgId = primaryOrgId;
	}

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public Long getLoginLogId() {
		return loginLogId;
	}

	public void setLoginLogId(Long loginLogId) {
		this.loginLogId = loginLogId;
	}

	public void setAllFunCode(String funCode) {
		this.allFunCode = funCode;
	}


	/**
	 * getMenuShow: 获取菜单的前台显示状态 <br/>
	 * 
	 * @author caijin
	 * @param menuId
	 * @return
	 * @since JDK 1.7
	 */
	public MenuShow getMenuShow(Long menuId) {
		return menuShow.get(menuId);
	}

	/**
	 * addMenuShow: 增加或修改菜单显示状态 <br/>
	 * 
	 * @author caijin
	 * @param menuId
	 * @param show
	 * @since JDK 1.7
	 */
	public void addMenuShow(Long menuId, boolean show) {
		menuShow.put(menuId, new MenuShow(userId, menuId, show));
	}

	/**
	 * setResources: 设置全部访问资源 <br/>
	 * 
	 * @author caijin
	 * @param resources
	 * @since JDK 1.7
	 */
	public void setResources(List<SecurityResource> resources) {
		this.resources = resources;
	}

	/**
	 * getResources: 描述方法的作用 <br/>
	 * 
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public List<SecurityResource> getResources() {
		return resources;
	}

	// ///////////////////////////////分割

	public Map<Long, MenuShow> getMenuShow() {
		return menuShow;
	}

	public MenuNode getSelectMenu() {
		return selectMenu;
	}

	public void setSelectMenu(MenuNode selectMenu) {
		this.selectMenu = selectMenu;
	}

	/**
	 * 签到系统的背景条 getNveStyle: 描述方法的作用 <br/>
	 * 只提供考试使用
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public String getNveStyle() {
		if (ObjUtil.isEmpty(selectMenu)) {
			return "";
		} else {
			if(ObjUtil.isNotEmpty(selectMenu.getParent())){
				return selectMenu.getParent().getIcon();
			}else{
				return "";
			}
			
		}
	}

	public void setMenuShow(Map<Long, MenuShow> menuShow) {
		this.menuShow = menuShow;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle;
	}

	public Integer getMainShowNum() {
		return mainShowNum;
	}

	public void setMainShowNum(Integer mainShowNum) {
		this.mainShowNum = mainShowNum;
	}

	public Integer getWinNum() {
		return winNum;
	}

	public void setWinNum(Integer winNum) {
		this.winNum = winNum;
	}

	public Long getDufHome() {
		return dufHome;
	}

	public void setDufHome(Long dufHome) {
		this.dufHome = dufHome;
	}

	public String getHomeUrl() {
		return forwardUrl;
	}

	public Long getAreaLanguagesId() {
		return areaLanguagesId;
	}

	public void setAreaLanguagesId(Long areaLanguagesId) {
		this.areaLanguagesId = areaLanguagesId;
	}

	public Long getNotificationMethodId() {
		return notificationMethodId;
	}

	public void setNotificationMethodId(Long notificationMethodId) {
		this.notificationMethodId = notificationMethodId;
	}

	@Override
	public Long getAgenciesId() {
		return agenciesId;
	}

	public void setAgenciesId(Long agenciesId) {
		this.agenciesId = agenciesId;
	}
	
	public String getAgenciesName() {
		return agenciesName;
	}

	public void setAgenciesName(String agenciesName) {
		this.agenciesName = agenciesName;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getLanguagesId() {
		return languagesId;
	}

	public void setLanguagesId(Long languagesId) {
		this.languagesId = languagesId;
	}

	public void setHomeUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	@Override
	public long getLoginTime() {
		return loginTime;
	}

	@Override
	public String getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}
	@Override
	public String getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public static void main(String[] args) {

	}

	public Integer getMessageNum() {
		return messageNum;
	}
	public void setMessageNum(Integer messageNum) {
		this.messageNum = messageNum;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	public String getMiddleheadUrl() {
		return middleheadUrl;
	}

	public void setMiddleheadUrl(String middleheadUrl) {
		this.middleheadUrl = middleheadUrl;
	}

	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public Integer getPoints1() {
		return points1;
	}
	public void setPoints1(Integer points1) {
		this.points1 = points1;
	}
	public String getSaysome() {
		return saysome;
	}
	public void setSaysome(String saysome) {
		this.saysome = saysome;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public String getApptype() {
		return apptype;
	}

	@Override
	public void setApptype(String apptype) {
		this.apptype=apptype;
	}
	
}
