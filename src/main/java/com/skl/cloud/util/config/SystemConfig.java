package com.skl.cloud.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.common.spring.SpringContextHolder;
import com.skl.cloud.service.common.CommonService;

public class SystemConfig {
	private static Logger logger = Logger.getLogger(SystemConfig.class);

	private static String activeProfile;

	private static Properties properties = new Properties();

	static {
		logger.info("Initializing the config property.");
		Environment environment = null;
		try {
			environment = BeanLocator.getBean(Environment.class);
		} catch (NullPointerException e) {
			logger.info("BeanLocator is null in batchjob environment");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		if (environment != null) {
			String[] actives = environment.getActiveProfiles();
			activeProfile = actives.length > 0 ? actives[0] : "local";
			
			// 初始化db系统参数
			CommonService commonService = BeanLocator.getBean(com.skl.cloud.service.common.CommonService.class);
			initProperties(commonService.getSystemConfigByType(1));
		} else {
			activeProfile = "batchjob";

			// 初始化db系统参数
			CommonService commonService = SpringContextHolder
					.getBizComponent(com.skl.cloud.service.common.CommonService.class);
			initProperties(commonService.getSystemConfigByType(1));
		}
		logger.info("current actives environment is: " + activeProfile);

		String propertyPath = "config/" + activeProfile;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL path = classLoader.getResource(propertyPath);

		loadFileProperties(path.getFile());

		logger.info("Finished initializing the config property.");
	}

	/**
	 * 
	 * 初始化系统参数
	 * <p>Creation Date: 2016年5月5日 and by Author: zhaonao </p>
	 * @param list
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 *
	 */
	private static void initProperties(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			properties.put(map.get("paramName"), map.get("paramValue"));
		}
	}

	/**
	 * Load systemConfig and serverConfig.
	 * @param configProperties Properties from classPath:conf/property
	 * @throws IOException 
	 */
	private static void loadFileProperties(String filePath) {
		File[] files = new File(filePath).listFiles(new FilenameFilter() {
			/**
			* Tests if a specified file should be included in a file list.
			*
			* @param   dir    the directory in which the file was found.
			* @param   name   the name of the file.
			* @return  <code>true</code> if and only if the name should be
			* included in the file list; <code>false</code> otherwise.
			*/
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".properties");
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			}
			InputStream input = null;
			try {
				input = new FileInputStream(file);
				properties.load(input);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
	}

	/**
	 * 
	 * 通过key查询value
	 * <p>Creation Date: 2016年5月6日 and by Author: zhaonao </p>
	 * @param key
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getProperty(String key) {
		// 第一匹配
		String value = properties.getProperty(activeProfile + "." + key);
		if (value == null) {
			// 第二匹配
			value = properties.getProperty(key);
		}
		if (value == null) {
			logger.warn("did not find the value of this key[" + key + "]");
		}
		return value;
	}

	/**
	 * 
	 * 通过key查询value,找不到返回defaultValue
	 * <p>Creation Date: 2016年5月6日 and by Author: zhaonao </p>
	 * @param key
	 * @param defaultValue
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * 
	 * 通过key和设备类型model查询value
	 * <p>Creation Date: 2016年5月6日 and by Author: zhaonao </p>
	 * @param key
	 * @param model
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getPropertyModel(String key, String model) {
		// 第一匹配
		String value = properties.getProperty(activeProfile + "." + model + "." + key);
		if (value == null) {
			// 第二匹配
			value = properties.getProperty(model + "." + key);
		}
		if (value == null) {
			// 第三匹配
			value = properties.getProperty(activeProfile + "." + key);
		}
		if (value == null) {
			// 第四匹配
			value = properties.getProperty(key);
		}

		if (value == null) {
			logger.warn("did not find the value of this key[" + key + "]");
		}
		return value;
	}

	/**
	 * 
	 * 通过key和设备类型model查询value,找不到返回defaultValue
	 * <p>Creation Date: 2016年5月6日 and by Author: zhaonao </p>
	 * @param key
	 * @param defaultValue
	 * @param model
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getPropertyModel(String key, String defaultValue, String model) {
		String value = getPropertyModel(key, model);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
}
