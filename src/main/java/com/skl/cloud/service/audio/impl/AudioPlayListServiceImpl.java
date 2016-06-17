package com.skl.cloud.service.audio.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.audio.AudioPlayListMapper;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.audio.AudioPlay;
import com.skl.cloud.remote.ipc.IPCameraRemote;
import com.skl.cloud.service.audio.AudioPlayListService;

@Service
public class AudioPlayListServiceImpl implements AudioPlayListService
{
	@Autowired(required = true)
	private AudioPlayListMapper audioPlayListMapper;

	@Remote
	private IPCameraRemote iPCameraRemote;

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAudioPlayList(String sn) throws ManagerException
	{
		S3FileData fileData = new S3FileData();
		fileData.setDeviceSn(sn);
		return audioPlayListMapper.getAudioPlayList(fileData);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getAudioPlayBySN(String sn, String fileName) throws ManagerException
	{
		S3FileData fileData = new S3FileData();
		fileData.setDeviceSn(sn);
		fileData.setFileName(fileName);
		List<Map<String, Object>> list = audioPlayListMapper.getAudioPlayList(fileData);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void deleteAudioPlay(String sn) throws ManagerException
	{
		audioPlayListMapper.deleteAudioPlay(sn);
	}
	
	@Override
	@Transactional
	public void deleteAudioPlayByUuids(List<String> uuids) throws ManagerException
	{
		audioPlayListMapper.deleteAudioPlayByUuids(uuids);
	}

	@Override
	@Transactional
	public void updateAudioPlayStatus(String uuid, String status) throws ManagerException
	{
		AudioPlay audioPlay = new AudioPlay();
		audioPlay.setUuid(uuid);
		audioPlay.setStatus(status);
		audioPlayListMapper.updateAudioPlay(audioPlay);
	}

	@Override
	@Transactional
	public void saveAudioPlay(List<AudioPlay> list) throws ManagerException
	{
		audioPlayListMapper.saveAudioPlay(list);
	}
	
	@Override
	@Transactional
	public void updateAudioPlay(List<AudioPlay> list) throws ManagerException
	{
		if(list == null || list.size()<=0)
		{
			return;
		}
		
		this.deleteAudioPlay(list.get(0).getSn());
		this.saveAudioPlay(list);
	}

	@Override
	public XRemoteResult issueAudioPlay(String sn, String xml) throws ManagerException
	{
		XRemoteResult xRemoteResult = null;
		try
		{
			xRemoteResult = iPCameraRemote.issueAudioPlay(sn, xml);
		} catch (Exception e)
		{
			throw new BusinessException("0x50010032", e);
		}
		return xRemoteResult;
	}

	@Override
	public XRemoteResult commandAudioPlay(String sn, String xml) throws ManagerException
	{
		XRemoteResult xRemoteResult = null;
		try
		{
			xRemoteResult = iPCameraRemote.commandAudioPlay(sn, xml);
		} catch (Exception e)
		{
			throw new BusinessException("0x50010033", e);
		}
		return xRemoteResult;
	}
}
