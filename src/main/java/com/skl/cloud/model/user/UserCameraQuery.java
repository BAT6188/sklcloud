package com.skl.cloud.model.user;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class UserCameraQuery extends IdEntity {
    // 用户id
    private Long userId;
    // 设备id
    private Long cameraId;
    // 设备openId
    private String openId;
    // 设备sn
    private String sn;
    // 设备deviceId
    private String deviceId;
    // 用户与ipc关系类型
    private Integer linkType;

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}