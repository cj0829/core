package org.csr.core.persistence.business.service;

import java.util.List;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.business.domain.Parameters;

/**
 * ClassName:ParameterService.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface ParameterService  {

    /**
     * @description:保存参数，需要验证参数名称是否唯一
     * @param: Parameter：参数对象
     * @return: boolean
     * @author:wangxiujuan
     */
    public boolean save(Parameters parameter);

    /**
     * findParameterValue: 通过系统参数名称，查询系统参数值<br/>
     * @author caijin
     * @param name
     * @return
     * @since JDK 1.7
     */
    public String findParameterValue(String name);
    
    /**
     * findPageByType: 描述方法的作用 <br/>
     * @author caijin
     * @param page
     * @param type
     * @return
     * @since JDK 1.7
     */
    public PagedInfo<Parameters> findPageByType(Page page,Byte[] type);
    
    /**
     * findListByType: 描述方法的作用 <br/>
     * @author caijin
     * @param type
     * @return
     * @since JDK 1.7
     */
    List<Parameters> findListByType(Byte[] type);
    
    /**
     * @description:用于验证添加参数时参数名唯一
     * @param: Parameter：参数对象
     * @return: boolean
     * @author:wangxiujuan
     */
    public boolean checkAddParameterName(String name);

    /**
     * @description:用于验证修改参数时参数名唯一
     * @param: Parameter：参数对象
     * @return: boolean
     * @author:wangxiujuan
     */
    public boolean checkUpdateParameterName(Long id,String name);

    /**
     * 检查名字是否存在。
     * @param id
     * @param name
     * @return
     */
	public boolean checkNameIsExist(Long id, String name);
	
	//-->跟license有关
	/**
	 * 查询序列
	 * @param name
	 * @return
	 */
	public Parameters findByName(String name);
	//<--

	public Parameters findById(Long parameterId);

	public void update(Parameters parameters);

	public void deleteSimple(Long[] ids);

	public PagedInfo<Parameters> findAllPage(Page page);

}
