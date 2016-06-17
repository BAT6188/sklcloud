package com.skl.cloud.dao;

import java.util.List;

import com.skl.cloud.common.exception.ManagerException;

public interface EventAlertMapper {
	public List<String> queryALLS3key(String sn) throws ManagerException;
	
	public void deleteBySN(String sn) throws ManagerException;
}
