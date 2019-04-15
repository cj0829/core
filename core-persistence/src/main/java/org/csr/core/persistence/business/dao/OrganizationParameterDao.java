package org.csr.core.persistence.business.dao;

import java.util.List;

import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.domain.OrganizationParameter;

/**
 * ClassName:OrganizationParameterDao.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-2-28上午10:08:55 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface OrganizationParameterDao extends BaseDao<OrganizationParameter,Long> {
	
    /**
     * @description:根据机构Id查询机构参数表信息
     * @param: root：机构Id
     * @return: List
     * @author:wangxiujuan
     */
    public List<OrganizationParameter> findByOrgId(Long orgId);
   
    /**
     * 
     * deleteForRoot: 描述方法的作用 <br/>
     * @author caijin
     * @param root
     * @since JDK 1.7
     */
	public void deleteForOrgId(Long orgId);
}
