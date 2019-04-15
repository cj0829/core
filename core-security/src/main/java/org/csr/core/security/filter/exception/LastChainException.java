package org.csr.core.security.filter.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.exception.AbstractValidationException;
import org.csr.core.exception.ExceptionChain;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;

public class LastChainException extends AbstractValidationException {

	public LastChainException(JumpStrategy returnHandler) {
		super(returnHandler);
	}

	@Override
	public void validation(HttpServletRequest request,HttpServletResponse response, Exception exception,ExceptionChain chain) throws IOException {
		boolean actionMode = UrlUtil.requiresAjax(request);
		actionMode = UrlUtil.requiresAjax(request);
		if (exception instanceof NullPointerException) {
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,"10001","对象空指针", null);
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage","对象空指针");
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"对象空指针");
			}
		}else{
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,"10000",exception.getMessage(), null);
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,exception.getMessage());
			}
		}
	}
}
