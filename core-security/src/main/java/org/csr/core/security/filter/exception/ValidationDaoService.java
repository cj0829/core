package org.csr.core.security.filter.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.exception.AbstractValidationException;
import org.csr.core.exception.DaoException;
import org.csr.core.exception.ExceptionChain;
import org.csr.core.exception.ServiceException;
import org.csr.core.exception.SuccessMssageException;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;

public class ValidationDaoService extends AbstractValidationException {

	public ValidationDaoService(JumpStrategy returnHandler) {
		super(returnHandler);
	}

	@Override
	public void validation(HttpServletRequest request,HttpServletResponse response, Exception exception,
			ExceptionChain chain) throws IOException {

		if (exception instanceof DaoException) {
			boolean actionMode = UrlUtil.requiresAjax(request);
			DaoException dp = (DaoException) exception;
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,dp.getErrorNo(),dp.getErrorMsg(), dp.getData());
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", dp.getErrorMsg());
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,dp.getErrorMsg());
			}
		} else if (exception instanceof ServiceException) {
			boolean actionMode = UrlUtil.requiresAjax(request);
			ServiceException sp = (ServiceException) exception;
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,sp.getErrorNo(),sp.getErrorMsg(), sp.getData());
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", sp.getErrorMsg());
				if (-999 == sp.getErrorCode()) {httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,sp.getErrorMsg());
				} else {
					httpResponse.sendError(sp.getErrorCode(), sp.getErrorMsg());
				}
			}
		}else if (exception instanceof SuccessMssageException) {
			boolean actionMode = UrlUtil.requiresAjax(request);
			SuccessMssageException sp = (SuccessMssageException) exception;
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.SUCCESS,sp.getErrorNo(),sp.getErrorMsg(), sp.getData());
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", sp.getErrorMsg());
				if (-999 == sp.getErrorCode()) {httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,sp.getErrorMsg());
				} else {
					httpResponse.sendError(sp.getErrorCode(), sp.getErrorMsg());
				}
			}
		} else {
			chain.validationException(request, response, exception);
		}
	}
}
