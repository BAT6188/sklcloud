package com.skl.cloud.service;

import java.util.List;

import com.skl.cloud.foundation.sns.model.Message;
import com.skl.cloud.model.AppSnsInfo;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;

public interface SnsService {

	void Notification();
	/**
	 * 对一个符合要求的用户推送
	 * @param appUser 必须要有系统的属性 国家属性
	 * @param ipCamera
	 * @param eventAlert
	 */
	void Notification(AppUser appUser, IPCamera ipCamera, EventAlert eventAlert);

	/**
	 * @param appUser 对一个符合要求的用户推送必须要有系统的属性 国家属性
	 * @param ipCamera
	 * @param eventAlert
	 * @param msg 推送的消息
	 */
	void Notification(AppUser appUser,IPCamera ipCamera, AppSnsInfo appSnsInfo,Message message);
	/**
	 * 对一群符合推送要求的用户进行推送
	 * @param list
	 * @param ipCamera  必须要model  kind  nickname
	 * @param eventAlert 必须要与时间 告警id
	 */
	void Notification(List<AppUser> list, IPCamera ipCamera,
			EventAlert eventAlert);

	/**
	 * @param list  
	 * @param ipCamera 必须要model  kind  nickname
	 * @param msg 要推送的消息
	 */
	void Notification(List<AppUser> list,IPCamera ipCamera, AppSnsInfo appSnsInfo ,Message message);

}
