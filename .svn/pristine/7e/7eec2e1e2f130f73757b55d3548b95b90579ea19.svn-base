package com.skl.cloud.model.user;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Share extends IdEntity {
    // 图片分享
    public static final Integer SHARE_TYPE_IMAGE = 0;
    // 视频分享
    public static final Integer SHARE_TYPE_VIDEO = 1;
    // 音乐分享
    public static final Integer SHARE_TYPE_MUSIC = 2;

    // 设备id
    private Long cameraId;
    // 用户id
    private Long userId;
    // 设备的sn
    private String cameraSn;
    // 分享的url及数据在云所储存的url
    private String linkUrl;
    // 分享显示的图片url及数据在云所储存的url
    private String imgUrl;
    // 分享的类型(图片，视频，音乐）
    private Integer shareType;
    // 分享开始时间
    private Date startDate;
    // 分享结束时间
    private Date endDate;
    // 创建时间
    private Date createDate;
    // 最后修改的时间
    private Date lUpdDate;
    // dataUrl
    private String dataUrl;
    //uuid
    private String uuid;

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String cameraSn) {
        this.cameraSn = cameraSn == null ? null : cameraSn.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getlUpdDate() {
        return lUpdDate;
    }

    public void setlUpdDate(Date lUpdDate) {
        this.lUpdDate = lUpdDate;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}