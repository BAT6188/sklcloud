package com.skl.cloud.controller.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.controller.web.dto.AlarmFO;
import com.skl.cloud.controller.web.dto.MediaFO;
import com.skl.cloud.controller.web.dto.MusicFO;
import com.skl.cloud.controller.web.dto.PlayStatusFO;
import com.skl.cloud.controller.web.dto.StoryFO;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.model.audio.Alarm;
import com.skl.cloud.model.audio.Media;
import com.skl.cloud.model.audio.Music;
import com.skl.cloud.model.audio.MusicList;
import com.skl.cloud.model.audio.Story;
import com.skl.cloud.remote.ipc.dto.audio.EventScheduleIO;
import com.skl.cloud.remote.ipc.dto.audio.MusicPlayStatusIO;
import com.skl.cloud.remote.ipc.dto.audio.ScheduleIO;
import com.skl.cloud.remote.ipc.dto.audio.ScheduleStatusIO;
import com.skl.cloud.service.audio.AudioService;
import com.skl.cloud.service.user.WechatUserService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.validator.ValidationUtils;

/**
 * @ClassName: AudioController
 * @Description: User request from wechat
 * @author liuhui,fulin
 * @date 2015年10月7日
 */
@Controller
@RequestMapping("/skl-cloud/wechat/")
public class AudioController extends FrontController {

	@Autowired
	private AudioService audioService;
	@Autowired
	private WechatUserService userService;
	
	/**
	 * 获得audio的云端文件
	 * @param openId
	 * @param sn
	 * @param type
	 * @param model
	 */
	@RequestMapping("device/getFiles.json")
	public void getMediaFiles(@Param String openId, @Param  String sn, @Param String type, Model model) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(type, new String[] { "type" });
		int mediaType = Media.MEDIA_TYPE_MUSIC;
		if(("music").equals(type)) {
			mediaType = Media.MEDIA_TYPE_MUSIC;
		} else if("alarm".equals(type)) {
			mediaType = Media.MEDIA_TYPE_ALARM;			
		} else if("story".equals(type)) {
			mediaType = Media.MEDIA_TYPE_STORY;			
		} else {
			AssertUtils.formatNotCorrect(new String[] { "type:" + type}); 
		}
		List<Media> medias = audioService.listMediaBySN(sn, mediaType);
		List<MediaFO> mediaFOs = new ArrayList<MediaFO>();
		if(!medias.isEmpty()) {
			for (Media media : medias) {
				MediaFO mediaFO = new MediaFO();
				mediaFO.setId(media.getId()+"");
				mediaFO.setName(media.getFileName());
				mediaFO.setFileUrl(media.getFileUrl());
				mediaFOs.add(mediaFO);
			}
		}
		model.addAttribute("table", mediaFOs);
	}
	
	
	/**
	 * 获取用户alarm/story/music列表
	 * @param openId
	 * @param type
	 * @param sn
	 * @param model
	 */
	@RequestMapping("device/voclist.json")
	public void listVoc(@Param String openId,@Param String type, @Param String sn, Model model) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(type, new String[] { "type" });
		// 获得music列表
		if(("music").equals(type)) {
			MusicList musicList = audioService.getMusicList(sn);
			List<MusicFO> muicFOs= new ArrayList<MusicFO>();
			if (musicList!=null) {
				List<Music> musics = musicList.getMusicList();
				for (Music music : musics) {
					MusicFO musicFO = musicToFO(music);
					muicFOs.add(musicFO);
				}
			}
			model.addAttribute("musicList", muicFOs);
		}
		else if("alarm".equals(type)) {
			List<Alarm> alarms = audioService.getAlarmList(sn);
			List<AlarmFO> alarmFOs= new ArrayList<AlarmFO>();
			for (Alarm alarm : alarms) {
				AlarmFO alarmFO = alarmToFO(alarm);
				alarmFOs.add(alarmFO);
			}
			model.addAttribute("alarmList", alarmFOs);
		}
		else if("story".equals(type)) {
			List<Story> storys = audioService.getStoryList(sn);
			List<StoryFO> storyFOs = new ArrayList<StoryFO>();
			for (Story stroy : storys) {
				StoryFO storyFO = storyToFO(stroy);
				storyFOs.add(storyFO);
			}
			model.addAttribute("storyList", storyFOs);
		}
		else {
			AssertUtils.formatNotCorrect(new String[] {"type:" + type}); 
		}
	}
	
	/**
	 * 删除ipc上的alarm/story/music
	 * 
	 * @param openId
	 * @param sn
	 * @param type
	 * @param id
	 */
	@RequestMapping("device/delete.json")
	public void deleteAudio(@Param String openId, @Param  String sn, @Param String type, @Param String id) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(type, new String[] { "type" });
		AssertUtils.isNotBlank(id, new String[] { "id" });
		// 删除music
		if(("music").equals(type)) {
			Long musicId = Long.parseLong(id);
			audioService.deleteMusic(sn, musicId);
		} else if("alarm".equals(type)) {
	    // 删除alarm
			Long alarmId = Long.parseLong(id);
			audioService.deleteAlarm(sn, alarmId);
		} else if("story".equals(type)) {
		// 删除story
			Long storyId = Long.parseLong(id);
			audioService.deleteStory(sn, storyId);
		} else {
			AssertUtils.formatNotCorrect(new String[] { "type:" + type}); 
		}
	}
	
	/**
	 * 增加Music
	 * @param openId
	 * @param sn
	 * @param mediaId
	 */
	@RequestMapping("device/upload.json")
	public void addMusic(@Param String openId, @Param String sn, @Param("id") String mediaId, @Param String taskId, @Param String type) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(type, new String[] { "type" });
		AssertUtils.isNotBlank(mediaId, new String[] { "mediaId" });
		AssertUtils.isNotBlank(taskId, new String[] { "taskId" });
		if ("music".equals(type)) {
			// 新建Music
			Music music = audioService.addMusic(sn, Long.parseLong(mediaId), taskId);
			// 发送至IPC
			audioService.addMusicToIPC(sn, music);
		}else {
			AssertUtils.formatNotCorrect(new String[] { "type:" + type}); 
		}
		
	}
	
	
	/**
	 * 获取ipc上是否正在播放music
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param model
	 */
	@RequestMapping("device/isPlayMusic.json")
	public void getPlayMusicStatus(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
		checkBind(openId, sn);
		MusicPlayStatusIO mps = audioService.getPlayMusicStatus(sn);
		// 0 当前不在播放; 1 正在播放; 2没文件列表不能播放
		String status = mps.getStatus();
		if (MusicPlayStatusIO.STATUS_PLAY.equalsIgnoreCase(status)) {
			model.addAttribute("isPlay", 1);
		}else if (MusicPlayStatusIO.STATUS_PAUSE.equalsIgnoreCase(status)) {
			model.addAttribute("isPlay", 0);
		}else if (MusicPlayStatusIO.STATUS_STOP.equalsIgnoreCase(status)) {
			model.addAttribute("isPlay", 2);
		}else {
			AssertUtils.throwBusinessEx(0x50000004); 
		}
		
		//剩余播放时间，将单位ms转换为s
		model.addAttribute("remainingTime",  MusicPlayStatusIO.STATUS_PLAY.equals(status) ? mps.getDelay()/1000 : 0);		
		
		// 设置播放总时长,将单位ms转换为s
		model.addAttribute("totalTime", mps.getTotalTime()/1000);
	}
	
	/**
	 * 设置音乐列表总播放时长
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param totalTime
	 */
	@RequestMapping("device/setTotalTime.json")
	public void setMusicTotalTime(@Param String openId, @Param String deviceId, @Param String sn, @Param Integer totalTime) {
		checkBind(openId, sn);
		AssertUtils.notNull(totalTime, new String[] { "totalTime" });
		if (0 < totalTime && totalTime <= 7200) {//播放时长最长只能设置为2小时，totalTime单位为秒
			audioService.setMusicTotalTime(sn, totalTime);
		}else {
			AssertUtils.formatNotCorrect(new String[] { "totalTime:" + totalTime}); 
		}
		
	}

	//--------------story-----------
	/**
	 * 新建或更改story列表的某一项
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param data
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("device/configure/story.json")
	public void setStoryList(@Param String openId, @Param String deviceId, @Param String sn, @Param String taskId, @Param StoryFO data){
		checkBind(openId, sn);
		AssertUtils.isNotBlank(taskId, new String[] {"taskId"});
		Story story = storyFOToStory(taskId, data);
		story.setCameraSn(sn);
		audioService.setStoryList(story);
	}
	

	//--------------alarm-------------
	/**
	 * 新建或更改alarm列表的某一项
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param data
	 */
	@RequestMapping("device/configure/alarm.json")
	public void setAlarmList(@Param String openId, @Param String deviceId, @Param String sn, @Param String taskId, @Param AlarmFO data) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(taskId, new String[] {"taskId"});
	    Alarm alarm = alarmFOToAlarm(taskId, data);
	    alarm.setCameraSn(sn);
	    audioService.setAlarmList(alarm);
	}
	

	/**
	 * 请求ipc播放/停止播放music
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param type
	 */
	@RequestMapping("device/playMusic.json")
	public void playMusic(@Param String openId, @Param String deviceId, @Param String sn, @Param String type) {
		checkBind(openId, sn);
		AssertUtils.isNotBlank(type, new String[] {"type"});
		if (StringUtils.equals(type, "play")||StringUtils.equals(type, "stop")) {
			audioService.playMusic(sn, type);
		}else {
			AssertUtils.formatNotCorrect(new String[] { "type:" + type}); 
		}
		
	}
	
	/**
	 * 与IPC对讲，将前置服务器存储在S3的录音文件URL等信息转发给IPC
	 * @param openId
	 * @param sn
	 * @param pathName
	 */
	@RequestMapping("device/talk.json")
	public void pushToTalk(@Param String openId, @Param String sn, @Param String pathName, @Param String fileName, @Param Integer fileSize, @Param String type){
		checkBind(openId, sn);
		AssertUtils.isNotBlank(pathName, new String[] {"pathName"}); 
		AssertUtils.isNotBlank(fileName, new String[] {"fileName"});
		AssertUtils.notNull(fileSize, new String[] {"fileSize"}); 
		if(fileSize <= 0) {
			AssertUtils.formatNotCorrect(new String[] {"fileSize:" + fileSize});
		}
		AssertUtils.isNotBlank(type, new String[] {"type"});
		
		audioService.pushToTalk(sn, pathName, fileName, fileSize, type);
		
	}
	
	/**
	 * 查询ipc目前在播放什么
	 * @param openId
	 * @param deviceId
	 * @param sn
	 * @param model
	 */
	@RequestMapping("device/isPlay.json")
	public void getPlayStatus(@Param String openId, @Param String deviceId, @Param String sn, Model model) {
		checkBind(openId, sn);
		ScheduleStatusIO playStatusIO = audioService.queryDeviceScheduleStatus(sn);
		int time = 0;
		// 如果是在播放音乐，组装相关信息返回
		if (playStatusIO.getPlayMusic() != null) {
			model.addAttribute("isPlay", PlayStatusFO.ISPLAY_PLAYING);
			model.addAttribute("type", "music");
			if (playStatusIO.getPlayMusic().getCurrentTime() == null) {
				AssertUtils.throwBusinessEx(0x50010036, new String[] {"currentTime"});
			}
            time = playStatusIO.getPlayMusic().getCurrentTime();
			if (time <= 0) {
				AssertUtils.throwBusinessEx(0x50010036, new String[] {"currentTime"});
			}else {
				model.addAttribute("remainingTime", time);
			}
			if (StringUtils.isBlank(playStatusIO.getPlayMusic().getFileName())) {
				AssertUtils.throwBusinessEx(0x50010036, new String[] {"fileName"});
			} else {
				model.addAttribute("name", playStatusIO.getPlayMusic().getFileName());
			}
			
		} else if (playStatusIO.getScheduleList() != null
				&& playStatusIO.getScheduleList().size() != 0) {
			// 如果是在播放故事，组装相关信息返回
			for (ScheduleIO scheduleIO : playStatusIO.getScheduleList()) {
				if (scheduleIO.getType().equals(EventScheduleIO.TYPE_STORY)) {
					model.addAttribute("isPlay", PlayStatusFO.ISPLAY_PLAYING);
					model.addAttribute("type", "story");
					if (scheduleIO.getCurrentTime() == null) {
						AssertUtils.throwBusinessEx(0x50010036, new String[] {"currentTime"});
					}
					time = scheduleIO.getCurrentTime();
					if (time <= 0) {
						AssertUtils.throwBusinessEx(0x50010036, new String[] {"currentTime"});
					}else {
						model.addAttribute("remainingTime", time);
					}
					if (StringUtils.isBlank(scheduleIO.getFileName())) {
						AssertUtils.throwBusinessEx(0x50010036, new String[] {"fileName"});
					} else {
						model.addAttribute("name", scheduleIO.getFileName());
					}
					
				}else {
					// 既没有播放故事也没有播放music的话，返回没有在播放
					model.addAttribute("isPlay", PlayStatusFO.ISPLAY_NOTPLAY);
					model.addAttribute("type", "");
					model.addAttribute("remainingTime", 0);
					model.addAttribute("name", "");
				}
			}
		} else {
			// 既没有播放故事也没有播放music的话，返回没有在播放
			model.addAttribute("isPlay", PlayStatusFO.ISPLAY_NOTPLAY);
			model.addAttribute("type", "");
			model.addAttribute("remainingTime", 0);
			model.addAttribute("name", "");
		}

	}
	
	/**
	 * 校验用户是否已关联该设备
	 * @param openId
	 * @param sn
	 */
	private void checkBind(String openId, String sn) {
		AssertUtils.isNotBlank(openId, new String[] {"openId"});
		AssertUtils.isNotBlank(sn, new String[] {"sn"});
		if(!userService.checkWebchatBind(openId, sn)) {
			AssertUtils.throwBusinessEx(0x50030002); 
		}
	}
	
	/**
	 * 将music转化为musicFO
	 * @param music
	 * @return
	 */
	private MusicFO musicToFO(Music music) {
		MusicFO musicFO = new MusicFO();
		musicFO.setId(music.getId() + "");
		musicFO.setName(music.getDisplayName());
		Media media = music.getMedia(); 
		if(media != null) {
			musicFO.setMediaId(Long.toString(media.getId()));  
			musicFO.setFileName(media.getFileName());
			musicFO.setFileUrl(media.getFileUrl());
			musicFO.setDuration(media.getPlayTime());
			if (StringUtils.isBlank(music.getDisplayName())) {
				musicFO.setName(media.getFileName());
			}
		}
		return musicFO;
	}
	
	/**
	 * 将alarm转化为alarmFO
	 * @param alarm
	 * @return
	 */
	private AlarmFO alarmToFO(Alarm alarm) {
		AlarmFO alarmFO =new AlarmFO();
		alarmFO.setId(Long.toString(alarm.getId()));
		alarmFO.setIsUsed(alarm.getActiveFlag());
		alarmFO.setPlayTime(DateUtil.date2string(alarm.getReserveTime(), "HH:mm"));
		String playMode = alarm.getPlayMode();
		if (StringUtils.equals(playMode, "single")) {
			alarmFO.setRepeat("single"); 
		}else {
			alarmFO.setRepeat(playMode);
		}
		alarmFO.setName(alarm.getDisplayName());
		Media media = alarm.getMedia();
		if(media != null) {
			alarmFO.setFileName(media.getFileName());
			alarmFO.setFileUrl(media.getFileUrl());
			alarmFO.setMediaId(Long.toString(media.getId()));
			if (StringUtils.isBlank(alarm.getDisplayName())) {  
	        	alarmFO.setName(media.getFileName());
			}
		}
		return alarmFO;
	}
	
	/**
	 * 将alarmFO转化为alarm
	 * @param taskId 
	 * @param alarmFO
	 * @return alarm
	 */
	private Alarm alarmFOToAlarm(String taskId, AlarmFO alarmFO) {
		
		Alarm alarm = new Alarm();
		AssertUtils.notNull(alarmFO.getId(), new String[] { "id" }); 
		if (alarmFO.getId().isEmpty()) {
			alarm.setId(null); 
		}else {                    
			alarm.setId(Long.parseLong(alarmFO.getId()));
		}
		//检验列表名字的合法性
		if (!ValidationUtils.checkListName(alarmFO.getName())) { 
			AssertUtils.formatNotCorrect(new String[] { "name:" + alarmFO.getName()}); 
		}
		alarm.setDisplayName(alarmFO.getName());
		int isUsed = alarmFO.getIsUsed();
		if (isUsed == Alarm.DISABLED || isUsed == Alarm.ENABLED) {
			alarm.setActiveFlag(isUsed);
		}else {
			AssertUtils.formatNotCorrect(new String[] { "isUsed:" + isUsed});
		}  
		alarm.setActiveFlag(alarmFO.getIsUsed());
		if (!ValidationUtils.checkDateTime02(alarmFO.getPlayTime())) {//检查时间格式为"HH:mm"的有效性
			AssertUtils.formatNotCorrect(new String[] { "playTime:" + alarmFO.getPlayTime()});
		}
		alarm.setReserveTime(DateUtil.str2Date(alarmFO.getPlayTime(), "HH:mm"));
		
		String repeat = alarmFO.getRepeat();
		AssertUtils.isNotBlank(repeat, new String[] { "repeat" });
		if (repeat.equals("single")) {
			alarm.setPlayMode("single");
		}else {
			if (!checkRepeat(repeat)) {
				AssertUtils.formatNotCorrect(new String[] { "repeat:" + repeat});
			}
			alarm.setPlayMode(alarmFO.getRepeat());
		} 
		
		// 检验mediaId的合法性
		AssertUtils.isNotBlank(alarmFO.getMediaId(), new String[] { "mediaId" });
		// 获取Media
		Media media = audioService.getMedia(Long.valueOf(alarmFO.getMediaId()));
		AssertUtils.existDB(media, new String[] { "mediaId:" + alarmFO.getMediaId()});
		// 判断media类型是否为alarm
		if (!media.getMediaType().equals(Media.MEDIA_TYPE_ALARM)) {
			AssertUtils.throwBusinessEx(0x50000001, new String[] { "mediaId:" + alarmFO.getMediaId() + " type"});
		}
		
		alarm.setMediaId(Long.parseLong(alarmFO.getMediaId())); 
		Date date = new Date();
		alarm.setCreateDate(date);
		alarm.setUpdateDate(date);
		alarm.setTaskId(taskId); 
		return alarm;
		
	}
	/**
	 * 将story转为storyFO
	 * @param story
	 * @return storyFO
	 */
	private StoryFO storyToFO(Story story) {
		StoryFO storyFO =new StoryFO();
		storyFO.setId(Long.toString(story.getId()));
		storyFO.setIsUsed(story.getActiveFlag());
		String playMode = story.getPlayMode();
		if (StringUtils.equals(playMode, "single")) {
			storyFO.setRepeat("single"); 
			storyFO.setPlayTime(DateUtil.date2string(story.getReserveTime(), "MM-dd HH:mm"));
		}else {
			storyFO.setRepeat(playMode);
			storyFO.setPlayTime(DateUtil.date2string(story.getReserveTime(), "HH:mm"));
		}
		storyFO.setName(story.getDisplayName()); 
		Media media = story.getMedia();
		if(media != null) {
			storyFO.setFileName(media.getFileName());
			storyFO.setFileUrl(media.getFileUrl());
			storyFO.setMediaId(Long.toString(media.getId()));
			if (StringUtils.isBlank(story.getDisplayName())) {
	        	storyFO.setName(media.getFileName()); 
			}
		}
		return storyFO;
	}
	
	/**
	 * 将storyFO转为story
	 * @param taskId 
	 * @param story
	 * @return storyFO
	 */
	private Story storyFOToStory(String taskId, StoryFO storyFO){

    	Story story = new Story();
    	AssertUtils.notNull(storyFO.getId(), new String[] { "id" });
		if (storyFO.getId().isEmpty()) {
			story.setId(null); 
		}else {
			story.setId(Long.parseLong(storyFO.getId()));
		}
		//检验列表名字的合法性
		if (!ValidationUtils.checkListName(storyFO.getName())) { 
			AssertUtils.formatNotCorrect(new String[] { "name:" + storyFO.getName()});
		}
		story.setDisplayName(storyFO.getName());
		int isUsed = storyFO.getIsUsed();
		if (isUsed == Story.DISABLED || isUsed == Story.ENABLED) {
			story.setActiveFlag(isUsed);
		}else {
			AssertUtils.formatNotCorrect(new String[] { "isUsed:" + isUsed});
		}
		
		String playTime = storyFO.getPlayTime();
		String repeat = storyFO.getRepeat();
		AssertUtils.isNotBlank(repeat, new String[] { "repeat" });
		if (repeat.equals("single")) {
			story.setPlayMode("single");
			//检验单次播放时，时间的有效性
		    Date pt = checkTime(playTime); 
		    if (pt == null) {
				AssertUtils.formatNotCorrect(new String[] { "playTime:" + playTime});
			}
			story.setReserveTime(pt);
		}else {
			if (!checkRepeat(repeat)) {
				AssertUtils.formatNotCorrect(new String[] { "repeat:" + repeat});
			}
			story.setPlayMode(repeat);
			if (!ValidationUtils.checkDateTime02(playTime)) {//检查时间格式为"HH:mm"的有效性，该格式对应着重复播放设置
				AssertUtils.formatNotCorrect(new String[] { "playTime:" + playTime});
			}
			story.setReserveTime(DateUtil.str2Date(playTime, "HH:mm"));
		} 
		
		// 检验mediaId的合法性
		AssertUtils.isNotBlank(storyFO.getMediaId(), new String[] { "mediaId" });
		// 获取Media
		Media media = audioService.getMedia(Long.valueOf(storyFO.getMediaId()));
		AssertUtils.existDB(media, new String[] { "mediaId:" + storyFO.getMediaId()});
		// 判断media类型是否为story
		if (!media.getMediaType().equals(Media.MEDIA_TYPE_STORY)) {
			AssertUtils.throwBusinessEx(0x50000001, new String[] { "mediaId:" + storyFO.getMediaId() + " type"});
		}
		
		story.setMediaId(Long.parseLong(storyFO.getMediaId())); 

		Date date = new Date();
		story.setCreateDate(date);
		story.setUpdateDate(date);
		story.setTaskId(taskId); 
		return story;
	}
	
	/**
	 * 检验story列表设置单次播放时，时间的有效性
	 * @param dateTime
	 * @param dateFomat 
	 */
	private Date checkTime(String dateTime) {
		// 检验是否为"MM-dd HH:mm"格式，该格式对应的是单次播放设置
		if (ValidationUtils.checkDateTime01(dateTime)) {
			// 单次播放需检验时间是否已过期
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dt1 = null;
			Calendar calendar = Calendar.getInstance();
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String dt = year+"-"+dateTime;
			try {
				dt1 = df.parse(dt);  
			} catch (ParseException e) {
				AssertUtils.formatNotCorrect(new String[] { "playTime:" + dateTime}); 
			}
			Date dt2 = new Date();
			
			int setInterval = 2;//设置最长预约时间为2个月
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(dt1);
			Calendar ca2 = Calendar.getInstance();
			ca2.setTime(dt2);
			int currentMon = ca2.get(Calendar.MONTH);
			int setMon = ca1.get(Calendar.MONTH);
			int interval = setMon - currentMon;
			if (0 <= interval && interval<= setInterval) {
				if (dt1.getTime()<dt2.getTime()) {//小于系统时间则不合法
					AssertUtils.formatNotCorrect(new String[] { "playTime:" + dateTime});
				}
				return dt1;
			}else if (interval <= (setInterval - 12)) {
				dt1.setYear(Integer.valueOf(year)+1-1900);
				return dt1;
			}else {
				AssertUtils.formatNotCorrect(new String[] { "playTime:" + dateTime});
			}
			
		}else {
			AssertUtils.formatNotCorrect(new String[] { "playTime:" + dateTime});
		}
		return null;
	}
	
	/**
	 * 检验重复播放时，repeat参数格式是否正确，正确格式为数字1、2、3、4、5、6、7其中一位或几位例如“1,2,5,6"的组合
	 * @param repeat
	 * @return
	 */
	private Boolean checkRepeat(String repeat) {
		//判断字符串最后一位是否为为1/2/3/4/5/6/7中一种，可避免产生如"1,2,3,,,"的字符串写入数据库
		if (!checkWeekday(repeat.substring(repeat.length()-1))) {
			return Boolean.FALSE;
		}
		 String str[] = repeat.split(",");
		 for(int i = 0;i < str.length - 1;i++){ //循环开始元素
			 //判断星期日期数字是否为1/2/3/4/5/6/7中一种
			 if (checkWeekday(str[i])) {
				 for(int j = i + 1;j < str.length;j++){ //循环后续所有元素
                     //如果相等，则重复
                     if(StringUtils.equals(str[i], str[j])){
                    	 //参数重复,返回false      
                    	 return Boolean.FALSE; 
                     }
            }
			}else {
				return Boolean.FALSE; 
			}  
		 }
		 //以上循环没有判断到数组最后一位是否为1/2/3/4/5/6/7中一种，在此添加判断
		 if (!checkWeekday(str[str.length-1])) {
			return Boolean.FALSE;
		}
		 return Boolean.TRUE;
	}
	
    /**
     * 判断星期日期数字字符串是否为1/2/3/4/5/6/7中一种
     * @param day
     * @return
     */
	private Boolean checkWeekday(String day) {
		if (day.equals("1")) {
			return Boolean.TRUE;
		}else if (day.equals("2")) {
			return Boolean.TRUE;
		}else if (day.equals("3")) {
			return Boolean.TRUE;
		}else if (day.equals("4")) {
			return Boolean.TRUE;
		}else if (day.equals("5")) {
			return Boolean.TRUE;
		}else if (day.equals("6")) {
			return Boolean.TRUE;
		}else if (day.equals("7")) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}	
}
