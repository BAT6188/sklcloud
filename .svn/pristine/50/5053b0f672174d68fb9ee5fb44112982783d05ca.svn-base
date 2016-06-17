package com.skl.cloud.service.ipc;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.ipc.StreamStatusCount;
import com.skl.cloud.util.constants.StreamProcessStatus;

/**
 * 对流状态进行逻辑处理
 * @author fulin
 *
 */
public interface StreamStatusService {

	/**
	 * 查询ipc流状态统计表信息，可以实体的sn与channelId作查询条件
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	public StreamStatusCount selectStatusCount (StreamStatusCount streamStatus) throws BusinessException;
	
	/**
	 * 通过SN查询ipc流状态统计表信息
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	public StreamStatusCount selectStreamStatusBySN (String sn) throws BusinessException;
	
	/**
	 * 通过SN和channelId查询ipc流状态统计表信息
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	public StreamStatusCount selectStreamStatusBySNAndChanneId (String sn, String channelId) throws BusinessException;
	
	/**
	 * 根据id,更新ipc流状态统计表信息
	 * @param streamStatus
	 * @throws BusinessException
	 */
	public void updateStatusCount (StreamStatusCount streamStatus) throws BusinessException;
	
	/**
	 * 只更新表里的stream status
	 * @param sn
	 * @param StreamProcessStatus preStatus
	 * @param StreamProcessStatus nextStatus
	 * @throws BusinessException
	 */
	public void updateStreamStatus (String sn, String preStatus, String nextStatus) throws BusinessException;
	
	/**
	 * 新增ipc流状态统计表信息
	 * @param streamStatus
	 * @throws BusinessException
	 */
	public void insertStatusCount (StreamStatusCount streamStatus) throws BusinessException;

	/**
	 * 记录ipc推流状态，并统计成功次数和播放总时间等
	 * @param sn
	 * @param pushStreamType
	 * @param streamStatus
	 * @throws BusinessException
	 */
	public void countStreamStatus(String sn, String pushStreamType, String streamStatus) throws BusinessException;
	
}
