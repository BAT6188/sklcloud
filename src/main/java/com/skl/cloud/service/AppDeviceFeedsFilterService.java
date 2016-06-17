package com.skl.cloud.service;

import java.util.List;
import java.util.Map;

public interface AppDeviceFeedsFilterService {

    /**
     * <获取用户被分享ipc设备信息>
     * @param userId [用户id]
     * @param sn [设备sn序列号]
     * @return [返回结果-被分享设备map对象]
     * @throws Exception [抛出异常]
     */
    public Map<String, Object> getShareDeviceByUserSn(String userId, String sn) throws Exception;

    /**
     * <设置分享设备feedsFilter使能>
     * @param userId [用户id]
     * @param list [设备信息list] map 的key值有“deviceId” ，“enable”
     * @throws Exception
     */
    public void updateDeviceFeedsFilter(String userId, List<Map<String, String>> list) throws Exception;

    /**
     * <查询分享设备feedsFilter使能>
     * @param userId [用户id]
     * @return [设备使能列表]
     * @throws Exception
     */
    public List<Map<String, Object>> queryDeviceFeedsFilter(String userId) throws Exception;

}
