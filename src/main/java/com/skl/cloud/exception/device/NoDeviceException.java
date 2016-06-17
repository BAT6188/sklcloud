package com.skl.cloud.exception.device;

import com.skl.cloud.common.exception.BusinessException;

@SuppressWarnings("serial")
public class NoDeviceException extends BusinessException {

	public NoDeviceException() {
		super(0x50000047);
	}

	public NoDeviceException(String message) {
		super(0x50000047, message);
	}

}
