package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduleIO {
	
	private String type;
	private String id;
	private String fileName;
	private Integer currentTime;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
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
	 * @return the currentTime
	 */
	public Integer getCurrentTime() {
		return currentTime;
	}
	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(Integer currentTime) {
		this.currentTime = currentTime;
	}
	
	
}
