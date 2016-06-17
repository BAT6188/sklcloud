package com.skl.cloud.dao.sub;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.sub.SubsysStreamStatus;

/**
 * 对子系统流资源状态表操作
 * @author fulin
 *
 */
public interface SubsysStreamStatusMapper {

	/**
	 * 根据cameraSn或cameraChannelId或serverId，查询子系统流资源状态表
	 * @param subsysStreamStatus
	 * @return
	 * @throws BusinessException
	 */
	public List<SubsysStreamStatus> select (SubsysStreamStatus subsysStreamStatus) throws BusinessException; 
	
	/**
	 * 插入信息到子系统流资源状态表
	 * @param subsysStreamStatus
	 * @return
	 * @throws BusinessException
	 */
	public void insert (SubsysStreamStatus subsysStreamStatus) throws BusinessException;
	
	/**
	 * 根据id，更新子系统流资源状态表
	 * @param subsysStreamStatus
	 * @return
	 * @throws BusinessException
	 */
	public void update (SubsysStreamStatus subsysStreamStatus) throws BusinessException;
	
	/**
	 * 根据cameraSn与cameraChannelId与serverUuid，删除子系统流资源状态表的数据
	 * @param subsysStreamStatus
	 * @return
	 * @throws BusinessException
	 */
	public void delete (SubsysStreamStatus subsysStreamStatus) throws BusinessException;
}
