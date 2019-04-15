package org.csr.core.exception;
import org.csr.core.util.ObjUtil;
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
public abstract class AbstractException extends RuntimeException {

	private static final long serialVersionUID = 1519826433419668798L;
	/**
	 */
	protected int errorCode=-999;
	protected String errorNo = "0";
	protected String errorMsg = "";
	protected Object data = null;
	
	public AbstractException(final String newErrorNo, final String errorMsg) {
		super(ObjUtil.isNotBlank(newErrorNo)?"[" + newErrorNo + "] " + errorMsg:errorMsg);
		this.errorNo = newErrorNo;
		this.errorMsg= errorMsg;
	}
	public AbstractException(final int errorCode,final String newErrorNo, final String errorMsg) {
		super(ObjUtil.isNotBlank(newErrorNo)?"[" + newErrorNo + "] " + errorMsg:errorMsg);
		this.errorNo = newErrorNo;
		this.errorMsg= errorMsg;
		this.errorCode= errorCode;
	}
	public AbstractException(final String newErrorNo, final String errorMsg, final Object data) {
		super(ObjUtil.isNotBlank(newErrorNo)?"[" + newErrorNo + "] " + errorMsg:errorMsg);
		this.errorNo = newErrorNo;
		this.errorMsg= errorMsg;
		this.data=data;
	}

	public AbstractException(final String newErrorNo, final String errorMsg,
			Throwable cause) {
		super(ObjUtil.isNotBlank(newErrorNo)?"[" + newErrorNo + "] " + errorMsg:errorMsg);
		this.errorNo = newErrorNo;
		this.errorMsg= errorMsg;
	}


	public String getErrorNo() {
		return errorNo;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
