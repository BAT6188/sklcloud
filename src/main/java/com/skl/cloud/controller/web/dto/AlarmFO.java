package com.skl.cloud.controller.web.dto;

public class AlarmFO {
	//列表Alarm的id
	private String id;
	//Alarm显示的名字
	private String name;
	//media文件的ID
	private String mediaId;
	//对应文件的文件名字
	private String fileName;
	//文件地址
	private String fileUrl;
	//生效的状态，0:不生效；1：生效
	private int isUsed;
	//播放Alarm的时间
	private String playTime;
	//重复播放的设置
	private String repeat;
	
	
	/**
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}
	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	/**
	 * @param fileUrl the fileUrl to set
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * @return the isUsed
	 */
	public int getIsUsed() {
		return isUsed;
	}
	/**
	 * @param isUsed the isUsed to set
	 */
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	
	/**
	 * @return the playTime
	 */
	public String getPlayTime() {
		return playTime;
	}
	/**
	 * @param playTime the playTime to set
	 */
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	/**
	 * @return the repeat
	 */
	public String getRepeat() {
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	
}
