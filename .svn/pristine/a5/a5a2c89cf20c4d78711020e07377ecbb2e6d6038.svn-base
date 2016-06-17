package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.service.sub.SubsysStreamStatusService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.constants.StreamProcessStatus;

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
@RequestMapping("/skl-cloud/app")
public class AppMiscellaneousController extends AppController {
	private static Logger logger = LoggerFactory.getLogger(AppMiscellaneousController.class);

	@Autowired(required = true)
	private StreamResourcesService streamResourcesService;

	@Autowired
	private IPCameraService ipcService;

	@Autowired
	private StreamStatusService streamStatusService;

	@Autowired
	private StreamStopService streamStopService;

	@Autowired
	private SubsysStreamStatusService subsysStreamStatusService;
	
	private final static String STREAM_ACCESS_TYPE_PTPT = "PUSH_TS_RTP_TCP";

	/**
	 * @Title: getAdressIpc
	 * @Description: app请求获取P2P/UPNP/STUN/指令中心信息
	 * @param ID
	 * @return ResponseEntity<String> (返回值说明)
	 * @author weibin
	 * @date 2015年12月08日
	*/
	@RequestMapping("/serverInfo")
	ResponseEntity<String> getAdressIpc() {
		return streamResourcesService.getAdress();
	}

	/**
	 * 获取视频直播Relay的URL
	 * @Title: getLiveRelayURL
	 * @param SN
	 * @return ResponseEntity<String>
	 * @author wangming; modify by fulin
	 * @date 2015年7月21日; modify at 2015/12/11
	 */
	@RequiresPermissions("video:relayLive:get")
	@RequestMapping("/streaming/snDevice/live/{SN}")
	public ResponseEntity<String> getLiveRelayURL(@PathVariable String SN) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		String mainUrl = "";
		// 获取请求用户的userId
		Long userId = this.getUserId();
		// 生成直播流的url
		try {
			mainUrl = streamResourcesService.getLiveRelayURL(SN, userId, "rtsp");
		} catch (BusinessException e) {
			logger.error("*****creat LiveRelayURL error ******", e);
			
			return new ResponseEntity<String>(XmlUtil.mapToXmlError("appQuerySnDeviceLive",
					e.getErrMsg()), HttpStatus.OK);
		}
		resultMap.put("mainUrl", mainUrl);
		return new ResponseEntity<String>(XmlUtil.mapToXmlRight("appQuerySnDeviceLive", resultMap), HttpStatus.OK);
	}

	/**    
	 * 
	  * getLiveRelayUrlBySn（APP获取指定设备的视频直播Relay的URL（推流））
	  * @Title: getLiveRelayUrlBySn
	  * @Description: TODO
	  * @param @param SN
	  * @param @return (参数说明)
	  * @return ResponseEntity<String> (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年1月20日
	 */
	@RequestMapping(value = "/push/streaming/snDevice/{streamType}/live/{SN}", method = RequestMethod.GET)
	public ResponseEntity<String> getLiveRelayUrlBySn(@PathVariable String streamType, @PathVariable String SN) {
		String mainUrl = "";
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		List<String> streamTypeList = new ArrayList<String>();
		streamTypeList.add("RTSP");
		streamTypeList.add("HLS");
		streamTypeList.add("RTMP");
		streamTypeList.add("RTMPS");

		String sReturn = null;
		try {
			// 记录日志
			PlatformLog pl = new PlatformLog();
			pl.setUserId(getUserId());
			pl.setSn(SN);
			pl.setModuleName("APP获取直播Relay的URL");
			pl.setLogContent("云端收到APP获取SN：" + SN + "直播Relay的URL请求。");
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			super.saveLog(pl);

			IPCamera ipCamera = ipcService.getIPCameraBySN(SN);
			// 校验SN是否存在
			if (ipCamera == null) {
				// 容错处理
				logger.error("****IPC的SN:"+SN +"****请求Relay接口开始时，Sn在数据库中不存在。********");
				throw new BusinessException("0x50020091");
			}
			
			if (!streamTypeList.contains(streamType)) {
				// 容错处理
				logger.error("****IPC的SN:"+SN +"****请求Relay接口开始时，请求的URL中包含了不支持的流类型。********");
				throw new BusinessException("0x50020092");
			}
			streamType = streamType.toLowerCase();

			boolean isLive = ipCamera.getIsLive() != null ? ipCamera.getIsLive() : false;
			boolean isOnLine = ipCamera.getIsOnline() != null ? ipCamera.getIsOnline() : false;

			if (!(isLive && isOnLine)) {

				// 容错处理:IPC不在线,流开关是关状态
				logger.error("****IPC的SN:" + SN + "****请求Relay接口开始时，检查到请求的IPC已离线，流开关是关的状态。********");
				throw new BusinessException("0x50020093");
			}

			if (isLive == false && isOnLine == true) {

				logger.error("****IPC的SN:" + SN + "****请求Relay接口开始时，检查到请求的IPC已离线，流开关是开的状态。********");
				throw new BusinessException("0x50020099");
			}

			if (isLive == true && isOnLine == false) {

				logger.error("****IPC的SN:" + SN + "****请求Relay接口开始时，检查到请求的IPC在线，流开关是关的状态。********");
				throw new BusinessException("0x50020100");
			}

			StreamStatusCount streamStatusCount = new StreamStatusCount();
			streamStatusCount.setSn(SN);

			StreamStatusCount streamStatus = streamStatusService.selectStatusCount(streamStatusCount);

			if (null == streamStatus) {

				logger.error("****IPC的SN:" + SN + "****查询到对应的IPC设备的流通道信息为null，流资源调度失败********");
				throw new BusinessException("0x50020094");
			}

			// 容错处理：IPC流通道参数每个字段都不能为空
			if (StringUtils.isBlank(streamStatus.getChannelId()) || StringUtils.isBlank(streamStatus.getChannelName())
					|| StringUtils.isBlank(streamStatus.getStreamType())
					|| StringUtils.isBlank(streamStatus.getVideoCode())
					|| StringUtils.isBlank(streamStatus.getAudioCode())
					|| StringUtils.isBlank(streamStatus.getResolutionH())
					|| StringUtils.isBlank(streamStatus.getResolutionW())) {

				logger.info("****IPC的SN:" + SN + "****查询到的IPC流通道参数信息中存在一个或者多个字段是空的情况********");
				throw new BusinessException("0x50020095");
			}

			// 记录relay请求总次数
			int relayReqNum = streamStatus.getTotalRelayReqNum() == null ? 0 : streamStatus.getTotalRelayReqNum();

			streamStatus.setTotalRelayReqNum(relayReqNum + 1);
			streamStatusService.updateStatusCount(streamStatus);

			// 1.设备已提供Relay的用户数是否达到配置表的上限值 (上限值暂时默认为10个用户数)(暂时注释)
			// int maxRelayNum = Integer.valueOf(SystemConfig.getProperty("stream.max.relay.num"));

			// 2.设备是否已提供P2P直播 (是否允许P2P与Relay同时支持)
			String isAllowP2PRelay = SystemConfig.getProperty("stream.allow.p2p.relay","1");

			// 1:不同时支持P2P与Relay
			if (isAllowP2PRelay.equals("1")) {
				if (streamStatus.getStreamStatus().equalsIgnoreCase("InP2PPlaying")) {
					logger.info("****IPC的SN:"+SN +"****请求Relay接口开始时，IPC设备已经提供了P2P。********");
					throw new BusinessException("0x50020096");
				}
			} else {
				logger.info("****IPC的SN:"+SN +"****同时支持P2P和Relay直播********");
			}
			// TODO 3.设备已提供Relay的总时长； 设备配置表中Relay总时长的限制；

			// 满足提供Relay的条件，查询对应的流资源调度的标记和接入流的方式
			String streamStatusFlg = streamStatus.getStreamStatus();
			String streamAccessType = streamStatus.getStreamType();
			//RTP
			if(STREAM_ACCESS_TYPE_PTPT.equalsIgnoreCase(streamAccessType)){
				// 判断是否为RTP推流，标记为ReadyRelay，代表该设备已经触发了流资源调度
				if (StreamProcessStatus.ReadyRelay.name().equalsIgnoreCase(streamStatusFlg)) {
					// 返回url
					mainUrl = streamResourcesService.getLiveRelayURL(SN, getUserId(), streamType);
					resultMap.put("mainUrl", mainUrl);
					sReturn = XmlUtil.mapToXmlRight("appQuerySnDeviceLive", resultMap);
				// 流资源调度标记为LoadingRelay时，代表该设备正在被其他App用户请求，需要等待并定时检查
				}else if(StreamProcessStatus.LoadingRelay.name().equalsIgnoreCase(streamStatusFlg)) {
					// 每隔一秒查询，共三次
					for (int i = 0; i < 3; i++) {
						Thread.sleep(1000);
	
						StreamStatusCount streamStatusTmp = streamStatusService.selectStatusCount(streamStatusCount);
						streamStatusFlg = streamStatusTmp.getStreamStatus();
	
						if (StreamProcessStatus.ReadyRelay.name().equalsIgnoreCase(streamStatusFlg)) {
							// 返回url
							mainUrl = streamResourcesService.getLiveRelayURL(SN, getUserId(), streamType);
							resultMap.put("mainUrl", mainUrl);
							sReturn = XmlUtil.mapToXmlRight("appQuerySnDeviceLive", resultMap);
						}
					}

					logger.info("****IPC的SN:" + SN
							+ "****在Relay调度开始之前，流标记一直显示是\"LoadingRelay\",流资源调度的资源被其他用户占用，暂时获取不到资源。********");
					throw new BusinessException("0x50020097");

					// 流资源调度标记为非ReadyRelay和非LoadingRelay：则要进行流资源调度的流程
				} else if (!StreamProcessStatus.ReadyRelay.name().equalsIgnoreCase(streamStatusFlg)
						&& !StreamProcessStatus.LoadingRelay.name().equalsIgnoreCase(streamStatusFlg)) {

					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("sn", SN);
					requestMap.put("streamAccessType", streamAccessType);
					// 调用流资源调度模块代码
					String responseXml = streamResourcesService.StreamResourceSchedule(requestMap);

					logger.info("****IPC的SN:" + SN + "****流资源调度完成后返回的xml:" + responseXml + "********");
					int i = responseXml.indexOf("</status>");
					String status = responseXml.charAt(i - 1) + "";
					// 判断流资源调度是否成功
					if (status.trim().equals("0")) {
						// 得到对应的视频直播Relay的URL
						mainUrl = streamResourcesService.getLiveRelayURL(SN, getUserId(), streamType);
						resultMap.put("mainUrl", mainUrl);
						sReturn = XmlUtil.mapToXmlRight("appQuerySnDeviceLive", resultMap);
					} else {
						throw new BusinessException("0x50010016");
					}
				}
			}
			// 记录日志
			pl.setLogContent("云端处理APP获取SN：" + SN + "直播Relay的URL请求成功。");
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			super.saveLog(pl);
		} catch (Exception e) {
			sReturn = getErrorXml("appQuerySnDeviceLive", e, this.getClass().getName());
			// 记录日志
			ManagerException ex = new ManagerException(e);
			PlatformLog pl = new PlatformLog();
			pl.setUserId(getUserId());
			pl.setSn(SN);
			pl.setModuleName("APP获取直播Relay的URL");
			pl.setLogContent("APP请求获取SN：" + SN + "直播Relay的URL失败。错误信息为：" + ex.getErrorCode() + "--" + ex.getErrMsg());
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			try {
				super.saveLog(pl);
				//更新不对的状态,失败次数加1
				StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySNAndChanneId(SN, "MAIN");
				streamStatus.setScheduleFailNum(streamStatus.getScheduleFailNum() + 1);
				if (streamStatus != null
						&& StreamProcessStatus.ReadyRelay.name().equals(streamStatus.getStreamStatus())) {
					streamStatus.setStreamStatus(StreamProcessStatus.Free.name());
				}
				streamStatusService.updateStatusCount(streamStatus);
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
