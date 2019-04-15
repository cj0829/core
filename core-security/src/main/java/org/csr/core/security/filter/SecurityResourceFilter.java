package org.csr.core.security.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.SecurityResource;
import org.csr.core.UserSession;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.context.anonymous.AnonymousToken;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.authority.UsernamePasswordToken;
import org.csr.core.security.exception.SecurityExceptions;
import org.csr.core.security.resource.DefaultSecurityService;
import org.csr.core.security.resource.MenuNodeBean;
import org.csr.core.security.resource.MenuNodeService;
import org.csr.core.security.resource.SecurityService;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

public  class SecurityResourceFilter extends BaseSecurityFilter {
	// ~ Static fields/initializers

	protected final Log logger = LogFactory.getLog(getClass());

	// ~ Instance fields
	// ================================================================================================
	private PathMatcher matcher = new AntPathMatcher();
	private List<String> defaultPermissionsUrl;
	protected SecurityService securityService = new DefaultSecurityService();
	protected MenuNodeService menuNodeService = null;
	private boolean alwaysReauthenticate = false;

	// ~ Methods
	// ========================================================================================================

	/**
	 * 检验参数设置，及常规配置
	 * 
	 * @throws ServletException
	 */
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		Assert.notNull(this.securityService, "A securityServices is required");
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, VirtualFilterChain chain) throws IOException, ServletException {
		Authentication authenticated = authenticateIfRequired();
		if (ObjUtil.isEmpty(authenticated)) {
			SecurityExceptions.security("No permission", "没有访问权限",request);
			return;
		}
		if(ObjUtil.isNotEmpty(menuNodeService)){
			List<MenuNodeBean> allMenu = menuNodeService.findAllMenu();
			if(ObjUtil.isNotEmpty(allMenu)){
				MenuNodeBean selectMenu = menuIsSelected(allMenu, request);
				if(ObjUtil.isNotEmpty(selectMenu)){
//					System.out.println("selectMenu="+selectMenu.getName());
					authenticated.getUserSession().setSelectMenu(selectMenu);;
				}
			}
		}
		//TODO 目前是采用的admin1的组织机构[]验证当前用户是否为超级管理员 
		if(Constants.USER_SUPER.equals(authenticated.getPrincipal())){
			//如果请问为ajax，不需要  注册菜单,如果为super，将不进行积分日志的记录
			//当前设置之后需要，后面的日志，或者其他使用当前的激活的功能点
			chain.doFilter(request, response);
			return;
		}
		//验证是否存在功能点
		List<SecurityResource> securityResources=isExistSecurityResource(authenticated);
		// 比较url是否为合法的url
		if(!checkPermissionsURl(request,securityResources,authenticated)){
			logger.debug("没有访问权限-url"+request.getRequestURI());
			SecurityExceptions.security("No permission", "没有访问权限",request);
			return;
		}
		chain.doFilter(request, response);
	}

	
	/**
	 * isExistSecurityResource: 功能点  查询功能点<br/>
	 * @author caijin
	 * @param authenticated
	 * @return
	 * @since JDK 1.7
	 */
	public List<SecurityResource> isExistSecurityResource(Authentication authenticated) {
		//如果当前的权限已经存在，则就不作查询了。
		if(ObjUtil.isNotEmpty(authenticated.getUserSession().getResources())){
			return authenticated.getUserSession().getResources();
		}
		//验证是否为默认权限
		@SuppressWarnings("unchecked")
		List<SecurityResource> defaults=(List<SecurityResource>) securityService.findResourcesByDefault();
		if(authenticated instanceof AnonymousToken){
			UserSession userSession=authenticated.getUserSession();
			@SuppressWarnings("unchecked")
			List<SecurityResource> securityResources=(List<SecurityResource>) securityService.findResourcesByAnonymous(userSession);
			securityResources.addAll(defaults);
			authenticated.getUserSession().setAllFunCode(toFunCode(securityResources));
			authenticated.getUserSession().setResources(securityResources);
			return  securityResources;
		}
		if(authenticated instanceof UsernamePasswordToken){
			UserSession userSession=authenticated.getUserSession();
			if(ObjUtil.isEmpty(userSession)){
				return null;
			}
			@SuppressWarnings("unchecked")
			List<SecurityResource> securityResources=(List<SecurityResource>) securityService.findResourcesByUser(userSession);
			securityResources.addAll(defaults);
			authenticated.getUserSession().setAllFunCode(toFunCode(securityResources));
			authenticated.getUserSession().setResources(securityResources);
			return  securityResources;
		}
	
		return null;
		
	}

	/**
	 * 判断url是不是默认权限的链接。
	 * @param request
	 * @param securityResources 
	 * @return
	 */
	protected boolean checkPermissionsURl(HttpServletRequest request, List<SecurityResource> securityResources,Authentication authenticated) {
		String url = request.getServletPath().toLowerCase();
		if (ObjUtil.isNotEmpty(defaultPermissionsUrl)) {
			for (String permissionsUrl : defaultPermissionsUrl) {
				if (matcher.match(permissionsUrl.toLowerCase(), url)) {
					//设置登录的url
					authenticated.getUserSession().setUrl(url);
					//当前设置之后需要，后面的日志，或者其他使用当前的激活的功能点
					authenticated.getUserSession().setSecurityResource(findPermissionsURl(request, securityResources));
					return true;
				}
			}
		}
		
		if (ObjUtil.isNotEmpty(securityResources)){
			for (SecurityResource resource:securityResources) {
				if (matcher.match(UrlUtil.addPrefixBackslash(resource.getForwardUrl()).toLowerCase(), url)) {
					//设置登录的url 和 当前功能点
					authenticated.getUserSession().setUrl(url);
					authenticated.getUserSession().setSecurityResource(resource);
					return true;
				}
				if(ObjUtil.isNotBlank(resource.getUrlRule())){
					String[] urlRules=resource.getUrlRule().split(",");
					for (String rule : urlRules) {
						if (matcher.match(UrlUtil.addPrefixBackslash(rule).toLowerCase(), url)) {
							//设置登录的url 和 当前功能点
							authenticated.getUserSession().setUrl(url);
							authenticated.getUserSession().setSecurityResource(resource);
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * toFunCode: 组合Code <br/>
	 * @author caijin
	 * @param attributes
	 * @return
	 * @since JDK 1.7
	 */
	protected String toFunCode(List<SecurityResource> attributes) {
		StringBuffer code=new StringBuffer();
		if(ObjUtil.isNotEmpty(attributes)){
			for(SecurityResource sr:attributes){
				code.append("|").append(sr.getCode());
			}
		}else{
			code.append("allFunCode");
		}
		return code.toString();
	}
	
	
	/**
	 * 根据当前的url查询符合的功能点
	 * @author caijin
	 * @param request
	 * @param securityResources
	 * @return
	 * @since JDK 1.7
	 */
	private SecurityResource findPermissionsURl(HttpServletRequest request, List<SecurityResource> securityResources) {
		String url = request.getServletPath().toLowerCase();
		if (ObjUtil.isEmpty(securityResources)){
			return null;
		}
		Iterator<SecurityResource> iterator = securityResources.iterator();
		while(iterator.hasNext()){
			SecurityResource resource = iterator.next();
			if(ObjUtil.isNotBlank(resource.getUrlRule())){
				String[] urlRules=resource.getUrlRule().split(",");
				for (String rule : urlRules) {
					if (matcher.match(UrlUtil.addPrefixBackslash(rule).toLowerCase(), url)) {
						return resource;
					}
				}
			}
		}
		return null;
	}

	protected MenuNodeBean menuIsSelected(List<MenuNodeBean> menus, HttpServletRequest request){
		String path = request.getServletPath().toLowerCase();
		if (ObjUtil.isNotEmpty(menus)) {
			for (MenuNodeBean resource : menus) {
				SecurityResource sr=resource.getSecurityResource();
				if(ObjUtil.isNotEmpty(sr) && ObjUtil.isNotBlank(sr.getForwardUrl())){
					if(matcher.match(UrlUtil.addPrefixBackslash(sr.getForwardUrl()).toLowerCase(), path)){
						return resource;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private Authentication authenticateIfRequired() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!alwaysReauthenticate) {
			return authentication;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	public boolean isAlwaysReauthenticate() {
		return alwaysReauthenticate;
	}

	public void setAlwaysReauthenticate(boolean alwaysReauthenticate) {
		this.alwaysReauthenticate = alwaysReauthenticate;
	}


	public List<String> getDefaultPermissionsUrl() {
		return defaultPermissionsUrl;
	}

	public void setDefaultPermissionsUrl(List<String> defaultPermissionsUrl) {
		this.defaultPermissionsUrl = defaultPermissionsUrl;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		Assert.notNull(securityService, "A securityServices is required");
		this.securityService = securityService;
	}

	public MenuNodeService getMenuNodeService() {
		return menuNodeService;
	}

	public void setMenuNodeService(MenuNodeService menuNodeService) {
		this.menuNodeService = menuNodeService;
	}
	
}
