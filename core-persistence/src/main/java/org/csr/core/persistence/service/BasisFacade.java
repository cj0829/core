/**
 * Project Name:core
 * File Name:sdfdsf.java
 * Package Name:org.csr.core.common
 * Date:2014-1-27上午9:59:27
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/
package org.csr.core.persistence.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.Sort;
import org.csr.core.web.bean.VOBase;

/**
 * ClassName:BasisServiceImpl <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-1-27 上午9:31:56 
 * @author   caijin 
 * @version  1.0  
 * @since    JDK 1.7
 *
 * 功能描述：这个是基础的服务，提供针对的T 泛型对象的  增、删、改、查，的最基本功能<br/>
 * 由于只是提供的最简单的方法。在具体业务中，有不满足的需求，允许自己扩展功能<br/>
 * 增、删、改，方法不允许继承<br/>
 * 公用方法描述： findAllPage(),findById() 是查询功能  <br/>
 */
public interface BasisFacade<T extends VOBase<ID>, ID extends Serializable> {

	/**
	 * nextSequence: 获取全局的sequence只是default_global的sequence <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Long nextSequence();
	/**
	 * findAllListPage: 查询全部的泛型对象的数据，分页的方式 <br/>
	 * @author caijin
	 * @param page
	 * @return
	 * @since JDK 1.7
	 */
	public PagedInfo<T> findAllPage(Page page);
	@SuppressWarnings("rawtypes")
	public PagedInfo<T> findAllPage(Page page,SetBean setBean);
	/**
	 * findAllListPage: 获取部分数据 <br/>
	 * @author caijin
	 * @param page
	 * @return
	 * @since JDK 1.7
	 */
	public List<T> findListByPage(Page page);
	@SuppressWarnings("rawtypes")
	public List<T> findListByPage(Page page,SetBean setBean);
	
	/**
	 * 
	 * findAllList: 获取全部集合 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public List<T> findAllList();
	@SuppressWarnings("rawtypes")
	public List<T> findAllList(SetBean setBean);
	
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(List<ID> ids);
	@SuppressWarnings("rawtypes")
	List<T> findByIds(List<ID> ids,SetBean setBean);
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(ID[] ids);
	@SuppressWarnings("rawtypes")
	List<T> findByIds(ID[] ids,SetBean setBean);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(List<ID> ids);
	@SuppressWarnings("rawtypes")
	Map<ID, T> findMapByIds(List<ID> ids,SetBean setBean);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(ID[] ids);
	@SuppressWarnings("rawtypes")
	Map<ID, T> findMapByIds(ID[] ids,SetBean setBean);
	
	
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(List<ID> ids,Sort sort);
	@SuppressWarnings("rawtypes")
	List<T> findByIds(List<ID> ids,Sort sort,SetBean setBean);
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(ID[] ids,Sort sort);
	@SuppressWarnings("rawtypes")
	List<T> findByIds(ID[] ids,Sort sort,SetBean setBean);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(List<ID> ids,Sort sort);
	@SuppressWarnings("rawtypes")
	Map<ID, T> findMapByIds(List<ID> ids,Sort sort,SetBean setBean);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(ID[] ids,Sort sort);
	@SuppressWarnings("rawtypes")
	Map<ID, T> findMapByIds(ID[] ids,Sort sort,SetBean setBean);
	
	/**
	 * findNameById: 根据id查询Name <br/>
	 * @author caijin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public String findNameById(Long id,String fieldName);
	/**
	 * findById: 查询单个对象，根据id <br/>
	 * 
	 * @author caijin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public T findById(ID id);
	@SuppressWarnings("rawtypes")
	public T findById(ID id,SetBean setBean);
	
    /**
     * deleteById: 描述方法的作用 <br/>
     * @author caijin
     * @param id
     * @since JDK 1.7
     */
	public void deleteSimple(ID id);
	
	/**
	 * 
	 * ids: 删除多组对象，根据id <br/>
	 * @author caijin
	 * @param id
	 * @since JDK 1.7
	 */
	public int deleteSimple(ID[] ids);
	
}
