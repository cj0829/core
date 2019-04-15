package org.csr.core.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认策略
 * @author caijin
 *
 */
public final class DefaultSecurityContextRepository implements SecurityContextRepository {

    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return SecurityContextHolder.createEmptyContext();
    }

    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    }

}
