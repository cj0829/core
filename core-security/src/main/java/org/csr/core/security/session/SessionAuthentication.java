package org.csr.core.security.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.Authentication;

/**
 * 认证session管理策略
 * @author caijin
 *
 */
public interface SessionAuthentication {
    void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response);
    
}