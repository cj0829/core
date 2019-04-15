/**
 * Project Name:core
 * File Name:LogoutException.java
 * Package Name:org.csr.core.security.exception
 * Date:2014-2-21上午11:26:00
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.security.exception;


/**
 * ClassName:LogoutException.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014-2-21上午11:26:00 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class LogoutException  extends AbstractSafeException {
	
	private static final long serialVersionUID = 4504827275644142648L;

	public LogoutException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public LogoutException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public LogoutException(String newErrorNo, String errorMsg, boolean actionMode) {
		super(newErrorNo, errorMsg);
		this.actionMode = actionMode;
	}

}

