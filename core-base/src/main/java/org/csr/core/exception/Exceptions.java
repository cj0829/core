package org.csr.core.exception;

public class Exceptions {
	/**
	 * dao: dao层抛出的异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @throws DaoException
	 * @since JDK 1.7
	 */
	public static void dao(final String newErrorNo, final String errorMsg) throws DaoException{
		throw new DaoException(newErrorNo,errorMsg);
	}
	
	/**
	 * dao: 带返回值的异常处理
	 * @author caijin
	 * @param newErrorNo
	 * @param errorMsg
	 * @param data 需要接收的返回值
	 * @throws DaoException
	 * @since JDK 1.7
	 */
	public static void dao(final String newErrorNo, final String errorMsg,final Object data) throws DaoException{
		throw new DaoException(newErrorNo,errorMsg,data);
	}
	
	/**
	 * service: 服务层抛出 异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @throws ServiceException
	 * @since JDK 1.7
	 */
	public static void service(final String newErrorNo, final String errorMsg) throws ServiceException{
		throw new ServiceException(newErrorNo,errorMsg);
	}
	public static void service(final int errorCode,final String newErrorNo, final String errorMsg) throws ServiceException{
		throw new ServiceException(errorCode,newErrorNo,errorMsg);
	}
	/**
	 * service: 服务层抛出 带返回值的 异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @param data 需要接收的返回值
	 * @throws ServiceException
	 * @since JDK 1.7
	 */
	public static void service(final String newErrorNo, final String errorMsg,final Object data) throws ServiceException{
		throw new ServiceException(newErrorNo,errorMsg,data);
	}
	/**
	 * service: 成功 这个不是异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @throws ServiceException
	 * @since JDK 1.7
	 */
	public static void success(final String newMessageNo, final String message) throws ServiceException{
		throw new SuccessMssageException(newMessageNo,message);
	}
	/**
	 * service:  成功 这个不是异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @param data 需要接收的返回值
	 * @throws ServiceException
	 * @since JDK 1.7
	 */
	public static void success(final String newMessageNo, final String message,final Object data) throws ServiceException{
		throw new SuccessMssageException(newMessageNo,message,data);
	}
}
