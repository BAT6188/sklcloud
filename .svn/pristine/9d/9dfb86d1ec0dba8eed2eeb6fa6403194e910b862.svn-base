package com.skl.cloud.model.audio;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Story extends IdEntity{
	// 生效状态 (0:disabled/1:enabled)
	public static final int ENABLED = 1;
	public static final int DISABLED = 0;
	
    // 状态(0:已删除;1:正常;2:等待IPC增加)
	public static final int STATUS_DEL = 0;
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_WAIT = 2;
	
    //媒体ID
    private Long mediaId;
    //媒体对象
    private Media media;
    //显示名称
    private String displayName;
    //设备id
    private Long cameraId;
    //设备sn
    private String cameraSn;
    //预约时间
    private Date reserveTime;
    //重复播放的类型
    private String playMode;
    //是否在生效状态 (0:disabled/1:enabled)
    private int activeFlag;
    //创建时间
    private Date createDate;
    //最后修改的时间
    private Date updateDate;
    // 任务编号
    private String taskId;
    // 状态(0:已删除;1:正常;2:等待IPC增加)
    private int status;

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String cameraSn) {
        this.cameraSn = cameraSn == null ? null : cameraSn.trim();
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

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
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return the activeFlag
	 */
	public int getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the playMode
	 */
	public String getPlayMode() {
		return playMode;
	}

	/**
	 * @param playMode the playMode to set
	 */
	public void setPlayMode(String playMode) {
		this.playMode = playMode;
	}
    
    
}