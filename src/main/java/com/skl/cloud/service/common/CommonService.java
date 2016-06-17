package com.skl.cloud.service.common;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.user.AppUser;

/**
 * @Package com.skl.cloud.service
 * @Title: CommonService
 * @Description: 公共查询
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月30日
 * @version V1.0
 */
public interface CommonService {

	/**
	  * @Title: queryAppUser
	  * @Description: 通过userId查询用户信息
	  * @param @param id
	  * @param @return
	  * @param @throws ManagerException (参数说明)
	  * @return AppUser (返回值说明)
	  * @author shaoxiong
	  * @date 2015年8月14日
	  */
	public AppUser queryAppUserById(Long id) throws ManagerException;
	
	/**
	 * 
	 * 根据类型获取系统参数
	 * <p>Creation Date: 2016年5月5日 and by Author: zhaonao </p>
	 * @param type
	 * @return
	 * @return List<Map<String,Object>>
	 * @throws
	 *
	 */
	public List<Map<String, Object>> getSystemConfigByType(Integer type);

	/**
	 * 
	 * 获取系统参数
	 * <p>Creation Date: 2016年5月5日 and by Author: zhaonao </p>
	 * @return
	 * @return List<Map<String,Object>>
	 * @throws
	 *
	 */
	public List<Map<String, Object>> getAllSystemConfig();
}
