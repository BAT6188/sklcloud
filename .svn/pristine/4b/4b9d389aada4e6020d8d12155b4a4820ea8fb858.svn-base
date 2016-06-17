package com.skl.cloud.dao.audio;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.audio.Music;
import com.skl.cloud.model.audio.MusicList;

public interface MusicListMapper {

	/**
	 * 根据设备sn获得音乐列表
	 * 
	 * @param sn
	 * @return
	 */
	MusicList getMusicListBySN(String sn);

	/**
	 * 保存musicList
	 * 
	 * @param musicList
	 */
	void saveMusicList(MusicList musicList);
	
	
	/**
	 * 设置音乐列表播放总时间
	 * @param listId
	 * @param totalTime
	 */
	void setMusicListTotalTime(@Param("listId") Long listId, @Param("totalTime") Long totalTime);

	/**
	 * 根据sn,id和status获得music
	 * @param sn
	 * @param musicId
	 * @param status
	 * @return
	 */
	Music getMusicBySN(@Param("sn") String sn, @Param("id")  Long musicId, @Param("status") int status);
	 
	/**
	 * 保存music
	 * 
	 * @param music
	 */
	void saveMusic(Music music);

	
	/**
	 * 设置music的状态
	 * 
	 * @param musicId
	 * @param status
	 */
	void setMusicStatus(@Param("musicId") Long musicId, @Param("status") int status);
    
	/**
	 * 根据SN获取musiclist的ID
	 * @param sn
	 * @return
	 */
	Long getIdBySn(String sn);
	
	/**
	 * 查询music表是否已存在该taskId
	 * @Description: 已存在该taskId,返回true
	 * @param taskId
	 * @return Boolean
	 */
	Boolean isExistTaskId(String taskId);

	/**
	 * 根据sn,id获得music
	 * @param sn
	 * @param musicId
	 * @return
	 */
	Music getMusicByIdSn(@Param("sn") String sn, @Param("id")  Long musicId);
}