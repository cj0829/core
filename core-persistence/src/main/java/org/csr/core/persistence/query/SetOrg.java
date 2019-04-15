/**
 * Project Name:core
 * File Name:SetOrg.java
 * Package Name:org.csr.core.persistence.query
 * Date:2014年8月7日上午11:15:15
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.query;

import java.io.Serializable;

import org.csr.core.persistence.business.domain.Organization;
import org.csr.core.persistence.domain.RootDomain;

/**
 * ClassName:SetOrg.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年8月7日上午11:15:15 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface SetOrg<O extends RootDomain<ID>,ID extends Serializable> {

	Long getOrgId(O domain);
	void  setValue(O domain, Organization org);

}

