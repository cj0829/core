package org.csr.core.exception;

public class ValidationException extends AbstractException {
	private static final long serialVersionUID = 1519826433419668798L;

	public ValidationException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}

	public ValidationException(int errorCode, String newErrorNo, String errorMsg) {
		super(errorCode, newErrorNo, errorMsg);
	}
}
