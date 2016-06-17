package com.skl.cloud.service.sub.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.dao.sub.StreamStopMapper;
import com.skl.cloud.exception.device.NoDeviceException;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.model.sub.SubsysAddress;
import com.skl.cloud.model.sub.SubsysStreamStatus;
import com.skl.cloud.remote.ipc.IPCameraRemote;
import com.skl.cloud.remote.ipc.dto.ipc.IPCStreamControlIO;
import com.skl.cloud.remote.ipc.dto.ipc.ReceiverAddress;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.service.sub.SubsysStreamStatusService;
import com.skl.cloud.util.common.HttpClientUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.constants.Constants.ServerSystemId;
import com.skl.cloud.util.constants.StreamProcessStatus;

@Service("streamStopService")
public class StreamStopServiceImpl implements StreamStopService {
	private Logger logger = LoggerFactory.getLogger(StreamStopServiceImpl.class);

	@Autowired
	private StreamStopMapper streamStopMapper;

	@Remote
	private IPCameraRemote iPCameraRemote;

	@Autowired
	private StreamStatusService streamStatusService;

	@Autowired
	private SubsysStreamStatusService subsysStreamStatusService;

	/**
	 * 请求流子系统释放对应SN的流资源
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> stopSubSysStream(String destIp, String sn, String channelId, ServerSystemId subSysId) {
		Map<String, String> retMap = new HashMap<String, String>();
		logger.info("---IPC的SN:"+ sn +"--subSysId: " + subSysId.getId() + "----parameter channel is----" + channelId);
		if (StringUtils.isEmpty(channelId)) {
			channelId = this.getSysChannel(sn, subSysId.getId());
			logger.info("---IPC的SN:"+ sn +"--subSysId: " + subSysId.getId() + "---- query channel from DB is----" + channelId);
		}
		if (StringUtils.isNotEmpty(channelId)) {
			logger.info("---IPC的SN:"+ sn +"--subSysId: " + subSysId.getId() + "--------------start stopStream --------");
			SubsysAddress subsysAddress = streamStopMapper.querySubSystemAddress(sn, subSysId.getId());
			String inputStopXml = getInputStopXml(sn, destIp, channelId);
			if (subsysAddress != null) {
				destIp = subsysAddress.getServer_private_ip() != null ? subsysAddress.getServer_private_ip() : "";
			}
			retMap.put("channelId", channelId);
			retMap.put("destIp", destIp);
			this.requestStopToSub(inputStopXml, subsysAddress);
		}
		return retMap;
	}

	/**
	 * 向IPC请求流停止操作
	 */
	@Override
	@Transactional
	public void stopIpcStream(String sn) throws SKLRemoteException {
		ReceiverAddress receiverAddress = new ReceiverAddress();
		receiverAddress.setAddressingFormatType("ipaddress");
		receiverAddress.setHostName("ec2-52-27-175-186.us-west-2.compute.amazonaws.com");
		receiverAddress.setIpAddress("192.168.139.110");
		receiverAddress.setIpv6Address("1030::C9B4:FF12:48AA:1A2B");
		receiverAddress.setPortNo("8001");

		IPCStreamControlIO iPCStreamControlIO = new IPCStreamControlIO();
		iPCStreamControlIO.setReceiverAddress(receiverAddress);
		iPCStreamControlIO.setId(UUID.randomUUID().toString());
		iPCStreamControlIO.setChannelId("MAIN");
		iPCStreamControlIO.setControl("stop");
		iPCStreamControlIO.setControlSource("cloud");
		iPCStreamControlIO.setProtocolType("2");
		this.stopIpcStream(sn, iPCStreamControlIO);
	}

	/**
	 * 向IPC请求流停止操作
	 */
	@Override
	@Transactional
	public void stopIpcStream(String sn, IPCStreamControlIO iPCStreamControlIO) throws SKLRemoteException {
		iPCameraRemote.ipcStreamControl(sn, iPCStreamControlIO);
	}

	/**
	 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
	 * @param destIp
	 * @param sn
	 * @param subSystemIds <link> Constants.ServerSystemId</link>
	 * @return void
	 */
	@Override
	@Transactional
	public void stopSubSystem(String destIp, String sn, int... subSystemIds) {
		// 查询流状态统计表是否含有该设备信息
		StreamStatusCount streamStatusCount = streamStatusService.selectStreamStatusBySNAndChanneId(sn, "MAIN");
		// 进行释放资源
		if (streamStatusCount != null && !StreamProcessStatus.ReleasingRelay.name().equals(streamStatusCount.getStreamStatus())) {
			if(!StreamProcessStatus.InP2PPlaying.name().equals(streamStatusCount.getStreamStatus())){
				// 更新到数据库
				streamStatusService.updateStreamStatus(sn, streamStatusCount.getStreamStatus(), StreamProcessStatus.ReleasingRelay.name());
				streamStatusCount.setStreamStatus(StreamProcessStatus.ReleasingRelay.name());
			}
			String channelId = "";
			// 向流服务发送停止指令
			for (int i = 0; i < subSystemIds.length; i++) {
				if (ServerSystemId.SERVER_LIVE_SERVICE.getId() == subSystemIds[i]) {
					try {
						Map<String, String> map = this.stopSubSysStream(destIp, sn, channelId,
								ServerSystemId.SERVER_LIVE_SERVICE);
						destIp = map.get("destIp") != null ? map.get("destIp") : "";
						channelId = map.get("channelId") != null ? map.get("channelId") : "";
					} catch (Exception e) {
						LoggerUtil.error("stopSsStream fail," + e.getMessage(), e, this.getClass().getName());
						LoggerUtil.error("****IPC的SN:"+ sn +"****请求释放直播流服务子系统残余的资源时出错********", this.getClass());
					}
				}
				// 向流处理发送停止指令
				if (ServerSystemId.SERVER_LIVE_DISPOSE.getId() == subSystemIds[i]) {
					try {
						Map<String, String> map = this.stopSubSysStream(destIp, sn, channelId,
								ServerSystemId.SERVER_LIVE_DISPOSE);
						destIp = map.get("destIp") != null ? map.get("destIp") : "";
						channelId = map.get("channelId") != null ? map.get("channelId") : "";
					} catch (Exception e) {
						LoggerUtil.error("stopLsStream fail," + e.getMessage(), e, this.getClass().getName());
						LoggerUtil.error("****IPC的SN:"+ sn +"****请求释放直播流处理子系统残余的资源时出错********", this.getClass());
					}
				}
				// 向流接入发送停止指令
				if (ServerSystemId.AS_LIVE.getId() == subSystemIds[i]) {
					try {
						this.stopSubSysStream(destIp, sn, channelId, ServerSystemId.AS_LIVE);
					} catch (Exception e) {
						LoggerUtil.error("stopAsStream fail," + e.getMessage(), e, this.getClass().getName());
						LoggerUtil.error("****IPC的SN:"+ sn +"****请求释放直播流接入子系统残余的资源时出错********", this.getClass());
					}
				}
			}
		}
		// 更新到数据库
		if(!StreamProcessStatus.InP2PPlaying.name().equals(streamStatusCount.getStreamStatus())){
			streamStatusService.updateStreamStatus(sn, streamStatusCount.getStreamStatus(), StreamProcessStatus.Free.name());
		}
	}

	/**
	 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
	 * @param destIp
	 * @param sn
	 * @param subSystemIds  <link> Constants.ServerSystemId</link>
	 * @return void
	 */
	@Override
	@Transactional
	public void stopSubSystem(String destIp, String sn, boolean isStopIpc, int... subSystemIds) {
		this.stopSubSystem(destIp, sn, subSystemIds);
		// 向IPC发送流停止指令
		if (isStopIpc) {
			try {
				logger.info("----------start request IPC sn: " + sn + " to stop relay stream------");
				this.stopIpcStream(sn);
			} catch (NoDeviceException ne){	
				logger.warn("******StreamStopIpc " + sn + " fail, " + ne.getMessage());
			} catch (Exception e) {
				logger.error("******StreamStopIpc " + sn + " fail,通知IPC停止上流时，失败。*******", e);
				//throw new BusinessException("0x50020107");
			}
		}
	}

	// 查询SN对应流子系统的channel,对应新表
	private String getSysChannel(String sn, Integer serverId) {
		String retVal = "";
		SubsysStreamStatus subsysStreamStatus = new SubsysStreamStatus();
		subsysStreamStatus.setCameraSn(sn);
		subsysStreamStatus.setServerId(serverId);
		List<SubsysStreamStatus> list = subsysStreamStatusService.select(subsysStreamStatus);
		if (list != null && !list.isEmpty()) {
			subsysStreamStatus = list.get(0);
			retVal = subsysStreamStatus.getCameraChannelId() != null ? subsysStreamStatus.getCameraChannelId() : "";
		}
		return retVal;
	}

	// 向子系统请求流停止操作， 要修改成subRemote
	private void requestStopToSub(String inputStopXml, SubsysAddress subsysAddress) {
		if (subsysAddress != null) {
			if (StringUtil.isEmpty(subsysAddress.getServer_private_ip())) {
				logger.warn("it's not query the sub system private ip, no need to stop stream in sub system.");
			} else {
				StringBuilder url = new StringBuilder();
				url.append("http://");
				url.append(subsysAddress.getServer_private_ip());
				url.append(":");
				url.append(subsysAddress.getServer_port());
				url.append("/skl-cloud/cloud/stream/stop");
				try {
					HttpClientUtil.httpPostBackgroundSubsystem(inputStopXml, url.toString());
				} catch (Exception e) {
					logger.error("it's fail to request sub system stop sn stream." + e.getMessage(), e);
				}
			}
		}
	}

	// generate the input stop stream string xml
	private String getInputStopXml(String sn, String destIp, String channel) {
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<streamStop>");
		sb.append("<sn>");
		sb.append(sn);
		sb.append("</sn>");
		sb.append("<channelId>");
		sb.append(channel);
		sb.append("</channelId>");
		sb.append("<destIp>");
		sb.append(StringUtils.isEmpty(destIp) ? "192.168.0.1" : destIp);
		sb.append("</destIp>");
		sb.append("</streamStop>");
		return sb.toString();
	}

}
