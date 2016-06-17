package com.skl.cloud.model.feed;

import java.util.Date;


public class DeviceDynamic {

	// private float temperature;
	// private float humidity;
	//
	// public float getTemperature() {
	// return temperature;
	// }
	// public void setTemperature(float temperature) {
	// this.temperature = temperature;
	// }
	// public float getHumidity() {
	// return humidity;
	// }
	// public void setHumidity(float humidity) {
	// this.humidity = humidity;
	// }
	//
	// public DeviceDynamic() {
	// super();
	// // TODO Auto-generated constructor stub
	// }
	private String Sn;
	private String temperature;
	private String humidity;
	private Date dateTime;
	
	public String getSn() {
		return Sn;
	}

	public void setSn(String sn) {
		Sn = sn;
	}

	public String getTemperature() {
		
		if (temperature == null) {
			//查不到该数据时，给app返回""
			temperature = "";
		}
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		if (humidity == null) {
			//查不到该数据时，给app返回""
			humidity = "";
		}
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public DeviceDynamic() {
		super();
		// TODO Auto-generated constructor stub
	}

}
