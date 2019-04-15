package org.csr.core.persistence.query.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.csr.core.AutoSetProperty;
import org.csr.core.Persistable;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.domain.SimpleDomain;
import org.csr.core.persistence.query.QueryUtils;
import org.csr.core.persistence.support.JpaEntityInfo;
import org.csr.core.userdetails.UserSessionBasics;
import org.csr.core.userdetails.UserSessionContext;
import org.csr.core.util.ClassUtil;
import org.csr.core.util.ObjUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class AdapterJpaDao<T, ID extends Serializable> implements InitializingBean{
	
	protected EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	void clear() {
		entityManager.clear();
	}
	
	void flush() {
		entityManager.flush();
	}

	@SuppressWarnings("rawtypes")
	T save(T entity, JpaEntityInfo<T, ID> entityInfo) {
		autoSetProperty(entity);
//		if (entityInfo.isNew(entity)) {
			// 自动添加创建时间和未删除标志
			try {
//				if (entity instanceof RootDomain) {
////					RootDomain save = (RootDomain) entity;
//					//TODO 用户自动设置需要在前台设置
////					UserSessionBasics us = UserSessionContext.getUserSession();
////					if (ObjUtil.longIsNull(save.getRoot())) {
////						if (ObjUtil.isNotEmpty(us) && ObjUtil.longNotNull(us.getRoot())) {
////							save.setRoot(us.getRoot());
////						}
////					}
//				}
				if (entity instanceof SimpleDomain) {
					SimpleDomain save = (SimpleDomain) entity;
					if (ObjUtil.isEmpty(save.getCreateTime())){
						save.setCreateTime(new Date());
					}
					//TODO 用户自动设置
					UserSessionBasics us = UserSessionContext.getUserSession();
					if (ObjUtil.isNotEmpty(us) && ObjUtil.longNotNull(us.getUserId())) {
						if(ObjUtil.isEmpty(save.getCreatedBy())){
							save.setCreatedBy(us.getUserId());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			entityManager.persist(entity);
			return entity;
//		} else {
//			// 自动添加更新时间
//			return update(entity);
//		}
	}

	@SuppressWarnings("rawtypes")
	T update(T entity) {
		autoSetProperty(entity);
		// 自动添加更新时间
		try {
			if (entity instanceof SimpleDomain) {
				SimpleDomain save = (SimpleDomain) entity;
				save.setUpdateTime(new Date().getTime());
//				TODO：
				UserSessionBasics us = UserSessionContext.getUserSession();
				if (ObjUtil.isNotEmpty(us) && ObjUtil.longNotNull(us.getUserId())) {
					save.setUpdateBy(us.getUserId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityManager.merge(entity);
	}
	
	private void autoSetProperty(T entity){
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(ObjUtil.isNotEmpty(fields)){
			for(int i=0;i<fields.length;i++){
				//增加缓存
				Field field=fields[i];
				AutoSetProperty notFind = field.getAnnotation(AutoSetProperty.class);
				if(ObjUtil.isNotEmpty(notFind)){
					try {
						field.setAccessible(true);
						Object oldValue= field.get(entity);
						if(ObjUtil.isNotEmpty(oldValue)){
							if(oldValue instanceof Persistable){
								Persistable<?> persist=(Persistable<?>) oldValue;
								if(ObjUtil.isNotEmpty(persist)){
									if(ObjUtil.isNotEmpty(persist.getId())){
										if(!ClassUtils.isCglibProxy(persist)){
											Persistable<?> newPersist =entityManager.find(persist.getClass(), persist.getId());
											if(ObjUtil.isEmpty(newPersist)){
												Exceptions.dao("", notFind.message()+"未找到，请联系管理员!");
											}
											ClassUtil.set(field, entity, newPersist);
										}
									}else{
										if(notFind.required()){
											Exceptions.dao("", "必须选择"+notFind.message());
										}
									}
								}
							}
						}else{
							if(notFind.required()){
								Exceptions.dao("", "必须选择"+notFind.message());
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	void delete(T entity) {
		if(ObjUtil.isNotEmpty(entity)){
			entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
		}
		
	}

	void deleteById(ID id,JpaEntityInfo<T, ID> entityInfo) {
		if(ObjUtil.isNotEmpty(entityInfo)){
			if (!exists(id,entityInfo)) {
				return;
			}
			delete(getById(id,entityInfo));
		}
	}

	void deleteByList(List<T> entitys) {
		for (T entity : entitys) {
			delete(entity);
		}
	}
	
	T getById(ID id,JpaEntityInfo<T, ID> entityInfo) {
		if(ObjUtil.isEmpty(id)){
			return null;
		}
		return entityManager.find(entityInfo.getJavaType(), id);
	}
	
	T getById(ID id,JpaEntityInfo<T, ID> entityInfo,LockModeType lockModeType) {
		if(ObjUtil.isEmpty(id)){
			return null;
		}
		return entityManager.find(entityInfo.getJavaType(), id, lockModeType);
	}
	
	
	boolean exists(ID id,JpaEntityInfo<T, ID> entityInfo) {
		if(ObjUtil.isEmpty(entityInfo)){
			Exceptions.service("notNull", "The entity must not be null!");
		}
		if (entityInfo.getIdAttribute() != null) {
			String entityName = entityInfo.getEntityName();
			String idAttributeName = entityInfo.getIdAttribute().getName();
			String existsQuery = QueryUtils.getQueryString(QueryUtils.EXISTS_QUERY_STRING, QueryUtils.DEFAULT_ALIAS, entityName, idAttributeName);
			TypedQuery<Long> query = entityManager.createQuery(existsQuery, Long.class);
			query.setParameter("id", id);
			return query.getSingleResult() == 1;
		} else {
			return getById(id,entityInfo) != null;
		}
	}
	
	public Long isExist(Finder finder) {
		TypedQuery<Object> query = entityManager.createQuery(finder.getHql(), Object.class);
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		finder.setParamsToQuery(new JpaQuery(query));
		Object result = query.getSingleResult();
		if(result instanceof Long){
			return (Long) result;
		}
		if(result instanceof Integer){
			return ObjUtil.toLong(result);
		}
		return null;
	}
	
	public int batchHandle(Finder finder) {
		Query query =  entityManager.createQuery(finder.getHql());
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		finder.setParamsToQuery(new JpaQuery(query));
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> find(Finder finder) {
		Query query =  entityManager.createQuery(finder.getHql());
		finder.setParamsToQuery(new JpaQuery(query));
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		return query.getResultList();
	}
	
	public PagedInfo<T> findPage(Finder finder,Page page) {
		
		//添加前台参数
		finder.putAutoParam("sys", page.toParam());
		finder.setSort(page.getSort());
		Long total=count(finder);
		Query query =  entityManager.createQuery(finder.getHql());
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		finder.setParamsToQuery(new JpaQuery(query));
		query.setFirstResult(page.getOffset());
		query.setMaxResults(page.getPageSize());
		@SuppressWarnings("unchecked")
		List<T> content = total > page.getOffset() ? query.getResultList() : Collections.<T> emptyList();
		return new PagedInfoImpl<T>(content, page, total);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByPage(Finder finder,Page page) {
		//添加前台参数
		finder.putAutoParam("sys", page.toParam());
		finder.setSort(page.getSort());
		Query query =  entityManager.createQuery(finder.getHql());
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		finder.setParamsToQuery(new JpaQuery(query));
		query.setFirstResult(page.getOffset());
		query.setMaxResults(page.getPageSize());
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Long count(Finder finder) {
		Query query = entityManager.createQuery(finder.getRowCountHql());
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		Assert.notNull(query);
		finder.setParamsToQuery(new JpaQuery(query));
		List<Object> totals = query.getResultList();
		Long total = 0L;
		for(int i=0;i<totals.size();i++) {
			Long elentityManagerent = ObjUtil.toLong( totals.get(i));
			total += elentityManagerent == null ? 0 : elentityManagerent;
		}
		return total;
	}
	@SuppressWarnings("unchecked")
	public Long countOriginalHql(Finder finder) {
		Query query =  entityManager.createQuery(finder.getHql());
		if(finder.isCacheable()){
			query.setHint("org.hibernate.cacheable", true);
		}
		Assert.notNull(query);
		finder.setParamsToQuery(new JpaQuery(query));
		List<Object> totals = query.getResultList();
		Long total = 0L;
		for(int i=0;i<totals.size();i++) {
			Long elentityManagerent = ObjUtil.toLong( totals.get(i));
			total += elentityManagerent == null ? 0 : elentityManagerent;
		}
		return total;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}