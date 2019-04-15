package org.csr.core.persistence.business.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.Param;
import org.csr.core.constant.YesorNo;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.business.dao.OrganizationDao;
import org.csr.core.persistence.business.domain.Organization;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.hibernate.HibernateDao;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:OrganizationDaoImpl.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-2-28上午10:10:28 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class OrganizationDaoImpl extends HibernateDao<Organization,Long>  implements OrganizationDao{

	@Override
	public Class<Organization> entityClass() {
		return Organization.class;
	}
	
	@Override
	public PagedInfo<Organization> findAllPage(Page page) {
		Finder finder=FinderImpl.create("select d from Organization d  where d.isDelete=false");
		finder.setCacheable(true);
		return findPage(page,finder);
	}
	
	/**
	 * 根据rootid查询全部的机构<br>
	 * 如果root为null，则查询全部机构
	 */
	@Override
	public List<Organization> findListAll(Param... params){
		Finder finder=FinderImpl.create("select d from Organization d");
		finder.append(" where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.setCacheable(true);
		if(ObjUtil.isNotEmpty(params)){
			finder.putAutoParam("org", ObjUtil.asList(params));
		}
		return find(finder);
	}
	
	/**
	 * 根据rootid查询全部的机构<br>
	 * 如果root为null，则查询全部机构
	 */
	@Override
	public List<Organization> findHasChildListAll(Param... params){
		Finder finder=FinderImpl.create("select d,(select count(ds.id) from Organization ds where ds.isDelete=false and ds.parentId=d.id and ds.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append(" from Organization d where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.setCacheable(true);
		if(ObjUtil.isNotEmpty(params)){
			finder.putAutoParam("org", ObjUtil.asList(params));
		}
		List<Object[]> list = find(finder);
		return arrayToOrganization(list);
	}
	
	
	
	
	@Override
	public List<Organization> findByIds(List<Long> ids) {
		if(ObjUtil.isEmpty(ids)){
			return new ArrayList<Organization>(0);
		}
		Finder finder=FinderImpl.create("select d from Organization d ");
		finder.append(" where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append("and d.id in (:ids)","ids",ids);
		finder.setCacheable(true);
		return find(finder);
	}
	
	public Organization findById(Long id){
		Organization org = super.findById(id);
		if(org==null || org.getIsDelete()){
			return null;
		}else{
			return org;
		}
	}
	
	/**
	 * 查询数据字典第一级数据
	 */
	@Override
	public PagedInfo<Organization> findOneLevelDataPage(Page page) {
		Finder finder=FinderImpl.create("select d,(select count(ds.id) from Organization ds where ds.isDelete=false and ds.parentId=d.id and ds.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append(" from Organization d where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append(" and d.parentId =:root","root",Organization.global);
		finder.setDetectAlias("d");
		finder.setCacheable(true);
		List<Object[]> list = findByPage(page,finder);
		return createPagedInfo(finder, page, arrayToOrganization(list));
	}
	
	
	@Override
	public List<Long> findChildrenIds(Long parentId) {
		Finder finder=FinderImpl.create("select d.id from Organization d ");
		finder.append(" where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append(" and d.parentId =:parentId","parentId",parentId);
		finder.setParam("parentId", parentId);
		finder.setCacheable(true);
		List<Long> list = find(finder);
		return list;
	}
	
	/**
	 * 用来递归查询，全部的机构
	 * @author caijin
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	private List<Organization> hasChildToOrgIds(List<Object[]> list){
		List<Organization> organizationList = new ArrayList<Organization>();
		for(int i=0;ObjUtil.isNotEmpty(list) && i<list.size();i++){
			Object[] obj = list.get(i);
			if(obj[1] != null && (Long)obj[1] >= 0){
				Organization orga = (Organization) obj[0];
				Long count=(Long)obj[1];
				if(count>0){
					organizationList.addAll(findAllChildrenList(orga.getId()));
				}
				organizationList.add(orga);
			}
		}
		return organizationList;
	}
	
	/**
	 * 用来全部的子项
	 * @author caijin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	private List<Organization> findAllChildrenList(Long id) {
		Finder finder=FinderImpl.create("select d,(select count(ds.id) from Organization ds where ds.isDelete=false and ds.parentId=d.id and ds.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append(" from Organization d where (d.isDelete=false and d.organizationStatus=").append(YesorNo.YES).append(")");
		finder.append("and d.parentId= :parentId","parentId", id);
		finder.setDetectAlias("d");
		finder.setCacheable(true);
		List<Object[]> list = find(finder);
		return hasChildToOrgIds(list);
	}
	
	private List<Organization> arrayToOrganization(List<Object[]> list){
		List<Organization> organizationList = new ArrayList<Organization>();
		for(int i=0;ObjUtil.isNotEmpty(list) && i<list.size();i++){
			Object[] obj = list.get(i);
			Organization orga = (Organization) obj[0];
			if(obj[1] != null && (Long)obj[1] > 0)
				orga.setChildCount(((Long)obj[1]).intValue());
			else
				orga.setChildCount(0);
			organizationList.add(orga);
		}
		return organizationList;
	}

	
}
