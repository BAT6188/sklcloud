package com.skl.cloud.dao.audio;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.audio.AudioPlay;

public interface AudioPlayListMapper
{

	/**
	 * 
	 * @Title:APP获取云端存放的指定IPC设备的音乐文件列表
	 * @param: fileData
	 * @author lizhiwei
	 * @date 2016年1月19日 下午2:16:32
	 */
	List<Map<String, Object>> getAudioPlayList(S3FileData fileData) throws ManagerException;

	/**
	 * 
	 * @Title:删除该设备下的所有音乐列表
	 * @param: sn
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	void deleteAudioPlay(String sn) throws ManagerException;
	
	/**
	 * 
	 * @Title:根据uuids删除音乐列表
	 * @param: uuids
	 * @author zhaonao
	 * @date 2016年1月20日 上午10:16:32
	 */
	void deleteAudioPlayByUuids(List<String> uuids) throws ManagerException;

	/**
	 * 
	 * @Title:更新列表文件中信息
	 * @param: audioPlay
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	void updateAudioPlay(AudioPlay audioPlay) throws ManagerException;

	/**
	 * 
	 * @Title:新增指定IPC设备的音乐文件列表信息
	 * @param: sn
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	void saveAudioPlay(List<AudioPlay> list) throws ManagerException;

}