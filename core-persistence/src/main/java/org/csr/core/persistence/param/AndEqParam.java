package org.csr.core.persistence.param;

import org.csr.core.persistence.query.Compare;


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
public class AndEqParam extends AndParam {

	public AndEqParam(String key, Object value) {
		super(key, value);
		compare=Compare.EQ;
	}
	
	public AndEqParam(String key, Object value,Class<?> property) {
		super(key,value,property);
		compare=Compare.EQ;
	}

}
