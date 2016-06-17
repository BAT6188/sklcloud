package com.skl.cloud.dao;

import com.skl.cloud.model.user.UserCameraQuery;


public interface AppdeviceLinkIpcMapper {
	
    /**
     * 
     * 取得这个设备被用户关联的总数
     * <p>Creation Date: 2016年5月18日 and by Author: fulin </p>
     * @param userCameraQuery
     * @return
     * @return Integer
     * @throws
     */
	public  Integer querySum(UserCameraQuery userCameraQuery);
    
    public Long queryminId(Long cameraId);
    
}
