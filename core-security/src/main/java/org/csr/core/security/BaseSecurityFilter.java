package org.csr.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.security.support.VirtualFilterChain;
import org.springframework.web.filter.GenericFilterBean;

/**
 * ClassName:BaseSecurityFilter.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:18:28 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public abstract class BaseSecurityFilter extends GenericFilterBean {

	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException { 
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, (VirtualFilterChain)chain);
	}
	/**
	 * @description:具体业务方法
	 * @param: 
	 * @return: void 
	 */
	protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response,VirtualFilterChain chain) throws IOException, ServletException;
	
}
