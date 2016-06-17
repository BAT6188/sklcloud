package com.skl.cloud.service.sub.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.sub.SubsysStreamStatusMapper;
import com.skl.cloud.model.sub.SubsysStreamStatus;
import com.skl.cloud.service.sub.SubsysStreamStatusService;

@Service("subsysStreamStatusService")
public class SubsysStreamStatusServiceImpl implements SubsysStreamStatusService{

	@Autowired
	private SubsysStreamStatusMapper subsysStreamStatusMapper;
	
	/**
	 * 根据cameraSn或cameraChannelId或serverId，查询子系统流资源状态表
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SubsysStreamStatus> select(SubsysStreamStatus subsysStreamStatus)
			throws BusinessException {
		return subsysStreamStatusMapper.select(subsysStreamStatus);
	}

	/**
	 * 插入信息到子系统流资源状态表
	 */
	@Override
	@Transactional
	public void insert(SubsysStreamStatus subsysStreamStatus)
			throws BusinessException {
		subsysStreamStatusMapper.insert(subsysStreamStatus);
	}

	/**
	 * 根据id，更新子系统流资源状态表
	 */
	@Override
	@Transactional
	public void update(SubsysStreamStatus subsysStreamStatus)
			throws BusinessException {
		subsysStreamStatusMapper.update(subsysStreamStatus);
	}

	/**
	 * 根据cameraSn与cameraChannelId与serverUuid，删除子系统流资源状态表的数据
	 */
	@Override
	@Transactional
	public void delete(SubsysStreamStatus subsysStreamStatus)
			throws BusinessException {
		subsysStreamStatusMapper.delete(subsysStreamStatus);
	}

}
