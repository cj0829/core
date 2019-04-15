package org.csr.core.persistence.business.dao;

import java.util.List;

import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.domain.Dictionary;

/**
 * ClassName:DictionaryDao.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-24下午4:06:41 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface DictionaryDao extends BaseDao<Dictionary,Long> {
	
	/**
	 * findOneLevel: 查询全部一级 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public List<Dictionary> findOneLevel();
	/**
	 * @description:查找子类的id
	 * @param: 
	 * @return: List 
	 */
	public List<Long> findChildrenIds(Long id);
	
	/**
	 * @description:根据父类id删除子类数据
	 * @param: 
	 * @return: void 
	 */
	public void deleteChildren(Long id);
	
	/**
	 * @description:判断数据字典类型是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	public Dictionary checkdictTypeOnly(Long parentId,String name);
	
	/**
	 * @description:判断数据字典值是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	public Dictionary checkdictValeuOnly(Long parentId,String value);
	
	/**
	 * @description:根据数据字典的divtValue值查询
	 * @param: Integer dictValue
	 * @return:Dictionary
	 */
	public Dictionary findByDictValue(Integer dictValue);
	
	/**
	 * @description:通过一级数据名称查询出其对应的所有子类数据
	 * @param: 
	 * @return: Map<Short,String> 
	 */
	public List<Dictionary> findDictListByName(String name);

	
}
