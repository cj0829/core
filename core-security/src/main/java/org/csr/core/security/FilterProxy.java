package org.csr.core.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ClassName:FilterProxy.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:19:25 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class FilterProxy implements Filter, ServletContextAware {
	
	private ServletContext servletContext;
	private FilterConfig filterConfig;
	/*
	 * 下个过滤器名字
	 */
	private String targetBeanName;
	/*
	 * 代理过滤器
	 */
	private Filter delegate;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		initFilterBean();
	}
	
	/**
	 * @description:初始化下个过滤器
	 * @param: 
	 * @return: void 
	 */
	private void initFilterBean() throws ServletException{
		if (this.targetBeanName == null) {
			this.targetBeanName = this.filterConfig.getFilterName();
		}
		synchronized (this) {
			WebApplicationContext wac = findWebApplicationContext();
			if (wac != null) {
				this.delegate = initDelegate(wac);
			}
		}
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Filter delegateToUse = null;
		synchronized (this) {
			if (this.delegate == null) {
				WebApplicationContext wac = findWebApplicationContext();
				if (wac == null) {
					throw new SecurityException("No WebApplicationContext found: no ContextLoaderListener registered?");
				}
				this.delegate = initDelegate(wac);
			}
			delegateToUse = this.delegate;
		}

		invokeDelegate(delegateToUse, request, response, chain);
	}

	private void invokeDelegate(
			Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		delegate.doFilter(request, response, filterChain);
	}
	
	/**
	 * @description:初始化spring中的过滤器
	 * @param: 
	 * @return: Filter 
	 */
	private Filter initDelegate(WebApplicationContext wac) throws ServletException {
		Filter delegate = (Filter) wac.getBean(getTargetBeanName(), Filter.class);
		delegate.init(getFilterConfig());
		return delegate;
	}
	
	/**
	 * @description:得到spring上下文对象
	 * @param: 
	 * @return: WebApplicationContext 
	 */
	private WebApplicationContext findWebApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
	
	/**
	 * @description:得到容器上下文对象
	 * @param: 
	 * @return: ServletContext 
	 */
	protected final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext);
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public String getTargetBeanName() {
		return targetBeanName;
	}

	public void destroy() {

	}
}
