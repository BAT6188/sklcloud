package com.skl.cloud.dao;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformCamera;

/**
 * @Package com.skl.cloud.dao
 * @Title: AppRelatedDevicesMapper
 * @Description: 查询关联设备
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年6月27日
 * @version V1.0
 */
public interface AppRelatedDevicesMapper {

	
	/**
	 * 
	  * queryUserCamera(通过sn查询对应的设备信息)
	  * @Title: queryUserCamera
	  * @param @param sn
	  * @param @return
	  * @param @throws ManagerException (参数说明)
	  * @return PlatformCamera (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2015年11月26日
	 */
	public PlatformCamera queryUserCamera(String sn) throws ManagerException;

	
}
