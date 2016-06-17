package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.service.AppDeviceFeedsFilterService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @Package com.skl.cloud.controller
 * @Title: AppDeviceFeedsFilterController
 * @Description: 分享设备Feeds filter使能
 * @Copyright: Copyright (c) 2015
 * @Company:深圳天彩智通软件有限公司
 * @author lizhiwei
 * @date 2015年10月24日
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/skl-cloud")
public class AppDeviceFeedsFilterController extends BaseController
{
	@Autowired(required = true)
	private AppDeviceFeedsFilterService appDeviceFeedsFilterService;

	/**
	 * <4.16 设置分享设备Feeds filter使能>
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@RequestMapping(value = "/app/device/feedsFilter/set", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	public ResponseEntity<String> setDeviceFeedsFilter(HttpServletRequest req, HttpServletResponse resp)
	{
		String userId = String.valueOf(getUserId());
		String sReturn = null;

		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (Object circleObj : (List) paraMap.get("deviceInfoList"))
			{
				Map<String, Object> circleMap = (Map<String, Object>) ((Map<String, Object>) circleObj).get("deviceInfo");
				String enable = XmlUtil.convertToString(circleMap.get("enable"), false);
				String SN = XmlUtil.convertToString(circleMap.get("SN"), false);

				// 校验：是否对此设备有分享权
				Map<String, Object> userDevice = appDeviceFeedsFilterService.getShareDeviceByUserSn(userId, SN);
				if (userDevice == null || userDevice.size() == 0)
				{
					throw new BusinessException("0x50020025");
				}

				Map<String, String> map = new HashMap<String, String>();
				map.put("deviceId", String.valueOf(userDevice.get("deviceId")));
				map.put("enable", enable);
				list.add(map);
			}

			appDeviceFeedsFilterService.updateDeviceFeedsFilter(userId, list);
			
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <4.15 查询分享设备Feeds filter使能>
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/app/device/feedsFilter/query", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	public ResponseEntity<String> queryDeviceFeedsFilter(HttpServletRequest req, HttpServletResponse resp)
	{
		String userId = String.valueOf(getUserId());
		String sReturn = null;

		try
		{
			List<Map<String, Object>> deviceList = appDeviceFeedsFilterService.queryDeviceFeedsFilter(userId);

			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			List<Object> deviceInfoList = new ArrayList<Object>();

			if (deviceList != null && deviceList.size() > 0)
			{
				for (Map<String, Object> device : deviceList)
				{
					Map<String, Object> deviceInfoMaps = new LinkedHashMap<String, Object>();
					Map<String, Object> deviceInfoMap = new LinkedHashMap<String, Object>();
					deviceInfoMap.put("enable", device.get("enable"));
					deviceInfoMap.put("deviceId", device.get("deviceId"));
					deviceInfoMap.put("deviceModel", device.get("deviceModel"));
					deviceInfoMap.put("deviceName", device.get("deviceName"));
					deviceInfoMap.put("SN", device.get("SN"));
					deviceInfoMap.put("deviceKind", device.get("deviceKind"));
					deviceInfoMap.put("ownerName", device.get("nickName"));

					deviceInfoMaps.put("deviceInfo", deviceInfoMap);
					deviceInfoList.add(deviceInfoMaps);
				}
			}
			backMap.put("deviceInfoList", deviceInfoList);
			sReturn = XmlUtil.mapToXmlRight("feedsFilter", backMap);
		} catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
