package org.csr.core.persistence.support;

import java.io.Serializable;

import org.csr.core.exception.Exceptions;
import org.csr.core.persistence.EntityInfo;

public abstract class AbstractEntityInfo<T, ID extends Serializable> implements EntityInfo<T, ID> {

	private final Class<T> domainClass;

	public AbstractEntityInfo(Class<T> domainClass) {
		if(null==domainClass){
			Exceptions.service("", "notNull");
		}
		this.domainClass = domainClass;
	}


	public Class<T> getJavaType() {
		return this.domainClass;
	}
}
