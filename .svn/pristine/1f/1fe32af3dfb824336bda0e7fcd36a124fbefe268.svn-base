package com.skl.cloud.dao.ipc;

import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.IPCameraSub;

public interface IPCMapper {

    /**
    * update(更新IPC上的数据)
    * @Title update
    * @param ipcamera
    */
    public void updateIpc(IPCamera ipcamera);
	
    public void updateIpcSub(IPCameraSub ipcSub);

    /**
     * 新增时保存IPCamera的信息
     * @Title insert
     * @param ipcamera the model of {@link IPCamera, IPCameraSub}
    */
    public void insertIpc(IPCamera ipcamera);
	
    public void insertIpcSub(IPCameraSub ipcSub);

    /**
     * 删除IPCamera的信息
     * @Title delete
     * @param id the ipc camera id
    */
    public void deleteIpc(Long id);
	
    public void deleteIpcSub(Long id);

    /**
     * 查询IPCamera(通过CameraId/SN/DeviceId/查询IPCamera的信息)
     * @Title queryIPCamera
     * @param ipcamera
    */
    public IPCamera queryIPCamera(IPCamera ipcamera);
	
    public IPCameraSub queryIPCameraSub(IPCameraSub ipcameraLink);
}