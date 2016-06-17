package com.skl.cloud.controller.app;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.PlatformIpcameraSensor;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.IpcHeartBeatService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @Package com.skl.cloud.controller
 * @Title: AppDevicesSensorController
 * @Description: APP获取用户IPC设备Sensor信息 Copyright: Copyright (c) 2015
 *               Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月24日
 * @version V1.0
 */
@Controller
@RequestMapping("skl-cloud/app")
public class AppDevicesSensorController extends BaseController {
    private static final Logger logger = Logger.getLogger(AppDevicesSensorController.class);
    
    @Autowired
    private IpcHeartBeatService ipcSensorService;

    @Autowired
    private AppUserService appUserService;


    @RequestMapping("/queryDevicesSensor/{SN}")
    public ResponseEntity<String> queryAppDevicesSensor(@PathVariable String SN) {
        String sReturn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appQueryDevicesSensor version=\"1.0\" xmlns=\"urn:skylight\"><ResponseStatus><statusCode>1</statusCode><statusString>1</statusString></ResponseStatus></appQueryDevicesSensor>";
        Long userId = getUserId();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // 查询数据
        try {
            IPCamera ipCamera = appUserService.getUserBindDevice(userId, SN);
            // 判断设备是否存在或者用户与ipc是否关联
            if (ipCamera == null) {
                return new ResponseEntity<String>(XmlUtil.mapToXmlError("appQueryDevicesSensor", "0x50020067"),
                        HttpStatus.OK);
            }

            // 获取离当前时间最近的一条关于该设备Sensor数据信息
            PlatformIpcameraSensor pis = ipcSensorService.getLatestIpcSensorBySn(SN);

            if (pis == null) {
                logger.info("**********查询到的Sensor数据信息为null**********");

                return new ResponseEntity<String>(XmlUtil.mapToXmlError("appQueryDevicesSensor", "0x50020068"),
                        HttpStatus.OK);
            }

            Map<String, Object> sensors = new LinkedHashMap<String, Object>();
            sensors.put("temperature", pis.getCameraTemperature());
            sensors.put("humidity", pis.getCameraHumidity());
            map.put("isLiveStatus", ipCamera.getIsLive() == null || !ipCamera.getIsLive() ? "0" : "1");
            map.put("sensors", sensors);
            sReturn = XmlUtil.mapToXmlRight("appQueryDevicesSensor", map);

        } catch (ManagerException me) {
            logger.error(me);
            super.LogException(new ManagerException("0x50000047"), this.getClass().getName());
            return new ResponseEntity<String>(XmlUtil.mapToXmlError("appQueryDevicesSensor", "0x50000047"),
                    HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }
}
