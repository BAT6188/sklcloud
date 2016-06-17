package com.skl.cloud.remote.ipc.dto.audio;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.skl.cloud.common.xml.JaxbDateSerializer;

@XmlRootElement(name="dateTimeRange")
@XmlAccessorType(XmlAccessType.FIELD)
public class DateTimeRangeIO {
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	private Date beginDateTime;
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	private Date endDateTime;
	
	
	/**
	 * @return the beginDateTime
	 */
	public Date getBeginDateTime() {
		return beginDateTime;
	}
	/**
	 * @param beginDateTime the beginDateTime to set
	 */
	public void setBeginDateTime(Date beginDateTime) {
		this.beginDateTime = beginDateTime;
	}
	/**
	 * @return the endDateTime
	 */
	public Date getEndDateTime() {
		return endDateTime;
	}
	/**
	 * @param endDateTime the endDateTime to set
	 */
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	
}
