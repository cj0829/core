package org.csr.core.security.session;

import org.csr.core.util.PropertiesUtil;

public class SessionRegistryFactory {
	
	private static SessionRegistry sessionRegistry;

	synchronized public static SessionRegistry getInstance() {
		
		if (sessionRegistry == null) {
			String cacheType = PropertiesUtil.getConfigureValue("cache.type");
			if("redis".equals(cacheType)){
				sessionRegistry = new SessionRegistryRedisImpl();
			}else{
				sessionRegistry =  new SessionRegistryImpl();
			}
		}
		return sessionRegistry;
	}
}
