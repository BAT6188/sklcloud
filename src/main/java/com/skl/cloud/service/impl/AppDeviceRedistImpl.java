package com.skl.cloud.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.AppDeviceRedistService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.encrypt.AESUtil;

@Service("appDeviceRedistService")
public class AppDeviceRedistImpl implements AppDeviceRedistService
{

	@Autowired
	private IPCameraService ipcService;

	/**
	 * APP用户向服务器注册IPC
	 */
	@Override
	@Transactional
	public ResponseEntity<String> register(InputStream inputstream) throws ManagerException
	{
	    LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
		String repXml = XmlUtil.responseXml("0", "0");
		// 解析报文
		String reqXml = XmlUtil.isChangeToStr(inputstream);
		Map<String, Object> reqMap = XmlUtil.getElementMap(reqXml, null);
		// 时间
		String strDateTime = XmlUtil.getDomValue(reqMap, "appDeviceRegist/dateTime");
		Date reqDate = null;
		if (strDateTime.contains("-"))
		{
			reqDate = DateUtil.str2Date(strDateTime.replace("T", " "), DateUtil.DATE_TIME_1_FULL_FORMAT);
		}
		if (strDateTime.contains("/"))
		{
			reqDate = DateUtil.str2Date(strDateTime.replace("T", " "), DateUtil.DATE_TIME_2_FULL_FORMAT);
		}
		if (reqDate == null)
		{
		    repXml = XmlUtil.responseXml("0x50000027", "");
		    return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}

		// 获取SN参数并检查数据
		String strSN = XmlUtil.getDomValue(reqMap, "appDeviceRegist/SN");
		if (StringUtils.isBlank(strSN))
		{
		    repXml = XmlUtil.responseXml("0x50000027", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}

		// 获取timeZone并检查数据
		String timeZone = XmlUtil.getDomValue(reqMap, "appDeviceRegist/timeZone");
		if (StringUtils.isBlank(timeZone))
		{
		    repXml = XmlUtil.responseXml("0x50000027", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}

		// 产品相关信息
		String strSessionInfo = XmlUtil.getDomValue(reqMap, "appDeviceRegist/sessionInfo");
		IPCamera ipcamera = ipcService.getIPCameraBySN(strSN);
		if (ipcamera == null)
		{
			// 查不到设备
		    repXml = XmlUtil.responseXml("0x50000047", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}

		// 和时间组合成密钥
		String strMac = StringUtils.trim(ipcamera.getMac());
		strMac = strMac.replace(":", "");
		strMac = strMac.replace("-", "");
		String hhSS = DateUtil.date2string(reqDate, "mmss");
		// 组合成密钥
		String encryKey = strMac + hhSS;

		// 解析sessionInfo密文
		String sessionInfo = "";
		strSessionInfo = strSessionInfo.replace("\n", "").trim();
		try
		{
		    sessionInfo = AESUtil.desEncrypt(strSessionInfo, encryKey, AESUtil.SIV);
		} catch (Exception e1)
		{
			LoggerUtil.error("Invalid AES key", e1, this.getClass().getName());
			repXml = XmlUtil.responseXml("0x50010002", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}
		
		sessionInfo = "<root>" + StringUtil.strTrim(sessionInfo) + "</root>";
        // 解析sessionInfo相关信息
        Map<String, Object> sessionMap = XmlUtil.getElementMap(sessionInfo, null);
        String sessionInfoSn = XmlUtil.getDomValue(sessionMap, "product/sn");
        if (StringUtils.isEmpty(sessionInfoSn)) {
            LoggerUtil.info("sessionInfoSn is null!", this.getClass().getName());
            repXml = XmlUtil.responseXml("0x50010002", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

		// only update timeZone
		ipcamera.setTimeZone(timeZone);

		try
		{
			// 激活(把相关信息填入表中)
			ipcService.updateIPCamera(ipcamera);
		} catch (ManagerException e)
		{
			LoggerUtil.error(" APP regist cloud IPC Error", e, this.getClass().getName());
			repXml = XmlUtil.responseXml("0x50000022", "");
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
		}

        return new ResponseEntity<String>(repXml, HttpStatus.OK);
	}

	/**
     * APP用户通过SN向服务器注册IPC
     */
	@Override
	@Transactional
	public ResponseEntity<String> registerBySN(InputStream inputstream) throws ManagerException
	{
		// 解析报文
		String reqXml = XmlUtil.isChangeToStr(inputstream);
		Map<String, Object> reqMap = XmlUtil.getElementMap(reqXml, null);
		// 判断是不是合法xml文件
		if (reqMap == null || reqMap.size() == 0)
		{
			return new ResponseEntity<String>(XmlUtil.responseXml("0x50000027", ""), HttpStatus.OK);
		}

		// 获取请求参数sn和timeZone
		String strSN = XmlUtil.getDomValue(reqMap, "appDeviceAuthorize/SN");
		String timeZone = XmlUtil.getDomValue(reqMap, "appDeviceAuthorize/timeZone");
		if (strSN == null || "".equals(strSN.trim()) || timeZone == null || "".equals(timeZone.trim()))
		{
			return new ResponseEntity<String>(XmlUtil.responseXml("0x50000029", ""), HttpStatus.OK);
		}
		IPCamera ipcamera = ipcService.getIPCameraBySN(strSN);
		// 判断ipc的合法性
		if (ipcamera == null)
		{
			// 查不到设备
			return new ResponseEntity<String>(XmlUtil.responseXml("0x50000002", ""), HttpStatus.OK);
		}
		ipcamera.setTimeZone(timeZone);

		// 更新数据库的ipc的时区
		try
		{
			ipcService.updateIPCamera(ipcamera);
		} catch (ManagerException e)
		{
			LoggerUtil.error(" APP registerBySN cloud Error", this.getClass().getName());
			return new ResponseEntity<String>(XmlUtil.responseXml("0x50020061", ""), HttpStatus.OK);
		}
		return new ResponseEntity<String>(XmlUtil.responseXml("0", "0"), HttpStatus.OK);
	}
}
