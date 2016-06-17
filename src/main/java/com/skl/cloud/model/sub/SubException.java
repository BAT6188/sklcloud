package com.skl.cloud.model.sub;

import java.io.Serializable;

public class SubException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  String uuid;
	 private String subsysType;
	 private String subsysPublicIp;
	 
	 private String subsysPrivateIp;
	 
	 private String subsysUuid;
	 
	 private  String exceptionType;
	 
	 private  String exceptionNotification;
	 
	 private  String exceptionContent;
	 
	 private String streamSn;
	 private String streamType;
	 private  String  createDate;
	
	 
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSubsysType() {
		return subsysType;
	}
	public void setSubsysType(String subsysType) {
		this.subsysType = subsysType;
	}
	public String getSubsysPublicIp() {
		return subsysPublicIp;
	}
	public void setSubsysPublicIp(String subsysPublicIp) {
		this.subsysPublicIp = subsysPublicIp;
	}
	public String getSubsysPrivateIp() {
		return subsysPrivateIp;
	}
	public void setSubsysPrivateIp(String subsysPrivateIp) {
		this.subsysPrivateIp = subsysPrivateIp;
	}
	public String getSubsysUuid() {
		return subsysUuid;
	}
	public void setSubsysUuid(String subsysUuid) {
		this.subsysUuid = subsysUuid;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getExceptionNotification() {
		return exceptionNotification;
	}
	public void setExceptionNotification(String exceptionNotification) {
		this.exceptionNotification = exceptionNotification;
	}
	public String getExceptionContent() {
		return exceptionContent;
	}
	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}
	public String getStreamSn() {
		return streamSn;
	}
	public void setStreamSn(String streamSn) {
		this.streamSn = streamSn;
	}
	public String getStreamType() {
		return streamType;
	}
	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
