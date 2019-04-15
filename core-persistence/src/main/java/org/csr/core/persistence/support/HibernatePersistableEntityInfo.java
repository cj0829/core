package org.csr.core.persistence.support;

import java.io.Serializable;

import org.csr.core.Persistable;
import org.hibernate.metadata.ClassMetadata;

public class HibernatePersistableEntityInfo<T extends Persistable<ID>, ID extends Serializable> extends HibernateMetamodelEntityInfo<T, ID> {

	public HibernatePersistableEntityInfo(Class<T> domainClass,ClassMetadata metamodel) {
		super(domainClass, metamodel);
	}

	@Override
	public ID getId(T entity) {
		return entity.getId();
	}
}
