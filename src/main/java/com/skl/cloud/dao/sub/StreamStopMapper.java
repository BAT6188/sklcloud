package com.skl.cloud.dao.sub;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.sub.SubsysAddress;

public interface StreamStopMapper {

	/**
	 * 查询流SN对应的流接入子系统信息
	 */
	public SubsysAddress querySubSystemAddress(@Param("sn") String sn, @Param("subSysId") int subSysId);
	
}
