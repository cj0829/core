package org.csr.core.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * ClassName:UrlUtils.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:18:39 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public final class UrlUtil {
	
	private static String pattern_ajax = "/**/ajax/**";
	private static String pattern_api = "/**/api/**";
	private static String pattern_app = "/**/app/**";
	private static String backslash = "/";
	
	private static PathMatcher matcher = new AntPathMatcher();

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
		return requiresAjax(request.getRequestURI());
	}
	
	/**
	 * 判断请求是否为ajax请求
	 * 
	 * @param request
	 * @param filterUrl
	 * @return
	 */
	public static boolean requiresAjax(String uri) {
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}
		return matcher.match(pattern_ajax, uri) || matcher.match(pattern_api, uri) || matcher.match(pattern_app, uri);
	}
	public static void main(String[] args) {
		String uri="/ajax/s/userStrategy/imporUpdate/updateStrategy.action";
		System.out.println(requiresAjax(uri));
	}
}
