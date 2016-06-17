package com.skl.cloud.foundation.sns.dto;

import java.util.Map;

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
public class AppleMessage extends BaseMessage {
	private Aps aps;
	private String id;
	//不显示给用户的消息
	@JsonProperty("service")
	@JsonUnwrapped
	private Object map;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Aps getAps() {
		return aps;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}		

	public Object getMap() {
		return map;
	}

	public void setMap(Object map) {
		this.map = map;
	}

	public void setText(String text){
		Aps aps = new Aps();
		aps.setAlert(text);
		this.aps=aps;
	}
	
	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert) {
		// TODO Auto-generated method stub
		String temp = super.showMessage(ipCamera, eventAlert);
		this.setText(temp);
		this.id = eventAlert.getId();
		return SampleMessageGenerator.jsonify(this);
	}
	
	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert,
			Message message) {
		// TODO Auto-generated method stub
		this.setText(message.getDescription());
		this.setMap(message.getMap());
		return SampleMessageGenerator.jsonify(this);
	}
}

class Aps{
	private String alert;
	private String badge;
	private String sound;
	
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
}
		
