package org.csr.core.exception;

/**
 * ClassName:DaoException.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-25下午4:20:16 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class DaoException extends AbstractException {

	private static final long serialVersionUID = 4381204538178312013L;

	public DaoException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public DaoException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public DaoException(String newErrorNo, String errorMsg, Object data) {
		super(newErrorNo, errorMsg,data);
	}
}
