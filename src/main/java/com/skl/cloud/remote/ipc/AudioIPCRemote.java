package com.skl.cloud.remote.ipc;


import org.springframework.http.HttpMethod;

import com.skl.cloud.foundation.remote.SKLRemote;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.annotation.RRoot;
import com.skl.cloud.foundation.remote.annotation.RVariable;
import com.skl.cloud.foundation.remote.ipc.IPCRemote;
import com.skl.cloud.foundation.remote.ipc.SN;
import com.skl.cloud.remote.ipc.dto.audio.EventScheduleIO;
import com.skl.cloud.remote.ipc.dto.audio.EventScheduleListIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicFileIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayCommandIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.ScheduleStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.TalkFileListIO;

public interface AudioIPCRemote extends SKLRemote {
	
	/**
	 * 查询IPC的music列表和状态
	 * @param sn
	 * @return
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/Music", method=HttpMethod.GET)
	MusicPlayStatusIO getMusicPlayStatus(@SN String sn) throws SKLRemoteException;
	
	/**
	 * 设置音乐列表
	 * @param sn
	 * @param status
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Music/{musicId}", rootName="musicPlayCommand", xmln="urn:skylight", method=HttpMethod.POST)
	void setMusicList(@SN String sn, @RRoot MusicPlayCommandIO playCommand, @RVariable("musicId") Long musicId) throws SKLRemoteException;
	
	
	
	/**
	 * 新增音乐
	 * @param sn
	 * @param music
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/Music/{musicId}", rootName="musicFile", xmln="urn:skylight", method=HttpMethod.POST,  timeout=0)
	void addMusic(@SN String sn, @RRoot MusicFileIO music, @RVariable("musicId") Long musicId) throws SKLRemoteException;
	
	/**
	 * 删除music
	 * @param sn
	 * @param musicId
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/Music/{musicId}", method=HttpMethod.DELETE)
	void deleteMusic(@SN String sn, @RVariable("musicId") Long musicId) throws SKLRemoteException;
	
	
	
	
	// -----------story-----------
	/**
	 * 查询IPC的story列表的某一项
	 * @param sn
	 * @return
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule{storyId}", method=HttpMethod.GET)
	EventScheduleIO queryStory(@SN String sn, @RVariable("storyId") String storyId) throws SKLRemoteException;

	/**
	 * 同步IPC的story/alarm列表
	 * @param sn
	 * @return
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule", method=HttpMethod.GET)
	EventScheduleListIO queryEventScheduleList(@SN String sn) throws SKLRemoteException;

	/**
	 * 删除story列表的一项
	 * @param sn
	 * @param storyId
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule/{storyId}", method=HttpMethod.DELETE)
	void deleteStory(@SN String sn, @RVariable("storyId") String storyId) throws SKLRemoteException;
    
	/**
	 * 新增story列表的一项
	 * @param sn
	 * @param story
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule", rootName="eventSchedule", xmln="urn:skylight", method=HttpMethod.POST)
	void addStory(@SN String sn, @RRoot EventScheduleIO eventScheduleIO) throws SKLRemoteException;

	/**
	 * 修改story列表的一项
	 * @param sn
	 * @param storyId
	 * @param eventScheduleIO
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule/{storyId}", rootName="eventSchedule", xmln="urn:skylight", method=HttpMethod.PUT)
	void modifyStory(@SN String sn, @RVariable("storyId") String storyId, @RRoot EventScheduleIO eventScheduleIO) throws SKLRemoteException;
	
	
	
	//-----------alarm------------
	

	/**
	 * 删除alarm列表的一项
	 * @param sn
	 * @param alarmId
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule/{alarmId}", method=HttpMethod.DELETE)
	void deleteAlarm(@SN String sn, @RVariable("alarmId") String alarmId) throws SKLRemoteException;

	/**
	 * 新增alarm列表的一项
	 * @param sn
	 * @param alarmId
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule", rootName="eventSchedule", xmln="urn:skylight", method=HttpMethod.POST)
	void addAlarm(@SN String sn, @RRoot EventScheduleIO eventScheduleIO) throws SKLRemoteException;

	/**
	 * 修改alarm列表的一项
	 * @param sn
	 * @param alarmId
	 * @param eventScheduleIO
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/fmlExt/Schedule/{alarmId}", rootName="eventSchedule", xmln="urn:skylight", method=HttpMethod.PUT)
	void modifyAlarm(@SN String sn, @RVariable("alarmId") String alarmId, @RRoot EventScheduleIO eventScheduleIO) throws SKLRemoteException;
    
	/**
	 * 对讲音频文件的信息转发给设备
	 * @param sn
	 * @param talkFileListIO
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/Custom/talk/play", rootName="TalkFileList", xmln="urn:skylight", method=HttpMethod.POST) 
	void pushToTalk(@SN String sn, @RRoot TalkFileListIO talkFileListIO) throws SKLRemoteException;
    
	
	/**
	 * 设备状态获取
	 * @param sn
	 * @return
	 * @throws SKLRemoteException
	 */
	@IPCRemote(uri="/PSIA/System/status", method=HttpMethod.GET)
	ScheduleStatusIO queryScheduleStatus(@SN String sn) throws SKLRemoteException;
}
