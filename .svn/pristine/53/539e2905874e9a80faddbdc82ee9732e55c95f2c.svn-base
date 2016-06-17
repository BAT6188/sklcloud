package com.skl.cloud.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.HttpMethod;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.common.util.DateUtils;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.EventScheduleService;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.S3ServiceType;

/**
 * @ClassName: AppGetOfflTimPictureController
 * @Description: APP获取摄像头的离线时间和离线图片的url
 * @author shaoxiong
 * @date 2015年7月17日
 */
@Controller
@RequestMapping("/skl-cloud/app")
public class AppGetOfflTimPictureController extends BaseController {
    @Autowired
    private IPCameraService ipcService;
    
    @Autowired
    private AppUserService userService;

    @Autowired
    private S3Service s3Service;
    
    @Autowired(required = true)
    private EventScheduleService eventScheduleService;

    /**
     * APP获取摄像头的离线时间和离线图片的url
     */
    @RequestMapping("/{SN}/Streaming/channels/{ID}/LatestVideoFrame")
    public ResponseEntity<String> getTimePicture(@PathVariable String SN, @PathVariable String ID) {
        LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();
        String latestTime = "";
        String pictureUrl = "";
        // 校验请求url里面的ID是否为“MAIN”
        if (!StringUtils.equals(ID, "MAIN")) {
            return new ResponseEntity<String>(XmlUtil.responseXml("0x50000001", "", "latestVideoFrame", returnMap),
                    HttpStatus.OK);
        }
        try {
            // 查询相关sn的IPC信息
            IPCamera ipcInfo = ipcService.getIPCameraBySN(SN);
            // 判断ipc的合法性
            if (ipcInfo == null) {
                // 查不到设备
                throw new BusinessException("0x50000002");
            } else if (ipcInfo.getOfflineTime() != null) {
                latestTime = DateUtil.date2string(ipcInfo.getOfflineTime(), DateUtil.DATE_TIME_2_FULL_FORMAT);
            }
            // 拼接保存在S3上的对应的设备的离线图片的key值和url
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("SN", SN);
            pictureUrl = s3Service.getUrlOfSystemSpaceByType(S3ServiceType.DEVICE_OFFLINE_PICTURE, paramMap);

        } catch (BusinessException me) {
            LoggerUtil.error("获取摄像头离线时间失败！", this.getClass().getName());
            return new ResponseEntity<String>(XmlUtil.responseXml(me.getErrMsg(), "", "latestVideoFrame", returnMap),
                    HttpStatus.OK);
        }
        // 排好顺序返回
        returnMap.put("latestTime", latestTime);
        returnMap.put("pictureUrl", pictureUrl);

        return new ResponseEntity<String>(XmlUtil.responseXml("0", "", "latestVideoFrame", returnMap), HttpStatus.OK);
    }
    
    /**
     * 时间推送后 app拉取事件详细
     * @param id
     * @return
     */
    @RequestMapping("/pushInfo/{id}.app")
    public ResponseEntity<String> getEvent(@PathVariable String id) {
    	String xml = "";
    	LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();
    	try {
        	EventAlert eventAlert =eventScheduleService.queryEventById(id);
        	if(eventAlert == null){
        		xml = XmlUtil.responseXml("0x50020024", "", "eventInfo", returnMap);
        	}else{
            	String sn = eventAlert.getCameraSerialno();
            	IPCamera ipCamera = ipcService.getIPCameraBySN(sn);
            	if(ipCamera == null){
            		xml = XmlUtil.responseXml("0x50020040", "", "eventInfo", returnMap);
            	}else{
    	        	returnMap.put("SN", eventAlert.getCameraSerialno());
    	        	returnMap.put("eventId", eventAlert.getEventId());
    	        	String pictureUrl = "";
    	        	if(eventAlert.getPhotoUrl() != null && eventAlert.getPhotoUrl().contains("?")){
    	        		String tempUrl = eventAlert.getPhotoUrl();
    	        		String s3UrlKey = tempUrl.substring(tempUrl.indexOf("/",tempUrl.indexOf("://")+3)+1, tempUrl.indexOf("?"));
    	        		pictureUrl =S3Factory.getDefault().getPresignedUrl(s3UrlKey, HttpMethod.GET).toString();
    	        	}else if(eventAlert.getPhotoUrl() != null && !eventAlert.getPhotoUrl().contains("?")){
    	        		String tempUrl = eventAlert.getPhotoUrl();
    	        		String s3UrlKey = tempUrl.substring(tempUrl.indexOf("/",tempUrl.indexOf("://")+3)+1);
    	        		pictureUrl =S3Factory.getDefault().getPresignedUrl(s3UrlKey, HttpMethod.GET).toString();
    	        	}
    	        	returnMap.put("pictureUrl", pictureUrl);
    	        	Date date = DateUtil.str2Date(eventAlert.getDateTime(), DateUtil.DATE_TIME_1_FULL_FORMAT);
    	        	String dateTime = DateUtils.getISO8601(date, ipCamera.getTimeZone());
    	        	returnMap.put("dateTime", dateTime);
    	        	returnMap.put("cameraName", ipCamera.getNickname());
    	        	xml = XmlUtil.responseXml("0", "0", "eventInfo", returnMap);
            	}
        	}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			xml = XmlUtil.responseXml("0x50000044", "", "eventInfo", returnMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			xml = XmlUtil.responseXml("0x50000005", "", "eventInfo", returnMap);
		}
    	return new ResponseEntity<String>(xml, HttpStatus.OK);
    }
}
