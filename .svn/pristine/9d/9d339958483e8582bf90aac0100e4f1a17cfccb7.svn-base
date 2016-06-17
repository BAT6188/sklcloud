package com.skl.cloud.remote.ipc.dto.audio;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="eventSchedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventScheduleIO {
	
	// status生效状态 (disable/enable)
	public static final String ENABLED = "enable";
	public static final String DISABLED = "disable";
	// type类型
	public static final String TYPE_STORY = "storyPlayback";
	public static final String TYPE_ALARM = "medicationReminder";
	
	private String id;
	private String type;
	// status生效状态， 值为disable/enable
	private String status;
	@XmlElement(name="dateTimeRange")
	private DateTimeRangeIO dateTimeRange;
	@XmlElement(name="timeBlock")
	@XmlElementWrapper(name="timeBlockList")
	private List<TimeBlockIO> timeBlockList;
	private ExtensionsIO extensions;
	
	
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
	 * @return the extensions
	 */
	public ExtensionsIO getExtensions() {
		return extensions;
	}
	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(ExtensionsIO extensions) {
		this.extensions = extensions;
	}
	/**
	 * @return the dateTimeRange
	 */
	public DateTimeRangeIO getDateTimeRange() {
		return dateTimeRange;
	}
	/**
	 * @param dateTimeRange the dateTimeRange to set
	 */
	public void setDateTimeRange(DateTimeRangeIO dateTimeRange) {
		this.dateTimeRange = dateTimeRange;
	}
	/**
	 * @return the timeBlockList
	 */
	public List<TimeBlockIO> getTimeBlockList() {
		return timeBlockList;
	}
	/**
	 * @param timeBlockList the timeBlockList to set
	 */
	public void setTimeBlockList(List<TimeBlockIO> timeBlockList) {
		this.timeBlockList = timeBlockList;
	}
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
	
	
}
