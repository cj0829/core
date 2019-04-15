package org.csr.core.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.cloudsession.CloudRequestContextHolder;
import org.csr.core.cloudsession.CloudRequestWrapper;
import org.csr.core.cloudsession.CloudSessionManager;
import org.csr.core.context.HttpRequestResponseHolder;
import org.csr.core.context.SecurityContext;
import org.csr.core.context.SecurityContextFactory;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.context.SecurityContextRepository;
import org.csr.core.exception.ExceptionChain;
import org.csr.core.exception.ValidationChainException;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.jump.SimpleJumpStrategy;
import org.csr.core.security.exception.AuthenticationException;
import org.csr.core.security.exception.LoginException;
import org.csr.core.security.exception.LogoutException;
import org.csr.core.security.exception.SessionException;
import org.csr.core.security.support.FilterInvocation;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.NestedServletException;

/**
 * ClassName:FilterChainProxy.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-18上午10:58:35 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class FilterChainProxy implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(FilterChainProxy.class);
	
	CloudSessionManager manager = CloudSessionManager.getInstance();
	
	static final String FILTER_APPLIED = "__pmt_security_scpf_applied";
	/**
	 * 登录页面 必填
	 */
	public static String loginFormUrl="/";
	/**
	 * 所有的过滤器
	 */
	private List<Filter> filters;
	/**
	 * 走过滤器的url模板
	 */
	private List<String> pathMatch;
	/**
	 * 不走过滤器的url模板
	 */
	private List<String> pathUnMatch;
	
	/**
	 * 提交session 过滤器
	 */
	private List<String> sessionMatch;
	/**
	 * 页面跳转处理，
	 */
	protected  JumpStrategy returnHandler;
	private PathMatcher matcher = new AntPathMatcher();
	//安全上下文
	private SecurityContextRepository repo;
	
	/**
	 * 异常处理链
	 */
	List<org.csr.core.exception.ValidationChainException> additionalExceptions = new ArrayList<org.csr.core.exception.ValidationChainException>();
	
	/**
	 * session管理方式
	 */
	public static String sessionManagerMode="localSession";
	/**
	 * session超时时间
	 */
	private int maxInactiveInterval=1800;
	/**
	 * sessionId名称
	 */
	private String sessionName;
	
	public FilterChainProxy() {
		Assert.notNull(loginFormUrl,"loginFormUrl cannot be null");
		this.repo = SecurityContextFactory.getInstance();
		returnHandler = new SimpleJumpStrategy(loginFormUrl);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// 获得所有filter并初始化这些filter
		Filter[] filters = obtainAllDefinedFilters();
		if(ObjUtil.isNotEmpty(filters)){
			for (int i = 0; i < filters.length; i++) {
				if (filters[i] != null) {
					filters[i].init(filterConfig);
				}
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//做一个装饰
		CloudRequestWrapper requestWrapper = new CloudRequestWrapper((HttpServletRequest)request,(HttpServletResponse) response,sessionManagerMode,maxInactiveInterval,sessionName);  
		//设置全局
		CloudRequestContextHolder.setRequestAttributes(requestWrapper);
		FilterInvocation fi = new FilterInvocation(requestWrapper, response, chain);
		
		String url = fi.getRequestUrl();
		Boolean matched = pathMatch(url);
		//获取session
		if(pathSessionMatch(url)){
			//保证每个session 都存在
			requestWrapper.getSession();
		}
		log.info("登录-url"+url);
	
		// 如果不需要过滤器则执行下一步
		if (filters == null || filters.size() == 0 || matched) {
			try {
			chain.doFilter(request, response);
			}catch(Exception e){
				e.printStackTrace();
				handleSpringSecurityException(httpRequest, httpResponse, chain, e);
			}finally {
				if(pathSessionMatch(url)){
					//保存session
					requestWrapper.commitSession();
					//清空本来的缓存
					requestWrapper.cleanSessionThreadLocal();
				}
				CloudRequestContextHolder.resetHttpServletRequest();
			}
			return;
		}
		// 如果有过滤器则执行
		// 一开始获取session中的安全上下文。
		HttpRequestResponseHolder holder = new HttpRequestResponseHolder(requestWrapper, httpResponse);
		SecurityContext contextBeforeChainExecution = repo.loadContext(holder);
		try {
			SecurityContextHolder.setContext(contextBeforeChainExecution);
			VirtualFilterChain virtualFilterChain = new VirtualFilterChain(fi,filters);
			virtualFilterChain.doFilter(fi.getRequest(), fi.getResponse());
		}catch(Exception e){
			e.printStackTrace();
			handleSpringSecurityException(httpRequest, httpResponse, chain, e);
		}finally {
			SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
			SecurityContextHolder.clearContext();
			//保存 安全上下文 到session 中
			repo.saveContext(contextAfterChainExecution, holder.getRequest(),holder.getResponse());
			request.removeAttribute(FILTER_APPLIED);
			//保存session
			requestWrapper.commitSession();
			//清空本来的缓存
			requestWrapper.cleanSessionThreadLocal();
			//
			CloudRequestContextHolder.resetHttpServletRequest();
		}
	}

	private void handleSpringSecurityException(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Exception exception)
			throws IOException, ServletException {
		String loginUrl=ObjUtil.toString(request.getAttribute("loginUrl"));
		String loginFormUrl=FilterChainProxy.loginFormUrl;
		if(ObjUtil.isNotBlank(loginUrl)){
			loginFormUrl=loginFormUrl+"?loginUrl="+loginUrl;
		}
		
		try{
			if(!response.isCommitted()){
				if (exception instanceof AuthenticationException) {
					AuthenticationException ep=(AuthenticationException) exception;
					ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,ep.getErrorMsg(),ep.getData());
					returnHandler.handle(request, response).returnHandle(loginFormUrl,((AuthenticationException) exception).getActionMode(),message);
				} else if (exception instanceof LoginException) {
					LoginException lp=(LoginException) exception;
					ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,lp.getErrorMsg(),lp.getData());
					returnHandler.handle(request, response).returnHandle(loginFormUrl,((LoginException) exception).getActionMode(), message);
				} else if (exception instanceof LogoutException) {
					LogoutException lp=(LogoutException) exception;
					ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,lp.getErrorMsg(),lp.getData());
					returnHandler.handle(request, response).returnHandle(loginFormUrl,((LogoutException) exception).getActionMode(), message);
				} else if (exception instanceof SessionException) {
					HttpServletResponse httpResponse = (HttpServletResponse) response;
					httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"service unavailable");
				}else if (exception instanceof org.csr.core.security.exception.SecurityException) {
					if (((org.csr.core.security.exception.SecurityException) exception).getActionMode()) {
						org.csr.core.security.exception.SecurityException sp = (org.csr.core.security.exception.SecurityException) exception;
						ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,sp.getErrorMsg(),sp.getData());
						returnHandler.handle(request, response).returnAjaxHandle(message);
					} else {
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,"Access Denied");
					}
				}else{
					if (exception instanceof NestedServletException) {
						exception = (Exception) exception.getCause();
					}
					new ExceptionChain(additionalExceptions).validationException(request, response, exception);
				}
			}
		}catch(Exception e){
			System.out.println("记录异常");
			 e.printStackTrace();
			boolean actionMode = UrlUtil.requiresAjax(request);
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL, "出错啦！请联系管理员",null);
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", "出错啦！请联系管理员");
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"出错啦！请联系管理员");
			}
		}
	}

	public void destroy() {
		// 获得所有filter并销毁这些filter
		Filter[] filters = obtainAllDefinedFilters();
		if(ObjUtil.isNotEmpty(filters)){
			for (int i = 0; i < filters.length; i++) {
				if (filters[i] != null) {
					filters[i].destroy();
				}
			}
		}
	}
	
	/**
	 * @description:判断这个url是否匹配指定的url，匹配不走过滤器，反之走 true：匹配，false：不匹配,不区分大小写
	 * @param:
	 * @return: Boolean
	 */
	private Boolean pathMatch(String url) {
		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}
		if (pathUnMatch != null && pathUnMatch.size() > 0) {
			for (String path : pathUnMatch) {
				if (matcher.match(path.toLowerCase(), url.toLowerCase()))
					return true;
			}
		}
		if (pathMatch != null && pathMatch.size() > 0) {
			for (String path : pathMatch) {
				if (matcher.match(path.toLowerCase(), url.toLowerCase()))
					return false;
			}
		}
		return pathSessionMatch(url);
	}

	
	/**
	 * @description:判断这个url是否匹配指定的url，匹配不走过滤器，反之走 true：匹配，false：不匹配,不区分大小写
	 * @param:
	 * @return: Boolean
	 */
	private Boolean pathSessionMatch(String url) {
		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}
		if (ObjUtil.isNotEmpty(sessionMatch)) {
			for (String path : sessionMatch) {
				if (matcher.match(path.toLowerCase(), url.toLowerCase()))
					return true;
			}
		}
		return false;
	}

	
	/**
	 * @description:将所有filter转化为数组
	 * @param:
	 * @return: Filter[]
	 */
	private Filter[] obtainAllDefinedFilters() {
		if(ObjUtil.isNotEmpty(filters)){
			return (Filter[]) filters.toArray(new Filter[0]);
		}
		return null;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	public void setExceptionChains(List<ValidationChainException> exceptionChains) {
		this.additionalExceptions = exceptionChains;
	}

	public void setPathMatch(List<String> pathMatch) {
		this.pathMatch = pathMatch;
	}

	public void setPathUnMatch(List<String> pathUnMatch) {
		this.pathUnMatch = pathUnMatch;
	}

	public void setLoginFormUrl(String loginFormUrl) {
		Assert.notNull(loginFormUrl,"loginFormUrl cannot be null");
		Assert.isTrue(UrlUtil.isValidRedirectUrl(loginFormUrl),loginFormUrl + " isn't a valid redirect URL");
		FilterChainProxy.loginFormUrl = loginFormUrl;
	}

	public void setreturnHandler(JumpStrategy returnHandler) {
		Assert.notNull(returnHandler, "successHandler cannot be null");
		
		this.returnHandler = returnHandler;
	}
	
	public List<String> getSessionMatch() {
		return sessionMatch;
	}

	public void setSessionMatch(List<String> sessionMatch) {
		this.sessionMatch = sessionMatch;
	}

	public String getSessionManagerMode() {
		return sessionManagerMode;
	}

	public void setSessionManagerMode(String sessionManagerMode) {
		FilterChainProxy.sessionManagerMode = sessionManagerMode;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	
	public void setListenerId(Object[] listeners) {
		manager.setApplicationEventListeners(listeners);
	}
}
