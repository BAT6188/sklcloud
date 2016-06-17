package com.skl.cloud.dao;

import java.util.List;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;

public interface P2pMapper
{
	public void insertP2pInfo(PlatformP2p p2p) throws Exception;

	public void insertMappingsInfo(List<PlatformMapping> mappingList) throws Exception;

	public void deleteP2pInfoBySn(String sn) throws Exception;

	public void deleteMappingsInfoBySn(String sn) throws Exception;
	
	public PlatformP2p queryP2pInfoBySn(String sn)throws ManagerException;
	
	public List<PlatformMapping> queryMappingsInfoBySn(String sn)throws ManagerException;
}
