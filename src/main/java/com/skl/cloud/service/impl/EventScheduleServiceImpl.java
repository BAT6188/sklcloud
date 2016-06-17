package com.skl.cloud.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.controller.web.dto.DetectionFO;
import com.skl.cloud.dao.EventScheduleMapper;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.SubsysAddress;
import com.skl.cloud.model.TriggerVideo;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.sub.SubsysStreamStatus;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.service.EventScheduleService;
import com.skl.cloud.service.SnsService;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.RabbitMqPublishService;
import com.skl.cloud.service.sub.SubsysStreamStatusService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.service.user.WechatUserService;
import com.skl.cloud.util.XmlNodeUtil;
import com.skl.cloud.util.common.HttpClientUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants.ServerSystemId;
import com.skl.cloud.util.constants.Constants.ipcModelType;
import com.skl.cloud.util.constants.EventId;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: EventScheduleServiceImpl
 * @Description: Event Schedule Copyright: Copyright (c) 2015
 *               Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月20日
 * @version V1.0
 */
@Service("eventScheduleService")
public class EventScheduleServiceImpl implements EventScheduleService {

    private static final Logger logger = Logger.getLogger(EventScheduleServiceImpl.class);
    
	@Autowired
    private EventScheduleMapper eventScheduleMapper;

    @Autowired
    private IPCameraService ipcService;
    
    @Autowired
    private StreamResourcesService streamResourcesService;
    
    @Autowired
    private AppUserService userService;
    
    @Autowired
    private WechatUserService wcUserService;
    
    @Autowired
	private SubsysStreamStatusService subsysStreamStatusService;
    
    @Autowired
    private RabbitMqPublishService mqService;
    
    private TriggerVideo triggerVideo;
    private  ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(10);
    
    /**
     * <p>Title: queryTriggerVideo</p> <p>Description:
     * 查询IPC相关流服务子系统及录制时间提前量等信息</p>
     * @param type
     * @return TriggerVideo
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#queryTriggerVideo(java.util
     * .List)
     */
    @Override
    @Transactional(readOnly = true)
    public TriggerVideo queryTriggerVideo(String type) throws ManagerException {
        return eventScheduleMapper.queryTriggerVideo(type);
    }

   

    /**
     * <p>Title: updateEventVideo</p> <p>Description: 业控更新录像触发事件的状态等信息</p>
     * @param map
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#updateEventVideo(java.util
     * .List)
     */
    @Override
    @Transactional
    public void updateEventVideo(Map<String, String> map) throws ManagerException {
        eventScheduleMapper.updateEventVideo(map);
    }

    /**
     * <p>Title: updateNotification</p> <p>Description: 查询事某一设备某一时间在表中的信息</p>
     * @param notification
     * @param sn
     * @param enable
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#updateNotification(java.util
     * .List)
     */
    @Transactional
    public void updateNotification(String notification, String sn, String userId, String enable)
            throws ManagerException {
        eventScheduleMapper.updateNotification(notification, sn, userId, enable);
    }

    /**
     * <p>Title: queryDeviceShare</p> <p>Description: 查询到家庭组中能看到此设备的用户</p>
     * @param sn
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#queryDeviceShare(java.util.List)
     */
    @Transactional(readOnly = true)
	public List<Map<String, String>> queryDeviceShare(String sn) throws ManagerException
	{
		return eventScheduleMapper.queryDeviceShare(sn);
	}
	
	 /**
     * <p>Title: queryUserCameraList</p> <p>Description: 查询该设备对应的用户设备关联表信息数据</p>
     * @param sn
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#queryUserCameraList(java.util.List)
     */
    @Transactional(readOnly = true)
	public List<Map<String, String>> queryUserCameraList(String sn) throws ManagerException
	{
		return eventScheduleMapper.queryUserCameraList(sn);
	}
	
	 /**
     * <p>Title: queryEventAlert</p> <p>Description: 查询事某一设备某一时间在表中的信息</p>
     * @param sn
     * @param channelId
     * @param eventID
     * @param dateTime
     * @author lizhiwei
     * @throws ManagerException
     * @see
     * com.skl.cloud.service.EventScheduleService#queryEventAlert(java.util.
     * List)
     */
    @Transactional(readOnly = true)
	public EventAlert queryEventAlert(String sn, String channelId, String eventID, String dateTime) throws ManagerException
	{
		return eventScheduleMapper.queryEventAlert(sn, channelId, eventID, dateTime);
	}

	/**
	 * @Title: recordingVideo
	 * @Description: 查询并判断IPC相关流服务子系统及录制时间提前量等信息是否完整
	 * @return boolean
	 * @author lizhiwei
	 * @date 2015年9月23日
	 */
    @Transactional
	public boolean searchTriggerVideo()
	{
		try
		{
			String type = "1";
			triggerVideo = eventScheduleMapper.queryTriggerVideo(type);
			if (triggerVideo != null && triggerVideo.getTime_variation() != null && !"".equals(triggerVideo.getTime_variation()))
			{
				logger.debug("true : IPC 相关流服务子系统及录制时间提前量等信息是完整的。");
				return true;
			} else
			{
				logger.debug("false : 录制时间提前的信息不完整，请核查。");
				return false;
			}
		} catch (Exception e)
		{
			logger.error("查询时间提前量信息失败。", e);
			return false;
		}
	}

	/**
	 * @Title: recordingVideo
	 * @Description: HLS录制视频/RTSP录制视频
	 * @param sn
	 * @param count
	 * @param EventID
	 * @param dateTime
	 * @return String
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年9月15日
	 */
    @Transactional
	public boolean recordingVideo(String sn, int count, String EventID, String dateTime)
	{
		SubsysStreamStatus subsysStreamStatus = new SubsysStreamStatus();

		// 查询sn对应下的直播流处理信息
		try
		{
			subsysStreamStatus.setCameraSn(sn);
			subsysStreamStatus.setServerId(ServerSystemId.SERVER_LIVE_DISPOSE.getId());
			List<SubsysStreamStatus> list = subsysStreamStatusService.select(subsysStreamStatus);
			if (list != null && !list.isEmpty()) {
				subsysStreamStatus = list.get(0);
			}
		} catch (Exception e)
		{
			logger.error("查询直播流信息失败", e);
			return false;
		}

		try
		{
			// 直播流录制请求
			if (subsysStreamStatus != null && subsysStreamStatus.getServerUuid() != null)
			{
				String uuid = subsysStreamStatus.getServerUuid();
				SubsysAddress subsysaddress = streamResourcesService.querySubsysIpPort(uuid);
				if (subsysaddress != null && subsysaddress.getIp() != null && subsysaddress.getPort() != null)
				{
					String rtpIp = subsysaddress.getIp();
					String rtpPort = subsysaddress.getPort();
					String sendHlsUrl = "http://" + rtpIp + ":" + rtpPort + "/skl-cloud/cloud/stream/live/record";
					return startRecordVideo(sn, count, EventID, sendHlsUrl, dateTime);
				} else
				{
					logger.error("查询数据库直播流处理信息出错。");
					return false;
				}
			} else
			{
				logger.info("查询该设备无对应的流处理信息。");
				return false;
			}
		} catch (Exception e)
		{
			logger.error("直播流处理信息数据出错，请核查。", e);
			return false;
		}
	}

	/**
	 * @Title: 收到的参数不满足并且计数小于3时，循环执行
	 * @param sn
	 * @param count
	 * @param EventID
	 * @author lizhiwei
	 * @date 2015年9月23日
	 */
    @Transactional
	public boolean counterExe(String sn, int count, String EventID, String dateTime)
	{
		if (count < 2)
		{
			if (!searchTriggerVideo())
			{
				return false;
			}
			count++;
			return recordingVideo(sn, count, EventID, dateTime);
		} else
		{
			LoggerUtil.error("计数器大于2，记录异常。", this.getClass());
			return false;
		}
	}

	/**
	 * @Title: 业控给对应子系统发送指令和报文信息
	 * @param sn
	 * @param EventID
	 * @param sendUrl
	 * @param dateTime
	 * @author lizhiwei
	 * @throws Exception
	 * @date 2015年9月23日
	 */
    @Transactional
	public boolean startRecordVideo(String sn, int count, String EventID, String sendUrl, String dateTime) throws Exception
	{

		// 获取接入子系统接口的消息体各元素的值
		Map<String, String> streamMap = assembleXml(sn, count, EventID, dateTime);

		// 组装XML文件
		String streamXml = checkoutXml(streamMap);
		InputStream inputStream = null;
		try
		{
			inputStream = HttpClientUtil.httpPostBackgroundSubsystem(streamXml, sendUrl);
			if (inputStream == null)
			{
				logger.info("子系统返回的流为空，请检查子系统。");
			}
		} catch (Exception e)
		{
			logger.error("请检查子系统信息。", e);
			return false;
		}

		if (contrastBody(inputStream, count, sn, EventID, dateTime))
		{
			logger.info("接入子系统返回的信息正确。");
			return true;
		}

		return false;
	}

	// 业控收到子系统返回的状态，开始更新表中 file_name、event_status等字段
	private boolean setEventVideo(String sn, String EventID, String dateTime)
	{
		// 业控更新录像触发事件的状态等信息
		HashMap<String, String> mapevent = new HashMap<String, String>();
		String date = dateTime.replaceAll("\\-|\\:|\\s", "");
		String channelId = "MAIN";
		String fileName = sn + "_" + channelId + "_" + EventID + "_" + date + ".mp4";
		String eventStatus = "2";
		mapevent.put("sn", sn);
		mapevent.put("channelId", channelId);
		mapevent.put("fileName", fileName);
		mapevent.put("eventStatus", eventStatus);
		mapevent.put("dateTime", dateTime);

		try
		{
			this.updateEventVideo(mapevent);
		} catch (Exception e)
		{
			logger.error("业控更新file_name、event_status等信息异常。", e);
			return false;
		}

		return true;
	}

	// 判断接入子系统返回的报文信息
	private boolean contrastBody(InputStream inputStream, int count, String sn, String EventID, String dateTime)
	{
		String resultFromStream = XmlUtil.isChangeToStr(inputStream);
		logger.info("the result from is" + resultFromStream);
		int statusPrefixIndex = resultFromStream.indexOf("<status>");
		int statusSuffixIndex = resultFromStream.indexOf("</status>");
		String status = resultFromStream.substring(statusPrefixIndex + "<status>".length(), statusSuffixIndex);
		LoggerUtil.info("接入处理子系统返回的报文为" + resultFromStream, this.getClass());
		if ("0".equals(status))
		{
			logger.info("true : 接入处理子系统返回正确，开始更新数据信息。");
			return setEventVideo(sn, EventID, dateTime);
		} else
		{
			logger.info("false ： 接入处理子系统返回的status不正确");
			return counterExe(sn, count, EventID, dateTime);
		}
	}

	private String checkoutXml(Map<String, String> streamMap)
	{
		String streamXml = "";
		try
		{
			streamXml = XmlNodeUtil.createXml(streamMap);
		} catch (Exception e)
		{
			LoggerUtil.error("组装Xml文件出错。", e.getMessage());
		}
		return streamXml;
	}

	/**
	 * 获取接入子系统接口的消息体各元素的值
	 * 
	 * @param sn
	 * @param EventID
	 * @return
	 */
	private Map<String, String> assembleXml(String sn, int count, String EventID, String dateTime)
	{
		int total_time = Integer.valueOf(triggerVideo.getTotal_record_time());
		int time_variation = Integer.valueOf(triggerVideo.getTime_variation());
		String recordIntervalTime = String.valueOf((total_time - time_variation - count) * 1000);
		String prevIntervalTime = String.valueOf((time_variation + count) * 1000);
		String date = dateTime.replaceAll("\\-|\\:|\\s", "");
		String eventName = sn + "_" + "MAIN" + "_" + EventID + "_" + date + ".mp4";
		Map<String, String> streamMap = new LinkedHashMap<String, String>();
		streamMap.put("action", "startRecording");
		streamMap.put("recordingOptions", "version");
		streamMap.put("sn", sn);
		streamMap.put("channelId", "MAIN");
		streamMap.put("eventName", eventName);
		streamMap.put("recordIntervalTime", recordIntervalTime);
		streamMap.put("prevIntervalTime", prevIntervalTime);
		streamMap.put("nextIntervalTime", "0");
		return streamMap;
	}

	/**
	 * 事件推送
	 * 
	 * @param sn
	 * @param EventID
	 * @param dateTime
	 * @param list
	 * @param model
	 * @return
	 */
	@Transactional
	public void addAlarmPush(final String sn, final String EventID, final String dateTime, final List<EventAlert> list, final String model)
	{
		exec.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					pushAlarmExtend(sn, EventID, dateTime, list, model);

				} catch (Exception e)
				{
					// 推送告警信息失败
					logger.error("推送告警信息失败。", e);
				}
			}
		}, 0, TimeUnit.SECONDS);
	}

	/**
	 * 触发录制视频
	 * 
	 * @param sn
	 * @param EventID
	 * @param dateTime
	 * @param list
	 * @param model
	 * @return
	 */
	@Transactional
	public void addRecordVideo(final String sn, final String EventID, final String dateTime, final List<EventAlert> list, final String model)
	{
		exec.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if ("MotionDetection".equals(EventID) || "SoundDetection".equals(EventID) || "ActivityDetection".equals(EventID))
					 {
						try
							{
								int count = 0;

								// 查询并判断IPC相关流服务子系统及录制时间提前量等信息是否完整
								if (!searchTriggerVideo())
								{
									logger.warn("查询并判断IPC相关流服务子系统及录制时间提前量等信息失败。");
								}

								// 通过查询数据库判断发送的视频录制请求是HLS类型还是RTSP类型
								if (!recordingVideo(sn, count, EventID, dateTime))
								{

									logger.warn("录制请求失败。");
								}

							} catch (Exception e)
							{
								logger.warn("录制请求失败。");
							}
						}
				} catch (Exception e)
				{
					// 查询告警消息失败
					logger.error("查询告警信息失败。", e);
				}
			}
		}, 0, TimeUnit.SECONDS);
	}

	/**
	 * 告警事件推送
	 * 
	 * @Title:
	 * @author lizhiwei
	 * @date 2016年1月13日 下午3:18:25
	 */
	@Transactional
	public void pushAlarmExtend(String sn, String EventID, String dateTime, List<EventAlert> list, String model)
	{
		{
			if (ipcModelType.isHPC03IPC(model))
			{
				List<Map<String, String>> userCameraList = queryUserCameraList(sn);

				if (userCameraList == null || userCameraList.size() == 0)
				{
					throw new BusinessException("0x50020040");
				}
				for (Map<String, String> map : userCameraList)
				{
					String userId = String.valueOf(map.get("userId"));
					String notification = String.valueOf(map.get("notification"));
					String enable = String.valueOf(map.get("enable"));

					if (!"true".equals(notification))
					{
						LoggerUtil.info("用户[" + userId + "]无通知权限，不调用SNS接口!", "用户[" + userId + "]无通知权限，不调用SNS接口!");
						continue;
					}

					if (!"true".equals(enable))
					{
						LoggerUtil.info("用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!", "用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!");
						continue;
					}

					IPCamera ipCamera = userService.getUserBindDevice(Long.valueOf(userId), sn);
					AppUser appUser = userService.getUserById(Long.valueOf(userId));
					SnsService snsService = BeanLocator.getBean(SnsService.class);
					LoggerUtil.info("将对应事件发送至推送服务器!", "eventId:" + EventID + "将对应事件发送至推送服务器!");
					for (int i = 0; i < list.size(); i++)
					{
						snsService.Notification(appUser, ipCamera, list.get(i));
					}
				}

			} else if (ipcModelType.isHPC03BIPC(model))
			{
				List<Map<String, String>> userCameraList = queryUserCameraList(sn);

				if (userCameraList == null || userCameraList.size() == 0)
				{
					throw new BusinessException("0x50020040");
				}
				for (Map<String, String> map : userCameraList)
				{
					String userId = String.valueOf(map.get("userId"));
					String notification = String.valueOf(map.get("notification"));
					String enable = String.valueOf(map.get("enable"));

					if (!"true".equals(notification))
					{
						LoggerUtil.info("用户[" + userId + "]无通知权限!", "用户[" + userId + "]无通知权限!");
						continue;
					}
					if (!"true".equals(enable))
					{
						LoggerUtil.info("用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!", "用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!");
						continue;
					}

					

					IPCamera ipCamera = userService.getUserBindDevice(Long.valueOf(userId), sn);
					AppUser appUser = userService.getUserById(Long.valueOf(userId));
					SnsService snsService = BeanLocator.getBean(SnsService.class);
					LoggerUtil.info("将对应事件发送至推送服务器!", "eventId:" + EventID + "将对应事件发送至推送服务器!");
					for (int i = 0; i < list.size(); i++)
					{
						snsService.Notification(appUser, ipCamera, list.get(i));
					}

					//将告警信息推送到前置服务器，前置服务器再推到微信用户
					WechatUser user = wcUserService.getUserByBindSn(sn);
					if (user != null )
					{
						//推送到前置服务器
						sendNotificationToFront(user.getOpenId(), ipCamera, list);
					}
				}
			} else
			{
				List<Map<String, String>> appDeviceShareList = queryDeviceShare(sn);

				if (appDeviceShareList == null || appDeviceShareList.size() == 0)
				{
					throw new BusinessException("0x50020042");
				}
				for (Map<String, String> map : appDeviceShareList)
				{
					String circleId = String.valueOf(map.get("circleId"));
					String userId = String.valueOf(map.get("userId"));
					String deviceStatus = String.valueOf(map.get("deviceStatus"));
					String notificationEnable = String.valueOf(map.get("notificationEnable"));
					AppUser appUser = userService.getUserById(Long.valueOf(userId));

					// 业务逻辑过滤
					if ("true".equals(deviceStatus) && "false".equals(notificationEnable))
					{
						LoggerUtil.info("用户[" + userId + "]是被分享者且分享使能为false!", "用户[" + userId + "]是被分享者且分享使能为false!");
						continue;
					}
					if (appUser == null)
					{
						LoggerUtil.info("用户[" + userId + "]不存在!", "用户[" + userId + "]不存在!");
						continue;
					}
					if (!"true".equals(appUser.getNotification()))
					{
						LoggerUtil.info("家庭组[" + circleId + "]中用户[" + userId + "]无通知权限!", "家庭组[" + circleId + "]中用户[" + userId + "]无通知权限!");
						continue;
					}
					UserCamera userCamera = userService.getUserCamera(Long.valueOf(userId), sn);

					if (userCamera == null)
					{
						LoggerUtil.info("用户[" + userId + "]与设备[" + sn + "]未关联!", "用户[" + userId + "]与设备[" + sn + "]未关联!");
						continue;
					}
					if (!"true".equals(userCamera.getEnable()))
					{
						LoggerUtil.info("用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!", "用户[" + userId + "]与设备[" + sn + "]关联的enable不为true!");
						continue;
					}
					if (userCamera.getUserNotification().isEmpty())
					{
						LoggerUtil.info("用户[" + userId + "]与设备[" + sn + "]关联的UserNotification为空!", "用户[" + userId + "]与设备[" + sn + "]关联的UserNotification为空!");
						continue;
					}
					// 将数据库查询到的字符串转换为字符串数组
					String[] arryNf = userCamera.getUserNotification().split(",");
					// 将对应事件发送至推送服务器
					if (isNotification(arryNf, EventID))
					{
						IPCamera ipCamera = userService.getUserBindDevice(Long.valueOf(userId), sn);
						SnsService snsService = BeanLocator.getBean(SnsService.class);
						LoggerUtil.info("将对应事件发送至推送服务器!", "eventId:" + EventID + "将对应事件发送至推送服务器!");
						for (int i = 0; i < list.size(); i++)
						{
							snsService.Notification(appUser, ipCamera, list.get(i));
						}
					} else
					{
						logger.info("eventId:" + EventID + "不推送");
					}
				}
			}
		}
	}
	

	private boolean isNotification(String[] arryNf, String EventID)
	{
		if (arryNf == null)
		{
			return false;
		}
		for (EventId event : EventId.values())
		{
			if (event.getDescription().equals(EventID))
			{
				return arryNf[event.getIndex()].contains("1") ? true : false;
			}
		}
		return false;
	}


	@Override
	@Transactional
	public void saveEventAlert(EventAlert eventAlert) throws ManagerException {
		eventScheduleMapper.saveEventAlertOne(eventAlert);
	}
	
	@Override
	@Transactional(readOnly = true)
	public EventAlert queryEventById(String id) throws ManagerException {
		return eventScheduleMapper.queryEventAlertById(id);
	}
	
	/**
	 * 把告警消息发送到前置子系统去通知微信用户。
	 * @param ipc
	 * @param eventAlerts
	 */
    private void sendNotificationToFront(String openId, IPCamera ipcamera, List<EventAlert> eventAlerts) {
        if (ipcamera != null) {
           
        	Integer alertFlag = 1;
            for (EventAlert eventAlert : eventAlerts) {
            	//默认推送
            	Integer motionAlertStatus = ipcamera.getMotionAlertStatus() == null ? 1:ipcamera.getMotionAlertStatus();
            	Integer soundAlertStatus = ipcamera.getSoundAlertStatus() == null ? 1:ipcamera.getSoundAlertStatus();
                // 使能打开而且是微信存在的相关告警，则发送MQ到前置
                if (("MotionDetection".equals(eventAlert.getEventId()) && motionAlertStatus == alertFlag)
                        || ("AudioDetection".equals(eventAlert.getEventId()) && soundAlertStatus == alertFlag)) {
                    DetectionFO fo = new DetectionFO();
                    fo.setOpenId(openId);
                    fo.setUrl(eventAlert.getPhotoUrl());
                    fo.setType("MotionDetection".equals(eventAlert.getEventId()) ? 0 : 1);
                    fo.setDevicename(ipcamera.getNickname());
                    fo.setAlerttime(eventAlert.getDateTime());
                    mqService.sendIPCDetectionToFrontQueue(fo);
                    LoggerUtil.info("将ipc移动侦测或声音侦测的告警信息推送至wechat前置服务器", "推送到微信用户");
                }else {
                	LoggerUtil.info("移动侦测或声音侦测使能未打开", "不推送到微信用户");
				}
                
            }
        }
    }
}
