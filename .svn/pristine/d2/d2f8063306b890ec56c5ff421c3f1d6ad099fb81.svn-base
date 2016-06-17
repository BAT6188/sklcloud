package com.skl.cloud.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.foundation.sns.SNS;
import com.skl.cloud.foundation.sns.SNSFactory;
import com.skl.cloud.foundation.sns.SampleMessageGenerator;
import com.skl.cloud.foundation.sns.SampleMessageGenerator.Platform;
import com.skl.cloud.foundation.sns.dto.MessageTemplet;
import com.skl.cloud.foundation.sns.model.Message;
import com.skl.cloud.model.AppSnsInfo;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.AppSnsInfoService;
import com.skl.cloud.service.SnsService;

@Service
public class SnsServiceImpl implements SnsService {
	private static final Logger log = Logger.getLogger(SnsServiceImpl.class);
	@Autowired
	private AppSnsInfoService appSnsInfoService;
	
	@Override
	public void Notification() {
	}

	@Override
	@Transactional
	public void Notification(AppUser appUser, IPCamera ipCamera,
			EventAlert eventAlert) {
		//判断参数
		if (eventAlert != null && appUser != null
				&& ipCamera != null) {
			try {
				//获取token和应用名
				String model = ipCamera.getModel(), system = appUser.getSystemType();
				String token = appUser.getPushToken(), applicationName = model + system;
				if (StringUtils.isBlank(token) || StringUtils.isBlank(system)) {
					log.warn("sns Notification is fail ,token is " + token + ",system is " + system);
					return;
				}
				SNS sns = SNSFactory.getDefault();
				Map<String, Object> msg = new HashMap<String, Object>();
				AppSnsInfo appSnsInfo = appSnsInfoService.getSnsInfoByModelSystem(model, system);
				//获得对应的平台
				Platform platform = getPlatform(appSnsInfo);
				msg.put(platform.name(), createMessage(ipCamera, eventAlert,platform));
				sns.Notification(applicationName, token, platform,appSnsInfo.getApiKey(),appSnsInfo.getSecretKey(), SampleMessageGenerator.jsonify(msg));
			} catch (AmazonServiceException ase) {
				log.error(ase);
				log.error("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon SNS, but was rejected with an error response for some reason."+"Error Message:    " + ase.getMessage()+"HTTP Status Code: " + ase.getStatusCode()+"AWS Error Code:   " + ase.getErrorCode()+"Error Type:       " + ase.getErrorType()+"Request ID:       " + ase.getRequestId());
			} catch (AmazonClientException ace) {
				log.error("Caught an AmazonClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with SNS, such as not "
						+ "being able to access the network.", ace);
			} catch (BusinessException e){
				log.error(e.getErrMsg(), e);
			} catch (Exception e) {
				log.error("推送失败,msg: "+e.getMessage(), e);
			}

		}
	}
	
	@Override
	@Transactional
	public void Notification(AppUser appUser, IPCamera ipCamera,AppSnsInfo appSnsInfo, Message message) {
		//判断参数
		if (appSnsInfo != null && appUser != null
			&&ipCamera != null) {
			try {
				//获取token和应用名
				String model =ipCamera.getModel(),system=appUser.getSystemType();
				String token = appUser.getPushToken(), applicationName = model+system;
				if (StringUtils.isBlank(token) || StringUtils.isBlank(system)) {
					log.warn("sns Notification is fail ,token is " + token + ",system is " + system);
					return;
				}
				SNS sns = SNSFactory.getDefault();
				Map<String, Object> map = new HashMap<String, Object>();
				//获得对应的平台
				Platform platform = getPlatform(appSnsInfo);
				map.put(platform.name(), createMessage(platform,message));
				sns.Notification(applicationName, token, platform,appSnsInfo.getApiKey(),appSnsInfo.getSecretKey(), SampleMessageGenerator.jsonify(map));
			} catch (AmazonServiceException ase) {
				log.error(ase);
				log.error("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon SNS, but was rejected with an error response for some reason."+"Error Message:    " + ase.getMessage()+"HTTP Status Code: " + ase.getStatusCode()+"AWS Error Code:   " + ase.getErrorCode()+"Error Type:       " + ase.getErrorType()+"Request ID:       " + ase.getRequestId());
			} catch (AmazonClientException ace) {
				log.error("Caught an AmazonClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with SNS, such as not "
						+ "being able to access the network.", ace);
			} catch (BusinessException e){
				log.error(e.getErrMsg(), e);
			} catch (Exception e) {
				log.error("推送失败,msg: "+e.getMessage(), e);
			}

		}
	}
	
	@Override
	@Transactional
	public void Notification(List<AppUser> list, IPCamera ipCamera,
			EventAlert eventAlert) {
		//判断参数
		if (eventAlert != null && list != null && list.size()>0
				&& ipCamera != null) {
			try {
				//获取token和应用名
				for (int i = 0; i < list.size(); i++) {
					AppUser appUser = list.get(i);
					Notification(appUser, ipCamera,eventAlert);
				}
			} catch (Exception e) {
				log.error("推送失败,msg: "+e.getMessage(), e);
			}

		}
	}

	@Override
	@Transactional
	public void Notification(List<AppUser> list,IPCamera ipCamera, AppSnsInfo appSnsInfo, Message message) {
		//判断参数
		if (appSnsInfo != null && list != null && list.size()>0
				) {
			try {
				//获取token和应用名
				for (int i = 0; i < list.size(); i++) {
					AppUser appUser = list.get(i);
					Notification(appUser,ipCamera,appSnsInfo,message);
				}
			} catch (Exception e) {
				log.error("推送失败,msg: "+e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 获取通过什么平台发送  
	 * @param appUser
	 * @param ipCamera
	 * @return
	 * @throws Exception 
	 */
	private Platform getPlatform(AppUser appUser, IPCamera ipCamera) throws Exception {
		// 判断用户的操作系统类型 如果为android 判断国家		
		String model = ipCamera.getModel();
		String systemType = appUser.getSystemType();
		AppSnsInfo appSnsInfo = appSnsInfoService.getSnsInfoByModelSystem(model, systemType);
		return Platform.valueOf(appSnsInfo.getPlatform());
	}
	
	/**
	 * 获取通过什么平台发送
	 * @param appSnsInfo
	 * @return
	 * @throws Exception
	 */
	private Platform getPlatform(AppSnsInfo appSnsInfo) throws Exception {
		// 判断用户的操作系统类型 如果为android 判断国家		
		return Platform.valueOf(appSnsInfo.getPlatform().toUpperCase());
	}
	
	/**
	 * 拼接消息
	 * @param ipCamera
	 * @param eventAlert
	 * @param platform
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private String createMessage(IPCamera ipCamera, EventAlert eventAlert,
			Platform platform) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		MessageTemplet object = (MessageTemplet) Class.forName(platform.getClassName()).newInstance();
		return object.toMessage(ipCamera, eventAlert);
	}
	
	/**
	 * 拼接消息
	 * @param ipCamera
	 * @param eventAlert
	 * @param platform
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private String createMessage(
			Platform platform,Message message) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		MessageTemplet object = (MessageTemplet) Class.forName(platform.getClassName()).newInstance();
		return object.toMessage(null, null,message);
	}
}
