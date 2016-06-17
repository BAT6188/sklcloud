package com.skl.cloud.common.exception;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 4975945477260392772L;
		
	protected int errCode = 1;

	public BusinessException() {
		super();
	}
	
	public BusinessException(int errCode) {
		this.errCode = errCode;
	}
	
	public BusinessException(int errCode , String message) {
		super(message);
		this.errCode = errCode;
	}
	
	public BusinessException(int errCode , String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public int getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return getMessage();
	}
}
