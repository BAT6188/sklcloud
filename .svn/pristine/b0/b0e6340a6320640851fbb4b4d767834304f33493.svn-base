package com.skl.cloud.dao;

import java.util.List;
import java.util.Map;

public interface AppDeviceFeedsFilterMapper {

    /**
     * <获取用户被分享ipc设备信息select>
     * @param userId [用户id]
     * @param sn [设备sn序列号]
     * @return [返回结果-设备map对象]
     * @throws Exception [抛出异常]
     */
    public Map<String, Object> getShareDeviceByUserSn(String userId, String sn) throws Exception;

    /**
     * <设置分享设备feedsFilter使能update>
     * @param userId [用户id]
     * @param deviceId [设备id]
     * @param feedsFilter [feedsFilter使能]
     * @throws Exception
     */
    public void updateDeviceFeedsFilter(String userId, String deviceId, String feedsFilter) throws Exception;

    /**
     * <查询分享设备feedsFilter使能select>
     * @param userId [用户id]
     * @return [设备使能列表]
     * @throws Exception
     */
    public List<Map<String, Object>> queryDeviceFeedsFilter(String userId) throws Exception;
}
