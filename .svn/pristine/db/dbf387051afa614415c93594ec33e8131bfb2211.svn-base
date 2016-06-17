package com.skl.cloud.model.ipc;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class IPCamera extends IdEntity {

	// 设备sn
	private String sn;

	// 摄像头mac地址
	private String mac;

	// 摄像头种类，如婴儿摄像头、老人摄像头等
	private String kind;

	// 摄像头型号，4个10进制数字，前2个表示系列，后两个表示分型，如：0001-0005表示LFM01-lFM05 0103表示HPC03
	private String model;

	// 摄像头的昵称
	private String nickname;

	// 摄像头生产日期
	private Date makeDate;

	// 设备的微信注册号(wechat+)
	private String deviceId;

	// 时区，格式为： +08:00
	private String timeZone;

	// 摄像头是否心跳正常 0 - 离线 1 - 在线
	private Boolean isLive;

	// 摄像头使能/流开关状态
	private Boolean isOnline;

	// 摄像头状态 0 - 未激活 1 - 已激活
	private Boolean status;

	// 摄像头最后离线时间
	private Date offlineTime;

	// 音量(wechat+)
	private Long speakerVolume;

	// motion alert状态 (1:on/0:off)(wechat+)
	private Integer motionAlertStatus;

	// sound alert状态 (1:on/0:off)(wechat+)
	private Integer soundAlertStatus;

	// 直播流分享使能 (1:允许分享； 0:不允许分享)(wechat+)
	private Integer liveShareStatus;

	// 实时图片抓拍唯一标识(wechat+)
	private String snapshotUuid;
	
	// ipc的密码
	private String password;

	// ipc从表
	private IPCameraSub ipcSub = new IPCameraSub();

	public void setId(Long id) {
		this.id = id;
		ipcSub.setId(id);
	}

	/**
	 * getter method
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * setter method
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
		ipcSub.setSn(sn);
	}

	/**
	 * getter method
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * setter method
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * getter method
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * setter method
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * getter method
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * setter method
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * getter method
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * setter method
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * getter method
	 * @return the makeDate
	 */
	public Date getMakeDate() {
		return makeDate;
	}

	/**
	 * setter method
	 * @param makeDate the makeDate to set
	 */
	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	/**
	 * getter method
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * setter method
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * getter method
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * setter method
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * getter method
	 * @return the isLive
	 */
	public Boolean getIsLive() {
		return isLive;
	}

	/**
	 * setter method
	 * @param isLive the isLive to set
	 */
	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * getter method
	 * @return the isOnline
	 */
	public Boolean getIsOnline() {
		return isOnline;
	}

	/**
	 * setter method
	 * @param isOnline the isOnline to set
	 */
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * getter method
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * setter method
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * getter method
	 * @return the offlineTime
	 */
	public Date getOfflineTime() {
		return offlineTime;
	}

	/**
	 * setter method
	 * @param offlineTime the offlineTime to set
	 */
	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	/**
	 * getter method
	 * @return the speakerVolume
	 */
	public Long getSpeakerVolume() {
		return speakerVolume;
	}

	/**
	 * setter method
	 * @param speakerVolume the speakerVolume to set
	 */
	public void setSpeakerVolume(Long speakerVolume) {
		this.speakerVolume = speakerVolume;
	}

	/**
	 * getter method
	 * @return the motionAlertStatus
	 */
	public Integer getMotionAlertStatus() {
		return motionAlertStatus;
	}

	/**
	 * setter method
	 * @param motionAlertStatus the motionAlertStatus to set
	 */
	public void setMotionAlertStatus(Integer motionAlertStatus) {
		this.motionAlertStatus = motionAlertStatus;
	}

	/**
	 * getter method
	 * @return the soundAlertStatus
	 */
	public Integer getSoundAlertStatus() {
		return soundAlertStatus;
	}

	/**
	 * setter method
	 * @param soundAlertStatus the soundAlertStatus to set
	 */
	public void setSoundAlertStatus(Integer soundAlertStatus) {
		this.soundAlertStatus = soundAlertStatus;
	}

	/**
	 * getter method
	 * @return the liveShareStatus
	 */
	public Integer getLiveShareStatus() {
		return liveShareStatus;
	}

	/**
	 * setter method
	 * @param liveShareStatus the liveShareStatus to set
	 */
	public void setLiveShareStatus(Integer liveShareStatus) {
		this.liveShareStatus = liveShareStatus;
	}

	/**
	 * getter method
	 * @return the ipcSub
	 */
	public IPCameraSub getIpcSub() {
		return ipcSub;
	}

	/**
	 * setter method
	 * @param ipcSub the ipcSub to set
	 */
	public void setIpcSub(IPCameraSub ipcSub) {
		this.ipcSub = ipcSub;
	}

	/**
	 * getter method
	 * @return the snapshotUuid
	 */
	public String getSnapshotUuid() {
		return snapshotUuid;
	}

	/**
	 * setter method
	 * @param snapshotUuid the snapshotUuid to set
	 */
	public void setSnapshotUuid(String snapshotUuid) {
		this.snapshotUuid = snapshotUuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}