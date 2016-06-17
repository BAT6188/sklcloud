package com.skl.cloud.service.audio.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.dao.audio.AlarmMapper;
import com.skl.cloud.dao.audio.MediaMapper;
import com.skl.cloud.dao.audio.MusicListMapper;
import com.skl.cloud.dao.audio.StoryMapper;
import com.skl.cloud.dao.ipc.IPCMapper;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.foundation.remote.exception.CanceledOperationIPCRemoteException;
import com.skl.cloud.model.audio.Alarm;
import com.skl.cloud.model.audio.Media;
import com.skl.cloud.model.audio.Music;
import com.skl.cloud.model.audio.MusicList;
import com.skl.cloud.model.audio.Story;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.remote.ipc.AudioIPCRemote;
import com.skl.cloud.remote.ipc.dto.audio.ActionParameterIO;
import com.skl.cloud.remote.ipc.dto.audio.DateTimeRangeIO;
import com.skl.cloud.remote.ipc.dto.audio.EventScheduleIO;
import com.skl.cloud.remote.ipc.dto.audio.ExtensionsIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicFileIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayCommandIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.PlayIO;
import com.skl.cloud.remote.ipc.dto.audio.PlayMusicIO;
import com.skl.cloud.remote.ipc.dto.audio.ScheduleStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.TalkFileIO;
import com.skl.cloud.remote.ipc.dto.audio.TalkFileListIO;
import com.skl.cloud.remote.ipc.dto.audio.TimeBlockIO;
import com.skl.cloud.remote.ipc.dto.audio.TimeRangeIO;
import com.skl.cloud.service.audio.AudioService;

@Service
public class AudioServiceImpl implements AudioService {
	
	@Remote
	private AudioIPCRemote audioIPCRemote;
	@Autowired
	private MusicListMapper musicListMapper;
	@Autowired
	private IPCMapper ipcMapper;
	@Autowired
	private MediaMapper mediaMapper;
	@Autowired
	private AlarmMapper alarmMapper;
	@Autowired
	private StoryMapper storyMapper;
	// ---------- music
	
	@Override
	@Transactional(readOnly = true)
	public List<Media> listMediaBySN(String sn, int type) {
		return mediaMapper.listMediaBySN(sn, type); 
	}

	@Override
	@Transactional(readOnly = true)
	public MusicList getMusicList(String sn) throws BusinessException {
		return musicListMapper.getMusicListBySN(sn); 
	}
	
	
	
	@Override
	@Transactional
	public Music addMusic(String sn, Long mediaId, String taskId) {
		MusicList musicList = getMusicList(sn);
		//如果对应的设备还没有音乐列表，则新建一条
		if(musicList == null) {
			musicList = createMusicList(sn);
		}
		//如果音乐列表的音乐文件已满6条，则抛出异常
		if (musicList.getMusicList() != null) {
			if (musicList.getMusicList().size() >= 6) {
				AssertUtils.throwBusinessEx(0x50030004);
			}
		}
		
		Long listId = musicList.getId(); 
		return createMusic(mediaId, listId, taskId);
	}
	

	@Override
	@Transactional
	public void addMusicToIPC(String sn, Music music) {
		// 发送给IPC
		MusicFileIO mf = new MusicFileIO();
		mf.setId(music.getId());
		mf.setFilename(music.getMedia().getFileName());
		mf.setFileurl(music.getMedia().getFileUrl());
		mf.setDisplayName(music.getDisplayName());
		mf.setFileSize(music.getMedia().getFileSize());
		//此处，ipc端目前用不上length字段---20151210
		mf.setLength(music.getMedia().getPlayTime());
		//此处，ipc端目前用不上location字段,给默认值0---20151210
		mf.setLocation(0L);
		try {
			audioIPCRemote.addMusic(sn, mf, mf.getId());
		} catch (CanceledOperationIPCRemoteException e) {
			// 取消操作, 标记删除
			musicListMapper.setMusicStatus(music.getId(), Music.STATUS_DEL);
			return;
		}
		// 增加成功
		musicListMapper.setMusicStatus(music.getId(), Music.STATUS_NORMAL);
	}
	
	private Music createMusic(Long mediaId, Long listId, String taskId) {
		// 获取Media
		Media media = mediaMapper.getMedia(mediaId);
		AssertUtils.existDB(media, new String[] { "mediaId:" + mediaId});
		// 判断media类型是否为music
		if (!media.getMediaType().equals(Media.MEDIA_TYPE_MUSIC)) {
			AssertUtils.throwBusinessEx(0x50000001, new String[] { "mediaId:" + mediaId + " type"});
		}
		//判断taskId是否已存在表里，taskId必须唯一
		Boolean isEx = musicListMapper.isExistTaskId(taskId);
		if (isEx) {
			AssertUtils.throwBusinessEx(0x50000055, new String[] { "taskId:" + taskId }); 
		}
		// 新增music
		Music music = new Music();
		music.setActiveFlag(Music.ENABLED);
		music.setDisplayName(media.getFileName());
		music.setListId(listId);
		music.setMedia(media);
		music.setTaskId(taskId); 
		music.setCreateDate(new Date());
		music.setUpdateDate(new Date());
		music.setStatus(Music.STATUS_WAIT);
		musicListMapper.saveMusic(music);
		return music;
	}

	@Override
	@Transactional
	public void deleteMusic(String sn, Long musicId) {
		int status = Music.STATUS_NORMAL; 
		Music music = musicListMapper.getMusicBySN(sn, musicId, status);
		if(music != null) {
			// 先把music删除
			musicListMapper.setMusicStatus(musicId, Music.STATUS_DEL);
			// 发送给IPC删除
			audioIPCRemote.deleteMusic(sn, musicId);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public MusicPlayStatusIO getPlayMusicStatus(String sn) {
		return audioIPCRemote.getMusicPlayStatus(sn);
	}
	

	@Override
	@Transactional
	public void setMusicTotalTime(String sn, Integer totalTime) {

		// 更新IPC
		MusicPlayCommandIO musicPlayCommand = new MusicPlayCommandIO();
		musicPlayCommand.setCommand("Delay");
		musicPlayCommand.setDelay(totalTime*1000);
		//wechat项目fileName字段用不上，随便给个值"0"---20151210
		musicPlayCommand.setFileName("0");
		//musicId 与IPC默认为一固定值---20151210
		Long musicId = 0L;
		audioIPCRemote.setMusicList(sn, musicPlayCommand, musicId);
		
		// 更新数据库播放时间
		MusicList musicList = musicListMapper.getMusicListBySN(sn);
		if(musicList == null) {
			createMusicList(sn);
			musicList = musicListMapper.getMusicListBySN(sn);
			
		}
		musicListMapper.setMusicListTotalTime(musicList.getId(), totalTime.longValue());
		
	}
	
	@Override
	@Transactional
	public void playMusic(String sn, String type) {
		// 更新IPC
		MusicPlayCommandIO musicPlayCommand = new MusicPlayCommandIO();
		musicPlayCommand.setCommand(StringUtils.equalsIgnoreCase(type, "play") ? "Play" : "Pause");
		//wechat项目fileName字段用不上，随便给个值"0"---20151210
		musicPlayCommand.setFileName("0");
		//musicId 与IPC默认为一固定值---20151210
		Long musicId = 0L;
		audioIPCRemote.setMusicList(sn, musicPlayCommand, musicId);
	}

	private MusicList createMusicList(String sn) {
		IPCamera ipCamera = new IPCamera();
		ipCamera.setSn(sn);
		IPCamera ipc = ipcMapper.queryIPCamera(ipCamera);
		MusicList ml = new MusicList();
		ml.setCameraId(ipc.getId());
		ml.setCameraSn(ipc.getSn());
		ml.setTotalTime(60L);
		ml.setCreateDate(new Date());
		ml.setUpdateDate(new Date());
		musicListMapper.saveMusicList(ml);
		return ml;
	}



	//--------- Alarm
	
	@Override
	@Transactional(readOnly = true)
	public List<Alarm> getAlarmList(String sn) throws BusinessException{ 
	int status = Alarm.STATUS_NORMAL;   
	return alarmMapper.getAlarmListBySN(sn,status);
    } 
	
	@Override
	@Transactional
	public void deleteAlarm(String sn, Long alarmId) {
		Alarm alarm = alarmMapper.getAlarmByIdSn(alarmId, sn, Alarm.STATUS_NORMAL); 
		if (alarm !=null) {
			//先把数据库的alarm删除
			alarmMapper.setAlarmStatus(alarmId, Alarm.STATUS_DEL);
			//再发送给IPC删除
			audioIPCRemote.deleteAlarm(sn,"alarm_" + Long.toString(alarmId));
		}
	}
	
	@Override
	@Transactional
	public void setAlarmList(Alarm alarm) throws BusinessException{
		//判断taskId是否已存在表里，taskId必须唯一
		Boolean isEx = alarmMapper.isExistTaskId(alarm.getTaskId());
		if (isEx) {
			AssertUtils.throwBusinessEx(0x50000055, new String[] { "taskId:" + alarm.getTaskId() }); 
		}
		
		//若alarmId为空，表示新增一项alarm列表的设置；若alarmId不为空，表示对已存在某项alarm进行更改
		if (alarm.getId()==null) {
			String sn = alarm.getCameraSn();
			//判断数据库是否已存在6条alarm
			List<Alarm> alarms = alarmMapper.getAlarmListBySN(sn, Alarm.STATUS_NORMAL);
			if (alarms.size() >= 6) {
				AssertUtils.throwBusinessEx(0x50030004); 
			}
			//往数据库alarm列表新增一项story
			alarm.setStatus(Alarm.STATUS_WAIT); 
			alarmMapper.addAlarm(alarm);
			//取得自增的标识列 ID的值
			long newId = alarm.getId();
			EventScheduleIO eventScheduleIO = new EventScheduleIO();
			eventScheduleIO = alarmToIO(newId, alarm);
			
			//发送给设备，通知设备新增一项
			try {
				audioIPCRemote.addAlarm(sn, eventScheduleIO);
			} catch (CanceledOperationIPCRemoteException e) {
				// 取消操作, 标记删除
				alarmMapper.setAlarmStatus(newId, Alarm.STATUS_DEL);
				return;
			}
			// 增加成功
			alarmMapper.setAlarmStatus(newId, Alarm.STATUS_NORMAL);
		}else {
			//先查询数据库是否存在该alarm项
			Alarm alarmDB = alarmMapper.getAlarmByIdSn(alarm.getId(), alarm.getCameraSn(), Alarm.STATUS_NORMAL); 
			AssertUtils.existDB(alarmDB, new String[] { "alarmSchedule" });
			
			alarmMapper.setTaskId(alarm);
			Long alarmId = alarm.getId();
			EventScheduleIO eventScheduleIO = new EventScheduleIO();
			eventScheduleIO = alarmToIO(alarmId, alarm);
			String sn = alarm.getCameraSn();
            //发送给设备修改
			try {
				audioIPCRemote.modifyAlarm(sn, "alarm_" + Long.toString(alarmId), eventScheduleIO);
			} catch (CanceledOperationIPCRemoteException e) {
				// 取消操作, 不做任何处理
				return;
			}
			//设备修改成功，才修改数据库
			alarm.setStatus(Alarm.STATUS_NORMAL); 
			alarmMapper.modifyAlarm(alarm);
		}
	}
	
	
	
	//-----------story
	
	@Override
	@Transactional(readOnly = true)
	public List<Story> getStoryList(String sn) throws BusinessException{
		int status = Story.STATUS_NORMAL;
		return storyMapper.getStoryListBySN(sn, status); 
	}
	
	@Override
	@Transactional
	public void deleteStory(String sn, Long storyId) throws BusinessException{
		Story story = storyMapper.getStoryByIdSn(storyId, sn, Story.STATUS_NORMAL); 
		if (story !=null){
			//先把数据库的story删除
			storyMapper.setStoryStatus(storyId, Story.STATUS_DEL);
			//再发送给IPC删除
			audioIPCRemote.deleteStory(sn, "story_" + Long.toString(storyId));
		}
	}


	@Override
	@Transactional
	public void setStoryList(Story story) throws BusinessException {
		//判断taskId是否已存在表里，taskId必须唯一
		Boolean isEx = storyMapper.isExistTaskId(story.getTaskId());
		if (isEx) {
			AssertUtils.throwBusinessEx(0x50000055, new String[] { "taskId:" + story.getTaskId() });
		}
				
		//若storyId为空，表示前置服务器要新增一项story列表的设置；若storyId不为空，表示前置服务器要对已存在某项story进行更改
		if (story.getId()==null) {
			String sn = story.getCameraSn();
			//判断数据库是否已存在6条故事
			List<Story> storys = storyMapper.getStoryListBySN(sn, Story.STATUS_NORMAL);
			if (storys.size() >= 6) {
				AssertUtils.throwBusinessEx(0x50030004); 
			}
			//往数据库story列表新增一项story
			story.setStatus(Story.STATUS_WAIT); 
			storyMapper.setStoryList(story);
			//取得自增的标识列 ID的值
			long newId = story.getId();
			EventScheduleIO eventScheduleIO = new EventScheduleIO();
			eventScheduleIO = storyToIO(newId, story);
			
			//发送给设备，通知设备新增一项
			try {
				audioIPCRemote.addStory(sn, eventScheduleIO);
			} catch (CanceledOperationIPCRemoteException e) {
				// 取消操作, 标记删除
				storyMapper.setStoryStatus(newId, Story.STATUS_DEL);
				return;
			}
			// 增加成功
			storyMapper.setStoryStatus(newId, Story.STATUS_NORMAL);
			
		}else {
			//先查询数据库是否存在该story项
			Story storyDB = storyMapper.getStoryByIdSn(story.getId(), story.getCameraSn(), Story.STATUS_NORMAL); 
			AssertUtils.existDB(storyDB, new String[] { "storySchedule" });
			
			storyMapper.setTaskId(story);
			Long storyId = story.getId();
			EventScheduleIO eventScheduleIO = new EventScheduleIO();
			eventScheduleIO = storyToIO(storyId, story);
			String sn = story.getCameraSn();
            //发送给设备修改
			try {
				audioIPCRemote.modifyStory(sn, "story_" + Long.toString(storyId), eventScheduleIO);
			} catch (CanceledOperationIPCRemoteException e) {
				// 取消操作, 不做任何处理
				return;
			}
			//设备修改成功，才修改数据库
			story.setStatus(Story.STATUS_NORMAL); 
			storyMapper.modifyStoryList(story);
		}
	}
	
	
	private EventScheduleIO storyToIO(Long id, Story story) {
		EventScheduleIO eventScheduleIO = new EventScheduleIO();
		eventScheduleIO.setId("story_" + Long.toString(id));
		eventScheduleIO.setType(EventScheduleIO.TYPE_STORY);
		String status = EventScheduleIO.DISABLED; 
		if (story.getActiveFlag()==Story.ENABLED) {
			status = EventScheduleIO.ENABLED;
		}else {
			status = EventScheduleIO.DISABLED;
		}
		eventScheduleIO.setStatus(status);
		
		DateTimeRangeIO dateTimeRange = new DateTimeRangeIO();
		dateTimeRange.setBeginDateTime(story.getReserveTime());
        dateTimeRange.setEndDateTime(new Date());
//		eventScheduleIO.setDateTimeRange(dateTimeRange);
		
		List<TimeBlockIO> timeBlockList = new ArrayList<TimeBlockIO>();
		String playMode = story.getPlayMode();
		TimeRangeIO timeRange = new TimeRangeIO();
		timeRange.setBeginTime(story.getReserveTime());
		//用不上，给默认值系统当前时间
		timeRange.setEndTime(new Date());
		if (StringUtils.equals(playMode, "single")) {
			
			TimeBlockIO timeBlock = new TimeBlockIO();
			timeBlock.setDayOfWeek(0);
			timeBlock.setTimeRange(timeRange);
			timeBlockList.add(timeBlock);
			
			eventScheduleIO.setDateTimeRange(dateTimeRange);
			
		}else {
			int ia[] =stringToint(playMode);
			for (int i = 0; i < ia.length; i++) {
				TimeBlockIO timeBlock = new TimeBlockIO();
				timeBlock.setDayOfWeek(ia[i]);
				timeBlock.setTimeRange(timeRange);
				timeBlockList.add(timeBlock);
			}
			
		}
		
		eventScheduleIO.setTimeBlockList(timeBlockList);
		Media media = mediaMapper.getMedia(story.getMediaId());
		
		PlayIO play = new PlayIO(); 
		play.setFilename(media.getFileName());
		play.setFiletype("mp3");
		play.setFileUrl(media.getFileUrl());
		play.setFileSize(media.getFileSize());
		play.setLength(media.getPlayTime()); 
		//用不上，给默认值0
		play.setLocation(0L);
		play.setDisplayName(story.getDisplayName()); 
		List<PlayIO> playList = new ArrayList<PlayIO>();
		playList.add(play);
		
		PlayMusicIO playMusic = new PlayMusicIO();
		playMusic.setPlaylist(playList);
		
		ActionParameterIO actionParameter = new ActionParameterIO();
		actionParameter.setPlayMusic(playMusic);
		actionParameter.setType("play_music");
		
		ExtensionsIO extensions = new ExtensionsIO();
		extensions.setActionParameter(actionParameter);
		
		eventScheduleIO.setExtensions(extensions); 
		return eventScheduleIO;
	}
	
	private EventScheduleIO alarmToIO(Long id, Alarm alarm) {
		EventScheduleIO eventScheduleIO = new EventScheduleIO();
		eventScheduleIO.setId("alarm_" + Long.toString(id));
		eventScheduleIO.setType(EventScheduleIO.TYPE_ALARM);
		String status = EventScheduleIO.DISABLED;
		if (alarm.getActiveFlag() == Alarm.ENABLED) {
			status = EventScheduleIO.ENABLED;
		}else {
			status = EventScheduleIO.DISABLED; 
		}
		eventScheduleIO.setStatus(status);
		
		List<TimeBlockIO> timeBlockList = new ArrayList<TimeBlockIO>();
		String playMode = alarm.getPlayMode();
		TimeRangeIO timeRange = new TimeRangeIO();
		timeRange.setBeginTime(alarm.getReserveTime());
		//用不上，给默认值系统当前时间
		timeRange.setEndTime(new Date());
		if (StringUtils.equals(playMode, "single")) {
			
			TimeBlockIO timeBlock = new TimeBlockIO();
			timeBlock.setDayOfWeek(0);
			timeBlock.setTimeRange(timeRange);
			
			timeBlockList.add(timeBlock);
			
		}else {
			int ia[] =stringToint(playMode);
			for (int i = 0; i < ia.length; i++) {
				TimeBlockIO timeBlock = new TimeBlockIO();
				timeBlock.setDayOfWeek(ia[i]);
				timeBlock.setTimeRange(timeRange);
				timeBlockList.add(timeBlock);
			}
			
		}
		eventScheduleIO.setTimeBlockList(timeBlockList);
		
		Media media = mediaMapper.getMedia(alarm.getMediaId());
		PlayIO play = new PlayIO();
		play.setFilename(media.getFileName());
		play.setFiletype("mp3");
		play.setFileUrl(media.getFileUrl());
		play.setFileSize(media.getFileSize());
		play.setLength(media.getPlayTime()); 
		play.setDisplayName(alarm.getDisplayName()); 
		//用不上，给默认值0
		play.setLocation(0L);
		List<PlayIO> playList = new ArrayList<PlayIO>();
		playList.add(play);
		
		PlayMusicIO playMusic = new PlayMusicIO();
		playMusic.setPlaylist(playList);
		
		ActionParameterIO actionParameter = new ActionParameterIO();
		actionParameter.setPlayMusic(playMusic);
		actionParameter.setType("play_music");
		
		ExtensionsIO extensions = new ExtensionsIO();
		extensions.setActionParameter(actionParameter);
		
		eventScheduleIO.setExtensions(extensions); 
		return eventScheduleIO;
	}
	
	
	private int[] stringToint(String playMode) {
		String str[] = playMode.split(",");  
		int array[] = new int[str.length];  
		for(int i=0;i<str.length;i++){  
		    array[i]=Integer.parseInt(str[i]); 
		}
		return array; 
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public void pushToTalk(String sn, String pathName, String fileName, int fileSize, String type) {
		
		TalkFileIO talkFileIO = new TalkFileIO();
		//目前不采用切片方案，故把ID默认为1--------2015.11.9
		talkFileIO.setId(1); 
		talkFileIO.setFileUrl(pathName); 
		talkFileIO.setFileName(fileName);
		talkFileIO.setFileSize(fileSize);
		talkFileIO.setFileType(type); 
		List<TalkFileIO> talkFileList = new ArrayList<TalkFileIO>();
		talkFileList.add(talkFileIO);
		TalkFileListIO talkFileListIO = new TalkFileListIO();
		talkFileListIO.setTalkFileList(talkFileList); 
		audioIPCRemote.pushToTalk(sn, talkFileListIO); 
	}

	@Override
	@Transactional(readOnly = true)
	public Media getMedia(Long mediaId) throws BusinessException {
		return mediaMapper.getMedia(mediaId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Media> listSysMediaByType(int mediaType)
			throws BusinessException {
		
		return mediaMapper.listSysMediaByType(mediaType);
	}

	@Override
	@Transactional(readOnly = true)
	public Music getMusicByIdSn(String sn, Long musicId)
			throws BusinessException {
		
		return musicListMapper.getMusicByIdSn(sn, musicId);
	}

	@Override
	@Transactional
	public void setMusicStatus(Long id, int status) throws BusinessException {
		musicListMapper.setMusicStatus(id, status);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Alarm getAlarmById(Long alarmId) throws BusinessException {
		
		return alarmMapper.getAlarmById(alarmId);
	}

	@Override
	@Transactional
	public void setAlarmStatus(Long alarmId, int status)
			throws BusinessException {
		alarmMapper.setAlarmStatus(alarmId, status);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Story getStoryById(Long storyId) throws BusinessException {
		
		return storyMapper.getStoryById(storyId);
	}

	@Override
	@Transactional
	public void setStoryStatus(Long storyId, int status)
			throws BusinessException {
		storyMapper.setStoryStatus(storyId, status);
		
	}

	@Override
	@Transactional(readOnly = true)
	public ScheduleStatusIO queryDeviceScheduleStatus(String sn)
			throws BusinessException {
		
		return audioIPCRemote.queryScheduleStatus(sn);
	}

}
