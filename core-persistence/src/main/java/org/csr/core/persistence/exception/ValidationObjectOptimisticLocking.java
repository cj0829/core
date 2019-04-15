package org.csr.core.persistence.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.exception.AbstractValidationException;
import org.csr.core.exception.ExceptionChain;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;

public class ValidationObjectOptimisticLocking extends AbstractValidationException {

	public ValidationObjectOptimisticLocking(JumpStrategy returnHandler) {
		super(returnHandler);
	}

	@Override
	public void validation(HttpServletRequest request,
			HttpServletResponse response, Exception exception,ExceptionChain chain) throws IOException {
		boolean actionMode = UrlUtil.requiresAjax(request);
		 if (exception instanceof javax.persistence.OptimisticLockException) {
			actionMode = UrlUtil.requiresAjax(request);
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,"您操作的对象，正有用户在操作，请您等待别人操作结束", null);
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage","您操作的对象，正有用户在操作，请您等待别人操作结束");
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"您操作的对象，正有用户在操作，请您等待别人操作结束");
			}
		}else if ("org.springframework.orm.ObjectOptimisticLockingFailureException".equals(exception.getClass().getName())
				&& "org.hibernate.StaleObjectStateException".equals(exception.getCause().getClass().getName())) {
			if (actionMode) {
				ReturnMessage message = new ReturnMessage(ReturnMessage.FAIL,
						"您操作的对象，正有用户在操作，请您等待别人操作结束", null);
				returnHandler.handle(request, response).returnAjaxHandle(message);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage","您操作的对象，正有用户在操作，请您等待别人操作结束");
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"您要操作对象，正有用户在操作，请您等待别人操作结束");
			}
		}else{
			chain.validationException(request, response, exception);
		}
	}

}
