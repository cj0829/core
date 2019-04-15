package org.csr.core;

import java.io.Serializable;
import java.util.List;

import org.csr.core.web.bean.MenuShow;

/**
 * 用户会话的个人资料信息.
 * 
 * @author caijin
 * 
 */

public abstract interface UserSession extends Serializable {

	public Long getUserId();
	
	public String getUserName();

	public String getLoginName();

	public long getLoginTime();
	
	public SecurityResource getSecurityResource();

	public void setSecurityResource(SecurityResource securityResource);

	public List<SecurityResource> getResources();

	public void setResources(List<SecurityResource> resources);

	public void setAllFunCode(String funCode);

	public void setUrl(String url);

	public void setSelectMenu(MenuNode selectMenu);

	public MenuShow getMenuShow(Long menuId);
	
//	public void setSkinName(String SkinName);
	
	public String getSkinName();

	public String getClientAddr();
	
	public void setClientAddr(String clientAddr);

	public Object getClientInfo();
	
	public Long getLoginLogId();
	
	public void setLoginLogId(Long loginLogId);

	public String getHomeUrl();

	public boolean isAnonymous();
	/**主域*/
	Long getPrimaryOrgId();

	public String getAvatarUrl();
	public String getMiddleheadUrl();
	public String getHeadUrl();

	public String getApptype();

	public void setApptype(String apptype);

	Long getAgenciesId();
}
