package com.skl.cloud.model.audio;

import java.io.Serializable;
import java.util.Date;

/**
 * @Package com.skl.cloud.model.audio
 * @Title: AudioPlay
 * @Description: t_platform_audio_play实体类
 * @Copyright: Copyright (c) 2015
 * @Company:深圳天彩智通软件有限公司
 * @author lizhiwei
 * @date 2016年1月19日
 * @version V1.0
 */
public class AudioPlay implements Serializable
{

	private static final long serialVersionUID = 10001L;

	private String uuid;
	private String displayName;
	private String sn;
	private String activeFlag;
	private String pictureId;
	private String status;
	private int sequence;
	private Date createTime;
	private Date changeTime;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getSn()
	{
		return sn;
	}

	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public String getActiveFlag()
	{
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag)
	{
		this.activeFlag = activeFlag;
	}

	public String getPictureId()
	{
		return pictureId;
	}

	public void setPictureId(String pictureId)
	{
		this.pictureId = pictureId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public int getSequence()
	{
		return sequence;
	}

	public void setSequence(int sequence)
	{
		this.sequence = sequence;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getChangeTime()
	{
		return changeTime;
	}

	public void setChangeTime(Date changeTime)
	{
		this.changeTime = changeTime;
	}

}
