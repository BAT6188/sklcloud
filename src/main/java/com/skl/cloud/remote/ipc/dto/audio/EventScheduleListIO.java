package com.skl.cloud.remote.ipc.dto.audio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="eventScheduleList")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventScheduleListIO {
	
	@XmlElement(name="eventSchedule")
	private List<EventScheduleIO> eventScheduleList;

	/**
	 * @return the eventScheduleList
	 */
	public List<EventScheduleIO> getEventScheduleList() {
		return eventScheduleList;
	}

	/**
	 * @param eventScheduleList the eventScheduleList to set
	 */
	public void setEventScheduleList(List<EventScheduleIO> eventScheduleList) {
		this.eventScheduleList = eventScheduleList;
	}
	
}
