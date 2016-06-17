package com.skl.cloud.dao.mes;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.mes.PlatformProd;
import com.skl.cloud.model.sub.SubException;

/**
 * @Package com.skl.cloud.dao
 * @Title: MesDataMapper
 * @Description: MES产品数据导入云端相关操作
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月10日
 * @version V1.0
 */
public interface MesDataMapper {
	
	/**
	 * 
	  * @Title: insertMesData
	  * @Description: 批量导入MES产品数据
	  * @param list
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月18日
	 */
	public void insertMesData(List<PlatformProd> list) throws ManagerException;
	
	/**
	 * 
	  * queryAmountOfTheDifference(查询产品原始表和IPC设备表之间记录不同的条数)
	  * @Title: queryAmountOfTheDifference
	  * @Description: TODO
	  * @param @throws ManagerException (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月12日
	 */
	public Long queryAmountOfTheDifference() throws ManagerException;
	
	/**
	 * 
	  * recordsFromProdToIpcamera(把t_platform_prod表记录插入到t_platform_ipcamera表里)
	  * @Title: recordsFromProdToIpcamera
	  * @Description: TODO
	  * @param @param map
	  * @param @throws ManagerException (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月7日
	 */
	public void recordsFromProdToIpcamera(Long dataCount) throws ManagerException;
	
	/**
	 * 
	  * @Title: delMesAll
	  * @Description: 批量删除云端MES数据
	  * @param list
	  * @throws ManagerException
	  * @author leiqiang
	  * @date 2015年6月18日
	 */
	public void deleteMesAll(List<String> list) throws ManagerException;

	
	
	/**
	 * 清理过期异常上报日志
	 */
	public void deleteExceptionLog();
}
