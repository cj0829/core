package org.csr.core.security.support;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * ClassName:VirtualFilterChain.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:20:27 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class VirtualFilterChain implements FilterChain {

	private FilterInvocation fi;
	private List<Filter> additionalFilters;
	/**
	 * 判断现在执行第几个过滤器
	 */
	private int currentPosition = 0;

	public VirtualFilterChain(FilterInvocation filterInvocation,
			List<Filter> additionalFilters) {
		this.fi = filterInvocation;
		this.additionalFilters = additionalFilters;
	}

	public void doFilter(ServletRequest request, ServletResponse response)throws IOException, ServletException {
		// 如果过滤器没有执行完，则继续执行过滤器
		if (currentPosition == additionalFilters.size()) {
			fi.getChain().doFilter(request, response);
		} else {
			currentPosition++;
			Filter nextFilter = (Filter) additionalFilters.get(currentPosition - 1);
			nextFilter.doFilter(request, response, this);
		}
	}
	public FilterChain getAppFilter(){
		return fi.getChain();
	}
}
