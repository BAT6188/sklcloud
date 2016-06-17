package com.skl.cloud.model;

import java.util.Date;
import java.util.List;

/**
 * @Package com.skl.cloud.model
 * @Title: PlatformCamera
 * @Description: t_platform_ipcamera实体类
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年6月15日
 * @version V1.0
 */
public class PlatformCamera {

	private Long cameraId;
	private String cameraSerialno;//序列号SN
	private String cameraMac;
	private String cameraNickname;
	private String cameraStyle;
	private String cameraModel;
	private String cameraPincode;
	private String cameraMakedate;
	private String cameraVersion;//摄像头版本号
	private Date cameraVerdate;
	private String cameraRandom;
	private String recRandom;
	private String sendRandom;
	private String p2pRandom;
	private String onlineStatus;
	private Integer cameraIslive;//是否在线
	private String cameraStatus;//摄像头状态
	private String cameraValidate;
	private String cameraExpired;
	private Integer cameraIsonline;
	private String cameraKind;//摄像头种类
	private String lenth;
	private String width;
    private List<PlatformUser> platformUser;
	private PlatformIpcameraSensor platformIpcameraSensor;

	public Long getCameraId() {
		return cameraId;
	}

	public void setCameraId(Long cameraId) {
		this.cameraId = cameraId;
	}

	public String getCameraSerialno() {
		return cameraSerialno;
	}

	public void setCameraSerialno(String cameraSerialno) {
		this.cameraSerialno = cameraSerialno;
	}

	public String getCameraMac() {
		return cameraMac;
	}

	public void setCameraMac(String cameraMac) {
		this.cameraMac = cameraMac;
	}

	public String getCameraNickname() {
		return cameraNickname;
	}

	public void setCameraNickname(String cameraNickname) {
		this.cameraNickname = cameraNickname;
	}

	public String getCameraStyle() {
		return cameraStyle;
	}

	public void setCameraStyle(String cameraStyle) {
		this.cameraStyle = cameraStyle;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	public void setCameraModel(String cameraModel) {
		this.cameraModel = cameraModel;
	}

	public String getCameraPincode() {
		return cameraPincode;
	}

	public void setCameraPincode(String cameraPincode) {
		this.cameraPincode = cameraPincode;
	}

	public String getCameraKind() {
		return cameraKind;
	}

	public void setCameraKind(String cameraKind) {
		this.cameraKind = cameraKind;
	}

	public String getCameraMakedate() {
		return cameraMakedate;
	}

	public void setCameraMakedate(String cameraMakedate) {
		this.cameraMakedate = cameraMakedate;
	}

	public String getCameraVersion() {
		return cameraVersion;
	}

	public void setCameraVersion(String cameraVersion) {
		this.cameraVersion = cameraVersion;
	}

	public String getCameraRandom() {
		return cameraRandom;
	}

	public void setCameraRandom(String cameraRandom) {
		this.cameraRandom = cameraRandom;
	}

	public String getRecRandom() {
		return recRandom;
	}

	public void setRecRandom(String recRandom) {
		this.recRandom = recRandom;
	}

	public String getSendRandom() {
		return sendRandom;
	}

	public void setSendRandom(String sendRandom) {
		this.sendRandom = sendRandom;
	}

	public String getP2pRandom() {
		return p2pRandom;
	}

	public void setP2pRandom(String p2pRandom) {
		this.p2pRandom = p2pRandom;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getCameraStatus() {
		return cameraStatus;
	}

	public void setCameraStatus(String cameraStatus) {
		this.cameraStatus = cameraStatus;
	}

	public Integer getCameraIslive() {
		return cameraIslive;
	}

	public void setCameraIslive(Integer cameraIslive) {
		this.cameraIslive = cameraIslive;
	}

	public String getCameraValidate() {
		return cameraValidate;
	}

	public List<PlatformUser> getPlatformUser() {
		return platformUser;
	}

	public void setPlatformUser(List<PlatformUser> platformUser) {
		this.platformUser = platformUser;
	}

	public Date getCameraVerdate() {
		return cameraVerdate;
	}

	public void setCameraVerdate(Date cameraVerdate) {
		this.cameraVerdate = cameraVerdate;
	}

	public void setCameraValidate(String cameraValidate) {
		this.cameraValidate = cameraValidate;
	}

	public String getCameraExpired() {
		return cameraExpired;
	}

	public void setCameraExpired(String cameraExpired) {
		this.cameraExpired = cameraExpired;
	}

	public Integer getCameraIsonline() {
		return cameraIsonline;
	}

	public void setCameraIsonline(Integer cameraIsonline) {
		this.cameraIsonline = cameraIsonline;
	}
	
	public String getLenth() {
		return lenth;
	}

	public void setLenth(String lenth) {
		this.lenth = lenth;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public PlatformIpcameraSensor getPlatformIpcameraSensor() {
		return platformIpcameraSensor;
	}

	public void setPlatformIpcameraSensor(PlatformIpcameraSensor platformIpcameraSensor) {
		this.platformIpcameraSensor = platformIpcameraSensor;
	}
}
