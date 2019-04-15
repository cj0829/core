package org.csr.core.security.support;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.security.util.UrlUtils;

/**
 * ClassName:FilterInvocation.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:20:18 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class FilterInvocation {
	private FilterChain chain;
    private ServletRequest request;
    private ServletResponse response;
    
    public FilterInvocation(ServletRequest request, ServletResponse response, FilterChain chain) {
        if ((request == null) || (response == null) || (chain == null)) {
            throw new SecurityException("Cannot pass null values to constructor");
        }

        if (!(request instanceof HttpServletRequest)) {
            throw new SecurityException("Can only process HttpServletRequest");
        }

        if (!(response instanceof HttpServletResponse)) {
            throw new SecurityException("Can only process HttpServletResponse");
        }

        this.request = request;
        this.response = response;
        this.chain = chain;
    }
    
    public String getRequestUrl() {
        return UrlUtils.getRequestUrl(this);
    }
    
    public HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) request;
    }
    
    public HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) response;
    }
    
    public FilterChain getChain() {
        return chain;
    }

	public ServletRequest getRequest() {
		return request;
	}

	public ServletResponse getResponse() {
		return response;
	}
    
    
}
