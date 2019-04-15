/**
 * Project Name:core
 * File Name:SucceedException.java
 * Package Name:org.csr.core.exception
 * Date:2014年6月12日下午4:57:32
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.exception;
/**
 * ClassName:SucceedException.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年6月12日下午4:57:32 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class SuccessMssageException extends AbstractException {
	/**
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 7519123816525777698L;

	public SuccessMssageException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public SuccessMssageException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}
	public SuccessMssageException(String newErrorNo, String errorMsg,Object data) {
		super(newErrorNo, errorMsg,data);
	}
}

