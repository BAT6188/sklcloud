package com.skl.cloud.service.fw;

import java.util.Map;

import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.model.PlatformIpcfw;

public interface FWService {

    /**
     * 远程获取ipc的版本信息
     * @param sn
     * @return
     * @throws SKLRemoteException
     */
    public String getRemoteIpcFWInfo(String sn) throws SKLRemoteException;
    
    /**
     * 请求查看ipc更新fw的状态
     * @param sn
     * @return
     */
    public String getRemoteFwUpdateStatus(String sn) throws SKLRemoteException;

    /**
     * 远程请求ipc升级fw
     * @param sn
     * @param platformIpcfw
     * @throws SKLRemoteException
     */
    public String updateRemoteFw(String sn, PlatformIpcfw platformIpcfw) throws SKLRemoteException;
    
    /**
     * 根据ipc类型获取最新的版本信息
     * @param model
     * @return
     */
    public Map<String, Object> getCloudLatestFwVersion(String model);

    /**
     * 通过model和version获取版本信息
     * @param model
     * @param version
     * @return
     */
    public PlatformIpcfw getPlatformIpcfwInfo(String model, String version);
}
