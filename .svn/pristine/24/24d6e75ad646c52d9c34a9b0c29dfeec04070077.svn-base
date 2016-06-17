package com.skl.cloud.foundation.sns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.skl.cloud.foundation.sns.SampleMessageGenerator;
import com.skl.cloud.foundation.sns.model.Message;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;

/**
 *  
 * @author weibin
 * @date 2016年3月4日
 */
public class BaiduMessage extends BaseMessage{
	private String id;
	private String description;
	//不显示给用户的消息
	@JsonProperty("service")
	@JsonUnwrapped
	private Object map;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Object getMap() {
		return map;
	}
	public void setMap(Object map) {
		this.map = map;
	}
	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert) {
		// TODO Auto-generated method stub
		this.id = eventAlert.getId();
		this.description = super.showMessage(ipCamera, eventAlert);
		return SampleMessageGenerator.jsonify(this);
	}
	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert,
			Message message) {
		// TODO Auto-generated method stub
		this.setDescription(message.getDescription());
		this.setMap(message.getMap());
		return SampleMessageGenerator.jsonify(this);
	}
}
