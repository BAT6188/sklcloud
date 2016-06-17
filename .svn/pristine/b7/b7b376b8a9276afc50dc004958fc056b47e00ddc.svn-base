package com.skl.cloud.controller.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.constants.S3ServiceType;
import com.skl.cloud.util.constants.Constants.ServerSystemId;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.controller.web.dto.ShareStatusFO;
import com.skl.cloud.dao.user.ShareMapper;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.model.sub.SubsysStreamStatus;
import com.skl.cloud.model.user.Share;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.remote.ipc.dto.ipc.RequestCurrentPictrueIO;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.service.sub.SubsysStreamStatusService;
import com.skl.cloud.service.user.WechatUserService;

/**
 * @ClassName: IPCameraController
 * @Description: User request IPC information from wechat
 * @author yangbin
 * @date 2015年10月8日
*/

@Controller
@RequestMapping("/skl-cloud/wechat")
public class IPCameraController extends FrontController {
	private static final Logger logger = Logger.getLogger(IPCameraController.class);

    
    @Autowired
    private WechatUserService userService;

    @Autowired
    private IPCameraService ipcService;
    
    @Autowired
    private StreamResourcesService streamResourcesService;

	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private StreamStatusService streamStatusService;
	
	@Autowired
	private SubsysStreamStatusService subsysStreamStatusService;
	
	@Autowired
	private StreamStopService streamStopService;
    
	@Autowired
	private ShareMapper shareMapper;
	
    /**
     * 微信用户查询设备是否在线
     * @param openId
     * @param deviceId
     */
    @RequestMapping("/device/queryOnline.json")
    public void queryIPCIsOnline(@Param String openId, @Param String deviceId, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        validateParam(openId, deviceId);

        // 2> 得到IPC设备云端的信息
        IPCamera ipcamera = userService.getUserBindDevice(openId, deviceId);
        if (ipcamera == null) {
			AssertUtils.throwBusinessEx(0x50030002);
		}
        model.addAttribute("status",
                ipcamera.getIsOnline() != null ? (ipcamera.getIsOnline() ? Constants.Code.CODE_1.getStringValue()
                        : Constants.Code.CODE_0.getStringValue()) : Constants.Code.CODE_0.getStringValue());
    }

    /**
     * 获取用户某个ipc的设置
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/getSettting.json")
    public void queryIPCSettting(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
    	// 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);

        // 2.0>检查SN与这个deviceId绑定的SN是否对应
        AssertUtils.existDB(ipcamera, new String[] { "sn" });
        if (!ipcamera.getDeviceId().equals(deviceId)) {
            throw new BusinessException(0x50000001, "deviceId [" + deviceId + "] not match to the sn.");
        }

        boolean needUpdate = Boolean.FALSE;

        // 2.1> 若云端Motion/Sound Detection 为null,则将状态改为1，默认为推送
        if (ipcamera.getMotionAlertStatus() == null) {
        	ipcamera.setMotionAlertStatus(1);
        	needUpdate = Boolean.TRUE;
		}
        if (ipcamera.getSoundAlertStatus() == null) {
        	ipcamera.setSoundAlertStatus(1);
        	needUpdate = Boolean.TRUE;
		}
        // 2.2> 如果云端没有Volume数据，则从IPC Remote端获取
        if (ipcamera.getSpeakerVolume() == null) {
            ipcamera = ipcService.getIPCRemoteVolume(ipcamera);
            needUpdate = Boolean.TRUE;
        }
        if (needUpdate) {
            // 同步设置到云端
            ipcService.updateIPCamera(ipcamera);
        }

        // 3> 組裝返回数据
        model.addAttribute("ipcName", ipcamera.getNickname());
        model.addAttribute("status", ipcamera.getLiveShareStatus());
        model.addAttribute("speakerVolume", ipcamera.getSpeakerVolume());
        model.addAttribute("motion", ipcamera.getMotionAlertStatus());
        model.addAttribute("sound", ipcamera.getSoundAlertStatus());
    }

    /**
     * 获取IPC Motion/Sound Detection 设置数据
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/getDetection.json")
    public void queryIPCDetection(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);

        // 2.1> 若云端Motion/Sound Detection 为null,则将状态改为1，默认为推送
        boolean needUpdate = Boolean.FALSE;
        if (ipcamera.getMotionAlertStatus() == null) {
        	ipcamera.setMotionAlertStatus(1);
        	needUpdate = Boolean.TRUE;
		}
        if (ipcamera.getSoundAlertStatus() == null) {
        	ipcamera.setSoundAlertStatus(1);
        	needUpdate = Boolean.TRUE;
		}
        // 2.2> 同步设置到云端
        if (needUpdate) {
            ipcService.updateIPCamera(ipcamera);
        }
        // 3> 組裝返回数据
        model.addAttribute("motion", ipcamera.getMotionAlertStatus());
        model.addAttribute("sound", ipcamera.getSoundAlertStatus());
    }

    /**
     * 设置IPC Motion/Sound Detection 参数
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     * @param motion
     * @param sound
     */
    @RequestMapping("/device/setDetection.json")
    public void updateIPCDetection(@Param String openId, @Param String deviceId, @Param String sn, @Param Integer motion,
            @Param Integer sound, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        AssertUtils.notNull(motion, new String[] { "motion" });
        AssertUtils.notNull(sound,  new String[] { "sound" });

        // 2> 得到用户绑定相应IPC设备的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);
        if (motion == 0 || motion == 1) {
        	ipcamera.setMotionAlertStatus(motion);
		}else {
			 AssertUtils.formatNotCorrect(new String[] { "motion:" + motion });
		} 
        if (sound == 0 || sound == 1) {
        	ipcamera.setSoundAlertStatus(sound);
		}else {
			 AssertUtils.formatNotCorrect(new String[] { "sound:" + sound});
		}
        
        // 3> 更新设置到云端
        ipcService.updateIPCamera(ipcamera);

    }

    /**
     * 获取某个ipc上的音量
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/getSpeakerVolume.json")
    public void queryIPCSpeakerVolume(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);

        // 如果云端没有数据则从IPC Remote端获取
        if (ipcamera.getSpeakerVolume() == null) {
            ipcamera = ipcService.getIPCRemoteVolume(ipcamera);
            // 同步设置到云端
            ipcService.updateIPCamera(ipcamera);
        }
        model.addAttribute("speakerVolume", ipcamera.getSpeakerVolume());
    }

    /**
     * 设置IPC上的音量, 百分比数值。
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/setSpeakerVolume.json")
    public void updateIPCSpeakerVolume(@Param String openId, @Param String deviceId, @Param String sn,
            @Param long speakerVolume, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        AssertUtils.notNull(speakerVolume, new String[] { "speakerVolume" });
        if (speakerVolume <= 0 || speakerVolume > 100) {
            AssertUtils.formatNotCorrect(new String[] { "speakerVolume:" + speakerVolume }); 
        }

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);
        ipcamera.setSpeakerVolume(speakerVolume);

        // 3> 同步设置到IPC设备端
        ipcService.updateIPCRemoteVolume(ipcamera);
        
        // 4> 更新设置到云端
        ipcService.updateIPCamera(ipcamera);
    }

    /**
     * 获取IPC直播视频流URL
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/liveUrl.json")
    public void getLiveRelayUrlBySn(@Param String openId, @Param String deviceId, @Param String sn,
            Model model) {
		
    	// 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipCamera = validateBind(user.getUserId(), sn);
        
        // 3> 判断设备的心跳是否正常、是否在线
        Boolean isLive = ipCamera.getIsLive();
		Boolean isOnLine = ipCamera.getIsOnline();
		if (isLive != true || isOnLine != true) {
			// 3.1> 设备不在线，抛出异常提示
			AssertUtils.throwBusinessEx(0x50020062);
		}
		
		StreamStatusCount streamStatusCount = new StreamStatusCount();
		streamStatusCount.setSn(sn);
		StreamStatusCount streamStatus = streamStatusService.selectStatusCount(streamStatusCount);

		if (null == streamStatus) {

			logger.error("********查询到对应的IPC设备的流通道信息为null，流资源调度失败********");
			AssertUtils.throwBusinessEx(0x50020087);
		}
		// 容错处理：IPC流通道参数每个字段都不能为空
		if (StringUtils.isBlank(streamStatus.getChannelId()) || StringUtils.isBlank(streamStatus.getChannelName())
				|| StringUtils.isBlank(streamStatus.getStreamType())
				|| StringUtils.isBlank(streamStatus.getVideoCode())
				|| StringUtils.isBlank(streamStatus.getAudioCode())
				|| StringUtils.isBlank(streamStatus.getResolutionH())
				|| StringUtils.isBlank(streamStatus.getResolutionW())) {

			logger.info("********查询到的IPC流通道参数信息中存在某个字段是空********");
			AssertUtils.throwBusinessEx(0x50020089);
		}
		
        // 4> 获取视频直播流的URL
        String [] liveUrl = new String [2];
        String liveRtspUrl = "";
        String liveHlsUrl = "";
        
        // 满足提供Relay的条件，查询对应的流资源调度的标记和接入流的方式
		String streamStatusFlg = streamStatus.getStreamStatus();
		String streamAccessType = streamStatus.getStreamType();

		// 判断是否为RTP推流，标记为ReadyRelay，代表该设备已经触发了流资源调度
		if (streamAccessType.equalsIgnoreCase("PUSH_TS_RTP_TCP")
				&& streamStatusFlg.equalsIgnoreCase("ReadyRelay")) {

			// 1.对已接入的App用户数进行+1更改
			streamStatus.setCurAccessRelayNum((streamStatus
					.getCurAccessRelayNum() == null ? 0 : streamStatus
					.getCurAccessRelayNum()) + 1);

			// 2.记录Relay开始时间
			streamStatus.setRelayStreamUpTime(new Date());

			// 3.Relay成功次数增1
			int relaySuccessNum = streamStatus.getTotalRelayPlayNum() == null ? 0
					: streamStatus.getTotalRelayPlayNum();

			streamStatus.setTotalRelayPlayNum(relaySuccessNum + 1);
			streamStatusCount.setScheduleFailNum(0);
			streamStatusService.updateStatusCount(streamStatus);

			// 3.返回url
			liveRtspUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "rtsp");
	        liveHlsUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "hls");
	        liveUrl [0] = liveHlsUrl;
	        liveUrl [1] = liveRtspUrl;
	        
	        model.addAttribute("urls", liveUrl);
			logger.info("********视频直播Relay的URL地址是:" + liveRtspUrl +"," + liveHlsUrl + "********");

		}
		// 流资源调度标记为LoadingRelay时，代表该设备正在被其他App用户请求，需要等待并定时检查
		if (streamAccessType.equalsIgnoreCase("PUSH_TS_RTP_TCP")
				&& streamStatusFlg.equalsIgnoreCase("LoadingRelay")) {

			// 每隔一秒查询，共三次
			for (int i = 0; i < 3; i++) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new BusinessException(e.getMessage());
				}

				StreamStatusCount streamStatusTmp = streamStatusService
						.selectStatusCount(streamStatusCount);
				streamStatusFlg = streamStatusTmp.getStreamStatus();

				if (streamStatusFlg.equalsIgnoreCase("ReadyRelay")) {

					// if (streamStatusTmp.getCurAccessRelayNum() != null) {
					//
					// if (streamStatusTmp.getCurAccessRelayNum() > maxRelayNum)
					// {
					//
					// throw new BusinessException("0x50020084");
					// }
					// } else {
					// logger.info("********当前设备已提供Relay的用户数在数据库中的字段是null，查不出具体值********");
					// }

					// 1.对已经接入的App用户数进行+1更改
					streamStatus.setCurAccessRelayNum((streamStatus
							.getCurAccessRelayNum() == null ? 0 : streamStatus
							.getCurAccessRelayNum()) + 1);

					// 2.记录Relay开始时间
					streamStatus.setRelayStreamUpTime(new Date());

					// 3.Relay成功次数增1
					int relaySuccessNum = streamStatus.getTotalRelayPlayNum() == null ? 0
							: streamStatus.getTotalRelayPlayNum();

					streamStatus.setTotalRelayPlayNum(relaySuccessNum + 1);
					streamStatusCount.setScheduleFailNum(0);
					streamStatusService.updateStatusCount(streamStatus);

					// 4.返回url
					liveRtspUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "rtsp");
			        liveHlsUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "hls");
			        liveUrl [0] = liveHlsUrl;
			        liveUrl [1] = liveRtspUrl;
			        
			        model.addAttribute("urls", liveUrl);
					logger.info("********视频直播Relay的URL地址是:" + liveRtspUrl +"," + liveHlsUrl + "********");
				}

			}
			AssertUtils.throwBusinessEx(0x50020083);
		}

		// 流资源调度标记为非ReadyRelay和非LoadingRelay：则要进行流资源调度的流程
		if (streamAccessType.equalsIgnoreCase("PUSH_TS_RTP_TCP")
				&& !streamStatusFlg.equalsIgnoreCase("ReadyRelay")
				&& !streamStatusFlg.equalsIgnoreCase("LoadingRelay")) {

			// 在进行流资源调度之前先检测子系统是否已经释放残余资源
			SubsysStreamStatus subsysStreamStatus = new SubsysStreamStatus();
			subsysStreamStatus.setCameraSn(sn);

			List<SubsysStreamStatus> list = subsysStreamStatusService
					.select(subsysStreamStatus);

			if (list.size() >= 1) {
				// 若没释放资源则请求释放资源
				try {
					streamStopService.stopSubSystem("", sn,
							ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);

				} catch (Exception e) {
					logger.error("**********请求释放子系统残余的资源时出错**********");
					AssertUtils.throwBusinessEx(0x50020088);
				}

			}

			streamStatus.setStreamStatus("LoadingRelay");

			// 更改标记为LoadingRelay
			streamStatusService.updateStatusCount(streamStatus);

			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("sn", sn);
			requestMap.put("streamAccessType", streamAccessType);

			// 调用流资源调度模块代码
			String responseXml = streamResourcesService
					.StreamResourceSchedule(requestMap);

			logger.info("********流资源调度完成后返回的xml:" + responseXml + "********");

			int i = responseXml.indexOf("</status>");
			String status = responseXml.charAt(i - 1) + "";
			// 判断流资源调度是否成功
			if (status.trim().equals("0")) {

				streamStatus = streamStatusService
						.selectStatusCount(streamStatusCount);

				// 1.对已接入的App用户数进行+1更改
				streamStatus.setCurAccessRelayNum((streamStatus
						.getCurAccessRelayNum() == null ? 0 : streamStatus
						.getCurAccessRelayNum()) + 1);

				// 2.记录Relay开始时间
				streamStatus.setRelayStreamUpTime(new Date());

				// 3.Relay成功次数增1
				int relaySuccessNum = streamStatus.getTotalRelayPlayNum() == null ? 0
						: streamStatus.getTotalRelayPlayNum();

				streamStatus.setTotalRelayPlayNum(relaySuccessNum + 1);
				streamStatusCount.setScheduleFailNum(0);
				streamStatusService.updateStatusCount(streamStatus);

				// 得到对应的视频直播Relay的URL
				liveRtspUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "rtsp");
		        liveHlsUrl = streamResourcesService.getLiveRelayURL(sn, user.getUserId(), "hls");
		        liveUrl [0] = liveHlsUrl;
		        liveUrl [1] = liveRtspUrl;
		        
		        model.addAttribute("urls", liveUrl);
				logger.info("********视频直播Relay的URL地址是:" + liveRtspUrl +"," + liveHlsUrl + "********");
			} else {
				AssertUtils.throwBusinessEx(0x50010016);
			}

		}
	}
    
    
    
    /**
     * 获取IPC分享Url：linkUrl，imgUrl，dataUrl等
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     */
    @RequestMapping("/device/shareUrl.json")
    public void queryIPCShareUrl(@Param String openId, @Param String deviceId, @Param String sn, @Param Long duration, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);
        if (duration == null) {
			AssertUtils.throwBusinessEx(0x50000001, new String[] { "duration"});
		}
        if (duration < -1 || duration ==0) {
        	AssertUtils.throwBusinessEx(0x50000001, new String[] { "duration:" + duration });
		}
        
        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);

        // 3> 得到视频分享的linkUrl,imgUrl,dataUrl
        Share share = ipcService.createIPCUserShare(user.getUserId(), duration, ipcamera);

        // 分享页面链接, 云端不提供
        model.addAttribute("linkUrl", share.getLinkUrl());
        // 分享显示图标的链接
        model.addAttribute("imgUrl", share.getImgUrl());
        // 视频或者音乐的链接
        model.addAttribute("dataUrl", share.getDataUrl());
    }

    /**
     * 修改IPC的名字
     * @param openId
     * @param deviceId the camera deviceId from wechat
     * @param sn the camera serial number
     * @param newName the camera new name
     */
    @RequestMapping("/device/modifyName.json")
    public void updateIPCNickname(@Param String openId, @Param String deviceId, @Param String sn,
            @Param String newName, Model model) {
        // 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);
        AssertUtils.isNotBlank(newName, new String[] { "newName"});

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);
        ipcamera.setNickname(newName);

        // 3> 更新数据
        ipcService.updateIPCamera(ipcamera);
    }

    /**
     * 请求某微信用户下的IPC实时预览图片列表
     * @param openId wechat openId
     */
    @RequestMapping("/device/previewList.json")
    public void queryIPCPreviewImg(@Param String openId, HttpServletResponse resp) {

    	// 1> 检查数据非空/用户是否存在/是否关注公众号	
        validateParam(openId);
        
        // 2> 通过opendId得到用户所绑定的所有IPC设备
        List<IPCamera> ipcList = userService.getUserBindDeviceList(openId);
        // 3> 组装返回信息
        List<Map<String, String>> imgUrls = new ArrayList<Map<String, String>>();
        String  serviceType = S3ServiceType.SYSTEM_DEVICE_WECHAT_PICTURE.getType();  
        String fileName = "device_wechat_picture.jpg";
        String url = "";
        String filePath = "";
       
        for (IPCamera ipcamera : ipcList) {
        	Map<String, String> map = new HashMap<String, String>();
            map.put("deviceId", ipcamera.getDeviceId());
            try {
            	Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("SN", ipcamera.getSn());
                filePath = s3Service.getUrlOfSystemSpaceByType(S3ServiceType.SYSTEM_DEVICE_WECHAT_PICTURE, paramMap);
				// ipc的SnapshotUuid为空（ipc从没抓拍过），fileName取默认值，否则以ipc上传的名字为证（已抓拍过，数据库有数据）
            	if (ipcamera.getSnapshotUuid() != null) {
					S3FileData file = s3Service.getUploadFileByUuid(ipcamera.getSnapshotUuid());
					if (file.getFileName() != null) {
						fileName = file.getFileName();
					}
				}
            	url = filePath + fileName;
			} catch (Exception e) {
				logger.info("**********获取url失败，请检查表t_platform_S3_Basic_Data是否有与" + serviceType + "相关的值*********");
			}
            map.put("url", url);
            imgUrls.add(map);
		}
        
        // 4> 异步处理，先返回结果
        JSONObject jsonResult = null;
        Map<String,Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("code", "0");
        resultMap.put("msg", "success");
        Map<String,Object> data = new LinkedHashMap<String, Object>();
        data.put("imgUrlList", imgUrls);
        resultMap.put("data", data);
        jsonResult = JSONObject.fromObject(resultMap);
        PrintWriter out;
		try {
			out = resp.getWriter();
			out.print(jsonResult);
			out.flush();
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
        // 5> 通知设备抓拍
        for (IPCamera ipcamera : ipcList) {
            if (ipcamera.getIsLive()) {
                // 通知各个设备实时抓拍图片上传到S3
                RequestCurrentPictrueIO requestCurrentPictrueIO = new RequestCurrentPictrueIO();
                requestCurrentPictrueIO.setServiceType(serviceType);
                String uuid = UUID.randomUUID().toString();
                // 判断设备是否已经存在某一个SnapshotUuid，有，uuid取以前的；否则为新生成的
                if (ipcamera.getSnapshotUuid() != null) {
                	uuid =ipcamera.getSnapshotUuid();
				}
                requestCurrentPictrueIO.setUuid(uuid);  
                
                try {
                	// 若表t_platform_S3_Basic_Data有对应的serviceType数据才通知ipc上传数据并记录到数据库
                	Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("SN", ipcamera.getSn());
                	String urlPath = s3Service.getUrlOfSystemSpaceByType(S3ServiceType.SYSTEM_DEVICE_WECHAT_PICTURE, paramMap);
                    
                	if ( urlPath != null) {
                         requestCurrentPictrueIO.setUrlPath(urlPath + fileName);
						 ipcService.requestCurrentPictrue(ipcamera.getSn(), requestCurrentPictrueIO);
						 if (ipcamera.getSnapshotUuid() == null) {
			                	// 新生成数据保存到数据库表t_platform_s3_file_data
						        S3FileData s3File = new S3FileData();
						        s3File.setFilePath(urlPath);
						        s3File.setUuid(uuid);
						        s3File.setFileName(fileName);
						        s3File.setServiceType(serviceType);
						        s3File.setFileStatus("0");
						        s3File.setDeviceSn(ipcamera.getSn()); 
						        s3Service.saveUploadFile(s3File);
			                    
			                    // 并将该uuid记录到ipc表
			                	IPCamera ipc = ipcService.getIPCameraBySN(ipcamera.getSn());
				                ipc.setSnapshotUuid(uuid);
				                ipcService.updateIPCamera(ipc); 
							}
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1); 
					logger.info("**********没法通知设备sn:" + ipcamera.getSn() + "实时抓拍，请检查表t_platform_S3_Basic_Data是否有与serviceType：" + serviceType + "相关的值或是否已成功连接设备*********");
				}
               
            }
        }
    }
    
    /**
     * 获取直播分享ID对应的参数
     * @param sid
     * @param model
     */
    @RequestMapping("/share/sid.json")
    public void queryShareStatus(@Param String sid, Model model) {
    	// 1> 检查数据非空
    	AssertUtils.notNull(sid, new String[] { "sid" });
    	// 2> 获取分享相关信息
    	ShareStatusFO shareStatus = ipcService.queryShareStatus(sid);
    	model.addAttribute("expireAt", shareStatus.getExpireAt());
    	model.addAttribute("linkUrl", shareStatus.getLinkUrl());
    	model.addAttribute("imageUrl", shareStatus.getImgUrl());
    	model.addAttribute("isExpire", shareStatus.getIsExpire());
    	model.addAttribute("openId", shareStatus.getOpenId());
    	model.addAttribute("deviceId", shareStatus.getDeviceId());
    	model.addAttribute("sn", shareStatus.getSn());
    	model.addAttribute("deviceName", shareStatus.getDeviceName());
    	
	}
    
    /**
     * 获取直播分享ID对应的dataUrl
     * @param sid
     * @param model
     */
    @RequestMapping("/device/share/dataUrl.json")
    public void queryShareLiveUrl(@Param String sid, Model model) {
    	// 1> 检查数据非空
    	AssertUtils.notNull(sid, new String[] { "sid" });
    	// 2> 获取有时限的直播流Url
    	String[] dataUrls = ipcService.queryShareLiveUrl(sid);
    	model.addAttribute("dataUrls", dataUrls);
    }
    
    /**
     * 设置ipc的直播url分享的使能
     * @param openId
     * @param deviceId
     * @param sn
     * @param status
     * @param model
     */
    @RequestMapping("/share/setShareStatus.json")
    public void setShareStatus(@Param String openId, @Param String deviceId, @Param String sn,
            @Param Integer status, Model model) {
    	// 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);
        
        // 3> 检查status参数是否正确
    	AssertUtils.notNull(status, new String[] { "status" });
    	
    	if (status ==0 || status == 1) {
    		
    		ipcamera.setLiveShareStatus(status); 

            // 4> 更新数据
            ipcService.updateIPCamera(ipcamera);
        	
            // 5> 如果为关闭分享，还需将分享表里该ipc的所有分享删掉（注：由于获取relay直播流url时也会在分享表生成一条分享（start_date与end_date均为null），但此条分享不能删除）
            if (status == 0) {
            	ipcService.deleteShareBySnExceptRelay(sn);
    		}
		}else {
			AssertUtils.formatNotCorrect(new String[] { "status:" +status });
		}
    	
    	
    }
    
    /**
     * 获取ipc的直播url分享的使能
     * @param openId
     * @param deviceId
     * @param sn
     * @param model
     */
    @RequestMapping("/share/getShareStatus.json")
    public void getShareStatus(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
    	// 1> 检查数据非空/用户是否存在/是否关注公众号
        WechatUser user = validateParam(openId, deviceId, sn);

        // 2> 得到用户绑定相应IPC设备云端的信息
        IPCamera ipcamera = validateBind(user.getUserId(), sn);
        
        // 3> 返回分享使能状态
        model.addAttribute("status",  ipcamera.getLiveShareStatus());
    }
    
    /**
     * 验证请求过来的参数是否合法并正确
     */
    private WechatUser validateParam(String openId) {
        AssertUtils.isNotBlank(openId, new String[] {"openId"});
        WechatUser wechatUser = userService.getUserByOpenId(openId);
        if (wechatUser == null) {
        	AssertUtils.throwBusinessEx(0x50030000, new String[] {"openId:" + openId});
	    }

        boolean subscribeFlag = WechatUser.SUBSCRIBE_FLAG_YES == wechatUser.getSubscribeFlag();
        AssertUtils.isSubscribe(subscribeFlag, new String[] {openId});
        
        return wechatUser;
    }

    /**
     * 验证请求过来的参数是否合法并正确
     */
    private WechatUser validateParam(String openId, String deviceId) {
        WechatUser wechatUser = validateParam(openId);
        AssertUtils.isNotBlank(deviceId, new String[] {"deviceId"});
        return wechatUser;
    }

    /**
     * 验证请求过来的参数是否合法并正确
     */
    private WechatUser validateParam(String openId, String deviceId, String sn) {
        WechatUser wechatUser = validateParam(openId, deviceId);
        AssertUtils.isNotBlank(sn, new String[] {"sn"});
        return wechatUser;
    }

    /**
     * 验证微信用户与设备是否绑定
     */
    private IPCamera validateBind(long userId, String sn) {
        IPCamera ipcamera = userService.getUserBindDevice(userId, sn);
        if (ipcamera == null) {
			AssertUtils.throwBusinessEx(0x50030002);
		}
        return ipcamera;
    }
}
