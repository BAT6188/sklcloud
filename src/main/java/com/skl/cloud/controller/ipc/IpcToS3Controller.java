package com.skl.cloud.controller.ipc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amazonaws.HttpMethod;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.foundation.mvc.method.annotation.ResponseName;
import com.skl.cloud.foundation.mvc.model.SKLModel;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.audio.Alarm;
import com.skl.cloud.model.audio.Music;
import com.skl.cloud.model.audio.Story;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.audio.AudioPlayListService;
import com.skl.cloud.service.audio.AudioService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.S3ServiceType;

@RequestMapping("/skl-cloud/device")
@Controller
public class IpcToS3Controller extends IpcController {
	private static final Logger logger = Logger.getLogger(IpcToS3Controller.class);

	@Autowired(required = true)
	private S3Service s3Service;

	@Autowired(required = true)
	private AudioPlayListService audioPlayListService;

	@Autowired(required = true)
	private IPCameraService iPCameraService;
	
	@Autowired(required = true)
	private AudioService audioService;

	/**
	 * IPC请求获取用户文件上传
	 * 
	 * @param userId
	 * @param SN
	 * @param BusinessType
	 * @return
	 */
	@RequestMapping("/file/url/request.ipc")
	@ResponseName("requestUrl")
	public void createUrlToS3(SKLModel sklModel, @Param String SN, @Param String serviceType, @Param String fileName,
			@Param String userId) {
		
		if (SN == null || SN.trim().equals("") || fileName == null || fileName.trim().equals("")) {
			throw new BusinessException(0x50000029,
					"sorry, missing element node in the XML document, please check if there is a missing parameter");
		}

		try {
			// 判断数据库中是否存在这种类型的serviceType
			if (!s3Service.isExistServiceType(serviceType)) {
				// 容错处理
				throw new BusinessException(0x50000050, "can not find the information in DB");
			}

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("deviceSn", SN);
			paramMap.put("userId", String.valueOf(userId));
			paramMap.put("SN", SN);
			paramMap.put("fileName", fileName);
			
			String uuid = UUID.randomUUID().toString();
			String urlPath = s3Service.getUrlAndSaveInfo(serviceType, uuid, paramMap);
			
			sklModel.addAttribute("uuid", uuid);
			sklModel.addAttribute("url", S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(urlPath + fileName), HttpMethod.PUT));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ManagerException ex = new ManagerException(e);
			throw new BusinessException(ex.getErrorCode());
		}
	}

	/**
	 * IPC进行设备文件上传完成确认
	 * 
	 */
	@RequestMapping("/file/upload/success/confirm.ipc")
	public ResponseEntity<String> userConfirmUploadS3Success(@Param String SN, @Param String uuid,
			@Param String serviceType) {
		String sReturn = null;

		try {
			if (SN == null || uuid == null || serviceType == null || SN.trim().equals("")
					|| uuid.trim().equals("") || serviceType.trim().equals("")) {
				// 请求参数有空值
				throw new BusinessException("0x50000048");
			}
			
			S3FileData fileData = s3Service.getCheckUploadFile(uuid, serviceType, SN);
			
			String s3Key = StringUtil.convertToS3Key(fileData.getFilePath() + fileData.getFileName());
			long fileSize = S3Factory.getDefault().getFile(s3Key).getContentLength();
			s3Service.updateUploadFile(uuid, serviceType, fileData.getFileName(), fileSize);
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn.toString(), HttpStatus.OK);
	}

	/**
	 * <IPC进行文件下载完成确认>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/file/download/success/confirm.ipc", method = RequestMethod.POST)
	public ResponseEntity<String> confirmDownloadFileSuccess(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;
		try {
			//为了配合wechat项目，不进行参数的正则校验
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req, false);

			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
			String serviceType = XmlUtil.convertToString(paraMap.get("serviceType"), false);
			String uuid = XmlUtil.convertToString(paraMap.get("uuid"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);

			// 校验：设备是否存在
			IPCamera ipCamera = iPCameraService.getIPCameraBySN(sn);
			if (ipCamera == null) {
				throw new BusinessException("0x50000047");
			}
			
			// --------------wechat,fulin------->>>>>>>
	        String wechatMusicType = "device_wechat_music";
	        String wechatAlarmType = "device_wechat_alarm";
	        String wechatStoryType = "device_wechat_story";
	        
			if (StringUtils.equals(serviceType, wechatMusicType)
					|| StringUtils.equals(serviceType, wechatAlarmType)
					|| StringUtils.equals(serviceType, wechatStoryType)) {
				
				if (serviceType.equals(wechatMusicType)) {
	        		// 根据musicId和 sn查询出对应的music信息
	                Music music = audioService.getMusicByIdSn(sn, Long.valueOf(uuid));
	    			if (music != null) {
	    				// 判断请求参数fileName的准确性
	                    if (music.getMedia() != null) {
							if (!music.getMedia().getFileName().equals(fileName)) {
								throw new BusinessException("0x50000001");
							}
						}else {
							throw new BusinessException("0x50000002");
						}
	    				// 修改music任务的是否成功状态
	    				audioService.setMusicStatus(music.getId(), Music.STATUS_NORMAL);

	    			}else {
	    				throw new BusinessException("0x50000002");
	    			}
	    		}
	    		
	    		if (serviceType.equals(wechatAlarmType)) {
	    			// 根据alarmId查询出对应的alarm信息
	    			Alarm alarm = audioService.getAlarmById(Long.valueOf(uuid));
	    			
	    			if (alarm != null) {
	    				// 判断请求参数SN的准确性
	    				if (!alarm.getCameraSn().equals(sn)) {
							throw new BusinessException("0x50000001");
						}
	    				// 判断请求参数fileName的准确性
	                    if (alarm.getMedia() != null) {
							if (!alarm.getMedia().getFileName().equals(fileName)) {
								throw new BusinessException("0x50000001");
							}
						}else {
							throw new BusinessException("0x50000002");
						}
	                    // 修改alarm任务的是否成功状态
						audioService.setAlarmStatus(alarm.getId(), Alarm.STATUS_NORMAL);
					}else {
						throw new BusinessException("0x50000002");
					}
	    		}
	    		
	            if (serviceType.equals(wechatStoryType)) {
	            	// 根据storyId查询出对应的story信息
	    			Story story = audioService.getStoryById(Long.valueOf(uuid));
	    			if (story != null) {
	    				// 判断请求参数SN的准确性
	    				if (!story.getCameraSn().equals(sn)) {
							throw new BusinessException("0x50000001");
						}
	    				// 判断请求参数fileName的准确性
	                    if (story.getMedia() != null) {
							if (!story.getMedia().getFileName().equals(fileName)) {
								throw new BusinessException("0x50000001");
							}
						}else {
							throw new BusinessException("0x50000002");
						}
	                    // 修改alarm任务的是否成功状态
	    				audioService.setStoryStatus(story.getId(), Story.STATUS_NORMAL);
					} else {
	    				throw new BusinessException("0x50000002");
					}
	            }
	            // <<<<<<---------wechat-----------
			} else {
			    S3FileData fileData = s3Service.getCheckUploadFile(uuid, serviceType, sn);

				// 获取下载文件信息比对(名称)
				if (!fileName.equals(fileData.getFileName())) {
					throw new BusinessException("0x50020022");
				}

				// 根据S3ServiceType的类型，进行具体的业务逻辑处理
				if (S3ServiceType.DEVICE_MUSIC.getType().equals(serviceType)) {
					// 更新audio.play数据列表
					audioPlayListService.updateAudioPlayStatus(uuid, "1");
				}
			}
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
