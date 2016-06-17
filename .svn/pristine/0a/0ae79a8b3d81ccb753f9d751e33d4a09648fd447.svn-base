package com.skl.cloud.service.feed;

import java.util.List;
import java.util.Map;

import com.skl.cloud.model.feed.DeviceDynamic;
import com.skl.cloud.model.feed.DevicesInfo;
import com.skl.cloud.model.feed.EventsInfo;
import com.skl.cloud.model.feed.UsersInfo;

public interface AppFeedService {

	// App设置过滤条件查询上报事件（告警）信息
	public List<EventsInfo> queryEventsByUserId(Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoByUserId(Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoByUserIdAndTimeLimited(Map<String, Object> eventMap);
	
	public List<DevicesInfo> queryDeviceInfoByUserIdAndTimeLimited(Map<String, Object> eventMap);
	
	public List<DevicesInfo> queryDeviceInfoByUserId(Map<String, Object> eventMap);

	public List<EventsInfo> queryLatest50EventsByUserId(
			Map<String, Object> eventMap);

	public List<EventsInfo> queryEventsBySn(Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoBySn(Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoBySnAndTimeLimited(Map<String, Object> eventMap);
	
	public List<DevicesInfo> queryDeviceInfoBySn(Map<String, Object> eventMap);

	public List<DevicesInfo> queryDeviceInfoBySnAndTimeLimited(Map<String, Object> eventMap);
	
	public List<EventsInfo> queryLatest50EventsBySn(Map<String, Object> eventMap);

	public List<EventsInfo> queryEventsByCircleId(Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoByCircleIdAndTimeLimited(
			Map<String, Object> eventMap);

	public List<UsersInfo> queryUserInfoByCircleId(Map<String, Object> eventMap);

	public List<DevicesInfo> queryDeviceInfoByCircleId(
			Map<String, Object> eventMap);

	public List<DevicesInfo> queryDeviceInfoByCircleIdAndTimeLimited(
			Map<String, Object> eventMap);

	public List<EventsInfo> queryLatest50EventsByCircleId(
			Map<String, Object> eventMap);

	public List<EventsInfo> queryRelativeCircleEventsByUserId(
			Map<String, Object> eventMap);

	public List<UsersInfo> queryRelativeCircleUserInfoByUserId(
			Map<String, Object> eventMap);

	public List<DevicesInfo> queryRelativeCircleDeviceInfoByUserId(
			Map<String, Object> eventMap);

	public List<EventsInfo> queryLatest50RelativeCircleEventsByUserId(
			Map<String, Object> eventMap);

	public List<DeviceDynamic> querySensorDynamicParamsBySn(
			Map<String, String> dynamicMap);

	public List<UsersInfo> queryRelativeCircleUserInfoByUserIdAndTimeLimited(
			Map<String, Object> map);

	public List<DevicesInfo> queryRelativeCircleDeviceInfoByUserIdAndTimeLimited(
			Map<String, Object> map);

	public List<DeviceDynamic> querySensorDynamicInfoBySnAndTime(
			Map<String, String> dynamicMap);

	public List<Long> queryAllCircleIdsByUserId(long userId);

	public List<Long> queryAllUserIdsByCircleId(long circleId);

	public List<String> queryAllSnByUserId(Map<String, Object> map);
}
