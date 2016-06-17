package com.skl.cloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.dao.AppSnsInfoMapper;
import com.skl.cloud.model.AppSnsInfo;
import com.skl.cloud.service.AppSnsInfoService;

/**
 *  
 * @author weibin
 * @date 2016年2月29日
 */
@Service
public class AppSnsInfoServiceImpl implements AppSnsInfoService {
	
	@Autowired
	private AppSnsInfoMapper appSnsInfoMapper;
	
	@Override
	@Transactional(readOnly = true)
	public AppSnsInfo getSnsInfoByModelSystem(String model, String systemType)
			throws Exception {
		// TODO Auto-generated method stub
		return appSnsInfoMapper.getSnsInfoByModelSystem(model, systemType);
	}

}
