package com.skl.cloud.dao;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.sub.SubsysAddress;

public interface StreamResourcesMapper {

	/**
	 * 查询stun服务器上报的信息
	 * @Title: getIPCAddress
	 * @param ManagerException (参数说明)
	 * @author yangbin
	 * @date 2015年12月30日
	*/
	public Map<String, Object> getIPCAddress() throws ManagerException;

	/**
	 * 按Server_sys_id查询当前子系统所有在运行的server信息
	 * @Title: getFreeSystemInfosBySystemId
	 * @param  serverSysId
	 * @param ManagerException (参数说明)
	 * @author yangbin
	 * @date 2015年12月30日
	*/
	public List<SubsysAddress> getFreeSystemInfosBySystemId(Integer serverSysId) throws ManagerException;

	/**
	 * 按Server_sys_id和开始的记录查询当前子系统总共所运行的server数
	 * @Title: getServerCountBySystemId
	 * @param  serverSysId
	 * @param ManagerException (参数说明)
	 * @author yangbin
	 * @date 2015年12月30日
	*/
	public SubsysAddress getFreeSystemInfoBySystemIdAndLimitStart(Integer serverSysId, Integer startNumber)
			throws ManagerException;

	/**
	  * 按Server_sys_id查询当前子系统总共所运行的server数
	  * @Title: getServerCountBySystemId
	  * @param  serverSysId
	  * @param ManagerException (参数说明)
	  * @author yangbin
	  * @date 2015年12月30日
	 */
	public Integer getServerCountBySystemId(Integer serverSysId) throws ManagerException;

	/**
	 * 
	  * getSubsystemAddressById(查询子系统的信息)
	  * @Title: getSubsystemAddressById
	  * @Description: TODO
	  * @param @param serverSysId
	  * @param @return
	  * @param @throws ManagerException (参数说明)
	  * @return SubsysAddress (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月29日
	 */
	public List<SubsysAddress> getSubsystemAddressById(Integer serverSysId) throws ManagerException;

	/**
	 * 
	 * @Title: querySubsysIpPort
	 * @Description: 查询流处理子系统对应服务器的IP和Port
	 * @param uuid
	 * @throws ManagerException
	 * @author lizhiwei
	 * @return SubsysAddress
	 * @date 2015年9月15日
	 */
	public com.skl.cloud.model.SubsysAddress querySubsysIpPort(String uuid) throws ManagerException;

	/**
	 * 
	 * @Title: queryEventStatus
	 * @Description: 查询接入子系统是否录制完成并返回状态
	 * @param sn
	 * @param channelId
	 * @param eventID
	 * @param dateTime
	 * @throws ManagerException
	 * @author lizhiwei
	 * @return String
	 * @date 2015年9月24日
	 */
	public String queryEventStatus(String sn, String channelId, String eventID, String dateTime)
			throws ManagerException;
}
