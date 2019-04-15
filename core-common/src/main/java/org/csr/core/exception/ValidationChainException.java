package org.csr.core.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ValidationChainException {

	public void validation(HttpServletRequest request, HttpServletResponse response,
			Exception exception, ExceptionChain chain) throws IOException;
}
