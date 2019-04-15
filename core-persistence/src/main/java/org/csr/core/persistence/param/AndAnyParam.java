package org.csr.core.persistence.param;

import org.csr.core.Param;

/**
 * ClassName:BasisServiceImpl <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014-1-27 上午9:31:56 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class AndAnyParam extends BaseParam implements Param {

	public AndAnyParam(String key, Object value, String compare) {
		this.key = key;
		this.value = value;
		this.compare = compare;
	}

	public AndAnyParam(String key, Object value, String compare,
			Class<?> property) {
		this(key, value, compare);
		this.property = property;
	}

}
