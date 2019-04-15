/**
 * Project Name:user
 * File Name:SetBean.java
 * Package Name:org.csr.core.service.impl
 * Date:2014年8月7日上午11:53:47
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.service;

import org.csr.core.Persistable;

/**
 * ClassName: SetBean.java <br/>
 * System Name：    elearning系统 <br/>
 * Date:     2014年8月7日上午11:53:47 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 功能描述：  <br/>
 */
public interface SetBean<T>{
	@SuppressWarnings("rawtypes")
	Persistable setValue(T doMain);
}

