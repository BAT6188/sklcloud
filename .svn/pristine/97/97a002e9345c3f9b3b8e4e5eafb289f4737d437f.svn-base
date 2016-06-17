package com.skl.cloud.controller.ipc;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.PlatformIpcameraSensor;
import com.skl.cloud.service.IpcHeartBeatService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @Package com.skl.cloud.controller
 * @Title: IpcHeartBeatController
 * @Description: 获取IPC的Sensor数据信息接口
 * @Company:深圳天彩智通软件有限公司
 * @Copyright: Copyright (c) 2015
 * @author leiqiang
 * @date 2015年6月24日
 * @version V1.0
 */
@Controller
@RequestMapping("/skl-cloud/device")
public class IpcHeartBeatController extends BaseController
{
	private static final Logger logger = Logger.getLogger(IpcHeartBeatController.class);
	
	@Autowired
	private IpcHeartBeatService ipcHeartBeatService;
	
	@Autowired
	private IPCameraService ipcService;
	
	/**
	 * @Title: ipcHeartBeat
	 * @param inputstream
	 * @return
	 * @author leiqiang
	 * @date 2015年6月24日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/environmentInfo")
	public ResponseEntity<String> ipcHeartBeat(HttpServletRequest req)
	{
		String sReturn =  null;
		
		try {
			
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			if (paraMap.get("sensors") == null) {
				throw new BusinessException("0x50000029");
			}
			Map<String, Object> sensors =new HashMap<String, Object>();
			try {
				sensors = (Map<String, Object>) paraMap.get("sensors");
			} catch (Exception e) {
				throw new BusinessException("0x50000029");
			}
			
			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
			Float fTemperature = Float.valueOf(XmlUtil.convertToString(sensors.get("temperature"), false));
			Integer iHumidity = Integer.valueOf(XmlUtil.convertToString(sensors.get("humidity"), false));
			
			// 判断是否存在该SN
			if(ipcService.getIPCameraBySN(sn) == null){
				throw new BusinessException("0x50000047");
			}
			// 判断humidity参数是否正确
			if (iHumidity < -1 || iHumidity > 100) {
				throw new BusinessException("0x50000001");
			}
			
			Date currentDate = new Date();	
			PlatformIpcameraSensor platformIpcameraSensor = new PlatformIpcameraSensor();
			platformIpcameraSensor.setCameraSerialno(sn);
			platformIpcameraSensor.setCameraTemperature(fTemperature);
			platformIpcameraSensor.setCameraHumidity(iHumidity);
			// IPC上报Sensor温度、湿度和空气质量的时间有云端维护，当前时间作为上报时间
			platformIpcameraSensor.setDateTime(currentDate);
			
			//清理一个小时的平均值
			Date lastTime = ipcHeartBeatService.getLastTime(sn);
			//if is the first heart beat, the lastTime is null, no need to clear
			if(lastTime != null){
    			Date baseLastTime = DateUtil.str2Date(DateUtil.date2Str(lastTime, DateUtil.DATE_TIME_1_HH_FORMAT), DateUtil.DATE_TIME_1_HH_FORMAT);
    			Date baseDate = DateUtil.str2Date(DateUtil.date2Str(currentDate, DateUtil.DATE_TIME_1_HH_FORMAT), DateUtil.DATE_TIME_1_HH_FORMAT);
    			if(baseLastTime.getTime() != baseDate.getTime()){
    				if(currentDate.getTime()-lastTime.getTime() > 30000){
    					ipcHeartBeatService.callAvgSensorRecordsByHour(DateUtil.addMinutes(baseLastTime, "60"), sn);
    					logger.info("太久没连接 重置");
    				}
    			}
    			if(currentDate.getTime()-baseDate.getTime() <= 30000){
    				ipcHeartBeatService.callAvgSensorRecordsByHour(baseDate,sn);
    				logger.info("一个小时清理一次");
    			}
			}
			
			// 插入Sensor数据
			ipcHeartBeatService.insertIpcHeartBeat(platformIpcameraSensor);
			
			sReturn = XmlUtil.mapToXmlRight();
			
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}						
		
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}
	

}
