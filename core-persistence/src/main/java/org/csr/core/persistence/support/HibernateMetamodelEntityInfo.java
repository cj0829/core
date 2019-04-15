package org.csr.core.persistence.support;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;


public class HibernateMetamodelEntityInfo<T, ID extends Serializable> extends HibernateEntityInfoSupport<T, ID> {

	private ClassMetadata metamodel;

	public HibernateMetamodelEntityInfo(Class<T> domainClass, ClassMetadata metamodel) {
		super(domainClass);
		Assert.notNull(metamodel);
		this.metamodel=metamodel;
	}


	@Override
	public ID getId(T entity) {
		BeanWrapper entityWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);
		return (ID) entityWrapper.getPropertyValue(metamodel.getIdentifierPropertyName());
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
