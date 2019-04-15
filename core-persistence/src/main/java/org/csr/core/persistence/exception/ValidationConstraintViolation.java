package org.csr.core.persistence.exception;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.csr.core.exception.AbstractValidationException;
import org.csr.core.exception.ExceptionChain;
import org.csr.core.jump.JumpStrategy;
import org.csr.core.persistence.validation.ValidationUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;

public class ValidationConstraintViolation extends AbstractValidationException{

	public ValidationConstraintViolation(JumpStrategy returnHandler) {
		super(returnHandler);
	}
	@Override
	public void validation(HttpServletRequest request,HttpServletResponse response, Exception exception,ExceptionChain chain) throws IOException {
		boolean actionMode = UrlUtil.requiresAjax(request);
		if(exception instanceof ConstraintViolationException){
			String message = errorAllMessage((ConstraintViolationException) exception);
			if(actionMode){
				ReturnMessage returnMessage = new ReturnMessage(ReturnMessage.FAIL,message);
				returnHandler.handle(request, response).returnAjaxHandle(returnMessage);
			}else{
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", message);
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,message);
			}
		}else if (ObjUtil.isNotEmpty(exception.getCause()) && exception.getCause() instanceof ConstraintViolationException){
			//String message = errorMessage((ConstraintViolationException) cause.getCause());
			String message = errorAllMessage((ConstraintViolationException) exception.getCause());
			if(actionMode){
				ReturnMessage returnMessage = new ReturnMessage(ReturnMessage.FAIL,message);
				returnHandler.handle(request, response).returnAjaxHandle(returnMessage);
			}else{
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", message);
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,message);
			}
		
		}else if (ObjUtil.isNotEmpty(exception.getCause()) && ObjUtil.isNotEmpty(exception.getCause().getCause())  && exception.getCause().getCause() instanceof ConstraintViolationException){
			//String message = errorMessage((ConstraintViolationException) cause.getCause().getCause());
			String message = errorAllMessage((ConstraintViolationException) exception.getCause().getCause());
			if(actionMode){
				ReturnMessage returnMessage = new ReturnMessage(ReturnMessage.FAIL,message);
				returnHandler.handle(request, response).returnAjaxHandle(returnMessage);
			}else{
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				request.setAttribute("errorMessage", message);
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,message);
			}
		}else{
			chain.validationException(request, response, exception);
		}
	}
		private String errorMessage(ConstraintViolationException exception){
			if(ObjUtil.isNotEmpty(exception)){
				Iterator<?> it=exception.getConstraintViolations().iterator();
				while (it.hasNext()) {
					ConstraintViolation<?> type = (ConstraintViolation<?>) it.next();
					return type.getMessage();
				}
			}
			return "无法获取异常信息";
		}
		
		private String errorAllMessage(ConstraintViolationException exception){
			String errorMessage="";
			if(ObjUtil.isNotEmpty(exception)){
				Iterator<?> it=exception.getConstraintViolations().iterator();
				while (it.hasNext()) {
					ConstraintViolation<?> type = (ConstraintViolation<?>) it.next();
					String name = type.getPropertyPath().toString();
					type.getRootBean();
					Class<?> c = type.getRootBeanClass();
					String cName = c.getName();
					String key = cName+"."+name;
					String chName = ValidationUtil.getValidationField(key);
					if(ObjUtil.isBlank(errorMessage)){
						errorMessage = chName+":"+type.getMessage();
					}else{
						errorMessage = errorMessage+"<br>"+chName+":"+type.getMessage();
					}
				}
				if(ObjUtil.isNotBlank(errorMessage)){
					return errorMessage;
				}
			}
			return "无法获取异常信息";
		}
		
}
