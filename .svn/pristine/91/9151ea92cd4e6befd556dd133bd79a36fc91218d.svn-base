package com.skl.cloud.controller.app;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.AppOwnedByOtherService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants;

/**
 * APP查询设备是否已被其他人关联
 * 
 * @author guangbo
 * @date 2015-07-28
 */
@Controller
@RequestMapping("/skl-cloud/app")
public class AppOwnedByOtherController extends BaseController {

	@Autowired(required = true)
	private AppOwnedByOtherService appOwnedByOtherService;

	@Autowired(required = true)
	private IPCameraService iPCameraService;

	/**
	  * APP用户向云端查询设备是否已被其他人关联
	  * @Title: queryownedByOther
	  * @param  SN
	  * @return ResponseEntity<String> (返回值说明)
	  * @author yangbin
	  * @date 2015年11月17日
	 */
	@RequestMapping("/device/{SN}/ownedByOther")
	public ResponseEntity<String> queryownedByOther(@PathVariable String SN) {
		Map<String, String> retMap = new HashMap<String, String>();
		LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
		String sReturn = "";

		try {
			retMap = appOwnedByOtherService.queryownedByOther(getUserId(), SN);
			repMap.put("isOwnedByOther", retMap.get("isOwnedByOther"));
			repMap.put("email", retMap.get("email"));
			repMap.put("nickName", retMap.get("nickName"));
			return new ResponseEntity<String>(XmlUtil.responseXml("0", "0", "ownedByOther", repMap), HttpStatus.OK);
		} catch (Exception e) {
			sReturn = getErrorXml("ownedByOther", e, this.getClass().getName());
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);

		}

	}

	/**
	  * @Title: queryStatus
	  * @Description: APP查询设备是否已连上云端
	  * @param @param SN
	  * @return ResponseEntity<String> (返回值说明)
	  * @author shaoxiong
	  * @date 2015年8月12日
	  */
	@RequestMapping("/device/{SN}/isLiveDevice")
	public ResponseEntity<String> queryStatus(@PathVariable String SN) {
		boolean isLive = appOwnedByOtherService.queryIpcIslive(SN);
		LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
		repMap.put("isLive", String.valueOf(isLive));
		return new ResponseEntity<String>(XmlUtil.responseXml("0", "0", "isLiveDevice", repMap), HttpStatus.OK);
	}
}
