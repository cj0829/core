package org.csr.core.security.exception;



/**
 * ClassName:SecurityException.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午6:07:02 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class SecurityException extends AbstractSafeException {

	private static final long serialVersionUID = 1519826433419668798L;

	public SecurityException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public SecurityException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public SecurityException(String newErrorNo, String errorMsg, boolean actionMode) {
		super(newErrorNo, errorMsg);
		this.actionMode = actionMode;
	}

}
