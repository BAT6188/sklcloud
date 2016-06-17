package com.skl.cloud.controller.web.dto;

public class UserIPCameraFO {
    // 设备状态
    private String status;
    // 设备名称
    private String name;
    // 最后一次图片URL
    private String imgUrl;
    // 设备ID
    private String deviceId;
    // 设备SN
    private String sn;
    // 设备音量
    private Long speakerVolume;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the sn
     */
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return the speakerVolume
     */
    public Long getSpeakerVolume() {
        return speakerVolume;
    }

    public void setSpeakerVolume(Long speakerVolume) {
        this.speakerVolume = speakerVolume;
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

}
