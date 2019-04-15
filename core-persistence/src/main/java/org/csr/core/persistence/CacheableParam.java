package org.csr.core.persistence;

import org.csr.core.Param;
import org.csr.core.util.ObjUtil;

public class CacheableParam implements Param {

	private final boolean cacheable;

	public CacheableParam(boolean cacheable) {
		this.cacheable = cacheable;
	}

	@Override
	public String getKey() {
		return ObjUtil.toString(cacheable);
	}

	public boolean isCacheable() {
		return cacheable;
	}

}
