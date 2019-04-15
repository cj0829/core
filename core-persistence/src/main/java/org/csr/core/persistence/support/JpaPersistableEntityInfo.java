package org.csr.core.persistence.support;

import java.io.Serializable;

import javax.persistence.metamodel.Metamodel;

import org.csr.core.Persistable;

public class JpaPersistableEntityInfo<T extends Persistable<ID>, ID extends Serializable> extends JpaMetamodelEntityInfo<T, ID> {

	public JpaPersistableEntityInfo(Class<T> domainClass,Metamodel metamodel) {
		super(domainClass, metamodel);
	}


	@Override
	public ID getId(T entity) {
		return entity.getId();
	}
}
