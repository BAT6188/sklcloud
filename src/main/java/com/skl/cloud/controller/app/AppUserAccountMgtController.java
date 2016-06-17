package com.skl.cloud.controller.app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.misc.BASE64Decoder;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.service.AppMyCircleMgtService;
import com.skl.cloud.service.AppUserAccountMgtService;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.constants.S3ServiceType;
import com.skl.cloud.util.encrypt.AESUtil;
import com.skl.cloud.util.validator.XmlElementValidator;

/**
 * 
 * @ClassName: AppUserAccountMgtController
 * @Description: 用户相关的接口类
 * @author guangbo
 * @date 2015年6月1日
 *
 */
@RequestMapping("/skl-cloud")
@Controller
public class AppUserAccountMgtController extends BaseController {

	@Autowired
	private AppUserAccountMgtService appUserAccountMgtService;

	@Autowired
	private AppUserService userService;

	@Autowired
	private AppMyCircleMgtService appMyCircleMgtService;

	@Autowired
	private S3Service s3Service;

	/**
	 * app用户注册, 对外仅限email类型
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/app/Security/AAA/users", method = RequestMethod.POST)
	public ResponseEntity<String> insertAppUserRegister(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String email = XmlUtil.convertToString(paraMap.get("email"), false);
			String passWord = XmlUtil.convertToString(paraMap.get("passWord"), false);
			String nickName = XmlUtil.convertToString(paraMap.get("nickName"), false);
			String clientKind = XmlUtil.convertToString(paraMap.get("clientKind"), false);
			String portraitId = XmlUtil.convertToString(paraMap.get("portraitId"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), true);

			// portraitId为-1时，fileName必填
			if ("-1".equals(portraitId)) {
				fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);
			}

			// 判断此邮箱是否已经注册过
			AppUser appUser = userService.getAppUserByEmail(email);
			if (appUser != null) {
				throw new BusinessException("0x50020028");
			}

			appUser = new AppUser();
			appUser.setKind(clientKind);
			appUser.setEmail(email);
			appUser.setName(nickName);
			appUser.setPassword(passWord);
			appUser.setPortraintId(portraitId);
			appUser.setPortraintUuid(UUID.randomUUID().toString());
			appUser.setUserType(0);
			appUser.setBindFlag(0);

			sReturn = XmlUtil.mapToXmlRight("appNewUser", appUserAccountMgtService.addUserByEmail(appUser, fileName));
		} catch (Exception e) {
			sReturn = getErrorXml("appNewUser", e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * app忘记密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/app/Security/AAA/users/forgetPw", method = RequestMethod.POST)
	public ResponseEntity<String> forgetPassword(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String email = XmlUtil.convertToString(paraMap.get("email"), false);

			// 重置密码邮件
			appUserAccountMgtService.forgetPw(email);

			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * 重置密码
	 * 
	 * @param req
	 * @param resp
	 * @param UID
	 * @return
	 */
	@RequestMapping(value = "/Security/AAA/users/reset/{UID}", method = RequestMethod.GET)
	public ResponseEntity<String> reSetPassword(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String UID) {
		String sReturn = null;
		try {
			// base64解密：获取userId
			String userId = new String(new BASE64Decoder().decodeBuffer(UID));

			// base64解密：获取key(cloudRandom)，也就是邮箱中得到的云端随机数
			String cloudRandom = req.getParameter("KEY");

			// base64解密：获取password
			String password = new String(new BASE64Decoder().decodeBuffer(req.getParameter("userPassword")));

			// MD5加密：新的password
			password = AESUtil.string2MD5(password);

			int count = appUserAccountMgtService.reSetPw(userId, cloudRandom, password);
			if (count <= 0) {
				throw new BusinessException("0x50020020");
			}

			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * @Title: setAppUserInfo
	 * @Description: APP设置用户头像、昵称、notification使能
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2015年12月31日
	 */
	@RequestMapping("/app/Security/AAA/users/info/set")
	public ResponseEntity<String> setAppUserInfo(InputStream inputStream) {
		// 获取当前用户id，（diges认证通过后）
		Long userId = getUserId();
		String sReturn = null;
		String sXml = ""; // 请求报文XML字符串
		try {
			try {
				sXml = XmlUtil.isChangeToStr(inputStream);
			} catch (Exception e) {
				// 请求报文解析失败
				throw new BusinessException("0x50010026");
			}

			Document doc = null;
			try {
				doc = DocumentHelper.parseText(sXml);
			} catch (DocumentException de) {
				throw new BusinessException("0x50010020");
			}
			Element root = doc.getRootElement(); // 获取根节点
			String nickName = root.elementTextTrim("nickName");
			String portraitId = root.elementTextTrim("portraitId");
			String notification = root.elementTextTrim("notification");
			String fileName = root.elementTextTrim("fileName");

			Map<String, Object> backMap = new LinkedHashMap<String, Object>();

			backMap = userService.updateAppUserInfo(userId, nickName, portraitId, notification, fileName);

			sReturn = XmlUtil.mapToXmlRight("setAppUserInfo", backMap);
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * @Title: GetAllDevicesInfo
	 * @Description: APP获取所有设备信息
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2016年1月6日
	 */
	@RequestMapping(value = "/app/Security/AAA/users/devicesInfo")
	public ResponseEntity<String> getAllDevicesInfo(HttpServletRequest req) {
		// 获取当前用户id，（diges认证通过后）
		Long userId = getUserId();
		String sReturn = null;
		try {
			AppUser appuser = userService.getUserById(userId);

			if (appuser == null) {
				throw new BusinessException("0x50020031");
			}

			List<IPCamera> list = userService.getUserBindDeviceList(userId);

			sReturn = responseXml("0", "0", list);
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}

	/**
	 * 
	 * @Title: queryNotification
	 * @Description: APP查询云端notification使能（APP_v3.5.9_4.14）
	 * @param request
	 * @param response
	 * @return String
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年10月23日
	 */
	@RequestMapping(value = "app/device/{SN}/notifications/query", produces = "text/html;charset=UTF-8")
	public ResponseEntity<String> queryNotification(HttpServletResponse response, HttpServletRequest request,
			@PathVariable String SN) throws ManagerException {
		// 获取当前用户id，（diges认证通过后）
		String userId = String.valueOf(getUserId());
		String sReturn = null;
		String[] arryNf;
		try {
			UserCamera userCamera = userService.getUserCamera(getUserId(), SN);
			if (userCamera != null && userCamera.getUserNotification() != null) {
				// 将数据库查询到的字符串转换为字符串数组
				arryNf = userCamera.getUserNotification().split(",");

				Map<String, String> map = new HashMap<String, String>();

				Map<String, Object> backMap = new LinkedHashMap<String, Object>();
				List<Object> eventAlertNotificationList = new ArrayList<Object>();

				if (arryNf.length == 1) {
					for (Entry<String, Integer> entry : Constants.EVENT_FEATURE.entrySet()) {
						String eventId = entry.getKey();
						String execute = "false";

						Map<String, Object> eventAlertNotificationMaps = new LinkedHashMap<String, Object>();
						Map<String, Object> eventAlertNotificationMap = new LinkedHashMap<String, Object>();

						eventAlertNotificationMap.put("id", eventId);
						eventAlertNotificationMap.put("execute", execute);

						eventAlertNotificationMaps.put("eventAlertNotification", eventAlertNotificationMap);
						eventAlertNotificationList.add(eventAlertNotificationMaps);
					}

				} else {
					for (Entry<String, Integer> entry : Constants.EVENT_FEATURE.entrySet()) {
						String temp = arryNf[entry.getValue()];
						String eventId = entry.getKey();

						Map<String, Object> eventAlertNotificationMaps = new LinkedHashMap<String, Object>();
						Map<String, Object> eventAlertNotificationMap = new LinkedHashMap<String, Object>();

						eventAlertNotificationMap.put("id", eventId);
						eventAlertNotificationMap.put("execute", !"0".equals(temp) ? "true" : "false");

						if (entry.getValue() == 0 || entry.getValue() == 2 || entry.getValue() == 25) {
							if (!"0".equals(temp)) {
								List<Object> detectionRegionList = new ArrayList<Object>();
								char[] t1 = temp.toCharArray();
								for (int i = 0; i < t1.length; i++) {
									Map<String, Object> regionIDMap = new LinkedHashMap<String, Object>();
									// “true”
									if ("1".equals(String.valueOf(t1[i]))) {
										regionIDMap.put("regionId", i + 1);
										detectionRegionList.add(regionIDMap);
									}
								}
								eventAlertNotificationMap.put("detectionRegionList", detectionRegionList);
							}
						}

						eventAlertNotificationMaps.put("eventAlertNotification", eventAlertNotificationMap);

						eventAlertNotificationList.add(eventAlertNotificationMaps);
					}

				}

				backMap.put("enable", userCamera.getEnable());
				backMap.put("eventAlertNotificationList", eventAlertNotificationList);

				sReturn = XmlUtil.mapToXmlRight("notifications", backMap);
			} else {
				sReturn = XmlUtil.mapToXmlError("0x50020040");
			}
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}

	/**
	 * 
	 * @Title: setNotification
	 * @Description: 设置设备在云端的notification使能（APP_v3.5.9_4.17）
	 * @param request
	 * @param response
	 * @return String
	 * @throws ManagerException
	 * @author lizhiwei
	 * @date 2015年10月20日
	 */
	@RequestMapping(value = "/app/device/{SN}/notifications/set", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public ResponseEntity<String> setNotification(HttpServletResponse response, HttpServletRequest request,
			@PathVariable String SN) throws ManagerException {
		// 获取当前用户id，（diges认证通过后）
		String userId = String.valueOf(getUserId());
		int max = 16;
		String sReturn = null;
		String renotifications = null;

		try {
			// 校验：SN格式合法
			if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("SN"), SN)) {
				sReturn = XmlUtil.mapToXmlError("0x50000033");
				return new ResponseEntity<String>(sReturn, HttpStatus.OK);
			}

			UserCamera userCamera = userService.getUserCamera(getUserId(), SN);
			if (userCamera == null) {
				throw new BusinessException("0x50020040");
			}
			String[] arryInit = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
					"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };

			String notifications = userCamera.getUserNotification();
			// 事件默认值（暂定共32个事件，默认全部不推送）
			if (notifications != null && !notifications.isEmpty() && !"0".equals(notifications)) {
				arryInit = notifications.split(",");
			}

			// 根据请求返回参数map
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(request);
			String enable = (String) paraMap.get("enable");
			// 校验：enable格式合法
			if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("enable"), enable)) {
				sReturn = XmlUtil.mapToXmlError("0x50000033");
				return new ResponseEntity<String>(sReturn, HttpStatus.OK);
			}
			String eventId = null;
			String execute = null;
			for (Object eventNotification : (List) paraMap.get("eventAlertNotificationList")) {
				Map<String, Object> notificationMap = (Map<String, Object>) ((Map<String, Object>) eventNotification)
						.get("eventAlertNotification");

				eventId = String.valueOf(notificationMap.get("id"));
				execute = String.valueOf(notificationMap.get("execute"));
				int index = Constants.EVENT_FEATURE.get(eventId);
				arryInit[index] = execute.equals("true") ? "1" : "0";

				String reStr = null;
				Object detectionRegionListObj = notificationMap.get("detectionRegionList");
				if (detectionRegionListObj != null) {
					char[] chArr = arrayInit(execute, max);
					for (Object detObj : (List) detectionRegionListObj) {
						Map<String, Object> detMap = (Map<String, Object>) detObj;
						int regionID = Integer.valueOf(String.valueOf(detMap.get("regionId")));
						chArr[regionID - 1] = execute.equals("true") ? '1' : '0';
					}

					reStr = String.valueOf(chArr).replace(",", "");
					reStr = reStr.substring(0, reStr.lastIndexOf("1") + 1);

					if (reStr != null) {
						arryInit[index] = reStr;
					}
				}
			}
			renotifications = (Arrays.toString(arryInit)).replaceAll("\\[|\\]|\\s+", "");
			// update userCamera
			userCamera.setUserNotification(renotifications);
			userCamera.setEnable(enable);
			userService.updateUserCamera(userCamera);

			sReturn = XmlUtil.mapToXmlRight();

		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	// 包含区域事件的excute为true
	public static char[] arrayInit(String flag, int max) {
		char value = "true".equals(flag) ? '0' : '1';
		char[] str = new char[max];
		for (int i = 0; i < max; i++) {
			str[i] = value;
		}
		return str;
	}

	// 组装<APP获取所有设备信息>的报文
	private String responseXml(String status, String statusString, List<IPCamera> list) {
		StringBuffer sb = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<devicesInfo version=\"1.0\" xmlns=\"urn:skylight\">");
		sb.append("<deviceInfoList>");
		if (list != null) {
			for (IPCamera ipcamera : list) {
				sb.append("<deviceInfo>");
				sb.append("<SN>" + ipcamera.getSn() + "</SN>");
				sb.append("<deviceModel>" + ipcamera.getModel() + "</deviceModel>");
				sb.append("<deviceKind>" + ipcamera.getKind() + "</deviceKind>");
				sb.append("<deviceName>" + ipcamera.getNickname() + "</deviceName>");
				sb.append("<onLineStatus>" + (Boolean.TRUE.equals(ipcamera.getIsLive()) ? "1" : "0")
						+ "</onLineStatus>");
				sb.append("<timeZone>" + ipcamera.getTimeZone() + "</timeZone>");
				sb.append("</deviceInfo>");
			}
		}
		sb.append("</deviceInfoList>");
		sb.append("<ResponseStatus><statusCode>" + status);
		sb.append("</statusCode><statusString>" + statusString);
		sb.append("</statusString></ResponseStatus>");
		sb.append("</devicesInfo>");
		return sb.toString();
	}

	/**
	 * @Title: GetAccountInfo
	 * @Description: APP用户获取账号信息
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2016年1月5日
	 */
	@RequestMapping(value = "/app/Security/AAA/users/info/{userId}")
	public ResponseEntity<String> GetAccountInfo(HttpServletRequest req, @PathVariable
	final Long userId) {
		// 获取当前用户id，（diges认证通过后）
		Long userid = getUserId();
		String sReturn = null;
		try {
			if (!userid.equals(userId)) {
				throw new BusinessException("0x50020039");
			}

			AppUser appuser = userService.getUserById(userId);
			if (appuser == null) {
				throw new BusinessException("0x50020031");
			}

			List<IPCamera> list = userService.getUserBindDeviceList(userId);
			sReturn = responseXml("0", "0", appuser, list);
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	// 组装APP用户获取账号信息
	private String responseXml(String status, String statusString, AppUser appuser, List<IPCamera> list)
			throws Exception {
		StringBuffer sb = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<appUserInfo version=\"1.0\" xmlns=\"urn:skylight\">");
		sb.append("<email>" + appuser.getEmail() + "</email>");
		sb.append("<nickName>" + appuser.getName() + "</nickName>");
		S3FileData file;
		if ("-1".equals(appuser.getPortraintId())) {
			if (appuser.getPortraintUuid() == null || "".equals(appuser.getPortraintUuid())) {
				throw new BusinessException("0x50020030");
			} else {
				file = s3Service.getUploadFileByUuid(appuser.getPortraintUuid());
			}
			sb.append("<portraitId>" + file.getFilePath() + file.getFileName() + "</portraitId>");
		} else {
			sb.append("<portraitId>" + appuser.getPortraintId() + "</portraitId>");

		}
		sb.append("<notification>" + appuser.getNotification() + "</notification>");

		sb.append("<deviceModels>");
		HashSet<String> strTemp = new HashSet<String>();
		if (list != null) {
			for (IPCamera ipcamera : list) {
				if (!strTemp.contains(ipcamera.getModel())) {
					sb.append("<model>" + ipcamera.getModel() + "</model>");
					strTemp.add(ipcamera.getModel());
				}
			}
		}
		sb.append("</deviceModels>");
		sb.append("<ResponseStatus><statusCode>" + status);
		sb.append("</statusCode><statusString>" + statusString);
		sb.append("</statusString></ResponseStatus>");
		sb.append("</appUserInfo>");
		return sb.toString();
	}

	/**
	 * <2.8	APP进行头像上传完成确认>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/app/Security/AAA/users/portrait.app", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPortraitConfirm(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;
		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String uuid = XmlUtil.convertToString(paraMap.get("uuid"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);

			S3FileData fileData = s3Service
					.getCheckUploadFile(uuid, S3ServiceType.SYSTEM_USER_PORTRAIT.getType(), null);

			if (!fileName.equals(fileData.getFileName())) {
				throw new BusinessException("0x50020080");
			}

			String s3Key = StringUtil.convertToS3Key(fileData.getFilePath() + fileData.getFileName());

			// 获取文件大小
			long fileSize = S3Factory.getDefault().getFile(s3Key).getContentLength();

			// 刷新db
			s3Service.updateUploadFile(uuid, S3ServiceType.SYSTEM_USER_PORTRAIT.getType(), fileData.getFileName(),
					fileSize);

			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			sReturn = XmlUtil.mapToXmlRight("portraitConfirm", backMap);
		} catch (Exception e) {
			sReturn = getErrorXml("portraitConfirm", e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
	
	
	/**
	 * 	<6.1	APP修改账户密码>
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/app/Security/AAA/users/changePw.app", method = RequestMethod.POST)
	public ResponseEntity<String> modifyPassword(HttpServletRequest req, HttpServletResponse resp) {

		String sReturn = null;
		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

			String oldPwd = XmlUtil.convertToString(paraMap.get("oldPassword"), false);
			String newPwd = XmlUtil.convertToString(paraMap.get("newPassword"), false);

			AppUser appUser = userService.getUserById(getUserId());

			if (appUser == null) {
				throw new BusinessException("0x50020031");
			}

			if (!oldPwd.equals(appUser.getPassword())) {
				throw new BusinessException("0x50020110");
			}
			
			appUser.setPassword(newPwd);
			userService.updateUser(appUser);

			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

}
