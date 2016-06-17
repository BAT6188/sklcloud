package com.skl.cloud.service.sub.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.dao.sub.StreamExceptionMapper;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.model.sub.SubException;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamExceptionService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.constants.Constants.ServerSystemId;
import com.skl.cloud.util.constants.StreamProcessStatus;

@Service("streamExceptionService")
public class StreamExceptionServiceImpl implements StreamExceptionService {
	private Logger logger = LoggerFactory.getLogger(StreamExceptionServiceImpl.class);

	@Autowired
	private StreamExceptionMapper streamExceptionMapper;

	@Autowired
	private StreamStopService streamStopService;

	@Autowired
	private StreamResourcesService streamResouceService;

	@Autowired
	private StreamStatusService streamStatusService;

	@Autowired
	private IPCameraService ipcService;
	// subsysType
	private final static String EXP_SUBSYSTYPE_LIVE_AS = "7";
	private final static String EXP_SUBSYSTYPE_LIVE_LS = "10";
	private final static String EXP_SUBSYSTYPE_LIVE_SS = "14";
	// exceptionType
	// 普通异常信息上报
	private final static String EXP_TYPE_1 = "1";
	// 流异常信息上报
	private final static String EXP_TYPE_2 = "2";
	// 流监控请求停止上报
	private final static String EXP_TYPE_3 = "3";

	private final static int RE_SCHEDULE_FAILED_NUM = 5;

	/**
	 * 保存上报的subException数据
	 */
	@Override
	public void insert(SubException subException) {
		streamExceptionMapper.insertException(subException);
	}

	/**
	 * 对子系统上报的Exception按type进行相应处理
	 */
	@Override
	@Async
	@Transactional(propagation=Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
	public void handleSubSysReportExp(SubException bean) {
		Map<String, String> xmlMap = new HashMap<String, String>();
		String sn = bean.getStreamSn();
		String destIp = bean.getSubsysPrivateIp();
		boolean reScheduleFlag = false;
		// 保存异常信息
		this.insert(bean);

		StreamStatusCount streamStatusCount = new StreamStatusCount();
		streamStatusCount.setSn(sn);
		streamStatusCount.setChannelId("MAIN");

		// WOWZA listener there is no stream up, report stream exception to BL.
		if (EXP_TYPE_2.equals(bean.getExceptionType()) && EXP_SUBSYSTYPE_LIVE_SS.equals(bean.getSubsysType())) {
			logger.warn("******流服务上报了60秒没有流的异常，SN: " + bean.getStreamSn() + "*******");
			// 查询流状态统计表的设备信息
			streamStatusCount = streamStatusService.selectStatusCount(streamStatusCount);
			logger.info(" SN: " + bean.getStreamSn() + "-- current status is: --"+streamStatusCount.getStreamStatus());
			if(StreamProcessStatus.getStreamInScheduleProcessStatuses().contains(streamStatusCount.getStreamStatus())){
				streamStatusService.updateStreamStatus(sn, streamStatusCount.getStreamStatus(), StreamProcessStatus.RelayErroFromSS.name());
				
				// 停止流子系统资源与IPC推流
				streamStopService.stopSubSystem(destIp, sn, true, ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);
			}
			// 查询流状态统计表的设备信息
			streamStatusCount = streamStatusService.selectStatusCount(streamStatusCount);
			// 增加失败的交数
			streamStatusCount.setScheduleFailNum(streamStatusCount.getScheduleFailNum() + 1);
			// 检查单位时间内IPC对应sn请求relay失败的次数
			if (RE_SCHEDULE_FAILED_NUM >= streamStatusCount.getScheduleFailNum()) {
				reScheduleFlag = true;
			}
			// 重新启动流资源调度
			if (reScheduleFlag && StreamProcessStatus.Free.name().equals(streamStatusCount.getStreamStatus())) {
				try {
					logger.error("******开始重新进行流资源调度*******");
					xmlMap.put("sn", sn);
					streamResouceService.StreamResourceSchedule(xmlMap);
				} catch (Exception e) {
					logger.error("******重新进行流资源调度失败*******", e);
				}
			}
			streamStatusService.updateStatusCount(streamStatusCount);
			// WOWZA listener there is no client connect relay resource any more, release resource.
		} else if (EXP_TYPE_3.equals(bean.getExceptionType()) && EXP_SUBSYSTYPE_LIVE_SS.equals(bean.getSubsysType())) {
			IPCamera ipcamera = ipcService.getIPCameraBySN(bean.getStreamSn());
			//只有HPC03A才先释放流
			if (Constants.ipcModelType.isHPC03IPC(ipcamera.getModel())) {
				logger.warn("******15秒无人观看，流服务上报请求释放流资源，SN: " + bean.getStreamSn() + "*******");
				// 停止流子系统资源与IPC推流
				streamStopService.stopSubSystem(destIp, sn, true, ServerSystemId.RTP_LIVE_SUBSYS_ALL_IDS);
				// 统计relay时长在IPC上报relay end那
			}
		} else {
			// //流接入或流处理上报的异常
			logger.info("no need to handle this exceptionType: " + bean.getExceptionType());
		}
	}
}
