package org.csr.core.exception;

import org.csr.core.jump.JumpStrategy;


public abstract class AbstractValidationException implements ValidationChainException {
	/**
	 * 页面跳转处理，
	 */
	public final JumpStrategy returnHandler;
	
	public AbstractValidationException(JumpStrategy returnHandler){
		this.returnHandler = returnHandler;
	}
}
