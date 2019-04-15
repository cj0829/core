package org.csr.core.persistence.business.dao.jpa;

import java.util.List;

import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.business.dao.ParameterDao;
import org.csr.core.persistence.business.domain.Parameters;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.jpa.JpaDao;
import org.csr.core.util.ObjUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:ParameterDaoImpl.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-2-28上午10:10:17 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
@Service("parameterDao")
@Lazy(false)
public class ParameterDaoImpl extends JpaDao<Parameters,Long> implements ParameterDao {
	@Override
	public Class<Parameters> entityClass() {
		return Parameters.class;
	}

    public PagedInfo<Parameters> findPageByType(Page page,Byte[] type){
    	Finder finder=FinderImpl.create("select p from Parameters p where 1=1");
    	boolean b=true;
    	for(int i=0;type!=null && i<type.length;i++){
    		if(b){
    			b=false;
    			finder.append(" and(");
    		}else{
    			finder.append(" or ");
    		}
    		finder.append("  p.parameterType=").append(type[i]);
    		finder.append(")");
    	}
    	finder.setCacheable(true);
    	return findPage(page,finder);
    }
    /**
     * @description:查询参数表信息
     * @param: page：分页信息
     * @return: ResultInfo
     * @author:wangxiujuan
     */
    @Override
    public List<Parameters> findListByType(Byte[] type) {
    	Finder finder=FinderImpl.create("select p from Parameters p where 1=1");
    	boolean b=true;
    	for(int i=0;type!=null && i<type.length;i++){
    		if(b){
    			b=false;
    			finder.append(" and(");
    		}else{
    			finder.append(" or ");
    		}
    		finder.append("  p.parameterType=").append(type[i]);
    		finder.append(")");
    	}
    	finder.setCacheable(true);
    	return find(finder);
    }

	@Override
	public Parameters findByName(String name) {
		Finder finder=FinderImpl.create("select p from Parameters p where p.name like :name");
		finder.setParam("name","%"+name+"%");
		List<Parameters> list=find(finder);
		if(ObjUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
    
}
