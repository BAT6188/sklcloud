package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="action_parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionParameterIO {

	private String type;
	@XmlElement(name="play_music")
	private PlayMusicIO playMusic;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the playMusic
	 */
	public PlayMusicIO getPlayMusic() {
		return playMusic;
	}
	/**
	 * @param playMusic the playMusic to set
	 */
	public void setPlayMusic(PlayMusicIO playMusic) {
		this.playMusic = playMusic;
	}
	
	
}
