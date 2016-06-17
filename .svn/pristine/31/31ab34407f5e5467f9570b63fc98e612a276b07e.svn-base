package com.skl.cloud.service.sub;

import java.util.Map;

import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.remote.ipc.dto.ipc.IPCStreamControlIO;
import com.skl.cloud.util.constants.Constants.ServerSystemId;

/**
 * 
 * @ClassName: StreamStopService
 * @Description: stop the sn stream
 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
 *
 * @author $Author$
 * @date $Date$
 * @version  $Revision$
 */
public interface StreamStopService {

	/**
	 * 请求流接入子系统释放对应SN的流资源
	 */
	public Map<String,String> stopSubSysStream(String destIp, String sn, String channelId, ServerSystemId subSysId);
	
	/**
	 * 向IPC请求流停止操作
	 */
	public void stopIpcStream(String sn) throws SKLRemoteException;

	/**
	 * 向IPC请求流停止操作
	 */
	public void stopIpcStream(String sn, IPCStreamControlIO iPCStreamControlIO);
	
	/**
	 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
	 * @param destIp
	 * @param sn
	 * @param subSystemIds
	 * @return void
	 */
	public void stopSubSystem(String destIp, String sn, int... subSystemIds);
	
	/**
	 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
	 * @param destIp
	 * @param sn
	 * @param subSystemIds
	 * @return void
	 */
	public void stopSubSystem(String destIp, String sn, boolean isStopIpc, int... subSystemIds);
}
