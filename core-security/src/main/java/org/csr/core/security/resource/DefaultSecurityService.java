package org.csr.core.security.resource;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.SecurityResource;
import org.csr.core.UserSession;


public class DefaultSecurityService implements SecurityService{


	@Override
	public List<SecurityResource> findResourcesByUser(UserSession user) {
		return new ArrayList<SecurityResource>();
	}

	@Override
	public List<? extends SecurityResource> findResourcesByAnonymous(
			UserSession user) {
		
		return new ArrayList<SecurityResource>();
	}

	@Override
	public List<? extends SecurityResource> findResourcesByDefault() {
		
		// Auto-generated method stub
		return new ArrayList<SecurityResource>();
	}
}
