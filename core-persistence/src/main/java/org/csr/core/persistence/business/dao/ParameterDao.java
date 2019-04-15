package org.csr.core.persistence.business.dao;

import java.util.List;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.business.domain.Parameters;

/**
 * ClassName:ParameterDao.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface ParameterDao extends BaseDao<Parameters,Long> {

    /**
     * @description:查询参数表信息
     * @param: page：分页信息
     * @return: ResultInfo
     * @author:wangxiujuan
     */
    public PagedInfo<Parameters> findPageByType(Page page,Byte[] type);
    /**
     * @description:查询参数表信息
     * @param: list：
     * @return: ResultInfo
     * @author:caijin
     */
    List<Parameters> findListByType(Byte[] type);
    
    /**
     * 根据name查询。
     * @param name
     * @return
     */
	public Parameters findByName(String name);

}
