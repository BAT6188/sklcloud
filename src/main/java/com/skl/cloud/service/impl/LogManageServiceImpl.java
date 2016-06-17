package com.skl.cloud.service.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.LogManageMapper;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.service.LogManageService;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: LogManageServiceImpl
 * @Description: 日志管理
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月9日
 * @version V1.0
 */
@Service("logManageService")
public class LogManageServiceImpl implements LogManageService {
	
	private LogManageMapper logManageMapper;

	public LogManageMapper getLogManageMapper() {
		return logManageMapper;
	}

	@Autowired(required = true)	
	public void setLogManageMapper(LogManageMapper logManageMapper) {
		this.logManageMapper = logManageMapper;
	}

	
	/*
	  * <p>Title: saveLog</p>
	  * <p>Description: </p>
	  * @param pl
	  * @throws ManagerException
	  * @see com.skl.cloud.service.LogManageService#saveLog(com.skl.cloud.model.PlatformLog)
	  */
	@Override
	@Transactional
	public void saveLog(PlatformLog pl) throws ManagerException {
		logManageMapper.saveLog(pl);
	}
	

	/*
	 * <p>Title: saveLog</p>
	 * <p>Description: </p>
	 * @param sUser
	 * @param sModule
	 * @param sContent
	 * @param sTime
	 * @throws ManagerException
	 * @see com.skl.cloud.service.LogManageService#saveLog(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void saveLog(String sUser, String sModule, String sContent, String sTime) throws ManagerException {
		logManageMapper.saveLog(sUser, sModule, sContent, sTime);
	}

	
	/*
	 * <p>Title: saveLog</p>
	 * <p>Description: </p>
	 * @param sUser
	 * @param sModule
	 * @param sContent
	 * @param dLog
	 * @throws ManagerException
	 * @see com.skl.cloud.service.LogManageService#saveLog(java.lang.String, java.lang.String, java.lang.String, java.sql.Date)
	 */
	@Override
	@Transactional
	public void saveLog(String sUser, String sModule, String sContent, Date dLog) throws ManagerException {
		logManageMapper.saveLog(sUser, sModule, sContent, dLog);
	}


	/*
	 * <p>Title: queryLog</p>
	 * <p>Description: </p>
	 * @return
	 * @throws ManagerException
	 * @see com.skl.cloud.service.LogManageService#queryLog()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PlatformLog> queryLog(PlatformLog pl) throws ManagerException {
		return logManageMapper.queryLog(pl);
	}

	/**
	 * 
	  * <p>Title: saveLogAlong</p>
	  * <p>Description: </p>
	  * @param pl
	  * @throws ManagerException
	  * @see com.skl.cloud.service.LogManageService#saveLogAlong(com.skl.cloud.model.PlatformLog)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLogAlong(PlatformLog pl) throws ManagerException {
		logManageMapper.saveLog(pl);
	}
}
