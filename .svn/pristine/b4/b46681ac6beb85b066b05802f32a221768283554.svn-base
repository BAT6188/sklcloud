package com.skl.cloud.service;

import java.util.Date;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformIpcameraSensor;

/**
 * @Package com.skl.cloud.service
 * @Title: IpcHeartBeatService
 * @Description: 获取IPC的Sensor数据信息接口
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月24日
 * @version V1.0
 */
public interface IpcHeartBeatService {
	
	/**
	 * 
	  * @Title: insertIpcHeartBeat
	  * @Description: TODO
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public void insertIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException;
	
	/**
	 * 
	  * @Title: updateIpcHeartBeat
	  * @Description: TODO
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public void updateIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException;
	
	/**
	 * 
	  * @Title: queryIpcHeartBeat
	  * @Description: TODO
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public Boolean queryIpcHeartBeat(String sn) throws ManagerException;
	
	public void callAvgSensorRecordsByHour(Date baseDate, String sn);

	public Date getLastTime(String sn);

    /**
     * @Title: queryAppDevicesSensor
     * @Description: 获取设备Sensor信息
     * @param sn
     * @return PlatformIpcameraSensor
     * @throws ManagerException
    */
    public PlatformIpcameraSensor getLatestIpcSensorBySn(String sn) throws ManagerException;
}
