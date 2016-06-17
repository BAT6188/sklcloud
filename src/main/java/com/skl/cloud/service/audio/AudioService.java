package com.skl.cloud.service.audio;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.audio.Alarm;
import com.skl.cloud.model.audio.Media;
import com.skl.cloud.model.audio.Music;
import com.skl.cloud.model.audio.MusicList;
import com.skl.cloud.model.audio.Story;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.ScheduleStatusIO;

public interface AudioService {

	// -----------Music
	
	/**
	 * 获得云端媒体文件
	 * 
	 * @param sn
	 * @param type
	 * @return
	 */
	List<Media> listMediaBySN(String sn, int type) throws BusinessException;
	 
	/**
	 * 根据设备获得音乐列表
	 * @param sn
	 * @return
	 * @throws BusinessException
	 */
	MusicList getMusicList(String sn) throws BusinessException;
	
	
	/**
	 * 新增Music项
	 * @param sn
	 * @param mediaId
	 */
	Music addMusic(String sn, Long mediaId, String taskId) throws BusinessException;
	
	/**
	 * 发送Music至IPC
	 * @param music
	 */
	void addMusicToIPC(String sn, Music music) throws BusinessException;
	
	/**
	 * 删除music
	 * @param sn
	 * @param musicId
	 */
	void deleteMusic(String sn, Long musicId) throws BusinessException;
	


	/**
	 * 获取音乐播放状态
	 * 
	 * @param sn
	 * @return
	 */
	MusicPlayStatusIO getPlayMusicStatus(String sn) throws BusinessException;
	
	
	/**
	 * 设置music的播放时长
	 * 
	 * @param sn
	 * @return
	 */
	void setMusicTotalTime(String sn, Integer totalTime) throws BusinessException; 
	
	/**
	 * 请求ipc播放/停止播放music
	 * @param sn
	 * @param type
	 */
	void playMusic(String sn, String type) throws BusinessException;
	
	
	// ---------------- Alarm
	
    /**
     * 根据设备获得alarm列表
     * @param sn
     * @return
     * @throws BusinessException
     */
    List<Alarm> getAlarmList(String sn) throws BusinessException;

    /**
     * 删除alarm
     * @param sn
     * @param alarmId
     */
	void deleteAlarm(String sn, Long alarmId) throws BusinessException; 
	
	/**
	 * 新增或更改某个设备alarm列表的某一项
	 * @param alarm
	 * @throws BusinessException
	 */
	void setAlarmList(Alarm alarm) throws BusinessException;
    
	
    // ------------------story
    
    /**
     * 根据设备获得story列表
     * @param sn
     * @return
     */
	List<Story> getStoryList(String sn) throws BusinessException;

	/**
	 * 删除story
	 * @param sn
	 * @param storyId
	 */
	void deleteStory(String sn, Long storyId) throws BusinessException;

	/**
	 * 新增或更改某个设备story列表的某一项
	 * @param story
	 * @throws BusinessException
	 */
	void setStoryList(Story story) throws BusinessException;

	/**
	 * 与IPC对讲，将S3的录音文件URL转发给IPC
	 * @param sn
	 * @param pathName
	 * @param fileSize
	 * @param fileName 
	 * @param type 
	 */
	void pushToTalk(String sn, String pathName, String fileName, int fileSize, String type) throws BusinessException;
    
	/**
	 * 根据mediaId 查询 media
	 * @param mediaId
	 * @return
	 * @throws BusinessException
	 */
	Media getMedia(Long mediaId) throws BusinessException;

	/**
	 * 根据media的类型查询media表系统预设的media
	 * @param mediaType
	 * @return
	 * @throws BusinessException
	 */
	List<Media> listSysMediaByType(int mediaType) throws BusinessException;

	/**
	 * 根据sn,id获得music
	 * @param sn
	 * @param musicId
	 * @return
	 * @throws BusinessException
	 */
	Music getMusicByIdSn(String sn, Long musicId) throws BusinessException;

	/**
	 * 修改music的状态
	 * @param id
	 * @param status
	 * @throws BusinessException
	 */
	void setMusicStatus(Long musicId, int status) throws BusinessException;

	/**
	 * 根据alarmId查询alarm列表中的某一项
	 * @param alarmId
	 * @return
	 * @throws BusinessException
	 */
	Alarm getAlarmById(Long alarmId) throws BusinessException;

	/**
	 * 修改alarm的状态
	 * @param alarmId
	 * @param status
	 * @throws BusinessException
	 */
	void setAlarmStatus(Long alarmId, int status) throws BusinessException;

	/**
	 * 根据storyId查询story列表中的某一项
	 * @param storyId
	 * @return
	 * @throws BusinessException
	 */
	Story getStoryById(Long storyId) throws BusinessException;

	/**
	 * 修改story的状态
	 * @param storyId
	 * @param status
	 * @throws BusinessException
	 */
	void setStoryStatus(Long storyId, int status) throws BusinessException;

	/**
	 * 查询设备任务状态
	 * @param sn
	 * @return
	 * @throws BusinessException
	 */
	ScheduleStatusIO queryDeviceScheduleStatus(String sn) throws BusinessException;

}
