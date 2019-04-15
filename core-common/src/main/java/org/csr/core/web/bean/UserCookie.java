package org.csr.core.web.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.csr.core.userdetails.SecurityUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ClassName:UserCookie.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午10:07:45 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class UserCookie {
	/**
     * 保存用户登陆状态到cookie
     * @param Users
     */
	public static void addUserInCookie(SecurityUser users) { 	 
		try {
			String cookieKey = "MemUsers";
        	StringBuffer cookieValue = new StringBuffer(users.getLoginName());
        	cookieValue.append(",").append(users.getId());  
        	cookieValue.append(",").append(users.getLoginName()); 
        	
			Cookie cook = new Cookie(cookieKey, URLEncoder.encode(cookieValue.toString(), "UTF-8"));
    		cook.setPath("/");
    		cook.setMaxAge(7 * 24 * 3600); 
//    		ServletActionContext.getResponse().addCookie(cook); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	} 
    /**
     * 退出登陆 
     */
    public static void removeUserInCookie(){
    	 HttpServletRequest request = getRequest();
		 Cookie[] cookies = request.getCookies();   
		 if(cookies != null){
	         for (Cookie cookie : cookies){
	            if (cookie.getName().startsWith("MemUsers")){ 
	                // 使cookie失效
	                cookie.setMaxAge(0);
	                cookie.setPath("/");
//	                ServletActionContext.getResponse().addCookie(cookie);
	            }
	         }
		 }
//         request.getSession().removeAttribute(Constants.USERSESSION);
    }
    protected static HttpServletRequest getRequest() {
		ServletRequestAttributes  attrs = (ServletRequestAttributes ) RequestContextHolder.getRequestAttributes();
		return attrs.getRequest();
	}
}
