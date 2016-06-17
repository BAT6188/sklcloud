package com.skl.cloud.util.common;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * @Package skl.util
 * @Title: 开发日志类
 * @Description: 开发人员调用日志类进行记录日志
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年5月27日
 * @version V1.0
 */
public class LoggerUtil {

	/**
	 * 
	 * @param message
	 * @param loggerName
	 */
	public static void info(String message, String loggerName) {
		if ((loggerName != null) && (!"".equals(loggerName.trim()))) {
			Logger logger = Logger.getLogger(loggerName.trim());
			if (logger.isInfoEnabled())
				logger.info(message);
		}
	}

	/**
	 * 
	 * @param message
	 * @param curClass
	 */
	public static void info(String message, Class curClass) {
		if (curClass != null) {
			Logger logger = Logger.getLogger(curClass);
			if (logger.isInfoEnabled())
				logger.info(message + "   in class : " + curClass.getName());
		}
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 * @param loggerName
	 */
	public static void info(String message, Throwable ex, String loggerName) {
		if ((loggerName != null) && (!"".equals(loggerName.trim()))) {
			Logger logger = Logger.getLogger(loggerName.trim());
			if (logger.isInfoEnabled())
				logger.info(message, ex);
		}
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 * @param curClass
	 */
	public static void info(String message, Throwable ex, Class curClass) {
		if (curClass != null) {
			Logger logger = Logger.getLogger(curClass);
			if (logger.isInfoEnabled())
				logger.info(message + "   in class : " + curClass.getName(), ex);
		}
	}

	/**
	 * 
	 * @param message
	 * @param loggerName
	 */
	public static void error(String message, String loggerName) {
		if ((loggerName != null) && (!"".equals(loggerName.trim()))) {
			Logger logger = Logger.getLogger(loggerName.trim());
			if (logger.isEnabledFor(Priority.ERROR))
				logger.error(message);
		}
	}

	/**
	 * 
	 * @param message
	 * @param curClass
	 */
	public static void error(String message, Class curClass) {
		if (curClass != null) {
			Logger logger = Logger.getLogger(curClass);
			if (logger.isEnabledFor(Priority.ERROR))
				logger.error(message);
		}
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 * @param loggerName
	 */
	public static void error(String message, Throwable ex, String loggerName) {
		if ((loggerName != null) && (!"".equals(loggerName.trim()))) {
			Logger logger = Logger.getLogger(loggerName.trim());
			if (logger.isEnabledFor(Priority.ERROR))
				logger.error(message, ex);
		}
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 * @param curClass
	 */
	public static void error(String message, Throwable ex, Class curClass) {
		if (curClass != null) {
			Logger logger = Logger.getLogger(curClass);
			if (logger.isEnabledFor(Priority.ERROR))
				logger.error(message, ex);
		}
	}

	
}
