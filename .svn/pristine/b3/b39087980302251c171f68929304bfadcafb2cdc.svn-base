package com.skl.cloud.dao;

import java.sql.Date;
import java.util.List;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformLog;

/**
 * @Package com.skl.cloud.dao
 * @Title: CommonMapper
 * @Description: 公共查询
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月30日
 * @version V1.0
 */
public interface LogManageMapper {
	
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
	  * @return
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年7月9日
	 */
	public List<PlatformLog> queryLog(PlatformLog pl) throws ManagerException;
}
