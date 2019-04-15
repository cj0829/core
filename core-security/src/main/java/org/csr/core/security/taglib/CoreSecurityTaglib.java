/**
 * Project Name:core
 * File Name:CoreSecurityTaglib.java
 * Package Name:org.csr.core.web.taglib
 * Date:2015-12-30上午10:41:52
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
*/

package org.csr.core.security.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.csr.core.Constants;
import org.csr.core.SecurityResource;
import org.csr.core.userdetails.UserSessionContext;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:CoreSecurityTaglib.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2015-12-30上午10:41:52 <br/>
 * @author   Administrator <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class CoreSecurityTaglib extends SimpleTagSupport{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private String code = "";
	
	private String yes = "yes";
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getYes() {
		return yes;
	}
	public void setYes(String yes) {
		this.yes = yes;
	}
	
	public void doTag() throws JspException, IOException {
	
		
		if(Constants.USER_SUPER.equals(UserSessionContext.getUserSession().getLoginName())){
			if("yes".equals(yes)){
				getJspBody().invoke(null);//每次invoke，都会执行一次标签body
			}
			return;
		}
		
		boolean isExt = false;
		if(ObjUtil.isNotBlank(code)){
			List<SecurityResource> resourcesList = UserSessionContext.getUserSession().getResources();
			if(ObjUtil.isNotEmpty(resourcesList)){
				for (SecurityResource securityResource : resourcesList) {
					if(code.equals(securityResource.getCode())){
						isExt=true;
						break;
					}
				}
			}
		}
		if("yes".equals(yes) && isExt){
			getJspBody().invoke(null);//每次invoke，都会执行一次标签body
		}else if("no".equals(yes) && !isExt){
			getJspBody().invoke(null);//每次invoke，都会执行一次标签body
		}
	}
}

