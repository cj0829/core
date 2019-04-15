package org.csr.core.exception;

/**
 * ClassName:ServiceException.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-25下午4:21:48 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述： 服务异常 <br/>
 * 公用方法描述：  <br/>
 */
public class ServiceException extends AbstractException {

	private static final long serialVersionUID = -5320019281220490082L;

	public ServiceException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public ServiceException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public ServiceException(String newErrorNo, String errorMsg,Object data) {
		super(newErrorNo, errorMsg,data);
	}

}
