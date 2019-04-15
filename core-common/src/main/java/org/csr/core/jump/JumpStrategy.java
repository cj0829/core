package org.csr.core.jump;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.web.bean.ReturnMessage;


/**
 * 跳转策略，一种为为重定向或者转发<br>
 * 一种为，ajax
 * @author caijin
 *
 */
public interface JumpStrategy {
	
	/**
	 * 综合，returnAjaxHandle，两种处理方式
	 * @param targetUrl
	 * @param actionMode
	 * @param message
	 * @return
	 * @throws IOException
	 */
	JumpStrategy returnHandle(String targetUrl,boolean actionMode,ReturnMessage message) throws IOException;
	/**
	 * ajxa 发生消息
	 * @param message
	 * @return
	 * @throws IOException
	 */
	JumpStrategy returnAjaxHandle(ReturnMessage message) throws IOException;
	
	/**
	 * 跳转处理
	 * @param targetUrl
	 * @param message
	 * @return
	 * @throws IOException
	 */
	JumpStrategy preHandle(String targetUrl) throws IOException;
	
	JumpStrategy handle(HttpServletRequest request, HttpServletResponse response);
}
