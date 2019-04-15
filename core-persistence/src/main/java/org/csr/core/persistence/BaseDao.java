package org.csr.core.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.csr.core.Param;
import org.csr.core.Persistable;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.Sort;

public interface BaseDao<T extends Persistable<ID>, ID extends Serializable> {
	
	/**
	 * nextSequence: 获取全局的sequence <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Long nextSequence();
	
	/**
	 * 创建一个新的实例
	 * @author caijin
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @since JDK 1.7
	 */
	public T newInstance() throws InstantiationException, IllegalAccessException;
	/**
	 * @param entity
	 */
	public T save(T entity);
	
	public void delete(T entity);
	
	public void deleteById(ID id);
	
	public int deleteByIds(ID[] ids);
	
	public int deleteByIds(List<ID> ids);
	
	public void deleteByList(List<T> entitys);
	
	public int deleteAll();
	
	public T update(T entity);
	
	public T findById(ID id);
	
	public T findById(ID id,LockModeType lockModeType);
	
	List<T> findByIds(List<ID> id);
	
	List<T> findByIds(ID[] id);
	
	Map<ID, T> findMapByIds(List<ID> ids);
	
	Map<ID, T> findMapByIds(ID[] ids);
	
	Map<ID, Object> findMapFieldByIds(String fieldName,List<ID> ids);
	
	Map<ID, Object> findMapFieldByIds(String fieldName,ID[] ids);
	
	Map<ID, Object> findMapFieldByIds(String fieldName,List<ID> ids,Sort sort);
	
	Map<ID, Object> findMapFieldByIds(String fieldName,ID[] ids,Sort sort);
	
	List<T> findByIds(List<ID> id,Sort sort);
	
	List<T> findByIds(ID[] id,Sort sort);
	
	Map<ID, T> findMapByIds(List<ID> ids,Sort sort);
	
	Map<ID, T> findMapByIds(ID[] ids,Sort sort);
	
	public void clear();
	
	public void batchFlush();
	
	List<T> findAll();
	
	List<T> findAll(Page page);
	
	List<T> findByParam(Param... param);

	List<T> findByParam(List<Param> params);
	
	public PagedInfo<T> findAllPage(Page page);
	
	/**
	 * 提供单个对象，的单个属性值<br>
	 * 例如，如果我要查询登录名，可以只是findFieldByParam("loginName",param);
	 * fieldName
	 * @author caijin
	 * @param fieldName
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	<O> O findFieldByParam(String fieldName,Param... param);
	
	<O> O findFieldByParam(String fieldName,List<Param> params);
	/**
	 * 提供查询一个列名的全部数据<br>
	 * 例如：我要查询一个表的id集合：findFieldListByParam("id",param);
	 * findFieldListByParam: 描述方法的作用 <br/>
	 * @author caijin
	 * @param fieldName 要查询的列名称
	 * @param param 查询参数
	 * @return
	 * @since JDK 1.7
	 */
	<O> List<O> findFieldListByParam(String fieldName,Param... param);
	
	<O> List<O> findFieldListByParam(String fieldName,List<Param> params);
	
	/**
	 * 提供查询一个列名的全部数据<br>
	 * 例如，top的查询findFieldTopByParam("name"param);
	 * @author caijin
	 * @param fieldName 要查询的列名称
	 * @param top 查询多少条
	 * @param param 查询参数
	 * @return
	 * @since JDK 1.7
	 */
	List<String> findFieldTopByParam(String fieldName,Integer top,Param... param);
	
	List<String> findFieldTopByParam(String fieldName,Integer top,List<Param> params);
	
	/**
	 * @param finder
	 * @return
	 */
	Long count(Finder finder);
	
	Long countParam(Param... param);
	Long countParam(List<Param> params);
	
	T exist(Finder finder);
	T existParam(Param... param);
	T existParam(List<Param> params);

	/**
	 * findOrgChildrenIds: 查询当前root下面的所有子机构，并返回机构ids提供查询 <br/>
	 * @author caijin
	 * @param root
	 * @return
	 * @since JDK 1.7
	 */
	List<Long> findOrgChildrenIds(Long... root);

	/**
	 * deleteByParams:通过参数删除数据。<br/>
	 * @author Administrator
	 * @param param
	 * @return
	 * @since JDK 1.7
	 */
	public int deleteByParams(Param... param);
	public int deleteByParams(List<Param> params);
	
}
