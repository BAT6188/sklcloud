package com.skl.cloud.remote.ipc.dto.audio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="play_music")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayMusicIO {

	@XmlElement(name="play")
	@XmlElementWrapper(name="playlist")
	private List<PlayIO> playlist;
	private String sequence;
	private String repeat;
	
	
	/**
	 * @return the playlist
	 */
	public List<PlayIO> getPlaylist() {
		return playlist;
	}
	/**
	 * @param playlist the playlist to set
	 */
	public void setPlaylist(List<PlayIO> playlist) {
		this.playlist = playlist;
	}
	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the repeat
	 */
	public String getRepeat() {
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	
	
	
}
