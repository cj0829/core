package org.csr.core.security.authority;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.Authentication;
import org.csr.core.UserSession;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.jump.SimpleJumpStrategy;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.exception.AuthenticationException;
import org.csr.core.security.exception.SecurityExceptions;
import org.csr.core.security.log.LoginLogSecurity;
import org.csr.core.security.session.ConcurrentSessionControl;
import org.csr.core.security.session.SessionAuthentication;
import org.csr.core.security.session.SessionRegistry;
import org.csr.core.security.session.SessionRegistryFactory;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.security.util.UrlUtils;
import org.csr.core.util.IpUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

/**
 * 认证
 * 
 * @author caijin
 * 
 */
public abstract class AuthenticationFilter extends BaseSecurityFilter {

	// ~ Instance fields
	// ================================================================================================

	protected ApplicationEventPublisher eventPublisher;
	/**
	 * 默认的安全认证链接 默认值为j_security_check
	 */
	public static String securityCheckUrl = "/j_security_check";
	public static final String LOGINURL = "loginUrl";
	/**
	 * ajax请求为true
	 */
	protected boolean actionMode = false;
	/**
	 * 登录验证失败返回的页面
	 */
	protected String errorURL = null;
	/**
	 * 登录成功跳转的页面
	 */
	protected String successUrl =null;
	
	protected  JumpStrategy returnHandler;
	/**
	 * 认证服务 策略
	 */
	private AuthenticationService authenticationService = new DefaultAuthenticationService();
	/**
	 * 登录日志
	 */
	private LoginLogSecurity loginLogSecurity=null;
	/**
	 * session策略
	 */
	private SessionAuthentication sessionAuthentication = null;
	private SessionRegistry sessionRegistry = SessionRegistryFactory.getInstance();

	private boolean allowSessionCreation = true;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * @param defaultFilterProcessesUrl
	 *            the default value for <tt>filterProcessesUrl</tt>.
	 */
	protected AuthenticationFilter() {
		returnHandler = new SimpleJumpStrategy(successUrl);
	}

	// ~ Methods
	// ========================================================================================================
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		Assert.hasLength(securityCheckUrl,"filterProcessesUrl must be specified");
		Assert.hasLength(errorURL,"errorURL must be specified");
		Assert.hasLength(successUrl,"successUrl must be specified");
		Assert.isTrue(UrlUtil.isValidRedirectUrl(securityCheckUrl),securityCheckUrl + " isn't a valid redirect URL");
		sessionAuthentication = new ConcurrentSessionControl(sessionRegistry);
	}

	public void doFilter(HttpServletRequest request,HttpServletResponse response, VirtualFilterChain chain)
			throws IOException, ServletException {
		// 判断url是否为
		if (!UrlUtils.requiresAuthentication(request, securityCheckUrl)) {
			chain.doFilter(request, response);
			return;
		}
		Authentication authResult;
		try {
			// 安全验证
			authResult = attemptAuthentication(request, response);
			if (authResult == null) {
				return;
			}
			// 设置登录url
			sessionAuthentication.onAuthentication(authResult, request, response);
		} catch (Exception failed) {
			// A 认证失败
			failed.printStackTrace();
			unsuccessfulAuthentication(request, response, failed);
			return;
		}
		// 认证成功
		successfulAuthentication(request, response, authResult);
		if(ObjUtil.isNotEmpty(loginLogSecurity)){
			authResult.getUserSession().setClientAddr(IpUtil.getIpAddr(request));
			Long loginLogId=loginLogSecurity.saveLoginOperation(authResult.getUserSession());
			SecurityContextHolder.getContext().getAuthentication().getUserSession().setLoginLogId(loginLogId);
		}
		// 跳转到登录页面 不能采用错误的异常的方式。
//		SecurityExceptions.success(request, actionMode);
		ReturnMessage message = new ReturnMessage(ReturnMessage.SUCCESS,"登录成功",new UserLoginBean(authResult.getUserSession()));
		if(ObjUtil.isNotBlank(successUrl)){
			message.setForwardUrl(successUrl);
		}
		
		if(ObjUtil.isNotBlank(authResult.getUserSession().getHomeUrl())){
			message.setForwardUrl(authResult.getUserSession().getHomeUrl());
		}
		String loginUrl = obtainLoginUrl(request);
		if(ObjUtil.isNotBlank(loginUrl)){
			message.setForwardUrl(loginUrl);
		}
		//跳转
		returnHandler.handle(request, response).returnHandle(successUrl, actionMode, message);
	}

	/**
	 * 认证
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param authResult
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
		}
		SecurityContextHolder.getContext().setAuthentication(authResult);
	}

	/**
	 * 认证失败
	 * 
	 * @param request
	 * @param response
	 * @param failed
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void unsuccessfulAuthentication(HttpServletRequest request,HttpServletResponse response, Exception failed) throws AuthenticationException {
		SecurityContextHolder.clearContext();
		SecurityExceptions.authentication("AuthenticationFailure",failed.getMessage(), actionMode);
	}

	public String getSecurityCheckUrl() {
		return securityCheckUrl;
	}

	public void setSecurityCheckUrl(String securityCheckUrl) {
		Assert.notNull(securityCheckUrl,"securityCheckUrl cannot be null");
		Assert.isTrue(UrlUtil.isValidRedirectUrl(securityCheckUrl),securityCheckUrl + " isn't a valid redirect URL");
		AuthenticationFilter.securityCheckUrl = securityCheckUrl;
	}

	/**
	 * clearSessionCookie:清空cookie中的键值 <br/>
	 * @author Administrator
	 * @since JDK 1.7
	 */
	private void clearCookie(HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		if(ObjUtil.isNotEmpty(cookies)){
			for (Cookie cookie : cookies) {
				cookie.setValue(null);
			}
		}
	}
	
	
	public LoginLogSecurity getLoginLogSecurity() {
		return loginLogSecurity;
	}

	public void setLoginLogSecurity(LoginLogSecurity loginLogSecurity) {
		Assert.notNull(loginLogSecurity, "A loginLogSecurity is required");
		this.loginLogSecurity = loginLogSecurity;
	}
	
	
	public void setApplicationEventPublisher(
			ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	protected boolean getAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}


	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		Assert.notNull(sessionRegistry,"sessionRegistry cannot be null");
		this.sessionRegistry = sessionRegistry;
	}

	public AuthenticationService getAuthenticationService() {
		return this.authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationServices) {
		this.authenticationService = authenticationServices;
	}

	public boolean isActionMode() {
		return actionMode;
	}

	public void setActionMode(boolean actionMode) {
		this.actionMode = actionMode;
	}
	
	
	public String getErrorURL() {
		return errorURL;
	}

	public void setErrorURL(String errorURL) {
		this.errorURL = errorURL;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	
	protected String obtainLoginUrl(HttpServletRequest request) {
		Object obj = request.getParameter(LOGINURL);
		return null == obj ? "" : obj.toString();
	}
	
	class UserLoginBean {
		private Long id;
		private String loginName;
		private String userName;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public UserLoginBean(UserSession userSession) {
			this.id=userSession.getUserId();
			this.loginName=userSession.getLoginName();
			this.userName=userSession.getUserName();
		}

	}

}
