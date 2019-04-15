package org.csr.core.persistence.query.jpa;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.csr.core.Param;
import org.csr.core.Persistable;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.page.Sort;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.PageRequest;
import org.csr.core.persistence.domain.RootDomain;
import org.csr.core.persistence.generator5.GlobalSequence;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.QueryUtils;
import org.csr.core.persistence.query.SetOrg;
import org.csr.core.persistence.support.JpaEntityInfo;
import org.csr.core.persistence.support.JpaEntityInfoSupport;
import org.csr.core.util.ObjUtil;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class JpaDao<T extends Persistable<ID>,ID extends Serializable>  implements BaseDao<T, ID> {
	
	private AdapterJpaDao<T,ID> jpaDao;
	protected JpaEntityInfo<T, ID> entityInfo;
	
	@Resource
	protected JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Resource(name="adapterJpaDao")
	public void setAdapterJpaDao(AdapterJpaDao<T,ID> jpaDao){
		this.jpaDao=jpaDao;
		Class<T> entityClass=entityClass();
		entityInfo = (JpaEntityInfo<T, ID>) JpaEntityInfoSupport.getMetadata(entityClass,jpaDao.entityManager);
	}
	public AdapterJpaDao<T,ID> getAdapterJpaDao(){
		return this.jpaDao;
	}
	
	public abstract Class<T> entityClass();

	public T newInstance() throws InstantiationException, IllegalAccessException{
		return entityClass().newInstance();
	}
	@Override
	public Long nextSequence(){
		SessionImplementor session = jpaDao.entityManager.unwrap(org.hibernate.engine.spi.SessionImplementor.class);
		return (Long) GlobalSequence.getSequence().generate(session);
	}
	
	@Override
	public T save(T entity) {
		return jpaDao.save(entity,entityInfo);
	}
	@Override
	public T update(T entity) {
		return jpaDao.update(entity);
	}
	
	@Override
	public void delete(T entity) {
		jpaDao.delete(entity);
	}

	@Override
	public void deleteById(ID id) {
		jpaDao.deleteById(id,entityInfo);
	}
	
	@Override
	public int deleteByIds(ID[] ids) {
		if(ObjUtil.isEmpty(ids)){
			return 0;
		}
		return deleteByIds(Arrays.asList(ids));
	}
	@Override
	public int deleteByIds(List<ID> ids){
		Finder finder = FinderImpl.create(QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityInfo.getEntityName()));
		finder.append(" where x.id in (:ids)","ids",ids);
		return jpaDao.batchHandle(finder);
	}
	@Override
	public void deleteByList(List<T> entitys) {
		jpaDao.deleteByList(entitys);
	}
	
	@Override
	public int deleteAll() {
		Finder finder = FinderImpl.create(QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityInfo.getEntityName()));
		return jpaDao.batchHandle(finder);
	}

	@Override
	public T findById(ID id) {
		return jpaDao.getById(id,entityInfo);
	}
	
	@Override
	public T findById(ID id,LockModeType lockModeType) {
		return jpaDao.getById(id,entityInfo,lockModeType);
	}
	
	
	@Override
	public Map<ID,T> findMapByIds(ID[] ids) {
		return findMapByIds(ids, null);
	}
	
	@Override
	public Map<ID,T> findMapByIds(List<ID> ids) {
		return findMapByIds(ids, null);
	}
	
	@Override
	public List<T> findByIds(List<ID> ids) {
		return findByIds(ids, null);
	}
	
	@Override
	public List<T> findByIds(ID[] ids) {
		return findByIds(ids, null);
	}
	
	@Override
	public Map<ID,T> findMapByIds(ID[] ids,Sort sort) {
		return findMapByIds(ObjUtil.asList(ids),sort);
	}
	
	@Override
	public Map<ID,T> findMapByIds(List<ID> ids,Sort sort) {
		HashMap<ID,T> haspMap=new LinkedHashMap<ID,T>();
		if(ObjUtil.isEmpty(ids)){
			return haspMap;
		}
		List<T> list =  findByIds(ids);
		for (int i = 0; i < list.size(); i++) {
			haspMap.put(list.get(i).getId(), list.get(i));
		}
		return haspMap;
	}
	
	public Map<ID, Object> findMapFieldByIds(String fieldName,List<ID> ids){
		return findMapFieldByIds(fieldName, ids ,null);
	};
	
	public Map<ID, Object> findMapFieldByIds(String fieldName,ID[] ids){
		return findMapFieldByIds(fieldName, ObjUtil.asList(ids),null);
	};
	
	public Map<ID, Object> findMapFieldByIds(String fieldName,ID[] ids,Sort sort){
		return findMapFieldByIds(fieldName, ObjUtil.asList(ids),sort);
	};
	public Map<ID, Object> findMapFieldByIds(String fieldName,List<ID> ids,Sort sort){
		HashMap<ID,Object> haspMap=new LinkedHashMap<ID,Object>();
		if(ObjUtil.isEmpty(ids)){
			return haspMap;
		}
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_FIELD_ID_NAME_QUERY,fieldName,entityInfo.getEntityName()));
		finder.append(" where x.id in (:ids)").setParam("ids",ids);
		List<Object[]> objs =  find(finder);
		for (int i = 0; i < objs.size(); i++) {
			Object[] object = objs.get(i);
			@SuppressWarnings("unchecked")
			ID id =  (ID) object[0];
			Object value = object[1];
			haspMap.put(id,value);
		}
		return haspMap;
	};
	
	@Override
	public List<T> findByIds(List<ID> ids,Sort sort) {
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<T>(0);
		}
		Finder finder=FinderImpl.create(QueryUtils.applySorting("select x from "+entityInfo.getEntityName()+" x where x.id in (:ids)", sort));
		finder.setParam("ids",ids);
		return find(finder);
	}
	
	
	@Override
	public List<T> findByIds(ID[] ids,Sort sort) {
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<T>(0);
		}
		return findByIds(Arrays.asList(ids),sort);
	}
	
	@Override
	public void clear() {
		jpaDao.clear();		
	}
	
	@Override
	public void batchFlush() {
		jpaDao.flush();		
	}
	
	public Long countParam(Param... param){
		return countParam(ObjUtil.toList(param));
	}
	@Override
	public Long countParam(List<Param> params) {
		Finder finder = FinderImpl.create(QueryUtils.getQueryString(QueryUtils.COUNT_QUERY_STRING, entityInfo.getEntityName()));
		finder.autoPackagingParams("sys", params);
		return countOriginalHql(finder);
	}
	@Override
	public T exist(Finder finder){
		List<T> list=findByPage(new PageRequest(1, 1),finder);
		if(ObjUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	/**
	 * TODO （错误:参数为空是会查全部。2016/1/13）
	 * @param key
	 * @param name
	 * @return
	 */
	@Override
	public T existParam(Param... param){
		return existParam(ObjUtil.toList(param));
	}
	
	@Override
	public T existParam(List<Param> params){
		List<T> list=findByPage(new PageRequest(1, 1),createFinderByParam(params));
		if(ObjUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<T> findAll() {
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_ALL_QUERY, entityInfo.getEntityName()));
		return jpaDao.find(finder);
	}

	@Override
	public List<T> findAll(Page page) {
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_ALL_QUERY, entityInfo.getEntityName()));
		return jpaDao.findByPage(finder, page);
	}
	@Override
	public PagedInfo<T> findAllPage(Page page) {
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_ALL_QUERY, entityInfo.getEntityName()));
		return (PagedInfo<T>) jpaDao.findPage(finder, page);
	}
	@Override
	public List<T> findByParam(Param... param) {
		return findByParam(ObjUtil.toList(param));
	}
	
	@Override
	public List<T> findByParam(List<Param> params) {
		return find(createFinderByParam(params));
	}
	@Override
	public int deleteByParams(Param... param) {
		return deleteByParams(ObjUtil.asList(param));
	}
	
	@Override
	public int deleteByParams(List<Param> params) {
		if(ObjUtil.isEmpty(params)){
			return 0;
		}
		Finder finder = FinderImpl.create(QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityInfo.getEntityName()));
		finder.putAutoParam("sys", params);
		return jpaDao.batchHandle(finder);
	}
	
	@Override
	public <O> O findFieldByParam(String fieldName,Param... param) {
		return findFieldByParam(fieldName, ObjUtil.toList(param));
	}
	@Override
	public <O> O findFieldByParam(String fieldName,List<Param> params) {
		List<O> list=findByPage(new PageRequest(1, 1),createFinderByFieldAndEqParam(fieldName, params));
		if(ObjUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public <O> List<O> findFieldListByParam(String fieldName,Param... param) {
		return find(createFinderByFieldAndEqParam(fieldName, ObjUtil.toList(param)));
	}
	@Override
	public <O> List<O> findFieldListByParam(String fieldName,List<Param> params) {
		return find(createFinderByFieldAndEqParam(fieldName, params));
	}
	@Override
	public List<String> findFieldTopByParam(String fieldName,Integer top,Param... params){
		return findFieldTopByParam(fieldName, top, ObjUtil.toList(params));
	}
	@Override
	public List<String> findFieldTopByParam(String fieldName,Integer top,List<Param> params){
		if(ObjUtil.isBlank(fieldName)){
			Exceptions.dao("","查询字段名称 不能为空");
		}
		Finder finder=createFinderByFieldAndEqParam(fieldName, params);
		if(ObjUtil.isEmpty(top)){
			top=1000;
		}
		return findByPage(new PageRequest(1,top),finder);
	}
	
	/**
	 * findOrgChildrenIds: 查询当前root下面的所有子机构，并返回机构ids提供查询 <br/>
	 * @see org.csr.core.persistence.BaseDao#findOrgChildrenIds(java.lang.Long[])
	 */
	@Override
	public List<Long> findOrgChildrenIds(Long... orgIds) {
		Finder finder=FinderImpl.create("select d.id,(select count(ds.id) from Organization ds where ds.parentId=d.id) from Organization d where 1=1");
		finder.append(" and d.parentId in :orgIds", "orgIds",Arrays.asList(orgIds));
		List<Object[]> list = find(finder);
		List<Long> ids=hasChildToOrgIds(list);
		if(ObjUtil.isEmpty(ids)){
			ids=new ArrayList<Long>(0);
		}
		ids.addAll(Arrays.asList(orgIds));
		return ids;
	}
	
	/**
	 * hasChildToIds: 检测统计查询，是否有子类对象。如果有子类对象的，拼接成ids <br/>
	 * @author caijin
	 * @param list 
	 * @return
	 * @since JDK 1.7
	 */
	private List<Long> hasChildToOrgIds(List<Object[]> list){
		List<Long> organizationList = new ArrayList<Long>();
		for(int i=0;ObjUtil.isNotEmpty(list) && i<list.size();i++){
			Object[] obj = list.get(i);
			if(obj[1] != null && (Long)obj[1] >= 0){
				Long orgaId = (Long) obj[0];
				Long count=(Long)obj[1];
				if(count>0){
					organizationList.addAll(findOrgChildrenIds(orgaId));
				}
				organizationList.add(orgaId);
			}
		}
		return organizationList;
	}
	
	/**
	 * 数组转字符串
	 * @param idArr：数组 
	 * @return String
	 */
	public <O extends RootDomain<ID>> List<Long> arrayTransforString(Iterable<O> idArr,SetOrg<O,ID> set){ 
		if (ObjUtil.isEmpty(idArr)) {
			return new ArrayList<>();
		}
		Iterator<O> it=idArr.iterator();
		Set<Long> ids=new HashSet<Long>();
		while (it.hasNext()) {
			ids.add(set.getOrgId(it.next()));
		}
		return Arrays.asList(ids.toArray(new Long[ids.size()]));
	} 
	///
	
	protected int batchHandle(Finder finder) {
		return jpaDao.batchHandle(finder);
	}

	protected int isExist(Finder finder) {
		Long num=jpaDao.isExist(finder);
		if(num!=null){
			return num.intValue();
		}
		return 0;
	}
	
	protected int compute(Finder finder) {
		Long num=jpaDao.isExist(finder);
		if(num!=null){
			return num.intValue();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	protected <O> List<O> find(Finder finder){
		return (List<O>) jpaDao.find(finder);
	}
	@SuppressWarnings("unchecked")
	protected <O> List<O> findByPage(Page page,Finder finder) {
		return (List<O>) jpaDao.findByPage(finder, page);
	}
	@SuppressWarnings("unchecked")
	protected <O> PagedInfo<O> findPage(Page page,Finder finder) {
		return (PagedInfo<O>) jpaDao.findPage(finder, page);
	}

	public Long count(Finder finder) {
		return jpaDao.count(finder);
	}
	

	protected Long countOriginalHql(Finder finder) {
		return jpaDao.countOriginalHql(finder);
	}
	
	/**
	 * 创建一个分页对象
	 * @param paramValues
	 * @return
	 */
	protected <O> PagedInfo<O> createPagedInfo(long count,Page page,List<O> items){
		return new PagedInfoImpl<O>(items, page, count);
	}
	/**
	 * 创建一个分页对象
	 * @param paramValues
	 * @return
	 */
	protected <O> PagedInfo<O> createPagedInfo(Finder finder,Page page,List<O> items){
		return new PagedInfoImpl<O>(items, page, count(finder));
	}
	
	/**
	 * createFinderByFieldAndEqParam: 查询但字段 <br/>
	 * 获取但字段
	 * @author caijin
	 * @param fieldName
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	private Finder createFinderByFieldAndEqParam(String fieldName,List<Param> params){
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_FIELDNAME_QUERY,fieldName,entityInfo.getEntityName()));
		return finder.autoPackagingParams("sys", params);
	}
	
	/**
	 * 默认给予转换
	 * @param paramValues
	 * @return
	 */
	private Finder createFinderByParam(List<Param> params){
		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_ALL_QUERY, entityInfo.getEntityName()));
		return finder.autoPackagingParams("sys", params);
	}
	
}
