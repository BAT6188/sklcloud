package com.skl.cloud.dao.ipc;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.ipc.StreamStatusCount;

/**
 * 对ipc流状态统计表进行操作
 * @author fulin
 *
 */
public interface StreamStatusCountMapper {

	/**
	 * 查询ipc流状态统计表信息，可以实体的sn或channelId作查询条件
	 * @param streamStatus
	 * @return
	 * @throws BusinessException
	 */
	public StreamStatusCount select (StreamStatusCount streamStatus) throws BusinessException;
	
	/**
	 * 根据id,更新ipc流状态统计表信息
	 * @param streamStatus
	 * @throws BusinessException
	 */
	public void update (StreamStatusCount streamStatus) throws BusinessException;
	
	/**
	 * 只更新表里的stream status
	 * @param sn
	 * @param StreamProcessStatus preStatus
	 * @param StreamProcessStatus nextStatus
	 * @throws BusinessException
	 */
	public boolean updateStatus(@Param("sn") String sn, @Param("preStatus") String preStatus,
			@Param("nextStatus") String nextStatus) throws BusinessException;

	/**
	 * 新增ipc流状态统计表信息
	 * @param streamStatus
	 * @throws BusinessException
	 */
	public void insert (StreamStatusCount streamStatus) throws BusinessException;
}
