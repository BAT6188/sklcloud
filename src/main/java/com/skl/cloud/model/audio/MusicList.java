package com.skl.cloud.model.audio;

import java.util.Date;
import java.util.List;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class MusicList extends IdEntity {
	// ipc编号
	private Long cameraId;
	// ipc的SN
	private String cameraSn;
	// 总播放时长
	private Long totalTime;
	// 创建时间
	private Date createDate;
	// 更新时间
	private Date updateDate;
	// 音乐列表
	private List<Music> musicList;
	
	
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
	 * @return the cameraSn
	 */
	public String getCameraSn() {
		return cameraSn;
	}
	/**
	 * @param cameraSn the cameraSn to set
	 */
	public void setCameraSn(String cameraSn) {
		this.cameraSn = cameraSn;
	}
	/**
	 * @return the totalTime
	 */
	public Long getTotalTime() {
		return totalTime;
	}
	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
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
	 * @return the musicList
	 */
	public List<Music> getMusicList() {
		return musicList;
	}
	/**
	 * @param musicList the musicList to set
	 */
	public void setMusicList(List<Music> musicList) {
		this.musicList = musicList;
	}
	
	
}
