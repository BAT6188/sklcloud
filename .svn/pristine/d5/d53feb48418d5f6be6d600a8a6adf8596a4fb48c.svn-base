package com.skl.cloud.dao;

import java.util.Date;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformIpcameraSensor;

/**
 * @Package com.skl.cloud.dao
 * @Title: IpcHeartBeatMapper
 * @Description: 获取IPC的Sensor数据信息接口
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月24日
 * @version V1.0
 */
public interface IpcHeartBeatMapper {

	/**
	  * @Title: insertIpcHeartBeat
	  * @Description: TODO
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public void insertIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException;

	/**
	  * @Title: updateIpcHeartBeat
	  * @Description: TODO
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public void updateIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException;

	/**
	  * @Title: queryIpcHeartBeat
	  * @Description: 查询设备是否存在
	  * @param sn
	  * @return
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月24日
	 */
	public Boolean queryIpcHeartBeat(String sn) throws ManagerException;

	/**
     * 更新每一小时的上报下平均值
     * @Title: getLastTime
     * @param sn
     * @param (参数说明)
     * @author yangbin
     * @date 2015年12月18日
    */
	public void callAvgSensorRecordsByHour(String baseDate, String sn);

	/**
	  * 得到最后一次上报更新的时间
	  * @Title: getLastTime
	  * @param @param sn
	  * @param @return (参数说明)
	  * @author yangbin
	  * @date 2015年12月18日
	 */
	public Date getLastTime(String sn);
	
	/**
      * @Title: queryAppDevicesSensor
      * @Description: 获取设备Sensor信息
      * @param sn
      * @return PlatformIpcameraSensor
      * @throws ManagerException
     */
	public PlatformIpcameraSensor queryLatestIpcSensorBySn(String sn) throws ManagerException;

}
