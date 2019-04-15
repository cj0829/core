/**
 * Project Name:core
 * File Name:SetJsonObject.java
 * Package Name:org.csr.core.util
 * Date:2015-11-5下午6:08:43
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
*/

package org.csr.core.util.support;

import com.alibaba.fastjson.JSONObject;

/**
 * ClassName:SetJsonObject.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2015-11-5下午6:08:43 <br/>
 * @author   Administrator <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public  interface SetJsonObject<O> {

	O setBean(JSONObject value);

}

