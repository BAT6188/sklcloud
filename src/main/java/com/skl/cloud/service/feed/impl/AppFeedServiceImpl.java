package com.skl.cloud.service.feed.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.dao.feed.AppFeedMapper;
import com.skl.cloud.model.feed.DeviceDynamic;
import com.skl.cloud.model.feed.DevicesInfo;
import com.skl.cloud.model.feed.EventsInfo;
import com.skl.cloud.model.feed.UsersInfo;
import com.skl.cloud.service.feed.AppFeedService;

/**
 * 用于App请求的Feed 的serviceImp
  * @ClassName: AppFeedServiceImpl
  * @author wangming
  * @date 2015年11月18日
  *
 */
@Service("appFeedService")
public class AppFeedServiceImpl implements AppFeedService {

	private AppFeedMapper appFeedMapper;

	public AppFeedMapper getAppFeedMapper() {
		return appFeedMapper;
	}

	@Autowired(required = true)
	public void setAppFeedMapper(AppFeedMapper appFeedMapper) {
		this.appFeedMapper = appFeedMapper;
	}

	@Override
	@Transactional
	public List<EventsInfo> queryEventsByUserId(Map<String, Object> eventMap) {
		return appFeedMapper.queryEventsByUserId(eventMap);

	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoByUserId(Map<String, Object> eventMap) {
		return appFeedMapper.queryUserInfoByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryDeviceInfoByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryLatest50EventsByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryLatest50EventsByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryEventsBySn(Map<String, Object> eventMap) {

		return appFeedMapper.queryEventsBySn(eventMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoBySn(Map<String, Object> eventMap) {

		return appFeedMapper.queryUserInfoBySn(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoBySn(Map<String, Object> eventMap) {

		return appFeedMapper.queryDeviceInfoBySn(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryLatest50EventsBySn(Map<String, Object> eventMap) {

		return appFeedMapper.queryLatest50EventsBySn(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryEventsByCircleId(Map<String, Object> eventMap) {

		return appFeedMapper.queryEventsByCircleId(eventMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoByCircleId(Map<String, Object> eventMap) {

		return appFeedMapper.queryUserInfoByCircleId(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoByCircleId(Map<String, Object> eventMap) {

		return appFeedMapper.queryDeviceInfoByCircleId(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryLatest50EventsByCircleId(Map<String, Object> eventMap) {

		return appFeedMapper.queryLatest50EventsByCircleId(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryRelativeCircleEventsByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryRelativeCircleEventsByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryRelativeCircleUserInfoByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryRelativeCircleUserInfoByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryRelativeCircleDeviceInfoByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryRelativeCircleDeviceInfoByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<EventsInfo> queryLatest50RelativeCircleEventsByUserId(Map<String, Object> eventMap) {

		return appFeedMapper.queryLatest50RelativeCircleEventsByUserId(eventMap);
	}

	@Override
	@Transactional
	public List<DeviceDynamic> querySensorDynamicParamsBySn(Map<String, String> dynamicMap) {

		return appFeedMapper.querySensorDynamicParamsBySn(dynamicMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryRelativeCircleUserInfoByUserIdAndTimeLimited(Map<String, Object> map) {

		return appFeedMapper.queryRelativeCircleUserInfoByUserIdAndTimeLimited(map);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryRelativeCircleDeviceInfoByUserIdAndTimeLimited(Map<String, Object> map) {

		return appFeedMapper.queryRelativeCircleDeviceInfoByUserIdAndTimeLimited(map);
	}

	@Override
	@Transactional
	public List<DeviceDynamic> querySensorDynamicInfoBySnAndTime(Map<String, String> dynamicMap) {
		return appFeedMapper.querySensorDynamicInfoBySnAndTime(dynamicMap);
	}

	@Override
	@Transactional
	public List<Long> queryAllCircleIdsByUserId(long userId) {

		return appFeedMapper.queryAllCircleIdsByUserId(userId);
	}

	@Override
	@Transactional
	public List<Long> queryAllUserIdsByCircleId(long circleId) {

		return appFeedMapper.queryAllUserIdsByCircleId(circleId);
	}

	@Override
	@Transactional
	public List<String> queryAllSnByUserId(Map<String, Object> map) {

		return appFeedMapper.queryAllSnByUserId(map);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoByCircleIdAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryUserInfoByCircleIdAndTimeLimited(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoByCircleIdAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryDeviceInfoByCircleIdAndTimeLimited(eventMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoBySnAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryUserInfoBySnAndTimeLimited(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoBySnAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryDeviceInfoBySnAndTimeLimited(eventMap);
	}

	@Override
	@Transactional
	public List<UsersInfo> queryUserInfoByUserIdAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryUserInfoByUserIdAndTimeLimited(eventMap);
	}

	@Override
	@Transactional
	public List<DevicesInfo> queryDeviceInfoByUserIdAndTimeLimited(Map<String, Object> eventMap) {
		return appFeedMapper.queryDeviceInfoByUserIdAndTimeLimited(eventMap);
	}
}
