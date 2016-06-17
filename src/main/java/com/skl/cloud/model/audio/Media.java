package com.skl.cloud.model.audio;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Media extends IdEntity{
	//数据的类型(0:music/1:story/2:alarm)
	public static final int MEDIA_TYPE_MUSIC = 0;
	public static final int MEDIA_TYPE_STORY = 1;
	public static final int MEDIA_TYPE_ALARM = 2;
	//源文件来源(0:预设/1:用户自己上传)
	public static final int SOURCE_TYPE_SYSTEM = 0;
	public static final int SOURCE_TYPE_USER = 1;
	
    //设备 id
    private Long cameraId;
    //源文件名
    private String fileName;
    //源文件地址
    private String fileUrl;
    // 文件大小
    private Long fileSize;
    //播放时长(秒)
    private Long playTime;
    //数据的类型(0:music/1:story/2:alarm)
    private Integer mediaType;
    //源文件来源(0:预设/1:用户自己上传)
    private Integer sourceType;
    //创建时间
    private Date createDate;
    //最后修改的时间
    private Date updateDate;
    
    
	/**
	 * @return the cameraId
	 */
	public Long getCameraId() {
		return cameraId;
	}
	/**
	 * @param cameraId the cameraId to set
	 */
	public void setCameraId(Long cameraId) {
		this.cameraId = cameraId;
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
	 * @return the playTime
	 */
	public Long getPlayTime() {
		return playTime;
	}
	/**
	 * @param playTime the playTime to set
	 */
	public void setPlayTime(Long playTime) {
		this.playTime = playTime;
	}
	/**
	 * @return the mediaType
	 */
	public Integer getMediaType() {
		return mediaType;
	}
	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}
	/**
	 * @return the sourceType
	 */
	public Integer getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
    
    
    
}