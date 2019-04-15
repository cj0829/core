package org.csr.core.security.exception;


/**
 * @author caijin
 * @version CoreCode 1.0
 * @since JDK-1.7.0
 * @date 2013-10-16����9:07:19
 */
public class AuthenticationException extends AbstractSafeException {

	private static final long serialVersionUID = 1519826433419668798L;

	public AuthenticationException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public AuthenticationException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public AuthenticationException(String newErrorNo, String errorMsg, boolean actionMode) {
		super(newErrorNo, errorMsg);
		this.actionMode = actionMode;
	}

}
