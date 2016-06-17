package com.skl.cloud.exception.common;

import com.skl.cloud.common.exception.BusinessException;

@SuppressWarnings("serial")
public class InvalidParameterException extends BusinessException {
	
	public InvalidParameterException() {
		super(0x50000001);
	}

	public InvalidParameterException(String message) {
		super(0x50000001, message);
	}
}
