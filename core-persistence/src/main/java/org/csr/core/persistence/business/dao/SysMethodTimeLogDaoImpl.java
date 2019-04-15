package org.csr.core.persistence.business.dao;

import org.csr.core.persistence.business.domain.SysMethodTimeLog;
import org.csr.core.persistence.query.jpa.JpaDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * ClassName:DictionaryDaoImpl.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-24下午4:07:40 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
@Service("sysMethodTimeLogDao")
@Lazy(false)
public class SysMethodTimeLogDaoImpl extends JpaDao<SysMethodTimeLog, Long>  implements SysMethodTimeLogDao {
	
	@Override
	public Class<SysMethodTimeLog> entityClass() {
		return SysMethodTimeLog.class;
	}
}
