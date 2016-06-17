package com.skl.cloud.dao;

import com.skl.cloud.model.AppSnsInfo;

/**
 *  
 * @author weibin
 * @date 2016年2月29日
 */
public interface AppSnsInfoMapper {
	/***
	 * 获取app推送平台的信息
	 * @param model ipc的model
	 * @param systemType app应用的操作系统
	 * @return
	 * @throws Exception
	 */
	 public AppSnsInfo getSnsInfoByModelSystem(String model, String systemType) throws Exception;
}
