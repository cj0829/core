/**
 * Project Name:core
 * File Name:sdfdsf.java
 * Package Name:org.csr.core.common
 * Date:2014-1-27上午9:59:27
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.csr.core.Param;
import org.csr.core.Persistable;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.Sort;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.CacheableParam;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:BasisServiceImpl <br/>
 * System Name：    <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin
 * @version  1.0 
 * @since    JDK 1.6
 * @see 	
 * 功能描述：最基本服务功能的实现 <br/>
 * 公用方法描述：<br/> 
 */
public abstract class SimpleBasisService<T extends Persistable<ID>, ID extends Serializable> implements BasisService<T,ID>{


	/**
	 * getDao: 获取子类的dao<br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public abstract BaseDao<T,ID> getDao();

	public Long nextSequence(){
		return getDao().nextSequence();
	}
	@Override
	public PagedInfo<T> findAllPage(Page page) {
		return getDao().findAllPage(page);
	}
	
	@Override
	public List<T> findListByPage(Page page) {
		return getDao().findAll(page);
	}
	
	public List<T> findAllList(){
		return getDao().findAll();
	}
	
	@Override
	public List<T> findByIds(List<ID> ids){
		return getDao().findByIds(ids);
	}
	
	@Override
	public List<T> findByIds(ID[] ids){
		return getDao().findByIds(ids);
	}
	
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids){
		return getDao().findMapByIds(ids);
	}
	
	@Override
	public Map<ID, T> findMapByIds(ID[] ids){
		return getDao().findMapByIds(ids);
	}
	@Override
	public List<T> findByIds(List<ID> ids,Sort sort){
		return getDao().findByIds(ids,sort);
	};
	@Override
	public List<T> findByIds(ID[] ids,Sort sort){
		return getDao().findByIds(ids,sort);
	};
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids,Sort sort){
		return getDao().findMapByIds(ids,sort);
	};
	@Override
	public Map<ID, T> findMapByIds(ID[] ids,Sort sort){
		return getDao().findMapByIds(ids,sort);
	};
	
  	@Override
   	public String findNameById(Long id,String name) {
   		AndEqParam and=new AndEqParam("id", id);
   		return getDao().findFieldByParam(name, and);
   	}
	
	@Override
	public T findById(ID id) {
		return getDao().findById(id);
	}
	@Override
	public T findById(ID id,LockModeType lockModeType) {
		return getDao().findById(id, lockModeType);
	}

	@Override
	public final boolean saveSimple(T entity) {
		getDao().save(entity);
		return true;
	}

	@Override
	public final void updateSimple(T entity) {
		getDao().update(entity);
	}

	
	@Override
	public final void deleteSimple(T entity) {
		getDao().delete(entity);
	}
	
	@Override
	public final void deleteSimple(ID id) {
		getDao().deleteById(id);
	}
	
	@Override
	public void clear() {
		getDao().clear();		
	}
	
	public void batchFlush(){
		getDao().batchFlush();
	}
//	@Override
//	public final void deleteSimple(String ids) {
//		
//	}

	@Override
	public final int deleteSimple(ID[] ids) {
		return getDao().deleteByIds(ids);
	}
	
	/**
	 * 创建一个分页对象
	 * @param paramValues
	 * @return
	 */
	protected List<Param> createParam(boolean cacheable){
		ArrayList<Param> params=new ArrayList<Param>();
		params.add(new CacheableParam(cacheable));
		return params;
	}
	/**
	 * check a String the parameter.
	 * 
	 * @param value
	 * @param name
	 */
	protected void checkParameter(Object value, String name) {
		if (ObjUtil.isEmpty(value)) {
			throw new IllegalArgumentException("The parameter (" + name + ") for current object value can't be null or empty!");
		}
	}
	/**
	 * check a Collection the paramter.
	 * 
	 * @param values
	 * @param name
	 */
	protected void checkParameter(Collection<?> values, String name) {
		if (null == values || values.isEmpty()) {
			throw new IllegalArgumentException("The parameter (" + name + ") for current object values can't be null or empty!");
		}
	}
	
}

