package org.csr.core.persistence.business.service;

import java.util.List;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.business.bean.OrganizationParameterResult;
import org.csr.core.persistence.business.domain.OrganizationParameter;

/**
 * ClassName:BasisServiceImpl <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface OrganizationParameterService  {

	/**
	 * update: 描述方法的作用 <br/>
	 * @author caijin
	 * @param organizationId
	 * @param parameterId
	 * @param value
	 * @since JDK 1.7
	 */
	public void update(Long organizationId, Long parameterId, String value);
    /**
     * 
     * deleteForRoot: 描述方法的作用 <br/>
     * @author caijin
     * @param root
     * @since JDK 1.7
     */
    public void deleteForOrgId(Long orgId);

    /**
     * 根据参数name。获取参数value
     * @param organizationId
     * @param id
     * @return
     */
    public String findParameterValue(Long orgId,String name);
    
    /**
     * findByRootAndName: 查询机构下的系统参数对象。机构id为RootId <br/>
     * @author caijin
     * @param root
     * @param name
     * @return
     * @since JDK 1.7
     */
    public OrganizationParameter findByNameForOrgId(Long root,String name);
   
	/**
     * findListPageForRoot: 查询机构下所有的参数，如果机构参数没有值 <br/>
     * 则去系统参数中取值。这里返回的是一个分页对象。
     * @author caijin
     * @param page
     * @param rootId
     * @param types
     * @return
     * @since JDK 1.7
     */
    public PagedInfo<OrganizationParameterResult> findListPageForOrgId(Page page,Long orgId,Byte[] types);
    
    /**
     * findListForRoot: 查询机构下所有的参数，如果机构参数没有值 <br/>
     * 则去系统参数中取值。这里给是一个list集合。
     * @author caijin
     * @param rootId
     * @return
     * @since JDK 1.7
     */
    List<OrganizationParameterResult> findListForOrgId(Long orgId,Byte[] types);
    
    
    /**
     * findByRootAndName: 根据参数id 查询当前机构下是否，存在值，如果不存在值，那么返回参数值<br/>
     * @author caijin
     * @param root
     * @param name
     * @return
     * @since JDK 1.7
     */
    public OrganizationParameterResult findByParameterForRoot(Long root,Long parameterId);
	

}
