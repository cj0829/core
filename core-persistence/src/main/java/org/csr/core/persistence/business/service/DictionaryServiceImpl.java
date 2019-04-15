package org.csr.core.persistence.business.service;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.csr.core.Constants;
import org.csr.core.constant.YesorNo;
import org.csr.core.exception.Exceptions;
import org.csr.core.persistence.business.dao.DictionaryDao;
import org.csr.core.persistence.business.domain.Dictionary;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.persistence.param.AndLikeParam;
import org.csr.core.util.ObjUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:DictionaryServiceImpl.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-28上午10:07:03 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
@Service("dictionaryService")
@Lazy(false)
public class DictionaryServiceImpl implements DictionaryService {

	@Resource
	private DictionaryDao dictionaryDao;

	@PostConstruct
	protected void iniCache() {
		//缓存
		List<Dictionary> list = dictionaryDao.findOneLevel();
		if (ObjUtil.isNotEmpty(list)) {
			for (Dictionary dic : list) {
				findList(dic.getDictType());
			}
		}
		findAllCacheList();
	}

	// 所有结点的孩子结点
	private final static Hashtable<String, List<Dictionary>> dictionary = new Hashtable<String, List<Dictionary>>();
	private final static Hashtable<String, Map<String, String>> dictionaryCache = new Hashtable<String, Map<String, String>>();

	/**
	 * @description:根据数据字典名获取数据字典列表
	 * @param: name：数据字典名
	 * @return: List<Dictionary>
	 */
	public List<Dictionary> findList(String name) {
		List<Dictionary> list = dictionary.get(name);
		if (list != null && list.size() > 0) {
			return dictionary.get(name);
		} else {
			list = dictionaryDao.findDictListByName(name);
			dictionary.put(name, list);
		}
		return list;
	}

	@Override
	public Hashtable<String, Map<String, String>> findAllCacheList() {
		if(ObjUtil.isEmpty(dictionaryCache)){
			List<Dictionary> list = dictionaryDao.findOneLevel();
			if (ObjUtil.isNotEmpty(list)) {
				for (Dictionary dic : list) {
					Map<String, String> dictMap = new LinkedHashMap<String, String>();
					List<Dictionary> childs = dic.getDictionaries();
					if (ObjUtil.isNotEmpty(childs)) {
						for (Dictionary dict : childs) {
							dictMap.put(dict.getDictValue(), dict.getDictType());
						}
					}
					dictionaryCache.put(dic.getDictType(), dictMap);
				}
			}
		}
		return dictionaryCache;
	}

	/*
	 * @description:通过数据类型/数据类型和子类数据值得到相对应子类数据信息 (non-Javadoc)
	 * 
	 * @see
	 * com.bluedot.csp.admin.system.service.DictionaryService#findDictMap(java
	 * .lang.String, java.lang.Integer[])
	 */
	public Map<String, String> findDictMap(String name) {
		Map<String, String> dictMap = new LinkedHashMap<String, String>();
		List<Dictionary> list = findList(name);
		if (list != null && list.size() > 0) {
			for (Dictionary dict : list) {
				dictMap.put(dict.getDictValue(), dict.getDictType());
			}
		}
		return dictMap;
	}

	/*
	 * @description:查询所有数据字典大类 (non-Javadoc)
	 * 
	 * @see
	 * com.bluedot.csp.admin.system.service.DictionaryService#findList(com.bluedot
	 * .framework.common.Page)
	 */
	public List<Dictionary> findAllList() {
		List<Dictionary> list = dictionaryDao.findAll();
		return list;
	}

	/*
	 * @description:添加/更新数据字典信息 先判断是更新还是添加 然后判断数据名称是否已经存在，若存在则返回提示信息
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bluedot.csp.admin.system.service.DictionaryService#save(com.bluedot
	 * .csp.domain.admin.system.Dictionary)
	 */
	public void save(Dictionary dict, Long parentId) {
		if (dict != null && dict.getId() != null && dict.getId() > 0) {
			Dictionary checkObjDict = checkdictTypeOnlyInChild(parentId, dict
					.getDictType().trim());
			if (checkObjDict != null
					&& !checkObjDict.getId().equals(dict.getId())) {
				Exceptions.service("DictTypeHasExist", "");
			}
			Dictionary checkValue = checkdictValueOnlyInChild(parentId,
					dict.getDictValue());
			if (checkValue != null && !checkValue.getId().equals(dict.getId())) {
				Exceptions.service("DictTypeHasExist", "");
			}
			;

			if (parentId != null) {
				Dictionary parent = findById(parentId);
				if (parent != null) {
					dict.setDictionary(parent);
				}
			}
			dictionaryDao.update(dict);
		} else {
			Dictionary checkObjDict = checkdictTypeOnlyInChild(parentId, dict
					.getDictType().trim());
			if (checkObjDict == null) {
				if (checkdictValueOnlyInChild(parentId, dict.getDictValue()) != null) {
					Exceptions.service("DictTypeHasExist", "");
				}
				;
				if (parentId != null) {
					Dictionary parent = findById(parentId);
					if (parent != null) {
						dict.setDictionary(parent);
						parent.setHasChild(YesorNo.YES);
						dictionaryDao.update(parent);
					}
				}
				dict.setHasChild(YesorNo.NO);
				dict.setId(dictionaryDao.nextSequence());
				dict.setRank(dict.getId());
				dictionaryDao.save(dict);
			} else {
				Exceptions.service("DictTypeHasExist", "");
			}
		}
	}

	/**
	 * @description:进入添加子菜单页面
	 * @param:
	 * @return: String
	 */
	@Override
	public String findDictTypeById(Long id) {
		AndEqParam and = new AndEqParam("id", id);
		return dictionaryDao.findFieldByParam("dictType", and);
	}

	/*
	 * @description:通过id查找数据字典信息 (non-Javadoc)
	 * 
	 * @see
	 * com.bluedot.csp.admin.system.service.DictionaryService#find(java.lang
	 * .Long)
	 */
	public Dictionary findById(Long id) {
		return dictionaryDao.findById(id);
	}

	/*
	 * @description:删除一个或多个对象 先删除子类对象 (non-Javadoc)
	 * 
	 * @see
	 * com.bluedot.csp.admin.system.service.DictionaryService#delete(java.lang
	 * .String)
	 */
	public void delete(String deleteIds) {
		deleteChildren(deleteIds);
		String[] ids = deleteIds.split(Constants.OPERATE_COLON);
		for (String id : ids) {
			if (id != null && !"".equals(id)) {
				dictionaryDao.deleteById(Long.valueOf(id));
			}
		}
	}

	/**
	 * @description:判断数据字典类型是否唯一
	 * @param:
	 * @return: Boolean
	 */
	@Override
	public Dictionary checkdictTypeOnly(String name) {
		AndEqParam and = new AndEqParam("dictType", name);
		return dictionaryDao.existParam(and);
	}

	/**
	 * @description:判断子节点类型是否唯一
	 * @param:
	 * @return: Boolean
	 */
	@Override
	public Dictionary checkdictTypeOnlyInChild(Long parentId, String name) {
		return dictionaryDao.checkdictTypeOnly(parentId, name);
	}

	/**
	 * @description:判断子节点值是否唯一
	 * @param:
	 * @return: Boolean
	 */
	@Override
	public Dictionary checkdictValueOnlyInChild(Long parentId, String value) {
		return dictionaryDao.checkdictValeuOnly(parentId, value);
	}

	/**
	 * @description:递归删除一个或多个对象的子类
	 * @param:
	 * @return: void
	 */
	private void deleteChildren(String deleteIds) {
		String[] ids = deleteIds.split(Constants.OPERATE_COLON);
		for (String id : ids) {
			if (id != null && !"".equals(id)) {
				String dictIds = this.findChildrenIds(Long.valueOf(id));
				if (dictIds != null && !"".equals(dictIds)) {
					deleteChildren(dictIds);
				}
				dictionaryDao.deleteChildren(Long.valueOf(id));
			}
		}
	}

	/**
	 * @description:查找子类的id并且拼成字符串
	 * @param:
	 * @return: String
	 */
	private String findChildrenIds(Long id) {
		List<Long> list = dictionaryDao.findChildrenIds(id);
		String ids = "";
		for (Long dictId : list) {
			ids += dictId + Constants.OPERATE_COLON;
		}
		if (ids != null && !"".equals(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		}
		return ids;
	}


	@Override
	public boolean checkNameIsExist(Long id, String name) {
		Dictionary dictionary = dictionaryDao.existParam(new AndEqParam(
				"dictType", name));
		if (ObjUtil.isNotEmpty(id)) {
			if (ObjUtil.isEmpty(dictionary) || dictionary.getId().equals(id)) {
				return false;
			}
		}
		if (ObjUtil.isEmpty(dictionary)) {
			return false;
		}
		return true;
	}

	@Override
	public List<Dictionary> findByDictType(String dictType) {
		return dictionaryDao
				.findByParam(new AndLikeParam("dictType", dictType));
	}

}
