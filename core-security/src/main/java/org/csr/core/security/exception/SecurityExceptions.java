package org.csr.core.security.exception;

import javax.servlet.http.HttpServletRequest;

import org.csr.core.exception.DaoException;
import org.csr.core.exception.ServiceException;
import org.csr.core.exception.SuccessMssageException;
import org.csr.core.util.UrlUtil;


public class SecurityExceptions {
	/**
	 * 认证失败-跳转到登录页面
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void authentication(final String newErrorNo, final String errorMsg, boolean actionMode) throws AuthenticationException{
		throw new AuthenticationException(newErrorNo,errorMsg,actionMode);
	}
	
	/**
	 * session失败，
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void session(final String newErrorNo, final String errorMsg,HttpServletRequest request) throws SessionException{
		boolean actionMode=UrlUtil.requiresAjax(request);
		throw new SessionException(newErrorNo,errorMsg,actionMode);
	}
	
	/**
	 * 跳转到登录页面
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void login(final String newErrorNo, final String errorMsg,HttpServletRequest request) throws SessionException{
		boolean actionMode=UrlUtil.requiresAjax(request);
		throw new LoginException(newErrorNo,errorMsg,actionMode);
	}
	
	/**
	 * 跳转到登录页面
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void logout(final String newErrorNo, final String errorMsg,HttpServletRequest request) throws SessionException{
		boolean actionMode=UrlUtil.requiresAjax(request);
		throw new LogoutException(newErrorNo,errorMsg,actionMode);
	}
	/**
	 * 权限验证
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void security(final String newErrorNo, final String errorMsg,HttpServletRequest request) throws SessionException{
		boolean actionMode=UrlUtil.requiresAjax(request);
		throw new SecurityException(newErrorNo,errorMsg,actionMode);
	}
	
	/**
	 * 服务错误
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void service(final String newErrorNo, final String errorMsg) throws SessionException{
		throw new ServiceException(newErrorNo,errorMsg);
	}
	
	
	/**
	 * 消息提示
	 * @param newErrorNo
	 * @param errorMsg
	 * @param actionMode 
	 */
	public static void message(final String newErrorNo, final String errorMsg) throws SessionException{
		throw new SuccessMssageException(newErrorNo,errorMsg);
	}
	/**
	 * dao: dao层抛出的异常 <br/>
	 * @author caijin
	 * @param newErrorNo 异常编码
	 * @param errorMsg   异常提示
	 * @param object 
	 * @throws DaoException
	 * @since JDK 1.7
	 */
	public static void dao(final String newErrorNo, final String errorMsg,final Object data) throws DaoException{
		throw new DaoException(newErrorNo,errorMsg,data);
	}

	/**
	 * service: service层抛出的异常 <br/>
	 * @author yjY
	 * @param errorNo
	 * @param errorMsg
	 * @param data 返回值
	 * @since JDK 1.7
	 */
	public static void service(String errorNo, String errorMsg,final Object data) {
		throw new ServiceException(errorNo,errorMsg,data);
		
	}

	/**
	 * 成功 返回消息
	 * @author yjY
	 * @param errorNo
	 * @param message
	 * @param data
	 * @since JDK 1.7
	 */
	public static void message(String errorNo, String message,final Object data) {
		throw new DaoException(errorNo,message,data);
		
	}
	
}
