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

import javax.persistence.LockModeType;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.Sort;

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
public interface BasisService<T, ID extends Serializable> {

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
	
	/**
	 * findAllListPage: 获取部分数据 <br/>
	 * @author caijin
	 * @param page
	 * @return
	 * @since JDK 1.7
	 */
	public List<T> findListByPage(Page page);
	
	/**
	 * 
	 * findAllList: 获取全部集合 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public List<T> findAllList();
	
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(List<ID> ids);
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(ID[] ids);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(List<ID> ids);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(ID[] ids);
	
	
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(List<ID> ids,Sort sort);
	/**
	 * findByIds: 描述方法的作用 <br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	List<T> findByIds(ID[] ids,Sort sort);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(List<ID> ids,Sort sort);
	/**
	 * findMapByIds: 根据id数组查询 全部对象。并返回id为key，对象为值得Map对象<br/>
	 * @author caijin
	 * @param ids
	 * @return
	 * @since JDK 1.7
	 */
	Map<ID, T> findMapByIds(ID[] ids,Sort sort);
	
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

	
	/**
	 * 通过加锁的方式，查询数据，方便多线程查询修改的时候使用
	 * @author caijin
	 * @param id
	 * @param lockModeType
	 * @return
	 * @since JDK 1.7
	 */
	public T findById(ID id,LockModeType lockModeType);
	/**
	 * 
	 * save: 保存对象 <br/>
	 * @author caijin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
    public boolean saveSimple(T entity);

    /**
     * 
     * update: 修改对象 <br/>
     * @author caijin
     * @param entity
     * @since JDK 1.7
     */
    public void updateSimple(T entity);
    
    
    
    /**
     * deleteSimple: 直接查处对象 <br/>
     * @author Administrator
     * @param entity
     * @since JDK 1.7
     */
    void deleteSimple(T entity);

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

	
	public void clear();
	
	public void batchFlush();
	
}
