package com.skl.cloud.service.ipc.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.ipc.StreamStatusCountMapper;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.service.LogManageService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.service.sub.StreamStopService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.constants.StreamProcessStatus;
import com.skl.cloud.util.pattern.Toolkits;

/**
 * 对ipc流状态统计表进行操作
 * @author fulin
 *
 */
@Service("streamStatusService")
public class StreamStatusServiceImpl implements StreamStatusService {
	private static Logger logger = LoggerFactory.getLogger(StreamStatusServiceImpl.class);
	
	@Autowired
	private StreamStatusCountMapper streamStatusCountMapper;

	@Autowired
	private StreamStopService streamStopService;
	
	@Autowired
	private LogManageService logManageService;
	// start
	private final static String STREAM_ACTION_START = "start";
	// end
	private final static String STREAM_ACTION_END = "end";
	// p2p
	private final static String STREAM_TYPE_P2P = "p2p";
	// relay
	private final static String STREAM_TYPE_RELAY = "relay";

	/**
	 * 查询ipc流状态统计表信息，可以实体的sn或channelId作查询条件
	 */
	@Override
	@Transactional(readOnly = true)
	public StreamStatusCount selectStatusCount(StreamStatusCount streamStatus) throws BusinessException {
		return streamStatusCountMapper.select(streamStatus);
	}

	/**
	 * 通过SN查询ipc流状态统计表信息
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public StreamStatusCount selectStreamStatusBySN(String sn) throws BusinessException {
		StreamStatusCount streamStatus = new StreamStatusCount();
		streamStatus.setSn(sn);
		return this.selectStatusCount(streamStatus);
	}

	/**
	 * 通过SN和channelId查询ipc流状态统计表信息
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public StreamStatusCount selectStreamStatusBySNAndChanneId(String sn, String channelId) throws BusinessException {
		StreamStatusCount streamStatus = new StreamStatusCount();
		streamStatus.setSn(sn);
		streamStatus.setChannelId(channelId);
		return this.selectStatusCount(streamStatus);
	}

	/**
	 * 根据id,更新ipc流状态统计表信息
	 */
	@Override
	@Transactional
	public void updateStatusCount(StreamStatusCount streamStatus) throws BusinessException {
		streamStatusCountMapper.update(streamStatus);
		
		// 记录到日志表
		PlatformLog pl = new PlatformLog();
		pl.setUserId(10001L);
		pl.setLogId(Toolkits.getSequenceID18());
		pl.setSn(streamStatus.getSn());
		pl.setModuleName("流状态更新");
		pl.setLogContent("对SN：" + streamStatus.getSn() + "的流状态进行更新，状态更新为: "+streamStatus.getStreamStatus());
		pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
		logManageService.saveLog(pl);
	}
	
	/**
	 * 只更新表里的stream status
	 * @param sn
	 * @param StreamProcessStatus preStatus
	 * @param StreamProcessStatus nextStatus
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 30)
	public void updateStreamStatus (String sn, String preStatus, String nextStatus) throws BusinessException{
		if(StreamProcessStatus.isExistsStreamStatus(preStatus) && StreamProcessStatus.isExistsStreamStatus(nextStatus)){
			boolean flag = streamStatusCountMapper.updateStatus(sn, preStatus, nextStatus);
			logger.info("----------updateStreamStatus sql execute flag----------"+flag);
			
			// 记录到日志表
			PlatformLog pl = new PlatformLog();
			pl.setUserId(10001L);
			pl.setLogId(Toolkits.getSequenceID18());
			pl.setSn(sn);
			pl.setModuleName("流状态更新");
			pl.setLogContent("对SN：" + sn + "的流状态进行更新，从当前状态："+preStatus+" 更新到: "+nextStatus);
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			logManageService.saveLog(pl);
		}else{
			logger.warn("it's not difinition in the enum class StreamProcessStatus for preStatus: "+preStatus +" or nextStatus: "+nextStatus);
		}
	}

	/**
	 * 新增ipc流状态统计表信息
	 */
	@Override
	@Transactional
	public void insertStatusCount(StreamStatusCount streamStatus) throws BusinessException {
		streamStatusCountMapper.insert(streamStatus);
	}

	/**
	 * 记录ipc推流状态，并统计成功次数和播放总时间等
	 */
	@Override
	@Transactional
	public void countStreamStatus(String sn, String pushStreamType, String streamStatus) throws BusinessException {
		// 校验streamStatus参数
		if (!STREAM_ACTION_START.equals(streamStatus) && !STREAM_ACTION_END.equals(streamStatus)) {
			throw new BusinessException("0x50000001");
		}

		// 查询流状态统计表是否含有该设备信息
		StreamStatusCount streamStatusCount = this.selectStreamStatusBySNAndChanneId(sn, "MAIN");
		if (streamStatusCount == null) {
			throw new BusinessException("0x50000002");
		}

		// 进行p2p流信息的统计
		if (STREAM_TYPE_P2P.equals(pushStreamType)) {
			// 对p2p start数据进行统计
			if (STREAM_ACTION_START.equals(streamStatus)) {
				// 修改流状态
				streamStatusCount.setStreamStatus(StreamProcessStatus.InP2PPlaying.name());
				// 记录开始时间
				streamStatusCount.setP2pStreamUpTime(new Date());
				// p2p成功次数增1
				int p2pSuccessNum = streamStatusCount.getTotalP2pPlayNum() == null ? 0 : streamStatusCount
						.getTotalP2pPlayNum();
				streamStatusCount.setTotalP2pPlayNum(p2pSuccessNum + 1);
				// 更新到数据库
				this.updateStatusCount(streamStatusCount);
			}else if (STREAM_ACTION_END.equals(streamStatus)) {
				// 修改流状态
				streamStatusCount.setStreamStatus(StreamProcessStatus.Free.name());
				// 记录结束时间
				streamStatusCount.setP2pStreamEndTime(new Date());
				// 若stop时，p2p播放流的开始时间不为null，才统计总时间；如果为null，说明上次有stop命令丢失，不进行统计行为
				if (streamStatusCount.getP2pStreamUpTime() != null) {
					// 统计p2p播放流的总共时间
					long p2pStartTime = streamStatusCount.getP2pStreamUpTime().getTime();
					long p2pEndTime = streamStatusCount.getP2pStreamEndTime().getTime();
					long p2pPlayTime = (p2pEndTime - p2pStartTime) / 1000;
					long totalP2pPlayTime = streamStatusCount.getTotalP2pPlayTime() == null ? 0 : streamStatusCount
							.getTotalP2pPlayTime();
					streamStatusCount.setTotalP2pPlayTime(totalP2pPlayTime + p2pPlayTime);
					// 将p2p播放流的开始时间设为null,以标记本次计算完毕
					streamStatusCount.setP2pStreamUpTime(null);
				}
				// 更新到数据库
				this.updateStatusCount(streamStatusCount);
			}
		// 对relay数据进行统计	
		} else if (STREAM_TYPE_RELAY.equals(pushStreamType)) {
			// 对relay start数据进行统计	
			if (STREAM_ACTION_START.equals(streamStatus)) {
				// 1.记录Relay开始时间
				streamStatusCount.setRelayStreamUpTime(new Date());
				// 2.Relay成功次数增1
				int relaySuccessNum = streamStatusCount.getTotalRelayPlayNum() == null ? 0 : streamStatusCount
						.getTotalRelayPlayNum();
				streamStatusCount.setTotalRelayPlayNum(relaySuccessNum + 1);
				// 3.重置relay失败调度次数
				streamStatusCount.setScheduleFailNum(0);
				// 更新到数据库
				this.updateStatusCount(streamStatusCount);
			}else if (STREAM_ACTION_END.equals(streamStatus)) {
				// 记录结束时间
				streamStatusCount.setRelayStreamEndTime(new Date());
				// 若stop时，relay播放流的开始时间不为null，才统计总时间；如果为null，说明上次有stop命令丢失，不进行统计行为
				if (streamStatusCount.getRelayStreamUpTime() != null) {
					// 统计relay播放流的总共时间
					long relayStartTime = streamStatusCount.getRelayStreamUpTime().getTime();
					long relayEndTime = streamStatusCount.getRelayStreamEndTime().getTime();
					long relayPlayTime = (relayEndTime - relayStartTime) / 1000;
					long totalRelayPlayTime = streamStatusCount.getTotalRelayPlayTime() == null ? 0 : streamStatusCount
							.getTotalRelayPlayTime();
					streamStatusCount.setTotalRelayPlayTime(totalRelayPlayTime + relayPlayTime);
					// 将relay播放流的开始时间设为null
					streamStatusCount.setRelayStreamUpTime(null);
				}
				// 更新到数据库
				this.updateStatusCount(streamStatusCount);
			}
		} else {
			throw new BusinessException("0x50000001");
		}

	}

}
