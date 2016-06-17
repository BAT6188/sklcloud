package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="musicPlayCommand")
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicPlayCommandIO {
    //起始播放的文件的文件名
	private String fileName;
	//文件播放的起始时间位置，ms为单位
	private int location;
	//播放控制，值为"Play, Pause, Stop"中一种
	private String command;
	//文件播放的延时时间（即离停止播放的时间），ms为单位
	private int delay;
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(int location) {
		this.location = location;
	}
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
}
