package com.skl.cloud.controller.app;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.model.PlatformIpcfw;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.fw.FWService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * 关于fw的控制器
 * 
 * @author weibin
 *
 */
@RequestMapping("/skl-cloud/app")
@Controller
public class AppDeviceFWController extends BaseController {
    // 返回状态码 Ok
    public static final String STATUS_OK = "0";
    // ipc fw下载完成
    public static final Integer OK = 0;
    // ipc 下载fw 中
    public static final Integer DOWNING = 1;
    // ipc 下载 fw 失败
    public static final Integer FAIL = 2;

    private static final Logger log = Logger.getLogger(AppDeviceFWController.class);

    @Autowired
    private FWService fWService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private IPCameraService ipcService;

    /**
     * App请求获取IPC当前FW信息
     * 
     * @param request
     * @param response
     * @param SN
     * @return
     */
    @RequestMapping(value = "/device/{SN}/System/deviceInfo")
    public ResponseEntity<String> deviceFW(HttpServletRequest request, HttpServletResponse response,
            @PathVariable String SN) {
        Long userId = getUserId();
        LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
        try {
            IPCamera ipcamera = userService.getUserBindDevice(userId, SN);
            // 返回到App的字段不能为空
            if (null != ipcamera) {
                if (StringUtils.isNotEmpty(ipcamera.getModel()) && StringUtils.isNotEmpty(ipcamera.getIpcSub().getVersion())) {
                    repMap.put("deviceName", ipcamera.getNickname());
                    repMap.put("model", ipcamera.getModel());
                    repMap.put("serialNumber", ipcamera.getSn());
                    repMap.put("macAddress", ipcamera.getMac());
                    repMap.put("fwVersion", ipcamera.getIpcSub().getVersion());
                    PlatformIpcfw ipcFw = fWService.getPlatformIpcfwInfo(ipcamera.getModel(), ipcamera.getIpcSub().getVersion());
                    if (ipcFw != null && ipcFw.getVersiondate() != null) {
                        repMap.put("fwReleaseDate",
                                DateUtil.date2Str(ipcFw.getVersiondate(), DateUtil.DATE_TIME_1_FULL_FORMAT));
                    } else {
                        repMap.put("fwReleaseDate", " ");
                    }
                    return new ResponseEntity<String>(XmlUtil.responseXml("0", "", "deviceInfo", repMap), HttpStatus.OK);
                } else {
                    // 调用IPC接口进行查询
                    try {
                        // get info from IPC remote
                        String deviceInfoXml = fWService.getRemoteIpcFWInfo(SN);
                        Map<String, Object> map = XmlUtil.getElementMap(deviceInfoXml);
                        if (null != map) {
                            // 更新版本信息等数据
                            ipcamera.setMac((String) map.get("macAddress"));
                            ipcamera.setNickname((String) map.get("deviceName"));
                            ipcamera.setModel((String) map.get("model"));
                            ipcamera.getIpcSub().setVersion((String) map.get("fwVersion"));
                            ipcamera.getIpcSub().setVersionDate(DateUtil.str2Date((String) map.get("fwReleaseDate"),
                                    DateUtil.DATE_TIME_2_FULL_FORMAT));
                            ipcService.updateIPCamera(ipcamera);
                        }
                        return new ResponseEntity<String>(XmlUtil.responseXml("0", "", "deviceInfo", map),
                                HttpStatus.OK);
                    } catch (SKLRemoteException e) {
                        log.error(e);
                        return new ResponseEntity<String>(XmlUtil.responseXml(
                                "0x" + Integer.toHexString(e.getErrCode()), "", "deviceInfo", repMap), HttpStatus.OK);
                    } catch (BusinessException e) {
                        log.error(e);
                        return new ResponseEntity<String>(XmlUtil.responseXml(
                                "0x" + Integer.toHexString(e.getErrCode()), "", "deviceInfo", repMap), HttpStatus.OK);
                    } catch (Exception e) {
                        log.error(e);
                        return new ResponseEntity<String>(XmlUtil.responseXml("0x50000044", "", "deviceInfo", repMap),
                                HttpStatus.OK);
                    }
                }
            } else {
                return new ResponseEntity<String>(XmlUtil.responseXml("0x50000047", "this SN ipc is not exist",
                        "deviceInfo", repMap), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<String>(XmlUtil.responseXml("0x50000044", "this SN ipc is not exist",
                    "deviceInfo", repMap), HttpStatus.OK);
        }
    }

    /**
     * App请求云端获取此IPC型号的最新FW信息
     * 
     * @param SN
     * @return
     */
    @RequestMapping("/Custom/fmlExt/device/{SN}/System/latestFirmware")
    public ResponseEntity<String> queryPlatformIpcfwInfo(@PathVariable String SN) {

        LinkedHashMap<String, Object> map1 = new LinkedHashMap<String, Object>();
        try {
            IPCamera ipcamera = ipcService.getIPCameraBySN(SN);
            if (ipcamera != null && StringUtils.isNotEmpty(ipcamera.getModel())) {
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)fWService.getCloudLatestFwVersion(ipcamera.getModel());
                if (map != null) {
                    return new ResponseEntity<String>(XmlUtil.responseXml("0", "", "firmwareLatest", map),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(XmlUtil.responseXml("0x50000002", "", "firmwareLatest", map),
                            HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<String>(XmlUtil.responseXml("0x50000002", "", "firmwareLatest", map1),
                        HttpStatus.OK);

            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<String>(XmlUtil.responseXml("0x50000044", "", "firmwareLatest", map1),
                    HttpStatus.OK);
        }
    }

    /**
     * App请求对IPC进行升级FW
     * 
     * @param req
     * @param resp
     * @param inputstream
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping("/firmwareUpdate")
    public ResponseEntity<String> firmwareUpdate(HttpServletRequest req, HttpServletResponse resp,
            InputStream inputstream) {
        ResponseEntity<String> respXml = null;
        long userId = getUserId();
        IPCamera ipCamera = null;
        boolean flags = false;
        boolean updatedCode = true;
        String status = "";
        try {
            // 解析报文
            Map<String, Object> map = XmlUtil.getRequestXmlParam(req);
            if (map != null) {
                // 获取参数
                String version = (String) map.get("fwVersion");
                String sn = (String) map.get("SN");
                // 判断参数
                if (StringUtils.isEmpty(sn) || StringUtils.isEmpty(version)) {
                    respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50000001", ""), HttpStatus.OK);
                } else {
                    // 判断用户与ipc是否关联
                    ipCamera = userService.getUserBindDevice(userId, sn);
                    if (ipCamera == null) {
                        respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50020067", ""), HttpStatus.OK);
                    } else {
                        // 得到版本信息
                        PlatformIpcfw platformIpcfw = fWService.getPlatformIpcfwInfo(ipCamera.getModel(), version);

                        // 远程请求ipc下载固件
                        String xml = fWService.updateRemoteFw(sn, platformIpcfw);
                        map = XmlUtil.getElementMap(xml);
                        status = (String) map.get("statusCode");
                        // 解析状态，跟新ipc的fw下载状态
                        if (STATUS_OK.equals(status)) {
                            flags = true;
                            xml = XmlUtil.mapToXmlRight();
                        }
                        respXml = new ResponseEntity<String>(xml, HttpStatus.OK);

                    }
                }
            } else {
                respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50000001", ""), HttpStatus.OK);
            }
        } catch (SKLRemoteException ske) {
            log.error(ske);
            log.error("远程连接的返回code--" + ske.getErrCode());
            status = Integer.toHexString(ske.getErrCode())+"";
            log.error("远程连接的返回code--" + status);
           if("13020007".equals(status)){
                log.info("ipc返回的statusCode--" + status);
                updatedCode = false;
                respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50020090", ""), HttpStatus.OK);
            } else {
            	log.info("ipc返回的statusCode--" + status);
            	respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50010030", ""), HttpStatus.OK);
            }          
            
        } catch (BusinessException be) {
            log.error(be);
            respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x" + be.getErrCode(), ""), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e);
            respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50000027", ""), HttpStatus.OK);
        } finally {
            // 更新ipc下载的fw状态到数据库
            try {
                if (ipCamera != null && updatedCode) {
                    if (flags) {
                        ipCamera.getIpcSub().setFwDownloadStatus(DOWNING);
                    } else {
                        ipCamera.getIpcSub().setFwDownloadStatus(FAIL);
                    }
                    ipcService.updateIPCamera(ipCamera);
                }
            } catch (Exception e2) {
                log.error(e2);
                respXml = new ResponseEntity<String>(XmlUtil.responseXml("0x50000044", ""), HttpStatus.OK);
            }
            return respXml;
        }
    }

    /**
     * App查询对应SN的IPC升级进度
     * @param sn
     * @return
     */
    @RequestMapping("/{SN}/System/updateFirmware/status")
    public ResponseEntity<String> updateFirmwareStatus(@PathVariable("SN") String sn) {
        String xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "1");
        long userId = getUserId();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            // 判断有没有关联
            IPCamera ipCamera = userService.getUserBindDevice(userId, sn);
            if (ipCamera != null) {
                // 判断fw的值
            	String errorReason = ipCamera.getIpcSub().getFwDownloadErrorReason();
                if (ipCamera.getIpcSub().getFwDownloadStatus() != null) {
                    int status = ipCamera.getIpcSub().getFwDownloadStatus();
                    // 判断数据库的fw状态值 1为正在升级 0为升级成功 2为升级失败
                    if (status == 0 || status == 2) {
                        map.put("updateSuccess", status == 0 ? "true" : "false");
                        if(errorReason == null){
                        	errorReason = "";
                        }
                        map.put("errorReason", errorReason);
                        map.put("downlaodPercentage", status == 0 ? "100" : "0");
                        xml = XmlUtil.mapToXmlRight("updateFirmwareStatus", map);
                    } else {
                        // 远程连接获取fw升级状态
                        xml = fWService.getRemoteFwUpdateStatus(sn);
                    }
                } else {
                    xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "0x50020067");
                }
            } else {
                xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "0x50020067");
            }
        } catch (SKLRemoteException e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "0x" + e.getErrCode());
        } catch (BusinessException e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "0x" + e.getErrCode());
        } catch (Exception e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("updateFirmwareStatus", "0x50020067");
        }
        return new ResponseEntity<String>(xml, HttpStatus.OK);

    }

}
