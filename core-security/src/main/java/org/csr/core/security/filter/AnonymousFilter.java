package org.csr.core.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.Authentication;
import org.csr.core.UserSession;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.context.anonymous.AnonymousToken;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.authority.AuthenticationService;
import org.csr.core.security.authority.DefaultAuthenticationService;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.userdetails.SecurityUser;
import org.csr.core.util.ObjUtil;

public class AnonymousFilter extends BaseSecurityFilter {
	/**
	 * 是否允许匿名访问
	 */
	protected boolean anonymous = false;
	/**
	 * 认证服务 策略
	 */
	private AuthenticationService authenticationService = new DefaultAuthenticationService();

	private String loginName;
	
	public AnonymousFilter() {
		super();
	}

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res,VirtualFilterChain chain) throws IOException, ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (anonymous && ObjUtil.isEmpty(authentication)) {
			SecurityContextHolder.getContext().setAuthentication(createAuthentication((HttpServletRequest) req));
		}
		chain.doFilter(req, res);
	}

	/**
	 * 创建一个匿名的认证
	 * 
	 * @param request
	 * @return
	 */
	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousToken auth = null;
		if(ObjUtil.isNotBlank(loginName)){
			SecurityUser securityUser = getAuthenticationService().findByLoginName(loginName);
			auth = new AnonymousToken(loginName,loginName);
			// 允许子类设置详细属性
			UserSession userSession=getAuthenticationService().setUserSession(securityUser);
			//获取当前类型，是采用app，登录还是，后台登录
			userSession.setApptype("s");
			//清除上次登录的权限信息
			auth.setUserSession(userSession);
//			 运行UserDetailsService的loadUserByUsername 再次封装Authentication（权限）
//			auth = new AnonymousToken(request.getSession().getId(),request.getServerPort());
		}else{
			auth = new AnonymousToken(request.getSession().getId(),request.getServerPort());
		}
	
		return auth;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
