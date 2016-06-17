package com.skl.cloud.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.IpcHeartBeatMapper;
import com.skl.cloud.model.PlatformIpcameraSensor;
import com.skl.cloud.service.IpcHeartBeatService;
import com.skl.cloud.util.common.DateUtil;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: IpcHeartBeatServiceImpl
 * @Description: 获取IPC的Sensor数据信息接口
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月24日
 * @version V1.0
 */
@Service("ipcHeartBeatService")
public class IpcHeartBeatServiceImpl implements IpcHeartBeatService {
	
    @Autowired
	private IpcHeartBeatMapper ipcHeartBeatMapper;

	/*
	 * <p>Title: insertIpcHeartBeat</p>
	 * <p>Description: </p>
	 * @param platformIpcameraSensor
	 * @throws ManagerException
	 * @see com.skl.cloud.service.IpcHeartBeatService#insertIpcHeartBeat(com.skl.cloud.model.PlatformIpcameraSensor)
	 */
	@Override
	@Transactional
	public void insertIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException {
		ipcHeartBeatMapper.insertIpcHeartBeat(platformIpcameraSensor);
	}

	/*
	 * <p>Title: updateIpcHeartBeat</p>
	 * <p>Description: </p>
	 * @param platformIpcameraSensor
	 * @throws ManagerException
	 * @see com.skl.cloud.service.IpcHeartBeatService#updateIpcHeartBeat(com.skl.cloud.model.PlatformIpcameraSensor)
	 */
	@Override
	@Transactional
	public void updateIpcHeartBeat(PlatformIpcameraSensor platformIpcameraSensor) throws ManagerException {
		ipcHeartBeatMapper.updateIpcHeartBeat(platformIpcameraSensor);
	}

	/*
	  * <p>Title: queryIpcHeartBeat</p>
	  * <p>Description: </p>
	  * @param platformIpcameraSensor
	  * @throws ManagerException
	  * @see com.skl.cloud.service.IpcHeartBeatService#queryIpcHeartBeat(com.skl.cloud.model.PlatformIpcameraSensor)
	  */
	@Override
	@Transactional(readOnly = true)
	public Boolean queryIpcHeartBeat(String sn) throws ManagerException {
		return ipcHeartBeatMapper.queryIpcHeartBeat(sn);
	}

	@Override
	@Transactional
	public void callAvgSensorRecordsByHour(Date baseDate, String sn) {
		ipcHeartBeatMapper.callAvgSensorRecordsByHour(DateUtil.date2Str(baseDate, DateUtil.DATE_TIME_1_FULL_FORMAT),sn);
	}

	@Override
	@Transactional(readOnly = true)
	public Date getLastTime(String sn) {
		return ipcHeartBeatMapper.getLastTime(sn);
	}
	
    /**
     * @Title: queryAppDevicesSensor
     * @Description: 获取设备Sensor信息
     * @param sn
     * @return PlatformIpcameraSensor
     * @throws ManagerException
    */
	@Override
	@Transactional(readOnly = true)
    public PlatformIpcameraSensor getLatestIpcSensorBySn(String sn) throws ManagerException{
	    return ipcHeartBeatMapper.queryLatestIpcSensorBySn(sn);
    }

}
