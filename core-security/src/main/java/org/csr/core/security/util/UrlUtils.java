package org.csr.core.security.util;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.csr.core.security.filter.FilterChainProxy;
import org.csr.core.security.support.FilterInvocation;
import org.csr.core.util.ObjUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * ClassName:UrlUtils.java <br/>
 * System Name：    博海云领 <br/>
 * Date:     2014-2-28上午9:18:39 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public final class UrlUtils {
	private static String pattern = "/**/ajax/**.action";
	private static String backslash = "/";
	
	private static PathMatcher matcher = new AntPathMatcher();

	public static String getRequestUrl(FilterInvocation fi) {
		HttpServletRequest r = fi.getHttpRequest();
		return r.getServletPath().toLowerCase();
	}

	/**
	 * getRequestUrl: 添加前缀 <br/>
	 * @author caijin
	 * @param fi
	 * @return
	 * @since JDK 1.7
	 */
	public static String addPrefixBackslash(String url) {
		if(ObjUtil.isEmpty(url)){
			return "";
		}
		if(url.startsWith(backslash)){
			return url;
		}
		return backslash+url;
	}
	/**
	 * Returns true if the supplied URL starts with a "/" or is absolute.
	 */
	public static boolean isValidRedirectUrl(String url) {
		return url != null && url.startsWith("/") || isAbsoluteUrl(url);
	}

	public static boolean isAbsoluteUrl(String url) {
		final Pattern ABSOLUTE_URL = Pattern.compile("\\A[a-z0-9.+-]+://.*",Pattern.CASE_INSENSITIVE);

		return ABSOLUTE_URL.matcher(url).matches();
	}

	/**
	 * 判断请求是否为ajax请求
	 * 
	 * @param request
	 * @param filterUrl
	 * @return
	 */
	public static boolean requiresAjax(HttpServletRequest request) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');

		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}
		return matcher.match(pattern, uri);
	}

	/**
	 * 当前的请求是否为：filterUrl
	 * 
	 * @param request
	 * @param filterUrl
	 * @return
	 */
	public static boolean requiresAuthentication(HttpServletRequest request,String filterUrl) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');

		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}

		if ("".equals(request.getContextPath())) {
			return uri.endsWith(filterUrl);
		}
		if((request.getContextPath()+"/").endsWith(uri)){
			uri = request.getContextPath()+FilterChainProxy.loginFormUrl;
		}
		return uri.endsWith(request.getContextPath() + filterUrl);
	}

	/**
	 * 当前的请求是否为：filterUrl
	 * 
	 * @param request
	 * @param filterUrl
	 * @return
	 */
	public static boolean requiresAuthentications(HttpServletRequest request,List<String> filterUrl) {
		if(ObjUtil.isEmpty(filterUrl)){
			return false;
		}
		
		for(String url:filterUrl){
			if(requiresAuthentication(request, url)){
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
//		String url="/dictionary/preList.action;/dictionary/ajax/list.action;".toLowerCase();
//		String url="ommon/role/ajax/list.action";
	}
}
