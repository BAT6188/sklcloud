package com.skl.cloud.service.mes;

import java.util.List;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.mes.PlatformProd;

/**
 * @Package com.skl.cloud.service
 * @Title: MesDataService
 * @Description: MES产品数据导入云端相关操作
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月10日
 * @version V1.0
 */
public interface MesDataService {
	
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
	  * @param @return
	  * @param @throws ManagerException (参数说明)
	  * @return Long (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月12日
	 */
	public Long queryAmountOfTheDifference() throws ManagerException;
	
	/**
	 * 
	  * @Title: delMesAll
	  * @Description: 批量删除云端数据
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
	
	/**
	 * 
	  * insertMesExcelData(Mes数据入库)
	  * @Title: insertMesExcelData
	  * @Description: TODO
	  * @param @return
	  * @param @throws ManagerException (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月8日
	 */
	public boolean insertMesExcelData() throws ManagerException;
	
	/**
	 * 
	  * insertIpcameraWithMesData(向设备IPC表插入Mes产品数据)
	  * @Title: insertIpcameraWithMesData
	  * @Description: TODO
	  * @param @return (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月8日
	 */
	public boolean insertIpcameraWithMesData();
}
