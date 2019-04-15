package org.csr.core.persistence.query.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.csr.core.AutoSetProperty;
import org.csr.core.Persistable;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.domain.SimpleDomain;
import org.csr.core.persistence.support.HibernateEntityInfo;
import org.csr.core.userdetails.UserSessionBasics;
import org.csr.core.userdetails.UserSessionContext;
import org.csr.core.util.ClassUtil;
import org.csr.core.util.ObjUtil;
import org.hibernate.Query;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class AdapterHibernateDao<T, ID extends Serializable> extends HibernateDaoSupport implements InitializingBean{
	
	
	void clear() {
		super.currentSession().clear();
	}
	
	void flush() {
		super.currentSession().flush();
	}

	@SuppressWarnings("rawtypes")
	T save(T entity, HibernateEntityInfo<T, ID> entityInfo) {
		autoSetProperty(entity);
//		if (entityInfo.isNew(entity)) {
			// 自动添加创建时间和未删除标志
			try {
//				if (entity instanceof RootDomain) {
//					RootDomain save = (RootDomain) entity;
////					TODO：自动设置创建人
//					UserSessionBasics us = UserSessionContext.getUserSession();
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
//					TODO：自动设置创建人
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
			super.currentSession().persist(entity);
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
//				TODO：设置自动更新时间
				UserSessionBasics us = UserSessionContext.getUserSession();
				if (ObjUtil.isNotEmpty(us) && ObjUtil.longNotNull(us.getUserId())) {
					save.setUpdateBy(us.getUserId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) super.currentSession().merge(entity);
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
											Persistable<?> newPersist = super.currentSession().load(persist.getClass(), persist.getId());
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
		Exceptions.service("notNull", "The entity must not be null!");
		super.currentSession().delete(super.currentSession().contains(entity) ? entity : super.currentSession().merge(entity));
	}

	void deleteById(ID id,HibernateEntityInfo<T, ID> entityInfo) {
		Exceptions.service("notNull", "The given id must not be null!");
		if (!exists(id,entityInfo)) {
			return;
//			throw new DaoException("1",String.format("No %s entity with id %s exists!",entityInfo.getJavaType(), id));
		}
		delete(getById(id,entityInfo));
	}

	void deleteByList(List<T> entitys) {
		for (T entity : entitys) {
			delete(entity);
		}
	}
	
	T getById(ID id,HibernateEntityInfo<T, ID> entityInfo) {
		if(ObjUtil.isEmpty(id)){
			return null;
		}
		return 	super.currentSession().load(entityInfo.getJavaType(), id);
	}
	
	T getById(ID id,HibernateEntityInfo<T, ID> entityInfo,LockModeType lockModeType) {
		if(ObjUtil.isEmpty(id)){
			return null;
		}
//		TODO: 2016-08-12 查询的锁 还没有实现
//		return 	super.currentSession().load(entityInfo.getJavaType(), id, lockModeType);
		return 	super.currentSession().load(entityInfo.getJavaType(), id);
	}
	
	
	boolean exists(ID id,HibernateEntityInfo<T, ID> entityInfo) {
		Exceptions.service("notNull", "The given id must not be null!");
//		if (entityInfo.getIdAttribute() != null) {
//			String entityName = entityInfo.getEntityName();
//			String idAttributeName = entityInfo.getIdAttribute().getName();
//			String existsQuery = QueryUtils.getQueryString(QueryUtils.EXISTS_QUERY_STRING, QueryUtils.DEFAULT_ALIAS, entityName, idAttributeName);
//			Query queryquery.setParameter(idAttributeName, id);
//			return query.list().size() == 1;
//		} else {
//			return getById(id,entityInfo) != null;
//		}
		return getById(id,entityInfo) != null;
	}
	
	public Long isExist(Finder finder) {
		Query query = super.currentSession().createQuery(finder.getHql());
		query.setCacheable(finder.isCacheable());
		finder.setParamsToQuery(new HibernateQuery(query));
		Object result = query.uniqueResult();
		if(result instanceof Long){
			return (Long) result;
		}
		if(result instanceof Integer){
			return ObjUtil.toLong(result);
		}
		return 0l;
	}
	
	public int batchHandle(Finder finder) {
		Query query =  super.currentSession().createQuery(finder.getHql());
		query.setCacheable(finder.isCacheable());
		finder.setParamsToQuery(new HibernateQuery(query));
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> find(Finder finder) {
		Query query = super.currentSession().createQuery(finder.getHql());
		finder.setParamsToQuery(new HibernateQuery(query));
		query.setCacheable(finder.isCacheable());
		return query.list();
	}
	
	public PagedInfo<T> findPage(Finder finder,Page page) {
		
		//添加前台参数
		finder.putAutoParam("sys", page.toParam());
		finder.setSort(page.getSort());
		Long total=count(finder);
		Query query =  super.currentSession().createQuery(finder.getHql());
		query.setCacheable(finder.isCacheable());
		finder.setParamsToQuery(new HibernateQuery(query));
		query.setFirstResult(page.getOffset());
		query.setMaxResults(page.getPageSize());
		@SuppressWarnings("unchecked")
		List<T> content = total > page.getOffset() ? query.list() : Collections.<T> emptyList();
		return new PagedInfoImpl<T>(content, page, total);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByPage(Finder finder,Page page) {
		//添加前台参数
		finder.putAutoParam("sys", page.toParam());
		finder.setSort(page.getSort());
		Query query =  super.currentSession().createQuery(finder.getHql());
		query.setCacheable(finder.isCacheable());
		finder.setParamsToQuery(new HibernateQuery(query));
		query.setFirstResult(page.getOffset());
		query.setMaxResults(page.getPageSize());
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public Long count(Finder finder) {
		Query query = super.currentSession().createQuery(finder.getRowCountHql());
		query.setCacheable(finder.isCacheable());
		Assert.notNull(query);
		finder.setParamsToQuery(new HibernateQuery(query));
		List<Long> totals = query.list();
		Long total = 0L;
		for (Long elentityManagerent : totals) {
			total += elentityManagerent == null ? 0 : elentityManagerent;
		}
		return total;
	}
	@SuppressWarnings("unchecked")
	public Long countOriginalHql(Finder finder) {
		Query query =  super.currentSession().createQuery(finder.getHql());
		query.setCacheable(finder.isCacheable());
		Assert.notNull(query);
		finder.setParamsToQuery(new HibernateQuery(query));
		List<Long> totals = query.list();
		Long total = 0L;
		for (Long elentityManagerent : totals) {
			total += elentityManagerent == null ? 0 : elentityManagerent;
		}
		return total;
	}
}