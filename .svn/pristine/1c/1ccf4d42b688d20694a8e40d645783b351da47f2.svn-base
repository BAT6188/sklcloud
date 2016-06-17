package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.PlatformCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.service.AppRelatedDevicesService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @Package com.skl.cloud.controller
 * @Title: AppRelatedDevicesController
 * @Description: 查询关联设备 Copyright: Copyright (c) 2015 Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年6月27日
 * @version V1.0
 */
@Controller
@RequestMapping("skl-cloud/app")
public class AppRelatedDevicesController extends BaseController {

	@Autowired(required = true)
	private AppUserService userService;
	
    @Autowired(required = true)
	private AppRelatedDevicesService appRelatedDevicesService;

	/**
	 * 
	 * @Title: queryAppDevicesSensor
	 * @Description: 查询用户关联设备
	 * @param inputstream
	 * @return ResponseEntity
	 * @author leiqiang
	 * @date 2015年6月29日
	 */
	@RequestMapping(value = "/queryRelatedDevices", method = RequestMethod.GET)
	public ResponseEntity<String> queryAppDevicesSensor(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = "";
		String root = "appQueryRelatedDevices";
		Long userId = getUserId();
		try {
			
			// 1> 获取用户信息
			AppUser user = userService.getUserById(userId);
			if (user == null)
			{
				throw new BusinessException("0x50020031");
			}
			
			Boolean isLink = false;
			
			// 2> 判断该app user是否已关联wechat 账号
			if (user.getBindFlag() == 1) {
				isLink = true;
			}	
			
			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			List<Map<String, Object>> ownDevices = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> installDevices = new ArrayList<Map<String,Object>>();
			
			// 3> 获取用户下的所有设备以及该用户所关联的用户下的所有设备的信息
			// 3.1> 获取用户当前账号下的所有设备的信息
			List<UserCamera> userCameras = userService.getUserCameraList(user.getId());
			if (userCameras != null ) {
				for (UserCamera userCamera : userCameras) {
					
					Map<String, Object> device = new LinkedHashMap<String, Object>();
					Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
					// 获取某设备的信息
					deviceMap = getDevicesInfoMap(userCamera.getCameraSerialno());
					device.put("devices", deviceMap);
					
					//ipc与user为绑定关系添加到ownDevices节点
					if (userCamera.getLinkType() == 0) {
						ownDevices.add(device);
					}
					//ipc与user为install关系添加到installDevices节点
					if (userCamera.getLinkType() == 1) {
						installDevices.add(device);
					}
				}
			}
			if (isLink) {
				// 3.2> 获取用户所关联的账户下的所有设备信息
				List<UserCamera> userCamerasLink = userService.getUserCameraList(user.getLinkUserId());
				if (userCameras != null ) {
					for (UserCamera userCamera : userCamerasLink) {
						if (userCamera.getIsUsedToShareUser() != null) {
							// IsUsedToShareUser为1,本账号与被关联的账号才均可见
							if (userCamera.getIsUsedToShareUser() == 1) {
								Map<String, Object> device = new LinkedHashMap<String, Object>();
								Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
								deviceMap = getDevicesInfoMap(userCamera.getCameraSerialno());
								device.put("devices", deviceMap);
								
								//ipc与user为绑定关系添加到ownDevices节点
								if (userCamera.getLinkType() == 0) {
									ownDevices.add(device);
								}
								
								//ipc与user为install关系添加到installDevices节点
								if (userCamera.getLinkType() == 1) {
									installDevices.add(device);
								}
							}
						}
					}
				}
			}
			backMap.put("ownDevices", ownDevices);
			backMap.put("installDevices", installDevices);
			
			// 返回xml
			sReturn = XmlUtil.mapToXmlRight(root, backMap);
		} catch (Exception e) {
			sReturn = getErrorXml(root, e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}
	
	// 获取某设备的信息
	private Map<String, Object> getDevicesInfoMap(String sn){
		//获取某设备的信息
		PlatformCamera platformCamera = appRelatedDevicesService.queryUserCamera(sn);
		Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
		deviceMap.put("deviceId", platformCamera.getCameraId());
		deviceMap.put("deviceModel", platformCamera.getCameraModel());
		deviceMap.put("deviceName", platformCamera.getCameraNickname());
		deviceMap.put("SN", platformCamera.getCameraSerialno());
		
		Map<String, Object> sensorMap = new LinkedHashMap<String, Object>();
		sensorMap.put("temperature", platformCamera.getPlatformIpcameraSensor().getCameraTemperature());
		sensorMap.put("humidity", platformCamera.getPlatformIpcameraSensor().getCameraHumidity());
		
		deviceMap.put("Sensors", sensorMap);
		deviceMap.put("deviceKind", platformCamera.getCameraKind());
		deviceMap.put("onLineStatus", platformCamera.getCameraIslive());
		return deviceMap;	
	}
}
