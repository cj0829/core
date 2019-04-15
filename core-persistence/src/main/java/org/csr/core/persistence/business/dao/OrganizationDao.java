package org.csr.core.persistence.business.dao;

import java.util.List;

import org.csr.core.Param;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.domain.Organization;

/**
 * ClassName:OrganizationDao.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014-2-28上午10:09:02 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public interface OrganizationDao extends BaseDao<Organization, Long> {

	public List<Organization> findListAll(Param... params);
	
	/**
	 * 根据条件查询全部的节点。并且查询出当前节点是否存在有下级节点
	 * @param params
	 * @return
	 */
	List<Organization> findHasChildListAll(Param... params);
	
	public PagedInfo<Organization> findOneLevelDataPage(Page page);

	public List<Long> findChildrenIds(Long parentId);


}
