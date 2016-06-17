package com.skl.cloud.service;

import java.util.List;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;

public interface P2pService
{
	/**
	 * <上报p2p信息>
	 * 
	 * @param p2p
	 * @throws ManagerException
	 */
	public void insertP2pInfo(PlatformP2p p2p) throws Exception;

	/**
	 * <上报p2p-mapping信息>
	 * 
	 * @param mappingList
	 * @throws ManagerException
	 */
	public void insertMappingsInfo(List<PlatformMapping> mappingList) throws Exception;
	
	
	/**
     * query PlatformP2p information by SN
     * @Title: getP2pInfoBySn
     * @param sn
     * @throws ManagerException
     * @return PlatformP2p
     * @author yangbin
     * @date 2015年12月30日
    */
	public PlatformP2p getP2pInfoBySn(String sn)throws ManagerException;
	
	/**
    * query List of PlatFormmapping information by SN
    * @Title: getMappingsInfoBySn
    * @param sn
    * @throws ManagerException
    * @return List of PlatFormmapping
    * @author yangbin
    * @date 2015年12月30日
   */
	public List<PlatformMapping> getMappingsInfoBySn(String sn)throws ManagerException;

}
