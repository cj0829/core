package org.csr.core.persistence.business.dao.jpa;

import java.util.List;

import org.csr.core.persistence.Finder;
import org.csr.core.persistence.business.dao.OrganizationParameterDao;
import org.csr.core.persistence.business.domain.OrganizationParameter;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.jpa.JpaDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:OrganizationParameterDaoImpl.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-2-28上午10:10:23 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
@Service("organizationParameterDao")
@Lazy(false)
public class OrganizationParameterDaoImpl extends JpaDao<OrganizationParameter,Long> implements OrganizationParameterDao {
	
    @Override
	public Class<OrganizationParameter> entityClass() {
		return OrganizationParameter.class;
	}
    
    /**
     * @description:根据机构Id查询机构参数表信息
     * @param: root：机构Id
     * @return: List
     * @author:wangxiujuan 
     */
    public List<OrganizationParameter> findByOrgId(Long orgId) {
		Finder finder=FinderImpl.create("select op from OrganizationParameter op where op.orgId = :orgId");
		finder.setParam("orgId", orgId);
		finder.setCacheable(true);
		return find(finder);
    }

	@Override
	public void deleteForOrgId(Long orgId) {
		Finder finder=FinderImpl.create("delete from OrganizationParameter op where op.orgId = :orgId");
		finder.setParam("orgId", orgId);
		batchHandle(finder);
	}

}
