package org.csr.core.context.anonymous;

import org.csr.core.Authentication;

public class AuthenticationTrustResolverImpl implements
		AuthenticationTrustResolver {
	// ~ Instance fields
	// ================================================================================================

	private Class<? extends Authentication> anonymousClass = AnonymousToken.class;

	// ~ Methods
	// ========================================================================================================

	Class<? extends Authentication> getAnonymousClass() {
		return anonymousClass;
	}

	public boolean isAnonymous(Authentication authentication) {
		if ((anonymousClass == null) || (authentication == null)) {
			return false;
		}

		return anonymousClass.isAssignableFrom(authentication.getClass());
	}

	public void setAnonymousClass(Class<? extends Authentication> anonymousClass) {
		this.anonymousClass = anonymousClass;
	}

}
