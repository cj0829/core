package org.csr.core.page;

import java.io.Serializable;

import org.csr.core.Param;

/**
 * ClassName:PropertyFilter.java <br/>
 * System Name：  <br/>
 * Date: 2014年3月17日上午11:45:54 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public interface QueryProperty extends Serializable{

	public Param toParam();
	
}
