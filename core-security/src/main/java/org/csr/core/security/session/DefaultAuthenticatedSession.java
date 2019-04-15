package org.csr.core.security.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.Authentication;

/**
 * 默认的为空策略。
 */
public final class DefaultAuthenticatedSession implements SessionAuthentication {

    public void onAuthentication(Authentication authentication, HttpServletRequest request,HttpServletResponse response) {
    }
}
