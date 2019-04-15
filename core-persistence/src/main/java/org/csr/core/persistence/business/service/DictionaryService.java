package org.csr.core.persistence.business.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.csr.core.persistence.business.domain.Dictionary;

/**
 * ClassName:DictionaryService.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-24下午4:06:54 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface DictionaryService {
	 /**
 	 * @description:根据数据字典名获取数据字典列表
 	 * @param: name：数据字典名
 	 * @return: List<Dictionary> 
 	 */
	public List<Dictionary> findList(String name);
	/**
	 * findAllCacheList: 查询缓存的全部数据 <br/>
	 * @author caijin
	 * @return 
	 * @since JDK 1.7
	 */
	public Hashtable<String, Map<String, String>> findAllCacheList();
	/**
	 * @description:查询所有数据字典大类
	 * @param: 
	 * @return: ResultInfo 
	 */
	public List<Dictionary> findAllList();
	
	/**
	 * 通过id查找数据字典那么成
	 * @param id
	 * @return
	 */
	public String findDictTypeById(Long id);
	/**
	 * @description:通过id查找数据字典信息
	 * @param: 
	 * @return: Dictionary 
	 */
	public Dictionary findById(Long id);
	
	/**
	 * @description:通过一级数据类型和子类的数据序号查找三级字典信息
	 * @param: 
	 * @return: Map<Short,String> 
	 */
	public Map<String, String> findDictMap(String name);
	
	/**
	 * @description:添加数据字典信息
	 * @param: dict-保存信息
	 * @param: parentId-父节点。如果想保持到根节点，parentId=null
	 * @return: void 
	 */
	public void save(Dictionary dict,Long parentId);
	/**
	 * @description:删除一个或多个对象
	 * 				先删除子类对象
	 * @param: 
	 * @return: void 
	 */
	public void delete(String deleteIds);  

	/**
	 * @description:判断数据字典类型是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	Dictionary checkdictTypeOnly(String type);
	/**
	 * @description:判断子节点类型是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	Dictionary checkdictTypeOnlyInChild(Long parentId, String name);
	
	/**
	 * @description:判断子节点值是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	Dictionary checkdictValueOnlyInChild(Long parentId, String value);
	
	 /**
     * 检查名字是否存在。
     * @param id
     * @param name
     * @return
     */
	public boolean checkNameIsExist(Long id, String name);
	
	/**
	 * findByDictType:通过数据字典类型查询<br/>
	 * @author Administrator
	 * @param dictType
	 * @return
	 * @since JDK 1.7
	 */
	public List<Dictionary> findByDictType(String dictType);

	
}
