package com.skl.cloud.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.P2pMapper;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;
import com.skl.cloud.service.P2pService;

@Service
public class P2pServiceImpl implements P2pService
{
	@Autowired(required = true)
	private P2pMapper p2pMapper;

	@Override
	@Transactional
	public void insertP2pInfo(PlatformP2p p2p) throws Exception
	{
		p2pMapper.deleteP2pInfoBySn(p2p.getCamera_serialno());

		if (p2p.getCreatetime() == null)
		{
			p2p.setCreatetime(new Date());
		}
		if (p2p.getModifytime() == null)
		{
			p2p.setModifytime(new Date());
		}
		p2pMapper.insertP2pInfo(p2p);
	}

	@Override
	@Transactional
	public void insertMappingsInfo(List<PlatformMapping> mappingList) throws Exception
	{
		p2pMapper.deleteMappingsInfoBySn(mappingList.get(0).getSn());
		p2pMapper.insertMappingsInfo(mappingList);
	}
	
	/**
     * query PlatformP2p information by SN
     * @Title: getP2pInfoBySn
     * @param sn
     * @throws ManagerException
     * @return PlatformP2p
     * @author yangbin
     * @date 2015年12月30日
    */
	@Override
	@Transactional(readOnly = true)
	public PlatformP2p getP2pInfoBySn(String sn)throws ManagerException{
		return p2pMapper.queryP2pInfoBySn(sn);
	}
	
	/**
    * query List of PlatFormmapping information by SN
    * @Title: getMappingsInfoBySn
    * @param sn
    * @throws ManagerException
    * @return List of PlatFormmapping
    * @author yangbin
    * @date 2015年12月30日
   */
	@Override
	@Transactional(readOnly = true)
	public List<PlatformMapping> getMappingsInfoBySn(String sn)throws ManagerException{
		return p2pMapper.queryMappingsInfoBySn(sn);
	}
}
