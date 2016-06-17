package com.skl.cloud.controller.ipc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.common.util.DateUtils;
import com.skl.cloud.controller.ipc.dto.FileIO;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.foundation.mvc.method.annotation.ResponseName;
import com.skl.cloud.foundation.mvc.model.SKLModel;
import com.skl.cloud.model.audio.Media;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.audio.AudioService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.S3ServiceType;

/**
 * @Package com.skl.cloud.controller.ipc
 * @Description: for IPC to get base information from Cloud business logic
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年11月5日
 * @version V1.0
 */
@Controller
@RequestMapping("/skl-cloud/device")
public class IpcMiscellaneousController extends IpcController {
    private static Logger logger = LoggerFactory.getLogger(IpcMiscellaneousController.class);
    
    @Autowired
    private IPCameraService ipcService;
        
    @Autowired
    private StreamResourcesService streamResourcesService;
    
	@Autowired
	private AudioService audioService;
    
    /**
     * IPC到云端同步时间
     * @param sn
     */
    @RequestMapping("/{sn}/dateTime.ipc")
    @ResponseName("systemTime")
    public void refreshTime (@PathVariable String sn, SKLModel model){
    	AssertUtils.isNotBlank(sn, new String[]{"sn"});
    	IPCamera ipcamera = ipcService.getIPCameraBySN(sn);
    	
    	if( null != ipcamera){
    		String time = DateUtils.getISO8601(new Date(), ipcamera.getTimeZone());
    		model.addAttribute("dateTime", time);
    	} else {
    		logger.warn("this device is not exist");
    		throw new BusinessException("0x50000047");
    	}
    }
    
    /**
     * @Title: getAdressIpc
     * @Description: IPC请求获取P2P/UPNP/STUN/指令中心信息
     * @param ID
     * @return ResponseEntity<String> (返回值说明)
     * @author yangbin
     * @date 2015年11月19日
    */
    @RequestMapping("/serverInfo/{ID}")
    ResponseEntity<String> getAdressIpc(@PathVariable String ID) {
        return streamResourcesService.getAdress();
    }
    
    /**
     * IPC上报到云端更新固件的进度以及状态
     * @param req
     * @param resp
     * @param inputstream
     * @return
     */
    @RequestMapping("/system/updateFirmware/status")
    public ResponseEntity<String> updateFirmwareStatu(HttpServletRequest req, HttpServletResponse resp,
            InputStream inputstream) {
        String sReturn = "";
        Map<String, Object> map;
        try {
            map = XmlUtil.getRequestXmlParam(req);
            String sn = (String) map.get("SN");
            String updateSuccess = (String) map.get("updateSuccess");       
            String errorReason = null;
            if(map.containsKey("errorReason")){
            	errorReason =(String) map.get("errorReason");
            }
            String downloadPercentage = (String) map.get("downloadPercentage");
            
            if (StringUtils.isNoneEmpty(sn) && StringUtils.isNoneEmpty(updateSuccess)
                    && StringUtils.isNoneEmpty(downloadPercentage)) {
                IPCamera ipCamera = ipcService.getIPCameraBySN(sn);
                if (ipCamera != null) {
                    // 当updateSuccess为true和downlaodPercentage为100，ipc下载fw成功
                    if ("true".equals(updateSuccess) && "100".equals(downloadPercentage)) {
                        // fw状态 0下载完成，1下载中，2下载失败
                        ipCamera.getIpcSub().setFwDownloadStatus(0);
                    } else {
                        ipCamera.getIpcSub().setFwDownloadStatus(2);
                    }
                    ipCamera.getIpcSub().setFwDownloadErrorReason(errorReason);
                    // 更新状态
                    ipcService.updateIPCamera(ipCamera);
                    sReturn = XmlUtil.mapToXmlRight();
                } else {
                    sReturn = XmlUtil.mapToXmlError("0x50000002");
                }
            } else {
                sReturn = XmlUtil.mapToXmlError("0x50000001");
            }
        } catch (BusinessException e) {
            logger.error(e.getErrMsg());
            sReturn = getErrorXml(e, this.getClass().getName());
        } catch (Exception e) {
            logger.error(e.getMessage());
            sReturn = XmlUtil.mapToXmlError("0x50000027");
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }
    
    /**
	 * IPC请求设备文件列表
	 * @param sklModel
	 * @param SN
	 * @param serviceType
	 */
	@RequestMapping("/file/list.ipc")
    @ResponseName("appGetFileList")
	public void getUserAudioList(SKLModel sklModel, @Param String SN, @Param String serviceType) {
		// 检验参数非空
	    if (StringUtils.isBlank(SN) || StringUtils.isBlank(serviceType)) {
	    	AssertUtils.throwBusinessEx(0x50000001);
		}
		// 检验SN是否存在
		IPCamera ipCamera = ipcService.getIPCameraBySN(SN);
		if (ipCamera == null) {
			AssertUtils.throwBusinessEx(0x50000047);
		}
        List<FileIO> files =new ArrayList<FileIO>();
        
		// 根据S3ServiceType的类型，进行具体的业务逻辑处理
		// ------------wechat,fulin-------->>>
		if (StringUtils.equals(serviceType, S3ServiceType.SYSTEM_MUSIC.getType())
				|| StringUtils.equals(serviceType, S3ServiceType.SYSTEM_NOTICE.getType())
				|| StringUtils.equals(serviceType, S3ServiceType.SYSTEM_STORY.getType())) {
			
			List<Media> medias = new ArrayList<Media>();
			if (StringUtils.equals(serviceType, S3ServiceType.SYSTEM_MUSIC.getType())) {
				 medias = audioService.listSysMediaByType(Media.MEDIA_TYPE_MUSIC);
			}
	        if (StringUtils.equals(serviceType, S3ServiceType.SYSTEM_NOTICE.getType())) {
	        	 medias = audioService.listSysMediaByType(Media.MEDIA_TYPE_ALARM);
			}
	        if (StringUtils.equals(serviceType, S3ServiceType.SYSTEM_STORY.getType())) {
	        	 medias = audioService.listSysMediaByType(Media.MEDIA_TYPE_STORY);
			}
	        
			for (Media media : medias) {
				FileIO file = new FileIO();
				file.setUuid(media.getId().toString());
				file.setFileSize(media.getFileSize());
				file.setUrl(media.getFileUrl());
				file.setFileName(media.getFileName());
				files.add(file);
			}
		}
		// <<<---------wechat-----------
		sklModel.addAttribute("list", files);
	}
}
