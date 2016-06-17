package com.skl.cloud.model.user;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class WechatUser extends IdEntity{
    // 关注的值
    public static final Integer SUBSCRIBE_FLAG_YES = 1;
    // 不关注的值
    public static final Integer SUBSCRIBE_FLAG_NO = 0;
    
	// 关联用户编号
	private Long userId;
    //微信的OpenID
    private String openId;
    //是否关注(1:关注,0:取关)
    private Integer subscribeFlag;
    //创建关注时间
    private Date createDate;
    //最后修改时间
    private Date updateDate;
    
    
    
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * @return the subscribeFlag
	 */
	public Integer getSubscribeFlag() {
		return subscribeFlag;
	}
	/**
	 * @param subscribeFlag the subscribeFlag to set
	 */
	public void setSubscribeFlag(Integer subscribeFlag) {
		this.subscribeFlag = subscribeFlag;
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

    
    
}