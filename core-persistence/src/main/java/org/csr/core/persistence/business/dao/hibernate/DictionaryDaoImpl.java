package org.csr.core.persistence.business.dao.hibernate;

import java.util.List;

import org.csr.core.persistence.Finder;
import org.csr.core.persistence.business.dao.DictionaryDao;
import org.csr.core.persistence.business.domain.Dictionary;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.hibernate.HibernateDao;

/**
 * ClassName:DictionaryDaoImpl.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-24下午4:07:40 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class DictionaryDaoImpl extends HibernateDao<Dictionary, Long>  implements DictionaryDao {
	
	@Override
	public Class<Dictionary> entityClass() {
		return Dictionary.class;
	}

	@Override
	public List<Dictionary> findOneLevel() {
		Finder finder=FinderImpl.create("select distinct d from Dictionary d left join fetch d.dictionaries dic where ");
		finder.append(" d.dictionary.id is null");
		finder.setCacheable(true);
		return this.find(finder);
	}
	
	/* 
	 * @description:查找子类的id
	 * (non-Javadoc)
	 * @see com.bluedot.csp.admin.system.dao.DictionaryDao#findChildrenIds(java.lang.Long)
	 */
	public List<Long> findChildrenIds(Long id){
		Finder finder=FinderImpl.create("select d.id from Dictionary d where d.dictionary.id= :dictId");
		finder.setParam("dictId", id);
		List<Long> list = find(finder);
		finder.setCacheable(true);
		return list;
	}
	
	/* 
	 * @description:根据父类id删除子类数据
	 * (non-Javadoc)
	 * @see com.bluedot.csp.admin.system.dao.DictionaryDao#deleteChildren(java.lang.Long)
	 */
	public void deleteChildren(Long parentId){
		Finder finder=FinderImpl.create("delete from Dictionary d where d.dictionary.id= :parentId");
		finder.setParam("parentId", parentId);
		this.batchHandle(finder);
	}
	
	/**
	 * @description:判断数据字典类型是否唯一
	 * @param: 
	 * @return: Boolean 
	 */
	@Override
	public Dictionary checkdictTypeOnly(Long parentId, String name) {
		Finder finder=FinderImpl.create("select d from Dictionary d where d.dictType=:dictType");
		finder.setParam("dictType", name);
		if(parentId!=null){
			finder.append(" and d.dictionary.id =:dictionary","dictionary", parentId);
		}else{
			finder.append(" and d.dictionary.id is null");
		}
		List<Dictionary> list = find(finder);
		finder.setCacheable(true);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * @description:根据数据字典的divtValue值查询
	 * @param: Integer dictValue
	 * @return:Dictionary
	 */
	@Override
	public Dictionary checkdictValeuOnly(Long parentId, String value) {
		Finder finder=FinderImpl.create("select d from Dictionary d where d.dictValue=:dictValue");
		finder.setParam("dictValue", value);
		if(parentId!=null){
			finder.append(" and d.dictionary.id =:dictionary","dictionary", parentId);
		}else{
			finder.append(" and d.dictionary.id is null");
		}
		List<Dictionary> list = find(finder);
		finder.setCacheable(true);
		if(list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	
	/* 
	 * @description:根据dictValue查询数据
	 * (non-Javadoc)
	 * @see com.bluedot.csp.admin.system.dao.DictionaryDao#findByDictValue(java.lang.Integer)
	 */
	public Dictionary findByDictValue(Integer dictValue) {
		Finder finder=FinderImpl.create("select d from Dictionary d where d.dictValue =:dictValue ");
		finder.setParam("dictValue", dictValue);
		List<Dictionary> list = this.find(finder);
		finder.setCacheable(true);
		return (Dictionary)list.get(0);
	}
	
	/* 
	 * @description:通过一级数据名称查询出其对应的所有子类数据
	 * (non-Javadoc)
	 * @see com.bluedot.csp.admin.system.dao.DictionaryDao#findDictListByName(java.lang.Integer)
	 */
	public List<Dictionary> findDictListByName(String name) {
		Finder finder=FinderImpl.create("select distinct d from Dictionary d left join fetch d.dictionaries dic where ");
		finder.append(" d.dictionary.dictType = :dictType","dictType", name);
		finder.setCacheable(true);
		return this.find(finder);
	}

}
