package com.skl.cloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: AppCircleMember
 * @Description: 家庭组（圈子）管理-家庭组成员对象
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
public class AppCircleMember implements Serializable
{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1621057596299527570L;

	/**
	 * 家庭组ID（主键）
	 */
	private String circleId;

	/**
	 * 家庭组成员ID（主键）
	 */
	private String memberId;

	/**
	 * 家庭组成员昵称
	 */
	private String memberNickName;

	/**
	 * 家庭组成员权限
	 */
	private String memberCircleRole;

	/**
	 * 是否新加入此组
	 */
	private String isNew;

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

	public String getMemberId()
	{
		return memberId;
	}

	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}

	public String getMemberNickName()
	{
		return memberNickName;
	}

	public void setMemberNickName(String memberNickName)
	{
		this.memberNickName = memberNickName;
	}

	public String getMemberCircleRole()
	{
		return memberCircleRole;
	}

	public void setMemberCircleRole(String memberCircleRole)
	{
		this.memberCircleRole = memberCircleRole;
	}

	public String getIsNew()
	{
		return isNew;
	}

	public void setIsNew(String isNew)
	{
		this.isNew = isNew;
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
