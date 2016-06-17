package com.skl.cloud.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.dao.StreamResourcesMapper;
import com.skl.cloud.dao.user.ShareMapper;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;
import com.skl.cloud.model.StreamingChannel;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.model.sub.SubsysAddress;
import com.skl.cloud.model.sub.SubsysStreamStatus;
import com.skl.cloud.model.user.Share;
import com.skl.cloud.remote.ipc.IPCameraRemote;
import com.skl.cloud.remote.ipc.dto.ipc.IPCStreamControlIO;
import com.skl.cloud.remote.ipc.dto.ipc.ReceiverAddress;
import com.skl.cloud.remote.stream.StreamSubSystemRemote;
import com.skl.cloud.remote.stream.dto.InputStreamIO;
import com.skl.cloud.remote.stream.dto.LiveStreamServiceControlIO;
import com.skl.cloud.remote.stream.dto.OutputStreamIO;
import com.skl.cloud.remote.stream.dto.StreamControlIO;
import com.skl.cloud.remote.stream.dto.StreamSourceIO;
import com.skl.cloud.service.LogManageService;
import com.skl.cloud.service.P2pService;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.service.sub.SubsysStreamStatusService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.constants.StreamProcessStatus;
import com.skl.cloud.util.constants.Constants.ServerSystemId;
import com.skl.cloud.util.constants.StreamType;
import com.skl.cloud.util.pattern.Toolkits;

@Service("streamResourcesService")
public class StreamResourcesServiceImpl implements StreamResourcesService {

	private static Logger logger = Logger.getLogger(StreamResourcesServiceImpl.class);

	private static final String STREAMTYPE_RTSP = "rtsp";
	private static final String STREAMTYPE_HLS = "hls";
	private static final String STREAMTYPE_RTMP = "rtmp";
	private static final String STREAMTYPE_RTMPS = "rtmps";

	private String LiveDisposeMinIp = null;
	private String LiveDisposeMinPort = null;

	private String asRtpMinIp = null;
	private String asRtpMinPort = null;

	private String LiveServiceMinIp = null;
	private String LiveServiceMinPort = null;

	@Autowired
	private StreamResourcesMapper streamResourcesMapper;

	@Autowired
	private ShareMapper shareMapper;

	@Autowired
	private IPCameraService ipcameraService;

	@Autowired
	private P2pService p2pService;

	@Remote
	private StreamSubSystemRemote streamRemote;

	@Remote
	private IPCameraRemote iPCameraRemote;

	@Autowired
	private StreamStatusService streamStatusService;

	@Autowired
	private StreamStopService streamStopService;

	@Autowired
	private SubsysStreamStatusService subsysStreamStatusService;
	
	@Autowired
	private LogManageService logManageService;
	
	/**
	 * get request url like: http://ip:port/
	 * it's use for StreamSubSystemRemote
	 */
	@Override
	@Transactional(readOnly = true)
	public String getStreamRemotePrefixUrl(String streamType) throws ManagerException {
		String retUrl = null;
		if (streamType.equals("LiveDispose")) {
			// 拼接请求直播流处理子系统uri的前缀
			retUrl = "http://" + LiveDisposeMinIp + ":" + LiveDisposeMinPort;
		} else if (streamType.equals("StreamAccess")) {
			// 拼接请求流接入子系统uri的前缀
			retUrl = "http://" + asRtpMinIp + ":" + asRtpMinPort;
		} else if (streamType.equals("LiveService")) {
			// 拼接请求流接入子系统uri的前缀
			retUrl = "http://" + LiveServiceMinIp + ":" + LiveServiceMinPort;
		}
		return retUrl;
	}

	/**
	 * 获取stun的公有ip和端口,云端的环境信息配置
	  * <p>Title: getAdress</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.skl.cloud.service.StreamResourcesService#getAdress()
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<String> getAdress() {
		try {
			Map<String, Object> map = streamResourcesMapper.getIPCAddress();
			if (map == null) {
				if (getServerInfo(null, null).size() == 0) {
					return new ResponseEntity<String>(XmlUtil.responseXml("0x50000002", "", "querySeverInfo",
							getServerInfo(null, null)), HttpStatus.OK);
				}
				return new ResponseEntity<String>(XmlUtil.responseXml("0", "", "querySeverInfo",
						getServerInfo(null, null)), HttpStatus.OK);
			} else {
				String stunHost = (String) map.get("stunHost");
				String stunPort = (String) map.get("stunPort");
				return new ResponseEntity<String>(XmlUtil.responseXml("0", "0", "querySeverInfo",
						getServerInfo(stunHost, stunPort)), HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(XmlUtil.responseXml("0x50000044", "", "querySeverInfo",
					getServerInfo(null, null)), HttpStatus.OK);
		}
	}

	/**
	 * fengzhuang
	 * @param stunHost
	 * @param stunPort
	 * @return
	 */
	@Transactional(readOnly = true)
	private LinkedHashMap<String, Object> getServerInfo(String stunHost, String stunPort) {

		LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
		if (SystemConfig.getProperty("deploy.p2p.host") != null) {
			repMap.put("p2pHost", SystemConfig.getProperty("deploy.p2p.host"));
		}
		if (SystemConfig.getProperty("deploy.p2p.port") != null) {
			repMap.put("p2pPort", SystemConfig.getProperty("deploy.p2p.port"));
		}
		if (SystemConfig.getProperty("deploy.upnp.url") != null) {
			repMap.put("upnpURI", SystemConfig.getProperty("deploy.upnp.url"));
		}
		if (stunHost != null) {
			repMap.put("stunHost", stunHost);
		}
		if (stunPort != null) {
			repMap.put("stunPort", stunPort);
		}
		repMap.put("commandSrvHost", SystemConfig.getProperty("deploy.command.srv.host"));
		repMap.put("commandSrvPort", SystemConfig.getProperty("deploy.command.srv.port"));
		return repMap;
	}

	// 得到子系统最优先请求的两个Server IP
	private List<SubsysAddress> getSubSysServerList(ServerSystemId subSysId) {
		List<SubsysAddress> retList = new ArrayList<SubsysAddress>();

		List<SubsysAddress> liveStreamServeList = streamResourcesMapper.getSubsystemAddressById(subSysId.getId());
		if (liveStreamServeList.size() == 0) {
			logger.info("********子系统都没有启动，查询不到子系统的数据,子系统id: ********" + subSysId.getId());
			return null;
		}
		// 计算权重和设置权重
		liveStreamServeList = this.calculateWeight(liveStreamServeList);

		// 得到最大权重的子系统的信息（即是负载最小的服务器信息）
		SubsysAddress theFirstServiceAddress = this.getMaxWeight(liveStreamServeList);
		retList.add(theFirstServiceAddress);
		// 去掉最大权重的
		liveStreamServeList.remove(theFirstServiceAddress);

		if (liveStreamServeList.size() != 0) {
			// 得到次最大权重的子系统信息（即是负载次最小的服务器信息）
			SubsysAddress theSecondServiceAddress = this.getMaxWeight(liveStreamServeList);
			retList.add(theSecondServiceAddress);
		}
		return retList;
	}

	/**
	 * getLiveRelayURL(获取视频直播Relay的URL)
	 * @Title: getLiveRelayURL
	 * @param sn,userId,streamType
	 * @return url
	 * @throws ManagerException
	/**
	 * @Title:
	 * @Description: RTP流资源调度逻辑过程 
	 * @author wangming
	 * @date 2015年12月08日 
	 */
	@Override
	@Transactional
	public String RTPLiveStreamControl(Map<String, String> map, Map<String, String> ipcMap) {
		String sReturn = "";
		final String ipcStatusCodeValuePath = "/ResponseStatus/statusCode";
		final String statusCodeValuePath = "/streamControl/responseStatus/statusCode";

		final String portValuePath = "/streamControl/responseInfo/inputStream/inputStreamPort";
		final String LiveServicePortValuePath = "/streamControl/responseInfo/streamChannel/streamPort";

		InputStreamIO inputStreamIO = new InputStreamIO();
		
		inputStreamIO.setSn(ipcMap.get("sn"));
		inputStreamIO.setChannelId(ipcMap.get("channelId"));
		inputStreamIO.setChannelName(ipcMap.get("channelName"));
		inputStreamIO.setStreamType(ipcMap.get("streamType"));
		inputStreamIO.setVideoCode(ipcMap.get("videoCode"));
		inputStreamIO.setAudioCode(ipcMap.get("audioCode"));
		inputStreamIO.setResolutionH(ipcMap.get("resolutionH"));
		inputStreamIO.setResolutionW(ipcMap.get("resolutionW"));
		inputStreamIO.setInputStreamProtocol("1");

		// ----SS begin------
		StreamSourceIO streamSourceIO = new StreamSourceIO();
		streamSourceIO.setSourceServerIp("null");
		streamSourceIO.setSourceServerType("1");
		LiveStreamServiceControlIO liveStreamServiceControlIO = new LiveStreamServiceControlIO();
		liveStreamServiceControlIO.setInputStream(inputStreamIO);
		liveStreamServiceControlIO.setStreamSource(streamSourceIO);

		List<SubsysAddress> ssServerList = this.getSubSysServerList(ServerSystemId.SERVER_LIVE_SERVICE);
		XRemoteResult LiveServiceResult = null;
		for (SubsysAddress subSysAddress : ssServerList) {
			LiveServiceMinIp = subSysAddress.getServer_private_ip();
			LiveServiceMinPort = subSysAddress.getServer_port();
			try {

				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****the business logic system requests for the LiveStreamServiceSystem********");
				// 业控请求直播流服务子系统
				LiveServiceResult = streamRemote.requestForLiveStreamServiceSystem(liveStreamServiceControlIO);
				if (LiveServiceResult != null && LiveServiceResult.getString(statusCodeValuePath).equals("0")) {
					// 请求成功则跳出循环
					break;
				}
			} catch (Exception e) {
				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****请求Ip为" + LiveServiceMinIp + ",port为" + LiveServiceMinPort + "的直播流服务子系统失败");
				logger.error(e.getMessage(), e);
			}
		}
		if (LiveServiceResult == null || !LiveServiceResult.getString(statusCodeValuePath).equals("0")) {
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****已把最小和次最小直播流服务子系统都请求了，依然失败********");
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";

			StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySN(ipcMap.get("sn"));
			streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.Free.name());

			// return sReturn;
			throw new BusinessException("0x50020102");
		}
		// ----SS end------

		// ----LS begin----
		// 直播流服务子系统返回开放的端口号
		String LiveServicePort = LiveServiceResult.getString(LiveServicePortValuePath);
		OutputStreamIO RTPProcessOutputStreamIO = new OutputStreamIO();
		RTPProcessOutputStreamIO.setOutputStreamProtocol("1");
		RTPProcessOutputStreamIO.setDestIp(LiveServiceMinIp);
		RTPProcessOutputStreamIO.setDestPort(LiveServicePort);

		List<OutputStreamIO> list = new ArrayList<OutputStreamIO>();
		list.add(RTPProcessOutputStreamIO);
		StreamControlIO RTPStreamProcessControlIO = new StreamControlIO();
		RTPStreamProcessControlIO.setInputStream(inputStreamIO);
		RTPStreamProcessControlIO.setOutputStreamList(list);

		List<SubsysAddress> lsServerList = this.getSubSysServerList(ServerSystemId.SERVER_LIVE_DISPOSE);
		XRemoteResult RTPProcessResult = null;
		for (SubsysAddress subSysAddress : lsServerList) {
			LiveDisposeMinIp = subSysAddress.getServer_private_ip();
			LiveDisposeMinPort = subSysAddress.getServer_port();
			try {
				
				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****the business logic system requests for the LiveStreamProcessSystem********");
				// 业控请求直播流处理子系统
				RTPProcessResult = streamRemote.requestForLiveStreamProcessSystem(RTPStreamProcessControlIO);
				if (RTPProcessResult != null && RTPProcessResult.getString(statusCodeValuePath).equals("0")) {
					// 请求成功则跳出循环
					break;
				}
			} catch (Exception e) {

				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****请求Ip为" + LiveDisposeMinIp + ",port为" + LiveDisposeMinPort + "的直播流处理子系统失败");
				logger.error(e.getMessage(), e);
			}
		}
		if (RTPProcessResult == null || !RTPProcessResult.getString(statusCodeValuePath).equals("0")) {
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****已把最小和次最小的直播流处理子系统都请求了，依然失败********");
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";
			
			StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySN(ipcMap.get("sn"));
			streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.LoadRelayError.name());

			// 释放直播流服务子系统的资源
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****开始请求释放直播流服务的资源********");
			streamStopService.stopSubSystem("", ipcMap.get("sn"), ServerSystemId.SERVER_LIVE_SERVICE.getId());
			// return sReturn;
			throw new BusinessException("0x50020103");
		}
		// ----LS end----

		// ----AS begin---
		// 直播流处理子系统返回开放的端口号
		String rtpStreamProcessPort = RTPProcessResult.getString(portValuePath);
		OutputStreamIO StreamAccessOutputStreamIO = new OutputStreamIO();
		StreamAccessOutputStreamIO.setOutputStreamProtocol("1");
		StreamAccessOutputStreamIO.setDestIp(LiveDisposeMinIp);
		StreamAccessOutputStreamIO.setDestPort(rtpStreamProcessPort);

		List<OutputStreamIO> StreamAccessList = new ArrayList<OutputStreamIO>();
		StreamAccessList.add(StreamAccessOutputStreamIO);
		StreamControlIO streamAccessControlIO = new StreamControlIO();
		streamAccessControlIO.setInputStream(inputStreamIO);
		streamAccessControlIO.setOutputStreamList(StreamAccessList);

		List<SubsysAddress> asServerList = this.getSubSysServerList(ServerSystemId.AS_LIVE);
		XRemoteResult RTPAccessResult = null;
		String asRtpPublicIp = null;

		for (SubsysAddress subSysAddress : asServerList) {
			asRtpMinIp = subSysAddress.getServer_private_ip();
			asRtpMinPort = subSysAddress.getServer_port();
			try {
				
				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****the business logic system requests for the streamAccessSystem********");
				// 业控请求直播流接入子系统
				RTPAccessResult = streamRemote.requestForStreamAccessSystem(streamAccessControlIO);
				if (RTPAccessResult != null && RTPAccessResult.getString(statusCodeValuePath).equals("0")) {

					asRtpPublicIp = subSysAddress.getServer_public_ip();
					// 请求成功则跳出循环
					break;
				}
			} catch (Exception e) {
				logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****请求Ip为" + asRtpMinIp + ",port为" + asRtpMinPort + "的流接入子系统失败");
				logger.error(e.getMessage(), e);
			}

		}

		if (RTPAccessResult == null || !RTPAccessResult.getString(statusCodeValuePath).equals("0")) {
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****已把最小和次最小的流接入子系统都请求了，依然失败********");
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";

			StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySN(ipcMap.get("sn"));
			streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.LoadRelayError.name());

			// 释放直播流服务和处理子系统的资源
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****开始请求释放流服务,流处理的资源********");
			streamStopService.stopSubSystem("", ipcMap.get("sn"), ServerSystemId.RTP_LIVE_SUBSYS_SSLS_IDS);
			// return sReturn;
			throw new BusinessException("0x50020104");
		}
		// ---AS end----

		// ---ipc begin----
		// 流接入子系统返回开放的端口号
		String StreamAccessPort = RTPAccessResult.getString(portValuePath);
		ReceiverAddress receiverAddress = new ReceiverAddress();
		receiverAddress.setAddressingFormatType("ipaddress");
		receiverAddress.setHostName("null");
		receiverAddress.setIpAddress(asRtpPublicIp);
		receiverAddress.setIpv6Address("null");
		receiverAddress.setPortNo(StreamAccessPort);

		IPCStreamControlIO iPCStreamControlIO = new IPCStreamControlIO();
		iPCStreamControlIO.setReceiverAddress(receiverAddress);
		iPCStreamControlIO.setId("0");
		iPCStreamControlIO.setChannelId("MAIN");
		iPCStreamControlIO.setControl("play");
		iPCStreamControlIO.setControlSource("cloud");
		iPCStreamControlIO.setProtocolType("2");

		// 通知IPC上传视频流
		XRemoteResult informIPCUploadStreamResult = null;
		try {
			informIPCUploadStreamResult = iPCameraRemote.ipcStreamControl(ipcMap.get("sn"), iPCStreamControlIO);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			logger.error("****IPC的SN:"+ ipcMap.get("sn") +"****通知IPC上传视频流时，出现异常，超时********");

			StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySN(ipcMap.get("sn"));
			streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.LoadRelayError.name());
			// 请求停止流
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****IPC上流失败，开始请求释放所有资源********");
			streamStopService.stopSubSystem("", ipcMap.get("sn"), true, ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);

			throw new BusinessException("0x50020106");

		}

		if (informIPCUploadStreamResult == null
				|| !informIPCUploadStreamResult.getString(ipcStatusCodeValuePath).equals("0")) {
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****通知IPC上传视频流时，返回的结果是null或者status不正确********");
			StreamStatusCount streamStatus = streamStatusService.selectStreamStatusBySN(ipcMap.get("sn"));
			streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.LoadRelayError.name());
			// 请求停止流
			logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****IPC上流失败，开始请求释放所有资源********");
			streamStopService.stopSubSystem("", ipcMap.get("sn"), true, ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";
			// return sReturn;

			throw new BusinessException("0x50020105");
		}

		logger.info("*****OK***" + ipcMap.get("sn") + " 流资源调度完成，开始检查流是否上到流服务********");
		
		// 记录到日志表
		PlatformLog pl = new PlatformLog();
		pl.setUserId(0L);
		pl.setLogId(Toolkits.getSequenceID18());
		pl.setSn(ipcMap.get("sn"));
		pl.setModuleName("流资源调度");
		pl.setLogContent("对SN：" + ipcMap.get("sn") + "流资源调度完成，开始检查流是否上到流服务");
		pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
		logManageService.saveLogAlong(pl);
		
		// ---ipc end---
		// 查询Wowza流状态

		SubsysStreamStatus subsysStreamStatus = new SubsysStreamStatus();
		subsysStreamStatus.setCameraSn(ipcMap.get("sn"));
		subsysStreamStatus.setServerId(Constants.ServerSystemId.SERVER_LIVE_SERVICE.getId());
		subsysStreamStatus.setStreamStatus(0);
		subsysStreamStatus.setStreamStep(2);

		List<SubsysStreamStatus> liveServiceList = subsysStreamStatusService.select(subsysStreamStatus);

		boolean flg = false; // 默认流没上到直播流服务子系统
		StreamStatusCount streamStatusCount = new StreamStatusCount();
		streamStatusCount.setSn(ipcMap.get("sn"));

		streamStatusCount = streamStatusService.selectStatusCount(streamStatusCount);
		
		// “2”表示流状态是正常的
		if (liveServiceList != null && !liveServiceList.isEmpty()) {
			logger.info("-------IPC的SN:"+ ipcMap.get("sn") +"-----------1-----liveServiceList size: "+liveServiceList.size());
			flg = true;
			// 流已推至流服务,修改标记为ReadyRelay
			streamStatusService.updateStreamStatus(streamStatusCount.getSn(), streamStatusCount.getStreamStatus(), StreamProcessStatus.ReadyRelay.name());
		} else {
			// 每隔一秒查询，共五次,由配置定，默认5秒
			String loopNum = SystemConfig.getProperty("stream.relay.up.loop.times", "5");
			int loopTimes = Integer.valueOf(loopNum);
			for (int i = 0; i < loopTimes; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("***IPC的SN:"+ ipcMap.get("sn") +"*****延迟出错********");
				}
				logger.info("----IPC的SN:"+ ipcMap.get("sn") +"-----it's times:"+i+" to check stream is up to wowza of SN:"+streamStatusCount.getSn());
				liveServiceList = subsysStreamStatusService.select(subsysStreamStatus);

				if (liveServiceList != null && !liveServiceList.isEmpty()) {
					logger.info("-----IPC的SN:"+ ipcMap.get("sn") +"-----------2-------liveServiceList size: "+liveServiceList.size());
					streamStatusCount = streamStatusService.selectStatusCount(streamStatusCount);
					// 流已推至流服务,修改标记为ReadyRelay
					streamStatusService.updateStreamStatus(streamStatusCount.getSn(), streamStatusCount.getStreamStatus(), StreamProcessStatus.ReadyRelay.name());
					flg = true;
					break;
				}

			}
			if (flg == false) {
				logger.warn("****IPC的SN:"+ ipcMap.get("sn") +"****流一直没有上到直播流服务子系统********");
				// 释放直播流服务、处理和接入子系统的资源
				streamStopService.stopSubSystem("", ipcMap.get("sn"), true, ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);

				sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";
				// return sReturn;
				throw new BusinessException("0x50020109");
			}
		}
		logger.info("****IPC的SN:"+ ipcMap.get("sn") +"****检查流是否上到流服务结束，是否有流********" + flg);
		logger.info("@@@@@@@@ OK!!! It was correct when the BusinessLogicSubSystem informed the IPC to upload the stream.@@@@@@@@");
		logger.info("@@@@@@@@ OK!!! sn:" + ipcMap.get("sn") + "@@@@@@@@");
		logger.info("@@@@@@@@ OK!!! The StreamResourceSchedule was completed successfully@@@@@@@@");

		// UDP源参考：rtsp://[ec2外网ip]:1935/live/[sn]_[channelid].stream
		sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>0</status></ipcCommand>";
		return sReturn;
	}

	/**
	 * @Title:
	 * @Description:获取指定IPC的流通道的信息
	 * @author wangming
	 * @date 2015年12月11日 
	 */
	@Override
	@Transactional
	public StreamingChannel getStreamChannelBySn(String cameraSerialno, String id) {
		try {
			cameraSerialno = StringUtils.trim(cameraSerialno);
			logger.info("***********sn的值：" + cameraSerialno + "***********");

			// 根据IP摄像头的序列号查询P2P记录，获取摄像头的外网IP
			PlatformP2p p2pBean = p2pService.getP2pInfoBySn(cameraSerialno);
			if (p2pBean == null) {
				logger.info("********获取不到P2P表信息********");
			}

			String mappingIp = null;
			// 获取外围的IP地址
			if (p2pBean != null) {
				mappingIp = p2pBean.getMappingIp();
			}
			List<PlatformMapping> mappingList = null;
			// 根据IP摄像头的序列号查询IPC的端口映射记录
			mappingList = p2pService.getMappingsInfoBySn(cameraSerialno);
			if (mappingList == null) {
				logger.info("*******获取不到t_platform_mapping表的信息********");
			}

			int mappingPort = 0;
			if (mappingList != null) {
				if (mappingList != null && !mappingList.isEmpty()) {
					for (PlatformMapping mappingBean : mappingList) {
						// 1:http类型
						if ("HTTP".equalsIgnoreCase(mappingBean.getPortType())) {
							mappingPort = Integer.valueOf(mappingBean.getMappingPort());
							break;
						}
					}
				}
			}

			if (mappingPort <= 0) {
				logger.info("********获取不到mappingPort********");
			}

			// 延迟3s请求IPC指令中心,"缓"一下
			// Thread.sleep(3 * 1000);
			// 请求IPC指令中心获取指定流通道参数信息
			String ipcResult = iPCameraRemote.getStreamChannelInfo(cameraSerialno, id);
			if (ipcResult == null) {
				logger.error("********获取指定流通道参数信息，IPC返回的结果信息是null********");
				logger.error("******The response from IPC Instruction Center Subsystem is null,please check IPC Instruction Center Subsystem********");
				return null;
			}

			logger.info("********the result from IPC instruction center is:" + ipcResult + "********");
			if (ipcResult == null || ipcResult.equals("")) {
				logger.info("*********************请求IPC设备超时，please check IPC********************");
				return null;
			}

			List<String> groupList = new ArrayList<String>();
			groupList.add("ControlProtocolList/ControlProtocol");
			Map<String, Object> parsertMap = XmlUtil.getElementMap(ipcResult, groupList);
			if (!parsertMap.containsKey("StreamingChannel/enabled")) {
				logger.error("********IPC返回的数据没有流使能通道开关等信息，业控请求的报文有错******");
			}

			// 判断流使能通道开关是否打开。
			if (parsertMap.get("StreamingChannel/enabled").equals("false")) {

				int prefixIndex = ipcResult.indexOf("<enabled>");
				String prefixString = ipcResult.substring(0, prefixIndex + ("<enabled>".length()));
				int suffixIndex = ipcResult.indexOf("</enabled>");
				String suffixString = ipcResult.substring(suffixIndex);
				ipcResult = prefixString + "true" + suffixString;

				// 请求IPC指令中心设置流使能开关为true
				String responseString = iPCameraRemote.setStreamEnable(cameraSerialno, id, ipcResult);
				logger.info("the result from IPC instruction center is:" + responseString);
				if (responseString == null) {
					logger.error("********The response from IPC Instruction Center Subsystem is null,please check IPC Instruction Center Subsystem********");
					return null;
				}

				// 对IPC指令中心返回的数据进行解析成Map数据格式
				Map map = XmlUtil.getElementMapCommand(responseString);
				if (responseString == null || responseString.equals("")) {
					logger.info("*********************请求设置IPC设备参数超时，please check IPC********************");
					return null;
				}

				List<String> groupList1 = new ArrayList<String>();
				groupList1.add("StatusCode");
				Map<String, Object> responseMap = XmlUtil.getElementMap(responseString, groupList1);
				if (!responseMap.get("ResponseStatus/statusCode").equals("1")) {
					logger.error("流使能开关还没开，设置流使能失败！");
					return null;
				}
				logger.info("********流使能设置成功，已经开启！********");

				// 再次把返回的流通道列表信息转换成Map数据类型
				parsertMap = XmlUtil.getElementMap(ipcResult, groupList);
			}

			if (parsertMap != null && !parsertMap.isEmpty()) {

				StreamingChannel channel = new StreamingChannel();
				channel.setCameraSerialno(cameraSerialno);
				channel.setId(XmlUtil.getDomValue(parsertMap, "StreamingChannel/id"));
				channel.setChannelName(XmlUtil.getDomValue(parsertMap, "StreamingChannel/channelName"));
				channel.setEnabled(XmlUtil.getDomValue(parsertMap, "StreamingChannel/enabled"));

				channel.setMappingIp(mappingIp);
				channel.setMappingPort(String.valueOf(mappingPort));
				channel.setRtspPortNo(XmlUtil.getDomValue(parsertMap, "Transport/rtspPortNo"));

				List protocolMapList = (List) parsertMap.get("ControlProtocolList/ControlProtocol");
				if (protocolMapList != null) {
					for (int j = 0; j < protocolMapList.size(); j++) {
						Map<String, String> protocolMap = (Map<String, String>) protocolMapList.get(j);

						String streamingTransport = protocolMap.get("ControlProtocol/streamingTransport");
						if (StringUtils.isNotBlank(streamingTransport)) {
							channel.addstreamingTransport(StringUtils.trim(streamingTransport));
						}
					}

				}
				channel.setVideoEnabled(XmlUtil.getDomValue(parsertMap, "Video/enabled"));
				channel.setVideoCodecType(XmlUtil.getDomValue(parsertMap, "Video/videoCodecType"));
				channel.setVideoResolutionWidth(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionWidth"));
				channel.setVideoResolutionHeight(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionHeight"));
				channel.setMaxFrameRate(XmlUtil.getDomValue(parsertMap, "Video/maxFrameRate"));

				channel.setAudioEnabled(XmlUtil.getDomValue(parsertMap, "Audio/enabled"));
				channel.setAudioCompressionType(XmlUtil.getDomValue(parsertMap, "Audio/audioCompressionType"));
				channel.setAudioBitRate(XmlUtil.getDomValue(parsertMap, "Audio/audioBitRate"));

				return channel;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return null;
	}

	/**
	 * 
	  * insertOrUpdateIPCameraInfo(新增或更新IPC流通道信息)
	  * @Title: insertOrUpdateIPCameraInfo
	  * @Description: TODO
	  * @param @param cameraSerialno
	  * @param @param id (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明) 
	  * @author wangming 
	  * @date 2016年3月31日  
	 */
	@Transactional
	public void insertOrUpdateIPCameraInfo(String cameraSerialno, String id) {

		// 请求IPC指令中心获取指定流通道参数信息
		String ipcResult = iPCameraRemote.getStreamChannelInfo(cameraSerialno, id);

		logger.info("**********the result from IPC instruction center is:" + ipcResult);
		if (ipcResult == null || ipcResult.equals("")) {
			logger.error("********获取指定流通道参数信息，IPC返回的结果信息不正确********");
			logger.error("******The response from IPC Instruction Center Subsystem is an error,please check IPC Instruction Center Subsystem********");
			logger.info("*********************请求IPC设备失败，please check IPC********************");
			return;
		}

		List<String> groupList = new ArrayList<String>();
		groupList.add("ControlProtocolList/ControlProtocol");
		Map<String, Object> parsertMap = XmlUtil.getElementMap(ipcResult, groupList);
		if (!parsertMap.containsKey("StreamingChannel/enabled")) {
			logger.error("********IPC返回的数据没有流使能通道开关等信息，业控请求的报文有错******");
		}

		// 判断流使能通道开关是否打开。
		if (parsertMap.get("StreamingChannel/enabled").equals("false")) {

			int prefixIndex = ipcResult.indexOf("<enabled>");
			String prefixString = ipcResult.substring(0, prefixIndex + ("<enabled>".length()));
			int suffixIndex = ipcResult.indexOf("</enabled>");
			String suffixString = ipcResult.substring(suffixIndex);
			ipcResult = prefixString + "true" + suffixString;

			// 请求IPC指令中心设置流使能开关为true
			String responseString = iPCameraRemote.setStreamEnable(cameraSerialno, id, ipcResult);
			logger.info("**********设置流使能开关：the result from IPC instruction center is:" + responseString);
			if (responseString == null) {
				logger.error("********The response from IPC Instruction Center Subsystem is null,please check IPC Instruction Center Subsystem********");
				return;
			}

			if (responseString == null || responseString.equals("")) {
				logger.info("*********************请求设置IPC设备失败，please check IPC********************");
				return;
			}

			List<String> groupList1 = new ArrayList<String>();
			groupList1.add("StatusCode");
			Map<String, Object> responseMap = XmlUtil.getElementMap(responseString, groupList1);
			if (!responseMap.get("ResponseStatus/statusCode").equals("0")) {
				logger.error("**********流使能开关还没开，设置流使能失败！**********");
				return;
			}
			logger.info("**********流使能设置成功，已经开启！**********");

			// 再次把返回的流通道列表信息转换成Map数据类型
			parsertMap = XmlUtil.getElementMap(ipcResult, groupList);
		}

		if (parsertMap != null && !parsertMap.isEmpty()) {

			StreamStatusCount streamStatusCount = new StreamStatusCount();
			streamStatusCount.setSn(cameraSerialno);

			StreamStatusCount streamStatus = streamStatusService.selectStatusCount(streamStatusCount);

			if (streamStatus != null) {
				streamStatus.setChannelId(XmlUtil.getDomValue(parsertMap, "StreamingChannel/id"));
				streamStatus.setChannelName(XmlUtil.getDomValue(parsertMap, "StreamingChannel/channelName"));
			} else {
				streamStatusCount.setChannelId(XmlUtil.getDomValue(parsertMap, "StreamingChannel/id"));
				streamStatusCount.setChannelName(XmlUtil.getDomValue(parsertMap, "StreamingChannel/channelName"));
			}

			List protocolMapList = (List) parsertMap.get("ControlProtocolList/ControlProtocol");
			if (protocolMapList != null) {
				for (int j = 0; j < protocolMapList.size(); j++) {
					Map<String, String> protocolMap = (Map<String, String>) protocolMapList.get(j);

					String streamingTransport = protocolMap.get("ControlProtocol/streamingTransport");

					if (StringUtils.isNotBlank(streamingTransport)) {

						if (streamStatus != null) {
							streamStatus.setStreamType(streamingTransport);
						} else {
							streamStatusCount.setStreamType(streamingTransport);
						}

					}
				}

			}

			if (streamStatus != null) {

				streamStatus.setAudioCode(XmlUtil.getDomValue(parsertMap, "Audio/audioCompressionType"));
				streamStatus.setVideoCode(XmlUtil.getDomValue(parsertMap, "Video/videoCodecType"));
				streamStatus.setResolutionH(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionHeight"));
				streamStatus.setResolutionW(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionWidth"));
			} else {
				streamStatusCount.setAudioCode(XmlUtil.getDomValue(parsertMap, "Audio/audioCompressionType"));
				streamStatusCount.setVideoCode(XmlUtil.getDomValue(parsertMap, "Video/videoCodecType"));
				streamStatusCount.setResolutionH(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionHeight"));
				streamStatusCount.setResolutionW(XmlUtil.getDomValue(parsertMap, "Video/videoResolutionWidth"));
			}

			if (streamStatus == null) {

				// 默认是Free状态
				streamStatusCount.setStreamStatus(StreamProcessStatus.Free.name());
				streamStatusCount.setCurAccessRelayNum(0);
				streamStatusCount.setScheduleFailNum(0);
				// 保存IPC返回的流通道参数信息
				streamStatusService.insertStatusCount(streamStatusCount);
			} else {

				streamStatus.setStreamStatus(StreamProcessStatus.Free.name());
				streamStatus.setCurAccessRelayNum(0);
				streamStatus.setScheduleFailNum(0);
				streamStatusService.updateStatusCount(streamStatus);
			}

		}

	}

	/**
	 * @Title:
	 * @Description:获取摄像头的局域网uri
	 * @author wangming
	 * @date 2015年12月16日
	 */
	@Transactional
	public String getStreamUri(String cameraSerialno, String id, String streamType) {
		try {
			String ipcResult = null;
			if (streamType.equals("PULL_HLS")) {
				// TODO no code
			} else if (streamType.equals("PULL_RTSP")) {
				logger.info("the result from IPC is:" + ipcResult);
			}
			List<String> groupList = new ArrayList<String>();

			Map<String, Object> parsertMap = XmlUtil.getElementMap(ipcResult, groupList);
			if (parsertMap != null && !parsertMap.isEmpty()) {
				String StreamURL = XmlUtil.getDomValue(parsertMap, "StreamInfo/StreamURL");
				return StreamURL;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return "";
	}

	@Transactional
	public String getLiveRelayURL(String sn, Long userId, String streamType) throws ManagerException {
		String liveUrl = "";
		// 获取生成直播流Url的各种参数
		final String channelId = "MAIN";
		final String serverPort = "1935";
		int serverId = Constants.ServerSystemId.SERVER_LIVE_SERVICE.getId();
		String serverIP = this.getRelayServerIp(sn, channelId, serverId);
		if (StringUtils.isBlank(serverIP)) {
			logger.info("********在获取直播Url时，获取到的流服务子系统的serverIP为空，流服务没有接受到流********");
			logger.info("********在拼装Relay Url时，查询不到IPC流在Wowza的数据。********");
			
			throw new BusinessException("0x50020098");
		}
		// 查询数据库是否存在对应ipc的永不过期的直播流分享的uuid
		String uuid = shareMapper.getUuidBySn(sn);
		// 不存在，则新建一条永不过期的直播流分享纪录
		if (StringUtils.isBlank(uuid)) {
			Share share = new Share();
			// 新生成一个uuid
			uuid = UUID.randomUUID().toString();
			share.setUuid(uuid);
			share.setUserId(userId);
			IPCamera ipCamera = ipcameraService.getIPCameraBySN(sn);
			if (ipCamera == null) {
				AssertUtils.throwBusinessEx(0x50000002);
				// throw new BusinessException("0x50000002");
			}
			share.setCameraId(ipCamera.getId());
			share.setCameraSn(sn);
			// 1代表视频流分享
			share.setShareType(1);
			shareMapper.insert(share);
		} else {
			// 存在纪录，则新生成一个uuid
			uuid = UUID.randomUUID().toString();
			// 修改该记录的userId和uuid
			shareMapper.updateUuidUserId(sn, userId, uuid);
		}

		if (StringUtils.equalsIgnoreCase(streamType, STREAMTYPE_RTSP)) {
			liveUrl = "rtsp://" + serverIP + ":" + serverPort + "/live/" + sn + "_" + channelId + ".stream" + "?id="
					+ uuid;
		} else if (StringUtils.equalsIgnoreCase(streamType, STREAMTYPE_HLS)) {
			liveUrl = "http://" + serverIP + ":" + serverPort + "/live/" + sn + "_" + channelId
					+ ".stream/playlist.m3u8" + "?id=" + uuid;
		} else if (StringUtils.equalsIgnoreCase(streamType, STREAMTYPE_RTMP)) {
			liveUrl = "rtmp://" + serverIP + ":" + serverPort + "/live/" + sn + "_" + channelId + ".stream" + "?id="
					+ uuid;
		} else if (StringUtils.equalsIgnoreCase(streamType, STREAMTYPE_RTMPS)) {
			liveUrl = "rtmps://" + serverIP + ":" + serverPort + "/live/" + sn + "_" + channelId + ".stream" + "?id="
					+ uuid;
		}

		if (logger.isInfoEnabled()) {
			logger.info("get IPC's live stream url from remote is: " + liveUrl);
		}
		return liveUrl;
	}

	// 进行流资源调度
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
	public String StreamResourceSchedule(Map<String, String> xmlMap) {
		String sReturn = "";

		StreamStatusCount streamStatusCount = new StreamStatusCount();
		streamStatusCount.setSn(xmlMap.get("sn"));

		StreamStatusCount streamStatus = streamStatusService.selectStatusCount(streamStatusCount);
		
		if (null == streamStatus) {
			logger.error("****IPC的SN:"+ xmlMap.get("sn") +"****查询对应的IPC设备的流通道信息为null********");
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";

			// return sReturn;
			throw new BusinessException("0x50020094");
		}

		// begin 在进行流资源调度之前先检测子系统是否已经释放残余资源
		SubsysStreamStatus subsysStreamStatus = new SubsysStreamStatus();
		subsysStreamStatus.setCameraSn(xmlMap.get("sn"));
		List<SubsysStreamStatus> list = subsysStreamStatusService.select(subsysStreamStatus);
		// 若没释放资源则请求释放资源
		if (list.size() >= 1) {
			int[] subSysIds = new int[list.size()];

			// 得到还有残留资源的子系统id
			for (int i = 0; i < list.size(); i++) {
				SubsysStreamStatus sss = list.get(i);
				subSysIds[i] = sss.getServerId();
			}
			// 请求释放子系统的资源信息
			streamStopService.stopSubSystem("", xmlMap.get("sn"), subSysIds);

		}
		// 更改标记为LoadingRelay
		streamStatusService.updateStreamStatus(streamStatus.getSn(), streamStatus.getStreamStatus(), StreamProcessStatus.LoadingRelay.name());
		streamStatus.setStreamStatus(StreamProcessStatus.LoadingRelay.name());
		// end

		Map<String, String> ipcMap = new LinkedHashMap<String, String>();

		// 有序地存储IPC参数信息
		ipcMap.put("channelId", streamStatus.getChannelId());
		ipcMap.put("sn", xmlMap.get("sn"));
		ipcMap.put("channelName", streamStatus.getChannelName());
		ipcMap.put("streamType", streamStatus.getStreamType());
		ipcMap.put("videoCode", streamStatus.getVideoCode());
		ipcMap.put("audioCode", streamStatus.getAudioCode());
		ipcMap.put("resolutionH", streamStatus.getResolutionH());
		ipcMap.put("resolutionW", streamStatus.getResolutionW());
		
		// 根据IPC流的类型，分别调用相应的流控制过程。
		switch (StreamType.getStreamType(ipcMap.get("streamType"))) {
		// case PULL_RTSP:
		// sReturn = this.RTSPLiveStreamControl(xmlMap, ipcMap);
		// break;
		case PUSH_TS_RTP_TCP:
			sReturn = this.RTPLiveStreamControl(xmlMap, ipcMap);
			break;
		}

		return sReturn;
	}

	/**
	 * 获取组装Relay url需要的流服务ip信息
	 * @param sn
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getRelayServerIp(String sn) throws ManagerException {
		final String channelId = "MAIN";
		int serverId = Constants.ServerSystemId.SERVER_LIVE_SERVICE.getId();
		return this.getRelayServerIp(sn, channelId, serverId);
	}

	/**
	 * 获取组装Relay url需要的流服务ip信息
	 * @param sn
	 * @param channelId
	 * @return String
	 */
	@Transactional(readOnly = true)
	public String getRelayServerIp(String sn, String channelId, int serverId) throws ManagerException {
		return shareMapper.getCreatRelayUrlInfo(sn, channelId, serverId);
	}

	/**
	 * 通过uuid检验直播流url是否过期
	 * @param uuid
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public int checkLiveUrlValidity(String uuid) throws BusinessException {
		int isExpire = 1;
		// 查询对应uuid的share信息
		Share share = shareMapper.queryShareByUUID(uuid);
		// 数据库不存在该uuid的share信息，则将isExpire值设为2
		if (share == null) {
			isExpire = 2;
		} else if (share.getEndDate() == null) {// 如果结束时间为null,表示永不过期
			isExpire = 0;
		} else {
			// 判断是否过期；未过期，将isExpire值设为0; 过期，将isExpire值设为1;
			isExpire = share.getEndDate().getTime() > new Date().getTime() ? 0 : 1;
		}
		return isExpire;
	}

	private String getMappingUrI(String uri, Map<String, String> ipcMap) {
		String protocol = uri.substring(0, uri.indexOf("//") + 2);
		String url = uri.substring(uri.indexOf("."), uri.length());
		url = url.substring(url.indexOf("/"), url.length());
		String mappingIpPort = ipcMap.get("ip") + ":" + ipcMap.get("port");
		logger.info("---getMappingUrI is: " + protocol + mappingIpPort + url);
		return protocol + mappingIpPort + url;
	}

	/**
	 * 
	  * calculateWeight(计算权重并为对象设置权重信息)
	  * @Title: calculateWeight
	  * @param @param list
	  * @param @return (参数说明)
	  * @return List<SubsysAddress> (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月29日
	 */
	@Transactional
	public List<SubsysAddress> calculateWeight(List<SubsysAddress> list) {

		for (SubsysAddress subsysAddress : list) {

			BigDecimal weight = new BigDecimal(0.0);

			// 判断心跳情况（权重为0.4）
			if (subsysAddress.getServer_heart_beat_time() != null) {

				long heartBeat = TimeUnit.MILLISECONDS.toSeconds((new Date().getTime() - (subsysAddress
						.getServer_heart_beat_time()).getTime()));

				if (heartBeat <= 60) {
					// 如果心跳时间小于60S，则视为正常

					weight = weight.add(new BigDecimal(0.4));
				}
			}

			if (subsysAddress.getServer_front_dispatched_status() != null) {

				// 判断上次调度失败情况（权重为0.3）
				if (subsysAddress.getServer_front_dispatched_status() == 0) {
					// 为0表示上次调度是成功的

					weight = weight.add(new BigDecimal(0.3));
				}
			}

			int actualAccessNum = 0;
			if (subsysAddress.getServer_actual_access_num() != null) {

				actualAccessNum = Integer.valueOf(subsysAddress.getServer_actual_access_num());
				// 判断目前有无流接入（权重为0.1）
				if (actualAccessNum > 0) {

					weight = weight.add(new BigDecimal(0.1));
				}
			}

			// 暂时设置是10
			int num = Integer.valueOf(SystemConfig.getProperty("stream.actual.access.num", "10"));
			// 判断目前流接入的数目（权重为0.2）
			if (actualAccessNum < num) {
				weight = weight.add(new BigDecimal(0.2));
			}

			subsysAddress.setWeight(weight.doubleValue());

		}

		return list;
	}

	/**
	 * 
	  * getMaxWeight(得到最大权重的服务器信息)
	  * @Title: getMaxWeight
	  * @param @param list
	  * @param @return (参数说明)
	  * @return SubsysAddress (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月29日
	 */
	@Transactional(readOnly = true)
	public SubsysAddress getMaxWeight(List<SubsysAddress> list) {

		double maxWeight = list.get(0).getWeight();// 默认最大的权重

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getWeight() > maxWeight) {

				maxWeight = list.get(i).getWeight();
			}
		}
		// 找出权重最大的是哪个SubsysAddress对象
		for (SubsysAddress subsysAddress : list) {

			if (subsysAddress.getWeight() == maxWeight) {

				return subsysAddress;
			}

		}

		return null;
	}

	/**
	 * @Description: 查询流处理子系统对应服务器的IP和Port
	 * @param uuid
	 * @return SubsysAddress
	 */
	@Transactional(readOnly = true)
	public com.skl.cloud.model.SubsysAddress querySubsysIpPort(String uuid) throws ManagerException {
		return streamResourcesMapper.querySubsysIpPort(uuid);
	}

	/**
	 * 
	 * @Description: 查询接入子系统返回状态
	 * @param sn
	 * @param channelId
	 * @param eventID
	 * @param dateTime
	 * @return String
	 */
	@Transactional(readOnly = true)
	public String queryEventStatus(String sn, String channelId, String eventID, String dateTime)
			throws ManagerException {
		return streamResourcesMapper.queryEventStatus(sn, channelId, eventID, dateTime);
	}
}
