package org.csr.core.persistence;

import java.io.Serializable;

/**
 * Metadata for entity types.
 * 
 */
public interface EntityInfo<T,ID extends Serializable> extends Serializable{

	String getEntityName();
	
	ID getId(T entity);
	
	Class<T> getJavaType();
}
