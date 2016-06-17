package com.skl.cloud.controller.web.dto;

public class MusicFO {
	// 编号
	private String id;
	//media文件的ID
	private String mediaId;
	//music的名称
	private String name;
	//对应的文件名
	private String fileName;
	// 文件相关url可用于试听播放
	private String fileUrl;
	// 音乐时长
	private Long duration;
	
	
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
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	
}
