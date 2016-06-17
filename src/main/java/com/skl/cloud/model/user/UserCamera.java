package com.skl.cloud.model.user;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class UserCamera extends IdEntity{
    //用户id
    private Long userId;
    //设备id
    private Long cameraId;
    //当地时间
    private Date localTime;
    //确认关联是否有效，0为无效，1为有效
    private Boolean confirm;
    //设备是否接收云端的消息推送
    private String userNotification;
    //设备sn
    private String cameraSerialno;
    //使能
    private String enable;

    //这个设备是否能被绑定的关联用户使用
    private Integer isUsedToShareUser;

    //用户与ipc的关系：0表示拥有关系，1表示install关系
    private Integer linkType;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public Date getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Date localTime) {
        this.localTime = localTime;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public String getUserNotification() {
        return userNotification;
    }

    public void setUserNotification(String userNotification) {
        this.userNotification = userNotification == null ? null : userNotification.trim();
    }

    public String getCameraSerialno() {
        return cameraSerialno;
    }

    public void setCameraSerialno(String cameraSerialno) {
        this.cameraSerialno = cameraSerialno == null ? null : cameraSerialno.trim();
    }

	public Integer getIsUsedToShareUser() {
		return isUsedToShareUser;
	}

	public void setIsUsedToShareUser(Integer isUsedToShareUser) {
		this.isUsedToShareUser = isUsedToShareUser;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

    
}