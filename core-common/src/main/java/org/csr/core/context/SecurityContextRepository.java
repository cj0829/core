package org.csr.core.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface SecurityContextRepository {
	
	/**
	 * 用于获取session中的安全上下文。
	 * @param requestResponseHolder
	 * @return
	 */
    SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder);

    void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response);
    /**
     * 是否存在session
     * @param request
     * @return
     */
    boolean containsContext(HttpServletRequest request);
    
}
