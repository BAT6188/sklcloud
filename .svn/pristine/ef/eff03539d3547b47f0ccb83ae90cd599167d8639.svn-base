package com.skl.cloud.controller.ipc;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.common.util.DateUtils;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.RandomTools;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.encrypt.AESUtil;

/**
 * @ClassName: IpcRegistController
 * @Description: IPC向云端注册鉴权
 * @author shaoxiong
 * @date 2015年6月15日 上午10:16:08
 */
@RequestMapping("skl-cloud/device")
@Controller
public class IpcAuthenticationController extends BaseController {

    @Autowired(required = true)
    private IPCameraService ipcService;

    /**
      * IPC向云端发送设备注册请求
      * @Title: regist
      * @Description: IPC向云端注册鉴权
      * @param inputStream
      * @return ResponseEntity<String>
      * @author yangbin
      * @date 2015年11月18日
     */
    @RequestMapping("/regist")
    public ResponseEntity<String> regist(InputStream inputStream) {
        LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
        final String repRoot = "deviceGenericInfo";
        String repXml = XmlUtil.responseXml("0", "0", repRoot, repMap);
        // 解析报文
        String reqXml = XmlUtil.isChangeToStr(inputStream);
        Map<String, Object> reqMap = XmlUtil.getElementMap(reqXml, null);

        // 时间
        String strDateTime = XmlUtil.getDomValue(reqMap, "deviceGenericInfo/dateTime");
        Date reqDate = null;
        if (strDateTime.contains("-")) {
            
            reqDate = DateUtil.str2Date(strDateTime.replace("T", " "), DateUtil.DATE_TIME_1_FULL_FORMAT);
        }
        if (strDateTime.contains("/")) {
            reqDate = DateUtil.str2Date(strDateTime.replace("T", " "), DateUtil.DATE_TIME_2_FULL_FORMAT);
        }
        if (reqDate == null) {
            repXml = XmlUtil.responseXml("0x50000027", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        // 获取SN参数
        String strSN = XmlUtil.getDomValue(reqMap, "deviceGenericInfo/SN");
        if (!StringUtil.isNullOrEmpty(strSN)) {
            // 查不到设备
            repXml = XmlUtil.responseXml("0x50000027", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        // 产品相关信息
        String strSessionInfo = XmlUtil.getDomValue(reqMap, "deviceGenericInfo/sessionInfo");
        IPCamera ipcamera = ipcService.getIPCameraBySN(strSN);
        if (ipcamera == null) {
            // 查不到设备
            repXml = XmlUtil.responseXml("0x50000047", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        // 和时间组合成密钥
        String strMac = StringUtil.strTrim(ipcamera.getMac());
        strMac = strMac.replace(":", "");
        strMac = strMac.replace("-", "");
        String hhSS = DateUtil.date2string(reqDate, "mmss");
        // 组合成密钥
        String encryKey = strMac + hhSS;

        // 解析sessionInfo密文
        String sessionInfo = "";
        strSessionInfo = strSessionInfo.replace("\n", "").trim();
        try {
            sessionInfo = AESUtil.desEncrypt(strSessionInfo, encryKey, AESUtil.SIV);
        } catch (Exception e1) {
            LoggerUtil.error("Invalid AES key", e1, this.getClass().getName());
            repXml = XmlUtil.responseXml("0x50010002", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        sessionInfo = "<root>" + StringUtil.strTrim(sessionInfo) + "</root>";
        // 解析IPC相关信息
        Map<String, Object> sessionMap = XmlUtil.getElementMap(sessionInfo, null);

        // 过期时间有修改
        String expired = XmlUtil.getDomValue(sessionMap, "product/expired");
        Date expiredDate = DateUtil.str2Date(expired, DateUtil.DATE_TIME_2_FULL_FORMAT);
        if (expiredDate == null) {
            LoggerUtil.info("expiredDate is null!", this.getClass().getName());
            repXml = XmlUtil.responseXml("0x50000027", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        // 生产时间
        String strmakedate = XmlUtil.getDomValue(sessionMap, "product/makedate");
        Date makedate = DateUtil.str2Date(strmakedate, DateUtil.DATE_TIME_2_FULL_FORMAT);
        if (makedate == null) {
            LoggerUtil.info("makedate is null!", this.getClass().getName());
            repXml = XmlUtil.responseXml("0x50000027", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }

        // random
        String random = XmlUtil.getDomValue(sessionMap, "root/random");
        String version = XmlUtil.getDomValue(sessionMap, "product/version");
        String cloudRandom = RandomTools.getRandom();
        ipcamera.getIpcSub().setVersion(version);
        ipcamera.getIpcSub().setVersionDate(new Date());
        ipcamera.getIpcSub().setReceIpcRandom(random);
        ipcamera.getIpcSub().setSendIpcRandom(cloudRandom);
        ipcamera.getIpcSub().setExpiredDate(expiredDate);
        ipcamera.setStatus(Boolean.TRUE);
        ipcamera.setMakeDate(makedate);

        try {
            // 激活(把相关信息填入表中)
            ipcService.updateIPCamera(ipcamera);

            // 同步时间到IPC, currentDate + APP save TimeZone in IPC table
            String dateTime = DateUtils.getISO8601(new Date(), ipcamera.getTimeZone());
            repMap.put("dateTime",dateTime);
            
            String authorize = expired + cloudRandom;
            try {
                authorize = AESUtil.Encrypt(authorize, random, AESUtil.SIV);
            } catch (Exception e) {
                super.LogException(new ManagerException("0x50000042"), this.getClass().getName());// 打印业务异常
                repXml = XmlUtil.responseXml("0x50000042", "", repRoot, repMap);
                return new ResponseEntity<String>(repXml, HttpStatus.OK);
            }
            repMap.put("authorize", authorize);

        } catch (ManagerException e) {
            LoggerUtil.error(" APP regist cloud IPC Error", e, this.getClass().getName());
            repXml = XmlUtil.responseXml("0x50000022", "", repRoot, repMap);
            return new ResponseEntity<String>(repXml, HttpStatus.OK);
        }
        repXml = XmlUtil.responseXml("0", "0", repRoot, repMap);
        return new ResponseEntity<String>(repXml, HttpStatus.OK);
    }
}
