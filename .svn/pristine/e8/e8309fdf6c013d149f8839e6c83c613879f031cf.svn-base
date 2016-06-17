package com.skl.cloud.remote.ipc.dto.audio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="musicPlayStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicPlayStatusIO {
	
	public static final String STATUS_PLAY = "Play";
	public static final String STATUS_PAUSE = "Pause";
	public static final String STATUS_STOP = "Stop";
	
	// 离停止播放的时间（Play, Pause状态需要此值， Stop状态则该值为0），单位ms，最大可到2小时
	@XmlElement(name="delay")
	private Long delay;
	// 总时长
	@XmlElement(name="totalTime")
	private Long totalTime;
	// Play, Pause, Stop” 当前播放状态,即有文件play pause则对应play pause,没有文件play  pause则stop
	@XmlElement(name="status")
	private String status;
	
	@XmlElement(name="musicFile")
	@XmlElementWrapper(name="musicFileList")
	private List<MusicFileIO> musicFiles;
	
	/**
	 * @return the delay
	 */
	public Long getDelay() {
		return delay;
	}
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(Long delay) {
		this.delay = delay;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the musicFiles
	 */
	public List<MusicFileIO> getMusicFiles() {
		return musicFiles;
	}
	/**
	 * @param musicFiles the musicFiles to set
	 */
	public void setMusicFiles(List<MusicFileIO> musicFiles) {
		this.musicFiles = musicFiles;
	}
	/**
	 * @return the totalTime
	 */
	public Long getTotalTime() {
		return totalTime;
	}
	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	
	
	
	

}
