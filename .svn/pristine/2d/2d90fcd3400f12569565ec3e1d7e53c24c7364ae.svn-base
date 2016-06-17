package com.skl.cloud.util.constants;

public enum EventId {
	MOTIONDETECTION(0,"MotionDetection"),
	SOUNDDETECTION(22,"SoundDetection"),
	TEMPERATUREDETECTION(23,"TemperatureDetection"),
	HUMIDITYDETECTION(24,"HumidityDetection"),
	ACTIVITYDETECTION(25,"ActivityDetection");
	private int index;
	private String description;
	private EventId(int index,String desc){
		this.index = index;
		this.description = desc;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
     
	public static void main(String[] args) {
		for(EventId event:EventId.values()){
			System.out.println("name--"+event.name());
			System.out.println("ordinal--"+event.ordinal());
			System.out.println("Index--"+event.getIndex());
			System.out.println("Description--"+event.getDescription());
		}
	}
}
