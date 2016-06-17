package com.skl.cloud.dao.test;

import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;

public interface TestControllerMapper {
	int insertNatInfo(Map<String, String> map) throws ManagerException;
}
