package com.skl.cloud.foundation.remote.exception;

import com.skl.cloud.foundation.remote.SKLRemoteException;

@SuppressWarnings("serial")
public class CanceledOperationIPCRemoteException extends SKLRemoteException {

	public CanceledOperationIPCRemoteException() {
		super(40, "IPC cancel the task");
	}
}
