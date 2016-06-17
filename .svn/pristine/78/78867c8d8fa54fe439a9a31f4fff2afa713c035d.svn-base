package com.skl.cloud.service;

import java.io.InputStream;

import org.springframework.http.ResponseEntity;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.UserCameraQuery;


public interface AppdeviceLinkIpcservice {
    
    /**
      * 取得这个设备被用户关联的总数,可以UserCameraQuery的cameraId或linkType作为查询条件，不set linkType默认全部linkType均返回
      * @Title: querySum
      * @param userCameraQuery
      * @return Integer (返回值说明)
      * @date 2015年12月17日
     */
    public Integer querySum(UserCameraQuery userCameraQuery);

    /**
     * 查询最早关联而且不在线用户的ID
     * @Title: querySum
     * @param cameraId
     * @param (参数说明)
     * @return int (返回值说明)
     * @date 2015年12月17日
    */
    public Long queryminId(Long cameraId);
    
    /**
     * 绑定设备
     * @param inputstream
     * @param userId
     * @return
     * @throws BusinessException
     */
    public void addDeviceLink(InputStream inputstream,Long userId) throws BusinessException;

    /**
     * 解除绑定的设备
     * @param sn
     * @return
     */
    public void deleteLinkIpc(String sn,Long userId);

    /**
     * 
     * install设备
     * <p>Creation Date: 2016年5月9日 and by Author: fulin </p>
     * @param userId
     * @param sn
     * @throws BusinessException
     * @return void
     * @throws
     *
     */
	public void installDevice(Long userId, String sn) throws BusinessException;
}
