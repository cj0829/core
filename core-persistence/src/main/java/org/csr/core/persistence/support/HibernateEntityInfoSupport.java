package org.csr.core.persistence.support;

import java.io.Serializable;

import org.csr.core.Persistable;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.Assert;

public abstract class HibernateEntityInfoSupport<T, ID extends Serializable> extends AbstractEntityInfo<T, ID>
		implements HibernateEntityInfo<T, ID> {

	public HibernateEntityInfoSupport(Class<T> domainClass) {
		super(domainClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> HibernateEntityInfoSupport<T, ?> getMetadata(Class<T> domainClass, SessionFactory sf) {

		Assert.notNull(domainClass);
		Assert.notNull(sf);
		ClassMetadata classMetadata=sf.getClassMetadata(domainClass);
		if (Persistable.class.isAssignableFrom(domainClass)) {
			return new HibernatePersistableEntityInfo(domainClass, classMetadata);
		} else {
			return new HibernateMetamodelEntityInfo(domainClass, classMetadata);
		}
	}

	public String getEntityName() {
		Class<?> domainClass = getJavaType();
		return domainClass.getSimpleName();
	}
}
