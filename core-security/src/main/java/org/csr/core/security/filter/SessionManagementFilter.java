package org.csr.core.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.context.SecurityContextFactory;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.context.SecurityContextRepository;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.jump.SimpleJumpStrategy;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.authority.AuthenticationFilter;
import org.csr.core.security.exception.SecurityExceptions;
import org.csr.core.security.session.ConcurrentSessionControl;
import org.csr.core.security.session.SessionAuthentication;
import org.csr.core.security.session.SessionInformation;
import org.csr.core.security.session.SessionRegistry;
import org.csr.core.security.session.SessionRegistryFactory;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.security.util.UrlUtils;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.springframework.util.Assert;

/**
 * session 管理
 * 
 * @author caijin
 * 
 */
public class SessionManagementFilter extends BaseSecurityFilter {
	// ~ Static fields/initializers
	// =====================================================================================
	static final String FILTER_APPLIED = "__spring_security_session_mgmt_filter_applied";

	// ~ Instance fields
	// ================================================================================================
	private final SecurityContextRepository securityContextRepository;
	private SessionAuthentication sessionAuthentication;
	private SessionRegistry sessionRegistry = SessionRegistryFactory.getInstance();
	
	/**
	 * 没有验证到session 的异常提示页面
	 */
	protected String errorURL = null;
	
	protected  JumpStrategy returnHandler;
	
	
	public SessionManagementFilter() {
		this(SecurityContextFactory.getInstance());
	}

	public SessionManagementFilter(SecurityContextRepository securityContextRepository) {
		returnHandler=new SimpleJumpStrategy(errorURL);
		this.securityContextRepository = securityContextRepository;
	
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		Assert.notNull(returnHandler,"returnHandler cannot be null");
		Assert.notNull(errorURL,"errorURL cannot be null");
		Assert.notNull(securityContextRepository,"SecurityContextRepository cannot be null");
		this.sessionAuthentication = new ConcurrentSessionControl(sessionRegistry);
	}
	
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, VirtualFilterChain chain)
			throws IOException, ServletException {
		if (request.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(request, response);
			return;
		}
		String loginUrl = obtainLoginUrl(request);
		if(ObjUtil.isNotBlank(loginUrl)){
			request.setAttribute(AuthenticationFilter.LOGINURL, loginUrl);
		}
		request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		//判断当前是什么类型的请求
		boolean actionMode = UrlUtil.requiresAjax(request);
		
		if (!securityContextRepository.containsContext(request)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				// 判断请求是否为登录请求
				try {
					// 复制新的session
					sessionAuthentication.onAuthentication(authentication,request, response);
				} catch (Exception e) {
					logger.debug("SessionAuthenticationStrategy rejected the authentication object",e);
					SecurityContextHolder.clearContext();
					SecurityExceptions.session("rejected the authentication object",e.getMessage(), request);
					return;
				}
			} else {
				//当前用户没有获取到登录认证/只能进入登录页面/不需要做toPage() 验证 是否直接访问
//				if (UrlUtils.requiresAuthentication(request, FilterChainProxy.loginFormUrl) || toPage(chain,request)) {
				if (UrlUtils.requiresAuthentication(request, FilterChainProxy.loginFormUrl)) {
					chain.getAppFilter().doFilter(request, response);
					return;
				}
//				System.out.println(request.getServletPath());
				// 跳转到登录页面
				
				SecurityExceptions.login("Please log in","请您登录系统", request);
				return;
			}
		}else{
        HttpSession session = request.getSession(false);
//        		这里如果是集群的话，会导致获取不到session。导致需要重新登陆，所有需要多次登陆，然后两边tomcat 都有缓存的数据，才能够正常使用。
        SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
        if (info != null) {
            if (info.isExpired()) {
            			//被踢下线，这里需要发一个通知。后面在实现。
            	SecurityContextHolder.clearContext();
            	sessionRegistry.removeSessionInformation(info.getSessionId());
            	clearSession(session);
            	SecurityExceptions.logout("rejected the authentication object","被提出", request);
                return;
            } else {
            	//session修改用户最后的登录时间
            	 sessionRegistry.refreshLastRequest(info.getSessionId());
			                	 //判断如果是登录jsp，那么
								if (UrlUtils.requiresAuthentication(request,FilterChainProxy.loginFormUrl)) {
			//						SecurityExceptions.success(request,UrlUtils.requiresAjax(request));
									request.setAttribute("_message_key", "您阿斯顿飞");
									returnHandler.handle(request, response).preHandle(errorURL);
									return;
								}
			                }
        }else{
        	
				if (UrlUtils.requiresAuthentication(request, FilterChainProxy.loginFormUrl)) {
						chain.getAppFilter().doFilter(request, response);
						return;
					}
				
				// 跳转到登录页面
				SecurityExceptions.login("Please log in","请您登录系统", request);
				return;
        	}
		}
		if (UrlUtils.requiresAuthentication(request,FilterChainProxy.loginFormUrl)) {
//			SecurityExceptions.success(request,UrlUtils.requiresAjax(request));
			request.setAttribute("_message_key", "请您登录系统");
			returnHandler.handle(request, response).preHandle(errorURL);
			return;
		}
		chain.doFilter(request, response);
	}
	/**
	 * clearSession: 清除session <br/>
	 * @author caijin
	 * @param session
	 * @since JDK 1.7
	 */
	private void clearSession(HttpSession session){
		if(ObjUtil.isNotEmpty(session)){
			session.removeAttribute(Constants.SECURITY_CONTEXT_KEY);
		}
	}
	
	public String getErrorURL() {
		return errorURL;
	}

	public void setErrorURL(String errorURL) {
		this.errorURL = errorURL;
	}
	
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		Assert.notNull(sessionRegistry,"sessionRegistry cannot be null");
		this.sessionRegistry = sessionRegistry;
	}
	
	protected String obtainLoginUrl(HttpServletRequest request) {
		Object obj = request.getParameter(AuthenticationFilter.LOGINURL);
		return null == obj ? "" : obj.toString();
	}
}
