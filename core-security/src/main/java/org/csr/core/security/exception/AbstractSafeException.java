/**
 * Project Name:core
 * File Name:SafeException.java
 * Package Name:org.csr.core.security.exception
 * Date:2014年4月15日下午11:21:29
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.security.exception;

import org.csr.core.exception.AbstractException;

/**
 * ClassName:SafeException.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年4月15日下午11:21:29 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public abstract class AbstractSafeException extends AbstractException {
	
	private static final long serialVersionUID = 5545508414680275995L;
	protected boolean actionMode;
	
	public AbstractSafeException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public AbstractSafeException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}
	public AbstractSafeException(String newErrorNo, String errorMsg, boolean actionMode) {
		super(newErrorNo, errorMsg);
		this.actionMode = actionMode;
	}
	
	public boolean getActionMode(){
		return actionMode;
	}
}

