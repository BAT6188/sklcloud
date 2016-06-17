package com.skl.cloud.remote.ipc;

import org.springframework.http.HttpMethod;

import com.skl.cloud.foundation.remote.SKLRemote;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.annotation.RText;
import com.skl.cloud.foundation.remote.annotation.RVariable;
import com.skl.cloud.foundation.remote.ipc.IPCRemote;
import com.skl.cloud.foundation.remote.ipc.SN;

public interface FWUpdateRemote  extends SKLRemote {
    
	/**
	 * 云端请求IPC下载更新固件
	 * @param sn
	 * @return
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/device/{SN}/system/updateFirmware", method=HttpMethod.POST)
	String  fwUpdate(@RVariable("SN") @SN String sn, @RText String xml) throws SKLRemoteException;
	
	/**
	 * 获取ipc升级进度
	 * @param sn
	 * @return
	 */
	@IPCRemote(uri="/device/{SN}/system/updateFirmware/status", method=HttpMethod.GET)
	String queryUpdateFirmwareStatus(@RVariable("SN") @SN String sn);
}
