package org.csr.core.persistence.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.csr.core.Param;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.bean.OrganizationParameterResult;
import org.csr.core.persistence.business.dao.OrganizationParameterDao;
import org.csr.core.persistence.business.domain.OrganizationParameter;
import org.csr.core.persistence.business.domain.Parameters;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.util.ObjUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:OrganizationParameterServiceImpl.java <br/>
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
@Service("organizationParameterService")
@Lazy(false)
public class OrganizationParameterServiceImpl implements
		OrganizationParameterService {

	@Resource
	private OrganizationParameterDao organizationParameterDao;
	@Resource
	ParameterService parameterService;

	@PostConstruct
	protected void iniCache(){
	}
	
	public BaseDao<OrganizationParameter, Long> getDao() {
		return organizationParameterDao;
	}

	/**
	 * 查询机构下所有的参数，如果机构参数没有值 <br/>
	 * 则去系统参数中取值。这里返回的是一个分页对象.
	 * 
	 * @see org.csr.core.service.ParameterService#findListPageForRoot(org.csr.core.persistence.Page,
	 *      java.lang.Long, java.lang.Integer[])
	 */
	public PagedInfo<OrganizationParameterResult> findListPageForOrgId(
			Page page, Long root, Byte[] types) {
		PagedInfo<Parameters> parameterResult = parameterService.findPageByType(page, types);
		List<OrganizationParameter> organizationParameterList = organizationParameterDao.findByOrgId(root);
		List<Parameters> parameterList = parameterResult.getRows();
		// 返回的记录集
		List<OrganizationParameterResult> recordSet = new ArrayList<OrganizationParameterResult>();
		for (Parameters para : parameterList) {
			String paraName = para.getName();
			OrganizationParameterResult op = new OrganizationParameterResult(para.getId(), paraName, root, para.getParameterValue(),para.getParameterType(), para.getRemark());
			recordSet.add(op);
			for (int i = 0; i < organizationParameterList.size(); i++) {
				OrganizationParameter organizationParameter = organizationParameterList
						.get(i);
				if (paraName.equals(organizationParameter.getName())) {
					op.setParameterValue(organizationParameter.getValue());
				}
			}
		}
		return createPagedInfo(parameterResult.getTotal(), page, recordSet);
	}

	private PagedInfo<OrganizationParameterResult> createPagedInfo(long total,Page page, List<OrganizationParameterResult> recordSet) {
		return new PagedInfoImpl<OrganizationParameterResult>(recordSet, page, total);
	}

	/**
	 * @description:查询机构参数
	 * @param: list：
	 * @return: ResultInfo
	 * @author:caijin
	 */
	@Override
	public List<OrganizationParameterResult> findListForOrgId(Long root, Byte[] types) {
		List<Parameters> parameterList = parameterService.findListByType(types);
		List<OrganizationParameter> organizationParameterList = organizationParameterDao.findByOrgId(root);
		List<OrganizationParameterResult> recordSet = new ArrayList<OrganizationParameterResult>();
		for (Parameters para : parameterList) {
			String paraName = para.getName();
			OrganizationParameterResult op = new OrganizationParameterResult(
					para.getId(), paraName, root, para.getParameterValue(),
					para.getParameterType(), para.getRemark());
			recordSet.add(op);
			for (int i = 0; i < organizationParameterList.size(); i++) {
				OrganizationParameter organizationParameter = organizationParameterList
						.get(i);
				if (paraName.equals(organizationParameter.getName())) {
					op.setParameterValue(organizationParameter.getValue());
				}
			}
		}
		return recordSet;
	}

	/**
	 * @description:查询机构下的系统参数对象。机构id为RootId
	 * @param: root：机构Id
	 * @return: List
	 * @author:wangxiujuan
	 */
	public OrganizationParameter findByNameForOrgId(Long orgId, String name) {
		List<Param> and = new ArrayList<Param>();
		if (ObjUtil.isNotEmpty(orgId)) {
			and.add(new AndEqParam("orgId", orgId));
		}
		if (ObjUtil.isNotEmpty(name)) {
			and.add(new AndEqParam("name", name));
		}
		List<OrganizationParameter> oparams = organizationParameterDao
				.findByParam(and.toArray(new Param[and.size()]));
		if (ObjUtil.isNotEmpty(oparams)) {
			return oparams.get(0);
		}
		return null;
	}

	@Override
	public String findParameterValue(Long orgId, String name) {
		if (ObjUtil.isEmpty(orgId)) {
			return parameterService.findParameterValue(name);
		} else {
			OrganizationParameter param = findByNameForOrgId(orgId, name);
			if (ObjUtil.isNotEmpty(param)) {
				return param.getValue();
			} else {
				return parameterService.findParameterValue(name);
			}
		}
	}

	@Override
	public void deleteForOrgId(Long orgId) {
		organizationParameterDao.deleteForOrgId(orgId);
	}

	@Override
	public OrganizationParameterResult findByParameterForRoot(Long orgId,
			Long parameterId) {
		if (ObjUtil.isEmpty(orgId)) {
			Exceptions.service("DictTypeHasExist", "");
		}
		Parameters para = parameterService.findById(parameterId);
		String name = para.getName();
		OrganizationParameterResult op = new OrganizationParameterResult(
				para.getId(), name, orgId, para.getParameterValue(),
				para.getParameterType(), para.getRemark());
		OrganizationParameter orgPara = findByNameForOrgId(orgId, name);
		if (orgPara != null) {
			op.setParameterValue(orgPara.getValue());
		}
		return op;
	}

	@Override
	public void update(Long organizationId, Long parameterId, String value) {
		Parameters parameter = parameterService.findById(parameterId);
		OrganizationParameter orgPara = findByNameForOrgId(organizationId,
				parameter.getName());
		if (ObjUtil.isNotEmpty(orgPara)) {
			orgPara.setValue(value);
			organizationParameterDao.update(orgPara);
		} else {
			OrganizationParameter orgParameter = new OrganizationParameter();
			orgParameter.setName(parameter.getName());
			orgParameter.setValue(value);
			orgParameter.setOrgId(organizationId);
			organizationParameterDao.save(orgParameter);
		}
	}
}
