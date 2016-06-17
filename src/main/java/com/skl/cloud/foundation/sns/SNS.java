package com.skl.cloud.foundation.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PlatformApplication;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.skl.cloud.foundation.sns.SampleMessageGenerator.Platform;
import com.skl.cloud.util.config.SystemConfig;

public class SNS {
	private static final Logger log = Logger.getLogger(SNS.class);
	private final AmazonSNS snsClient;
	private List<PlatformApplication> platformApplications;
	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();

	public SNS(AmazonSNS snsClient) {
		this.snsClient = snsClient;
	}

	static {
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
	}

	
	/**
	 * 根据平台推送
	 * 
	 * @param applicationName
	 *            应用名
	 * @param token
	 *            设备应用安装返回的凭证，百度为channelId + "|"+
	 *            userId,apns是deviceToken，gcm的是registrationId
	 * @param platform
	 *            推送的平台
	 * @param msg
	 *            推送的消息
	 * @throws Exception
	 */
	public void Notification(String applicationName, String token,
			Platform platform,String certificate,String privateKey, String msg) throws Exception {
		// 各个平台注册账号的账号与秘钥
		notification(platform, certificate, privateKey, token,
				applicationName, attributesMap, msg);
	}

	/**
	 * 设置百度属性
	 * @return
	 */
	private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("1"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("default-channel-msg-key"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("0"));
		return notificationAttributes;
	}

	/**
	 * 创建application推送端点
	 * 
	 * @param applicationName
	 *            应用名
	 * @param platform
	 *            通过什么平台推送
	 * @param principal
	 *            平台的秘钥
	 * @param credential
	 *            平台的秘钥
	 * @return
	 */
	private CreatePlatformApplicationResult createPlatformApplication(
			String applicationName, Platform platform, String principal,
			String credential) {
		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(platform.name());
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	/**
	 * 创建终端的端点
	 * 
	 * @param platform
	 *            平台
	 * @param customData
	 *            ？
	 * @param platformToken
	 *            验证
	 * @param applicationArn
	 *            应用推送的arn
	 * @return
	 */
	private CreatePlatformEndpointResult createPlatformEndpoint(
			Platform platform, String customData, String platformToken,
			String applicationArn) {
		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		String token = platformToken;
		String userId = null;
		
		if (platform == Platform.BAIDU) {
			String[] tokenBits = platformToken.split("\\|");
			token = tokenBits[0];
			userId = tokenBits[1];
			Map<String, String> endpointAttributes = new HashMap<String, String>();
			endpointAttributes.put("UserId", userId);
			endpointAttributes.put("ChannelId", token);
			platformEndpointRequest.setAttributes(endpointAttributes);
		}		
		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	/**
	 * 删除应用的终端
	 * 
	 * @param applicationArn
	 */
	private void deletePlatformApplication(String applicationArn) {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	/**
	 * 推送消息到设备终端
	 * 
	 * @param endpointArn
	 * @param platform
	 * @param attributesMap
	 * @return
	 */
	private PublishResult publish(String endpointArn, Platform platform,
			Map<Platform, Map<String, MessageAttributeValue>> attributesMap,
			String message) {
		PublishRequest publishRequest = new PublishRequest();
		Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
				.get(platform));
		if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
			publishRequest.setMessageAttributes(notificationAttributes);
		}
		publishRequest.setMessageStructure("json");


		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTargetArn(endpointArn);

		// Display the message that will be sent to the endpoint/
		System.out.println("{Message Body: " + message + "}");
		StringBuilder builder = new StringBuilder();
		builder.append("{Message Attributes: ");
		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			builder.append("(\"" + entry.getKey() + "\": \""
					+ entry.getValue().getStringValue() + "\"),");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		System.out.println(builder.toString());

		publishRequest.setMessage(message);
		return snsClient.publish(publishRequest);
	}

	/**
	 * 推送消息
	 * @param platform
	 * @param principal
	 * @param credential
	 * @param platformToken
	 * @param applicationName
	 * @param attrsMap
	 */
	private void notification(Platform platform, String principal,
			String credential, String platformToken, String applicationName,
			Map<Platform, Map<String, MessageAttributeValue>> attrsMap,
			String msg) {
		// Create Platform Application. This corresponds to an app on a
		// platform.
		CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(
				applicationName, platform, principal, credential);
		System.out.println(platformApplicationResult);

		// The Platform Application Arn can be used to uniquely identify the
		// Platform Application.
		String platformApplicationArn = platformApplicationResult
				.getPlatformApplicationArn();
		
		System.out.println("platformApplicationArn----:"+platformApplicationArn);

		// Create an Endpoint. This corresponds to an app on a device.
		CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				platform,
				"CustomData - Useful to store endpoint specific data",
				platformToken, platformApplicationArn);
		//设置端点可推送  
		SetEndpointAttributesRequest setEndpointAttributesRequest = new SetEndpointAttributesRequest();
		setEndpointAttributesRequest.addAttributesEntry("Enabled", "true");
		setEndpointAttributesRequest.setEndpointArn(platformEndpointResult.getEndpointArn());
		snsClient.setEndpointAttributes(setEndpointAttributesRequest);
		
		System.out.println(platformEndpointResult);
		// Publish a push notification to an Endpoint.
		PublishResult publishResult = publish(
				platformEndpointResult.getEndpointArn(), platform, attrsMap,
				msg);
		System.out.println("Published! \n{MessageId="
				+ publishResult.getMessageId() + "}");
		// Delete the Platform Application since we will no longer be using it.
		// deletePlatformApplication(platformApplicationArn);
	}

	/**
	 * 分装消息
	 * 
	 * @param platform
	 * @return
	 */
	private String getPlatformSampleMessage(Platform platform, Map<String, Object> message) {
		switch (platform) {
		case APNS:
			return SampleMessageGenerator.getSampleAppleMessage(message);
		case APNS_SANDBOX:
			return SampleMessageGenerator.getSampleAppleMessage(message);
		case GCM:
			return SampleMessageGenerator.getSampleAndroidMessage(message);
		case BAIDU:
			return SampleMessageGenerator.getSampleBaiduMessage(message);
		default:
			throw new IllegalArgumentException("Platform not supported : "
					+ platform.name());
		}
	}

	/**
	 * 设置应用推送端点属性
	 * 
	 * @param notificationAttributes
	 * @return
	 */
	public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
			Map<String, MessageAttributeValue> notificationAttributes) {
		Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();

		if (notificationAttributes == null)
			return validAttributes;

		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			if (!StringUtils.isBlank(entry.getValue().getStringValue())) {
				validAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return validAttributes;
	}

}
