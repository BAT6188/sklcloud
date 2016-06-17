package com.skl.cloud.foundation.remote;

import com.skl.cloud.common.exception.BusinessException;

public class SKLRemoteException extends BusinessException {
	private static final long serialVersionUID = -2691082495140867333L;

	public SKLRemoteException() {
		super();
	}

	public SKLRemoteException(String message, Throwable cause) {
		super(message, cause);
	}

	public SKLRemoteException(String message) {
		super(message);
	}

	public SKLRemoteException(Throwable cause) {
		super(cause);
	}

	public SKLRemoteException(int errCode, String message, Throwable cause) {
		super(errCode, message, cause);
	}
	
	public SKLRemoteException(int errCode, String message) {
		super(errCode, message);
	}


}
