package com.skl.cloud.service.common.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.common.CommonMapper;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.common.CommonService;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: CommonServiceImpl
 * @Description: 公共查询
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月30日
 * @version V1.0
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
	
    @Autowired(required = true)
	private CommonMapper commonMapper;

	@Override
	@Transactional
	public AppUser queryAppUserById(Long id) throws ManagerException {
		
		return commonMapper.queryAppUserById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getSystemConfigByType(Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paramType", type);
		return commonMapper.selectSystemConfig(map);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAllSystemConfig() {
		Map<String, Object> map = new HashMap<String, Object>();
		return commonMapper.selectSystemConfig(map);
	}
}
