package org.csr.core.persistence.support;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Metamodel;

import org.csr.core.Persistable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class JpaEntityInfoSupport<T, ID extends Serializable> extends AbstractEntityInfo<T, ID>
		implements JpaEntityInfo<T, ID> {

	public JpaEntityInfoSupport(Class<T> domainClass) {
		super(domainClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> JpaEntityInfoSupport<T, ?> getMetadata(Class<T> domainClass, EntityManager em) {

		Assert.notNull(domainClass);
		Assert.notNull(em);
		Metamodel metamodel = em.getMetamodel();
		if (Persistable.class.isAssignableFrom(domainClass)) {
			return new JpaPersistableEntityInfo(domainClass, metamodel);
		} else {
			return new JpaMetamodelEntityInfo(domainClass, metamodel);
		}
	}

	public String getEntityName() {
		Class<?> domainClass = getJavaType();
		Entity entity = domainClass.getAnnotation(Entity.class);
		boolean hasName = null != entity && StringUtils.hasText(entity.name());

		return hasName ? entity.name() : domainClass.getSimpleName();
	}
}
