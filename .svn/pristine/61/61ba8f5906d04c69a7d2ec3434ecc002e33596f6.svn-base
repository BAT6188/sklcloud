package com.skl.cloud.remote.ipc.dto.audio;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.skl.cloud.common.xml.JaxbDateSerializer2;

@XmlRootElement(name="timeRange")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeRangeIO {
	@XmlJavaTypeAdapter(JaxbDateSerializer2.class)
	private Date beginTime;
	@XmlJavaTypeAdapter(JaxbDateSerializer2.class)
	private Date endTime;
	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
