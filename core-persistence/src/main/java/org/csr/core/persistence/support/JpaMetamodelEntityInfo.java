package org.csr.core.persistence.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.IdClass;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class JpaMetamodelEntityInfo<T, ID extends Serializable> extends JpaEntityInfoSupport<T, ID> {

	private final IdMetadata<T> idMetadata;

	public JpaMetamodelEntityInfo(Class<T> domainClass, Metamodel metamodel) {

		super(domainClass);
		Assert.notNull(metamodel);
		ManagedType<T> type = metamodel.managedType(domainClass);
		if (type == null) {
			throw new IllegalArgumentException("The given domain class can not be found in the given Metamodel!");
		}

		if (!(type instanceof IdentifiableType)) {
			throw new IllegalArgumentException("The given domain class does not contain an id attribute!");
		}

		this.idMetadata = new IdMetadata<T>((IdentifiableType<T>) type);
	}

	@SuppressWarnings("unchecked")
	public ID getId(T entity) {
		BeanWrapper entityWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);
		if (idMetadata.hasSimpleId()) {
			return (ID) entityWrapper.getPropertyValue(idMetadata.getSimpleIdAttribute().getName());
		}

		BeanWrapper idWrapper = new DirectFieldAccessFallbackBeanWrapper(idMetadata.getType());
		boolean partialIdValueFound = false;

		for (SingularAttribute<? super T, ?> attribute : idMetadata) {
			Object propertyValue = entityWrapper.getPropertyValue(attribute.getName());
			if (propertyValue != null) {
				partialIdValueFound = true;
			}
			idWrapper.setPropertyValue(attribute.getName(), propertyValue);
		}

		return (ID) (partialIdValueFound ? idWrapper.getWrappedInstance() : null);
	}


	public SingularAttribute<? super T, ?> getIdAttribute() {
		return idMetadata.getSimpleIdAttribute();
	}

	private static class IdMetadata<T> implements Iterable<SingularAttribute<? super T, ?>> {

		private final IdentifiableType<T> type;
		private final Set<SingularAttribute<? super T, ?>> attributes;

		@SuppressWarnings("unchecked")
		public IdMetadata(IdentifiableType<T> source) {

			this.type = source;
			this.attributes = (Set<SingularAttribute<? super T, ?>>) (source.hasSingleIdAttribute() ? Collections.singleton(source.getId(source.getIdType().getJavaType())) : source.getIdClassAttributes());
		}

		public boolean hasSimpleId() {
			return attributes.size() == 1;
		}

		public Class<?> getType() {

			try {
				return type.getIdType().getJavaType();
			} catch (IllegalStateException e) {
				// see https://hibernate.onjira.com/browse/HHH-6951
				IdClass annotation = type.getJavaType().getAnnotation(IdClass.class);
				return annotation == null ? null : annotation.value();
			}
		}

		public SingularAttribute<? super T, ?> getSimpleIdAttribute() {
			return attributes.iterator().next();
		}

		public Iterator<SingularAttribute<? super T, ?>> iterator() {
			return attributes.iterator();
		}
	}

	private static class DirectFieldAccessFallbackBeanWrapper extends BeanWrapperImpl {

		public DirectFieldAccessFallbackBeanWrapper(Object entity) {
			super(entity);
		}

		public DirectFieldAccessFallbackBeanWrapper(Class<?> type) {
			super(type);
		}

		@Override
		public Object getPropertyValue(String propertyName) {
			try {
				return super.getPropertyValue(propertyName);
			} catch (NotReadablePropertyException e) {
				Field field = ReflectionUtils.findField(getWrappedClass(), propertyName);
				ReflectionUtils.makeAccessible(field);
				return ReflectionUtils.getField(field, getWrappedInstance());
			}
		}

		@Override
		public void setPropertyValue(String propertyName, Object value) {
			try {
				super.setPropertyValue(propertyName, value);
			} catch (NotWritablePropertyException e) {
				Field field = ReflectionUtils.findField(getWrappedClass(), propertyName);
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, getWrappedInstance(), value);
			}
		}
	}
}
