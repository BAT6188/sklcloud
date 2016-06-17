package com.skl.cloud.service.audio;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.model.audio.AudioPlay;

public interface AudioPlayListService
{
	/**
	 * 
	 * @Title:查询该用户下对应设备的音乐文件列表
	 * @param: sn
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月19日 下午2:16:32
	 */
	List<Map<String, Object>> getAudioPlayList(String sn) throws ManagerException;

	/**
	 * 
	 * @Title:查询该用户下对应设备的音乐文件信息
	 * @param: sn
	 * @param: fileName
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月19日 下午2:16:32
	 */
	public Map<String, Object> getAudioPlayBySN(String sn, String fileName) throws ManagerException;

	/**
	 * 
	 * @Title:删除该设备下的所有音乐列表
	 * @param: sn
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	public void deleteAudioPlay(String sn) throws ManagerException;
	
	/**
	 * 
	 * @Title:根据uuids删除音乐列表
	 * @param: uuids
	 * @Description: TODO
	 * @author zhaonao
	 * @date 2016年2月4日 上午10:16:32
	 */
	public void deleteAudioPlayByUuids(List<String> uuids) throws ManagerException;

	/**
	 * 
	 * @Title:更新列表文件中的状态
	 * @param: uuid
	 * @param: status
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	public void updateAudioPlayStatus(String uuid, String status) throws ManagerException;

	/**
	 * 
	 * @Title:新增指定IPC设备的音乐文件列表信息
	 * @param: sn
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月20日 上午10:16:32
	 */
	public void saveAudioPlay(List<AudioPlay> list) throws ManagerException;
	
	/**
	 * 
	 * @Title: 更新指定IPC设备的音乐文件列表信息
	 * @param: sn
	 * @Description: TODO
	 * @author zhaonao
	 * @date 2016年3月16日 上午10:16:32
	 */
	public void updateAudioPlay(List<AudioPlay> list) throws ManagerException;

	/**
	 * 
	* @Title:  通知IPC下发play音乐列表
	* @param: sn
	* @param: xml
	* @Description: TODO
	* @author lizhiwei
	* @date 2016年1月21日 上午11:03:01
	 */
	public XRemoteResult issueAudioPlay(String sn, String xml) throws ManagerException;
	/**
	 * 
	 * @Title:  对指定IPC设备上音乐文件的操作
	 * @param: sn
	 * @param: xml
	 * @Description: TODO
	 * @author lizhiwei
	 * @date 2016年1月21日 上午11:03:01
	 */
	public XRemoteResult commandAudioPlay(String sn, String xml) throws ManagerException;

}
