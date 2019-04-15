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

import org.csr.core.Param;
import org.csr.core.Persistable;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.Sort;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.CacheableParam;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.persistence.util.PersistableUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.VOBase;

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
public abstract class SimpleBasisFacade<T extends VOBase<ID>,P extends Persistable<ID>, ID extends Serializable> implements BasisFacade<T,ID>{

	/**
	 * getDao: 获取子类的dao<br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public abstract BaseDao<P,ID> getDao();
	public abstract T wrapBean(P doMain);
	
	public Long nextSequence(){
		return getDao().nextSequence();
	}
	@Override
	public String findNameById(Long id,String fieldName){
		AndEqParam and=new AndEqParam("id", id);
   		return getDao().findFieldByParam(fieldName, and);
	}
	
	
	@Override
	public PagedInfo<T> findAllPage(Page page) {
		return PersistableUtil.toPagedInfoBeans(getDao().findAllPage(page), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PagedInfo<T> findAllPage(Page page,SetBean setBean) {
		if(ObjUtil.isEmpty(setBean)){
			return findAllPage(page);
		}
		return PersistableUtil.toPagedInfoBeans(getDao().findAllPage(page),setBean);
	}

	
	@Override
	public List<T> findListByPage(Page page) {
		return PersistableUtil.toListBeans(getDao().findAll(page), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findListByPage(Page page,SetBean setBean) {
		if(ObjUtil.isEmpty(setBean)){
			return findListByPage(page);
		}
		return PersistableUtil.toListBeans(getDao().findAll(page),setBean);
	}
	
	@Override
	public List<T> findAllList(){
		return PersistableUtil.toListBeans(getDao().findAll(), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List<T> findAllList(SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findAllList();
		}
		return PersistableUtil.toListBeans(getDao().findAll(), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	
	@Override
	public List<T> findByIds(List<ID> ids){
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<T>(0);
		}
		return PersistableUtil.toListBeans(getDao().findByIds(ids), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByIds(List<ID> ids,SetBean setBean){
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<T>(0);
		}
		if(ObjUtil.isEmpty(setBean)){
			return findByIds(ids);
		}
		return PersistableUtil.toListBeans(getDao().findByIds(ids),setBean);
	}
	
	@Override
	public List<T> findByIds(ID[] ids){
		return PersistableUtil.toListBeans(getDao().findByIds(ids), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByIds(ID[] ids,SetBean setBean){
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<T>(0);
		}
		if(ObjUtil.isEmpty(setBean)){
			return findByIds(ids);
		}
		return PersistableUtil.toListBeans(getDao().findByIds(ids),setBean);
	}
	
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids){
		return PersistableUtil.toMap(this.findByIds(ids));
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findMapByIds(ids);
		}
		return PersistableUtil.toMap(this.findByIds(ids,setBean));
	}
	
	
	
	@Override
	public Map<ID, T> findMapByIds(ID[] ids){
		return this.findMapByIds(ObjUtil.toList(ids));
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<ID, T> findMapByIds(ID[] ids,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findMapByIds(ids);
		}
		return this.findMapByIds(ObjUtil.toList(ids),setBean);
	}
	@Override
	public List<T> findByIds(List<ID> ids,Sort sort){
		
		return PersistableUtil.toListBeans(getDao().findByIds(ids,sort), new SetBean<P>() {
			@Override
			public T setValue(P doMain) {
				return wrapBean(doMain);
			}
		});
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> findByIds(List<ID> ids,Sort sort,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findByIds(ids,sort);
		}
		return PersistableUtil.toListBeans(getDao().findByIds(ids,sort),setBean);
	};
	
	@Override
	public List<T> findByIds(ID[] ids,Sort sort){
		return this.findByIds(ObjUtil.toList(ids),sort);
	};
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<T> findByIds(ID[] ids,Sort sort,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findByIds(ids,sort);
		}
		return this.findByIds(ObjUtil.toList(ids),sort,setBean);
	};
	
	
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids,Sort sort){
		return PersistableUtil.toMap(this.findByIds(ids,sort));
	};
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<ID, T> findMapByIds(List<ID> ids,Sort sort,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findMapByIds(ids,sort);
		}
		return PersistableUtil.toMap(this.findByIds(ids,sort,setBean));
	};
	
	
	@Override
	public Map<ID, T> findMapByIds(ID[] ids,Sort sort){
		return this.findMapByIds(ObjUtil.toList(ids), sort);
	};
	
	
	@Override
	@SuppressWarnings("rawtypes")
	public Map<ID, T> findMapByIds(ID[] ids,Sort sort,SetBean setBean){
		if(ObjUtil.isEmpty(setBean)){
			return findMapByIds(ids,sort);
		}
		return this.findMapByIds(ObjUtil.toList(ids), sort,setBean);
	};
	
	@Override
	public T findById(ID id) {
		return this.wrapBean(getDao().findById(id));
	}
	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findById(ID id,SetBean setBean) {
		P findById = getDao().findById(id);
		return (T) setBean.setValue(findById);
	}

	
	@Override
	public final void deleteSimple(ID id) {
		getDao().deleteById(id);
	}
	
	@Override
	public int deleteSimple(ID[] ids) {
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
	 * check a Object the parameter.
	 * @param value
	 * @param name
	 */
	protected void checkParameter(Object value, String message) {
		if (ObjUtil.isEmpty(value)) {
			Exceptions.service("", message);
		}
	}
	
	/**
	 * check a String the parameter.
	 * 
	 * @param value
	 * @param name
	 */
	protected void checkParameter(String value, String message) {
		if (ObjUtil.isBlank(value)) {
			Exceptions.service("", message);
		}
	}
	/**
	 * check a Collection the paramter.
	 * 
	 * @param values
	 * @param name
	 */
	protected void checkParameter(Collection<?> values, String message) {
		if (null == values || values.isEmpty()) {
			Exceptions.service("", message);
		}
	}
	
	
	/**
	 * check a Object the parameter.
	 * 
	 * @param value
	 * @param name
	 */
	protected void checkParameter(Object value,String code, String message) {
		if (ObjUtil.isEmpty(value)) {
			Exceptions.service(code, message);
		}
	}
	
	/**
	 * check a String the parameter.
	 * 
	 * @param value
	 * @param name
	 */
	protected void checkParameter(String value,String code, String message) {
		if (ObjUtil.isBlank(value)) {
			Exceptions.service(code, message);
		}
	}
	/**
	 * check a Collection the paramter.
	 * 
	 * @param values
	 * @param name
	 */
	protected void checkParameter(Collection<?> values,String code, String message) {
		if (null == values || values.isEmpty()) {
			Exceptions.service(code, message);
		}
	}
}

