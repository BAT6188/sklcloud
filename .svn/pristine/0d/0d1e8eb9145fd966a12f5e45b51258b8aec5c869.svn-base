package com.skl.cloud.service.test.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.test.TestControllerMapper;
import com.skl.cloud.service.test.TestControllerService;

@Service("testControllerService")
public class TestControllerServiceImpl implements TestControllerService {
	@Autowired(required = true)
	private TestControllerMapper testControllerMapper;

	@Override
	@Transactional
	public int insertNatInfo(Map<String, String> map) throws ManagerException {
		return testControllerMapper.insertNatInfo(map);
	}

}
