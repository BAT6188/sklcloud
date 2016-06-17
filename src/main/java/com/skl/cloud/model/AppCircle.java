package com.skl.cloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: AppCircle
 * @Description: 家庭组（圈子）管理-家庭组对象
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
public class AppCircle implements Serializable
{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -7445975411521026083L;

	/**
	 * 家庭组ID（主键）
	 */
	private String circleId;

	/**
	 * 家庭组类型
	 */
	private String circleType;

	/**
	 * 家庭组名称
	 */
	private String circleName;

	/**
	 * 家庭组简介
	 */
	private String circleInfo;

	/**
	 * 家庭组图标ID
	 */
	private String portraitId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改时间
	 */
	private Date changeTime;

	public String getCircleId()
	{
		return circleId;
	}

	public void setCircleId(String circleId)
	{
		this.circleId = circleId;
	}

	public String getCircleType()
	{
		return circleType;
	}

	public void setCircleType(String circleType)
	{
		this.circleType = circleType;
	}

	public String getCircleName()
	{
		return circleName;
	}

	public void setCircleName(String circleName)
	{
		this.circleName = circleName;
	}

	public String getCircleInfo()
	{
		return circleInfo;
	}

	public void setCircleInfo(String circleInfo)
	{
		this.circleInfo = circleInfo;
	}

	public String getPortraitId()
	{
		return portraitId;
	}

	public void setPortraitId(String portraitId)
	{
		this.portraitId = portraitId;
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
