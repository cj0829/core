package org.csr.core.persistence.business.service;

import java.util.List;

import javax.annotation.Resource;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.dao.ParameterDao;
import org.csr.core.persistence.business.domain.Parameters;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.util.ObjUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:ParameterServiceImpl.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014-1-27 上午9:31:56 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
@Service("parameterService")
@Lazy(false)
public class ParameterServiceImpl implements ParameterService {
	@Resource
	private ParameterDao parameterDao;

	public BaseDao<Parameters, Long> getDao() {
		return parameterDao;
	}

	/**
	 * @description:保存参数，需要验证参数名称是否唯一
	 * @param: Parameter：参数对象
	 * @return: boolean
	 * @author:wangxiujuan
	 */
	public boolean save(Parameters parameter) {
		if (!checkAddParameterName(parameter.getName())) {
			parameterDao.save(parameter);
			return true;
		}
		return false;
	}

	/**
	 * findParameterValue: 通过系统参数名称，查询系统参数值<br/>
	 * 
	 * @author caijin
	 * @param name
	 * @return
	 * @since JDK 1.7
	 */
	public String findParameterValue(String name) {
		AndEqParam nameAnd = new AndEqParam("name", name);
		List<Parameters> params = parameterDao.findByParam(nameAnd);
		if (params != null && params.size() > 0) {
			return params.get(0).getParameterValue();
		}
		return "";
	}

	@Override
	public PagedInfo<Parameters> findPageByType(Page page, Byte[] type) {
		return parameterDao.findPageByType(page, type);
	}

	@Override
	public List<Parameters> findListByType(Byte[] type) {
		return parameterDao.findListByType(type);
	}

	/**
	 * @description:用于验证添加参数名是否唯一
	 * @param: Parameter:参数对象
	 * @return: boolean
	 * @author:wangxiujuan
	 */
	@Override
	public boolean checkAddParameterName(String name) {
		AndEqParam and = new AndEqParam("name", name);
		Parameters param = parameterDao.existParam(and);
		if (ObjUtil.isNotEmpty(param)) {
			return true;
		}
		return false;
	}

	/**
	 * @description:用于验证修改参数名是否唯一
	 * @param: Parameter:参数对象
	 * @return: boolean
	 * @author:wangxiujuan
	 */
	public boolean checkUpdateParameterName(Long id, String name) {
		AndEqParam and = new AndEqParam("name", name);
		Parameters param = parameterDao.existParam(and);
		if (ObjUtil.isEmpty(param) || param.getId().equals(id)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkNameIsExist(Long id, String name) {
		Parameters parameters = parameterDao.existParam(new AndEqParam("name",
				name));
		if (ObjUtil.isNotEmpty(id)) {
			if (ObjUtil.isEmpty(parameters) || parameters.getId().equals(id)) {
				return false;
			}
		}
		if (ObjUtil.isEmpty(parameters)) {
			return false;
		}
		return true;
	}

	@Override
	public Parameters findByName(String name) {
		if(ObjUtil.isBlank(name)){
			return null;
		}
		return parameterDao.findByName(name);
//		List<Parameters> list=parameterDao.findByParam(new AndLikeParam("name", name));//方法不好用。
	}

	@Override
	public Parameters findById(Long parameterId) {
		return parameterDao.findById(parameterId);
	}

	@Override
	public void update(Parameters parameters) {
		parameterDao.update(parameters);
	}

	@Override
	public void deleteSimple(Long[] ids) {
		parameterDao.deleteByIds(ids);
	}

	@Override
	public PagedInfo<Parameters> findAllPage(Page page) {
		return parameterDao.findAllPage(page);
	}

}
