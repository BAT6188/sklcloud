package com.skl.cloud.remote.ipc.dto.audio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="deviceStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduleStatusIO {

	private IsPlayMusicIO playMusic;
	
	@XmlElement(name="schedule")
	@XmlElementWrapper(name="scheduleList")
	private List<ScheduleIO> scheduleList;

	/**
	 * @return the playMusic
	 */
	public IsPlayMusicIO getPlayMusic() {
		return playMusic;
	}

	/**
	 * @param playMusic the playMusic to set
	 */
	public void setPlayMusic(IsPlayMusicIO playMusic) {
		this.playMusic = playMusic;
	}

	/**
	 * @return the scheduleList
	 */
	public List<ScheduleIO> getScheduleList() {
		return scheduleList;
	}

	/**
	 * @param scheduleList the scheduleList to set
	 */
	public void setScheduleList(List<ScheduleIO> scheduleList) {
		this.scheduleList = scheduleList;
	}
	
	
}
