package org.csr.core.security.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.csr.core.Authentication;
import org.csr.core.UserSession;
import org.csr.core.cache.CacheApi;
import org.csr.core.cache.CacheFactory;
import org.csr.core.exception.Exceptions;
import org.csr.core.security.authority.AuthenticationFilter;
import org.csr.core.security.authority.UsernamePasswordToken;
import org.csr.core.security.exception.SecurityExceptions;
import org.csr.core.userdetails.SecurityUser;
import org.csr.core.util.Md5PwdEncoder;
import org.csr.core.util.PropertiesUtil;
import org.csr.core.web.bean.ReturnMessage;

/**
 * ClassName:LoginAuthenticationFilter.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-18上午10:59:00 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 *  登录过滤器
 */
public class LoginAuthenticationFilter extends AuthenticationFilter {

	CacheApi cacheApi = CacheFactory.createApi(PropertiesUtil.getConfigureValue("cache.type"));
	public static final String VALIDATE_CODE = "validateCode";
	public static final String APP_TYPE = "apptype";
	public static final String LOGINNAME = "loginName";
	public static final String PASSWORD = "password";
	/**
	 * 只允许post请求
	 */
	private boolean postOnly = true;

	// ~ Constructors
	// ===================================================================================================
	public LoginAuthenticationFilter() {
		super();
	}

	/**
	 * 登录安全验证
	 * @throws IOException 
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {
		if (postOnly && !request.getMethod().equals("POST")) {
			Exceptions.service(request.getMethod(), "");
		}
		String obtainAppType = obtainAppType(request);
		String tu = PropertiesUtil.getConfigureValue("isNotcheckValidateCode");
		// 登录验证码
		if("web".equals(obtainAppType)){
			if(!"true".equals(tu)){
				checkValidateCode(request);
			}
		}
		
//		String requestHeader = request.getHeader("user-agent");
//		if (isMobileDevice(requestHeader)) {
//			logger.debug("使用手机浏览器");
//		} else {
//			logger.debug("使用web浏览器");
//		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		// 验证用户账号与密码是否对应
		username = username.trim();
		SecurityUser user = getAuthenticationService().findByLoginName(username);
		if (user == null || !user.getPassword().equals(password)) {
//			SecurityExceptions.authentication("password or username is notEquals", "", actionMode);
			ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,"用户名密码错误");
			request.getSession().setAttribute("login_error_message", "用户名密码错误");
			returnHandler.handle(request, response).returnHandle(errorURL, actionMode, message);
			return null;
		}
		
		UsernamePasswordToken authRequest = new UsernamePasswordToken(username);
		// 允许子类设置详细属性
		UserSession userSession=getAuthenticationService().setUserSession(user);
		//获取当前类型，是采用app，登录还是，后台登录
		userSession.setApptype(obtainAppType);
		
		//清除上次登录的权限信息
		cacheApi.del(userSession.getUserId()+"_ResourcesByUser");
		authRequest.setUserSession(userSession);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication（权限）
		return authRequest;
	}

	/**
	 * 验证码校验
	 * 
	 * @param request
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String sessionValidateCode = obtainSessionValidateCode(session);
		// 让上一次的验证码失效
		session.setAttribute(VALIDATE_CODE, null);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			SecurityExceptions.authentication("validateCode.notEquals", "", actionMode);
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainSessionValidateCode(HttpSession session) {
		Object obj = session.getAttribute(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(LOGINNAME);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainPassword(HttpServletRequest request) {
		String password = Md5PwdEncoder.encodePassword(request.getParameter(PASSWORD));
		return null == password ? "" : password.toString();
	}
	protected String obtainAppType(HttpServletRequest request) {
		String apptype = request.getParameter(APP_TYPE);
		return null == apptype ? "web" : apptype.toString();
	}
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public boolean isMobileDevice(String requestHeader) {
		/**
		 * android : 所有android设备 mac os : iphone ipad windows
		 * phone:Nokia等windows系统的手机
		 */
		String[] deviceArray = new String[] { "android", "mac os", "windows phone" };
		if (requestHeader == null){
			return false;
		}
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}
}
