package com.skl.cloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.AppRelatedDevicesMapper;
import com.skl.cloud.model.PlatformCamera;
import com.skl.cloud.service.AppRelatedDevicesService;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: AppRelatedDevicesServiceImpl
 * @Description: 查询关联设备
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年6月27日
 * @version V1.0
 */
@Service("appRelatedDevicesService")
public class AppRelatedDevicesServiceImpl implements AppRelatedDevicesService {

    @Autowired(required = true)
	private AppRelatedDevicesMapper appRelatedDevicesMapper;

	@Override
	@Transactional(readOnly = true)
	public PlatformCamera queryUserCamera(String sn) throws ManagerException {
		
		return appRelatedDevicesMapper.queryUserCamera(sn);
	}


}
