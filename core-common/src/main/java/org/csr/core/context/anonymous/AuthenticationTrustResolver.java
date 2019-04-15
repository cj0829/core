package org.csr.core.context.anonymous;

import org.csr.core.Authentication;


/**
 * 匿名登录
 * @author caijin
 *
 */
public interface AuthenticationTrustResolver {
	
    boolean isAnonymous(Authentication authentication);

}
