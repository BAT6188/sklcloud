package com.skl.cloud.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.AppDeviceFeedsFilterMapper;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.AppDeviceFeedsFilterService;
import com.skl.cloud.service.ipc.IPCameraService;

@Service("appDeviceFeedsFilterService")
public class AppDeviceFeedsFilterServiceImpl implements AppDeviceFeedsFilterService {
    @Autowired
    private AppDeviceFeedsFilterMapper appDeviceFeedsFilterMapper;

    @Autowired
    private IPCameraService ipcService;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getShareDeviceByUserSn(String userId, String sn) throws Exception {
        IPCamera ipcamera = ipcService.getIPCameraBySN(sn);
        if (ipcamera == null) {
            throw new BusinessException("0x50000047");
        }
        return appDeviceFeedsFilterMapper.getShareDeviceByUserSn(userId, sn);
    }

    @Override
    @Transactional
    public void updateDeviceFeedsFilter(String userId, List<Map<String, String>> list) throws Exception {
    	
    	if (list.size() > 0) {
    		for (Map<String, String> map : list)
    		{
        		appDeviceFeedsFilterMapper.updateDeviceFeedsFilter(userId, map.get("deviceId"), map.get("enable"));

    		}
		}
    	
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryDeviceFeedsFilter(String userId) throws Exception {
        return appDeviceFeedsFilterMapper.queryDeviceFeedsFilter(userId);
    }

}
