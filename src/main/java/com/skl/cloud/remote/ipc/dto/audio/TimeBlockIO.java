package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="timeBlock")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeBlockIO {
	
	@XmlElement(name="dayOfWeek")
	private int dayOfWeek;
	@XmlElement(name="timeRange")
	private TimeRangeIO timeRange;
	/**
	 * @return the dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	/**
	 * @return the timeRange
	 */
	public TimeRangeIO getTimeRange() {
		return timeRange;
	}
	/**
	 * @param timeRange the timeRange to set
	 */
	public void setTimeRange(TimeRangeIO timeRange) {
		this.timeRange = timeRange;
	}
	
}
