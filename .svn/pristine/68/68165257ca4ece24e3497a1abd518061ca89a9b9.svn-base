package com.skl.cloud.foundation.sns;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.util.common.LoggerUtil;



public class SampleMessageGenerator {

	/*
	 * This message is delivered if a platform specific message is not specified
	 * for the end point. It must be set. It is received by the device as the
	 * value of the key "default".
	 */
	public static final String defaultMessage = "This is the default message";

	private static final ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 推送平台
	 * @author weibin
	 *
	 */
	public static enum Platform {
		// Apple Push Notification Service
		APNS("AppleMessage"),
		// Sandbox version of Apple Push Notification Service
		APNS_SANDBOX("AppleMessage"),
		// Amazon Device Messaging
		GCM(""),
		// Baidu CloudMessaging Service
		BAIDU("BaiduMessage");
				
		private String className;
		private String packageName = "com.skl.cloud.foundation.sns.dto";
		
		private  Platform(){
		}
		private  Platform(String name){
			this.className =name;
		}
		public String getClassName() {
			return packageName+"."+className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}	
		
	}

	public static String jsonify(Object message) {
		try {
			objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			LoggerUtil.error("转换json失败", e, "SampleMessageGenerator");
			throw new BusinessException("sns推送消息转化为json报错");
		}
	}

	public static String getSampleAppleMessage(Map<String, Object> msg) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		if(!msg.containsKey("alert")){
			throw new BusinessException("app消息没有alert信息");
		}
		msg.put("badge", 9);
		msg.put("sound", "default");
		appleMessageMap.put("aps", msg);
		return jsonify(appleMessageMap);
	}

	public static String getSampleAndroidMessage(Map<String, Object> msg) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		androidMessageMap.put("collapse_key", "Welcome");
		androidMessageMap.put("data", msg);
		androidMessageMap.put("delay_while_idle", true);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);
		return jsonify(androidMessageMap);
	}

	public static String getSampleBaiduMessage(Map<String, Object> msg) {
		return jsonify(msg);
	}

}