/**
 * Project Name:core
 * File Name:AndLTParam.java
 * Package Name:org.csr.core.persistence.param
 * Date:Apr 9, 20157:09:33 PM
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.param;

import org.csr.core.persistence.query.Compare;


/**
 * ClassName:AndLTParam.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     Apr 9, 20157:09:33 PM <br/>
 * @author   yjY <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class OrLTParam extends OrParam {
	public OrLTParam(String key, Object value) {
		super(key, value);
		compare=Compare.LT;
	}
	
	public OrLTParam(String key, Object value,Class<?> property) {
		super(key,value,property);
		compare=Compare.LT;
	}

}

