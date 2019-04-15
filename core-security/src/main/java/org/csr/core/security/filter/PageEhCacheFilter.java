/**
 * Project Name:elearning-74
 * File Name:PageEhCacheFilter.java
 * Package Name:com.elearning.core.intercepter
 * Date:2016-7-27下午2:47:41
 * Copyright (c) 2016, csr版权所有 ,All rights reserved 
*/

package org.csr.core.security.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.csr.core.security.cache.EhCachingFilter;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.util.ObjUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import net.sf.ehcache.CacheManager;

/**
 * ClassName:PageEhCacheFilter.java <br/>
 * System Name：    在线学习系统 <br/>
 * Date:     2016-7-27下午2:47:41 <br/>
 * @author   Administrator <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class PageEhCacheFilter extends EhCachingFilter {
	 private final static Logger log = Logger.getLogger(PageEhCacheFilter.class);
	    
	 private PathMatcher matcher = new AntPathMatcher();
	    
	    private boolean headerContains(final HttpServletRequest request, final String header, final String value) {
	        logRequestHeaders(request);
	        final Enumeration<String> accepted = request.getHeaders(header);
	        while (accepted.hasMoreElements()) {
	            final String headerValue = (String) accepted.nextElement();
	            if (headerValue.indexOf(value) != -1) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    /**
	     * @see net.sf.ehcache.constructs.web.filter.Filter#acceptsGzipEncoding(javax.servlet.http.HttpServletRequest)
	     * <b>function:</b> 兼容ie6/7 gzip压缩
	     * @author hoojo
	     * @createDate 2012-7-4 上午11:07:11
	     */
	    @Override
	    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
	        boolean ie6 = headerContains(request, "User-Agent", "MSIE 6.0");
	        boolean ie7 = headerContains(request, "User-Agent", "MSIE 7.0");
	        return acceptsEncoding(request, "gzip") || ie6 || ie7;
	    }

		@Override
		protected CacheManager getCacheManager() {
			
			 return CacheManager.getInstance();
		}

	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(httpRequest.getMethod())
				.append(httpRequest.getRequestURI())
				.append(httpRequest.getQueryString());
		String key = stringBuffer.toString();
		return key;
	}

	@Override
	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, VirtualFilterChain chain)
			throws IOException, ServletException {
		String url = request.getServletPath();
		boolean flag = false;
		if (ObjUtil.isNotEmpty(cacheURLs)) {
			for (String path : cacheURLs) {
				if (matcher.match(path.toLowerCase(), url.toLowerCase())) {
					flag = true;
					break;
				}
			}
		}
		// 如果包含我们要缓存的url 就缓存该页面，否则执行正常的页面转向
		if (flag) {
			String query = request.getQueryString();
			if (query != null) {
				query = "?" + query;
			}
			log.info("当前请求被缓存：" + url + query);
			super.doFilter(request, response, chain);
		} else {
			chain.doFilter(request, response);
		}

	}
}

