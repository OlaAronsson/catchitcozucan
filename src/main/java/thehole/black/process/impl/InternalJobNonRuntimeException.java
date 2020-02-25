package thehole.black.process.impl;

import thehole.black.process.ErrorCodeCarrier;

class InternalJobNonRuntimeException extends RuntimeException implements ErrorCodeCarrier {

	private static final int NO_ERROR = 0;
	private final int errorCode;

	protected InternalJobNonRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = NO_ERROR;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}
}
