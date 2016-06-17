package com.skl.cloud.dao.fw;

import com.skl.cloud.model.PlatformIpcfw;

public interface FWServiceMapper {
	
	/**
	 * 通过model来获取云端最新fw通过版本号和发布时间进行排序
	 * @param model
	 * @param version
	 * @return
	 */
	public PlatformIpcfw queryCloudLatestFwVersion(String model);
	
	/**
	 * 通过model和version来获取版本信息
	 * @param model
	 * @param version
	 * @return
	 */
	public PlatformIpcfw queryPlatformIpcfwInfo(String model, String version);
}
