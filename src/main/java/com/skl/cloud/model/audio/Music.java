package com.skl.cloud.model.audio;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Music extends IdEntity{
	
	// 生效状态 (0:disabled/1:enabled)
	public static final int ENABLED = 1;
	public static final int DISABLED = 0;
	
    // 状态(0:已删除;1:正常;2:等待IPC增加)
	public static final int STATUS_DEL = 0;
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_WAIT = 2;
	
    // 媒体对象
    private Media media;
    // 列表编号
    private Long listId;
    //显示名字
    private String displayName;
    //生效状态 (0:disabled/1:enabled)
    private Integer activeFlag;
    //创建时间
    private Date createDate;
    //最后修改的时间
    private Date updateDate;
    // 任务编号
    private String taskId;
    // 状态(0:已删除;1:正常;2:等待IPC增加)
    private int status;

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the listId
	 */
	public Long getListId() {
		return listId;
	}

	/**
	 * @param listId the listId to set
	 */
	public void setListId(Long listId) {
		this.listId = listId;
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

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    
	
	
    
}