package com.skl.cloud.service;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.TriggerVideo;

/**
 * @Package com.skl.cloud.service
 * @Title: EventScheduleService
 * @Description: Event Schedule Copyright: Copyright (c) 2015
 *               Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月17日
 * @version V1.0
 */
public interface EventScheduleService
{
	/**
	 * 
	 * @Title: saveEventAlert
	 * @Description: 保存IPC设备上报（告警）事件
	 * @param list
	 *            :EventAlert
	 * @throws ManagerException
	 * @author leiqiang
	 * @date 2015年7月21日
	 */
	public void saveEventAlert(EventAlert eventAlert) throws ManagerException;

	/**
	 * 
	 * @Title: queryTriggerVideo
	 * @Description: 查询IPC相关流服务子系统及录制时间提前量等信息
	 * @param type
	 * @throws ManagerException
	 * @author lizhiwei
	 * @return TriggerVideo 某一种类型产品的时间提前量信息
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
	 * @date 2015年9月23日
	 */
	public void updateEventVideo(Map<String, String> map) throws ManagerException;
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
	 * @Title: AlarmPush
	 * @Description: 事件推送
	 * @param sn
	 * @param EventID
	 * @param dateTime
	 * @param list
	 * @param model
	 * @throws ManagerException                                                                                                    
	 * @author lizhiwei
	 * @date 2016年3月1日
	 */
	public void addAlarmPush(String sn, String EventID, String dateTime, List<EventAlert> list, String model) throws ManagerException;
	/**
	 * 
	 * @Title: RecordVideo
	 * @Description: 触发录制视频
	 * @param sn
	 * @param EventID
	 * @param dateTime
	 * @param list
	 * @param model
	 * @throws ManagerException                                                                                                    
	 * @author lizhiwei
	 * @date 2016年3月1日
	 */
	public void addRecordVideo(String sn, String EventID, String dateTime, List<EventAlert> list, String model) throws ManagerException;
	
    /**
	 * @Title: 
	 * @Description: 根据id获取时间
	 * @param id
	 * @throws ManagerException
	 */
	public EventAlert queryEventById(String id) throws ManagerException;
}
