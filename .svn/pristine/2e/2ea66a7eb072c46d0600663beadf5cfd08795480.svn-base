package com.skl.cloud.dao;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.TriggerVideo;

/**
 * @Package com.skl.cloud.dao
 * @Title: EventScheduleMapper
 * @Description: Event Schedule Copyright: Copyright (c) 2015
 *               Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月17日
 * @version V1.0
 */
public interface EventScheduleMapper
{

	/**
	 * 
	 * @Title: queryTriggerVideo
	 * @Description: 查询IPC相关流服务子系统及录制时间提前量等信息
	 * @param type
	 * @return TriggerVideo 某一种类型产品的时间提前量信息
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年9月15日
	 */
	public TriggerVideo queryTriggerVideo(String type) throws ManagerException;

	/**
	 * 
	 * @Title: updateEventVideo
	 * @Description: 业控更新录像触发事件的状态等信息
	 * @param map
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年9月24日
	 */
	public void updateEventVideo(Map<String, String> map) throws ManagerException;

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
	 * @date 2015年9月24日
	 */
	public String queryEventStatus(String sn, String channelId, String eventID, String dateTime) throws ManagerException;

	/**
	 * 
	 * @Title: queryEventAlert
	 * @Description: 查询事某一设备某一时间在表中的信息
	 * @param sn
	 * @param channelId
	 * @param eventID
	 * @param dateTime
	 * @throws ManagerException
	 * @author lizhiwei
	 * @return EventAlert
	 * @date 2015年10月22日
	 */
	public EventAlert queryEventAlert(String sn, String channelId, String eventID, String dateTime) throws ManagerException;

	/**
	 * 
	 * @Title: updateNotification
	 * @Description: 查询事某一设备某一时间在表中的信息
	 * @param notification
	 * @param sn
	 * @param enable
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年10月22日
	 */
	public void updateNotification(String notification, String sn, String userId, String enable) throws ManagerException;
	
	/**
	 * 
	 * @Title: queryDeviceShare
	 * @Description: 查询到家庭组中能看到此设备的用户
	 * @param notification
	 * @param sn
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2016年2月2日
	 */
	public List<Map<String, String>> queryDeviceShare(String sn) throws ManagerException;
	
	/**
	 * 
	 * @Title: queryUserCameraList
	 * @Description: 查询该设备对应的用户设备关联表信息数据
	 * @param sn
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2016年2月16日
	 */
	public List<Map<String, String>> queryUserCameraList(String sn) throws ManagerException;
	
	/**
	 * @Title: 
	 * @Description: 获取的eventAlert
	 * @param id
	 * @return
	 */
	public EventAlert queryEventAlertById(String id)throws ManagerException;

	/**
	 * 保存evertALERT
	 * @param eventAlert
	 * @throws ManagerException
	 */
	public void saveEventAlertOne(EventAlert eventAlert)throws ManagerException;
}
