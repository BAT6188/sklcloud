package com.skl.cloud.service;

import java.sql.Date;
import java.util.List;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformLog;

/**
 * @Package com.skl.cloud.service
 * @Title: LogManageService
 * @Description: 日志管理
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月9日
 * @version V1.0
 */
public interface LogManageService {
	
	
	/**
	  * 
	  * @Title: saveLog
	  * @Description: 记录日志
	  * @param pl
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年7月9日
	 */
	public void saveLog(PlatformLog pl) throws ManagerException;
	
	
	/**
	  * 
	  * @Title: saveLog
	  * @Description: 记录日志
	  * @param sUser
	  * @param sModule
	  * @param sContent
	  * @param sTime
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年7月9日
	 */
	public void saveLog(String sUser, String sModule, String sContent, String sTime) throws ManagerException;

	
	/**
	  * 
	  * @Title: saveLog
	  * @Description: 记录日志
	  * @param sUser
	  * @param sModule
	  * @param sContent
	  * @param dLog
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年7月9日
	 */
	public void saveLog(String sUser, String sModule, String sContent, Date dLog) throws ManagerException;
	
	
	/**
	  * 
	  * @Title: queryLog
	  * @Description: 查询日志记录
	  * @param pl
	  * @return List<PlatformLog>
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年7月9日
	 */
	public List<PlatformLog> queryLog(PlatformLog pl) throws ManagerException;
	
	/**
	  * 
	  * @Title: saveLog
	  * @Description: 记录日志（新开的独立事务）
	  * @param pl
	  * @throws ManagerException
	  * @author fulin
	  * @date 2016年4月20日
	 */
	public void saveLogAlong(PlatformLog pl) throws ManagerException;
}
