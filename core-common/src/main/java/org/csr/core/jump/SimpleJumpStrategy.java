package org.csr.core.jump;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.web.bean.ReturnMessage;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class SimpleJumpStrategy implements JumpStrategy {
	protected RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	protected String defaultTargetUrl = "/";
	private String targetUrlParameter = null;
	private boolean alwaysUseDefaultTargetUrl = false;
	private boolean useReferer = false;
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	public SimpleJumpStrategy(String defaultTargetUrl){
		this.defaultTargetUrl=defaultTargetUrl;
	}
	@Override
	public JumpStrategy handle(HttpServletRequest request,HttpServletResponse response) {
		this.request=request;
		this.response=response;
		return this;
	}
	public JumpStrategy returnHandle(String targetUrl, boolean actionMode,ReturnMessage message) throws IOException {
		Assert.notNull(this.request, "A request is required");
		Assert.notNull(this.response, "A response is required");
		if (actionMode) {
			// 返回json字符串
			redirectStrategy.ajaxMessage(request, response, message);
		} else {
			redirectStrategy.sendRedirect(request, response, targetUrl);
		}
		clearAuthenticationAttributes(request);
		return this;
	}
	
	public JumpStrategy returnAjaxHandle(ReturnMessage message) throws IOException {
		Assert.notNull(this.request, "A request is required");
		Assert.notNull(this.response, "A response is required");
		redirectStrategy.ajaxMessage(request, response, message);
		clearAuthenticationAttributes(request);
		return this;
	}
	
	public JumpStrategy preHandle(String targetUrl) throws IOException {
		redirectStrategy.sendRedirect(request, response, targetUrl);
		clearAuthenticationAttributes(request);
		return this;
	}

	/**
	 * Removes temporary authentication-related data which may have been stored
	 * in the session during the authentication process.
	 */
	protected final void clearAuthenticationAttributes(
			HttpServletRequest request) {
//		HttpSession session = request.getSession(false);
//		if (session == null) {
//			return;
//		}
//		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	
	/**
	 * Builds the target URL according to the logic defined in the main class
	 * Javadoc.
	 */
	protected String determineTargetUrl(HttpServletRequest request,HttpServletResponse response) {
		if (alwaysUseDefaultTargetUrl) {
			return defaultTargetUrl;
		}
		// Check for the parameter and use that if available
		String targetUrl = null;

		if (targetUrlParameter != null) {
			targetUrl = request.getParameter(targetUrlParameter);
			if (StringUtils.hasText(targetUrl)) {
				return targetUrl;
			}
		}

		if (useReferer && !StringUtils.hasLength(targetUrl)) {
			targetUrl = request.getHeader("Referer");
		}

		if (!StringUtils.hasText(targetUrl)) {
			targetUrl = defaultTargetUrl;
		}
		return targetUrl;
	}


	
}
