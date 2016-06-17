package com.skl.cloud.model.user;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

/**
 * 
 * @ClassName: UserRole
 * @Description: TODO
 * <p>Creation Date: 2016年5月31日 and by Author: zhaonao </p>
 *
 * @author $Author: zhaonao $
 * @date $Date: 2016-06-02 16:03:09 +0800 (Thu, 02 Jun 2016) $
 * @version  $Revision: 9331 $
 */
@SuppressWarnings("serial")
public class UserRole extends IdEntity {

	// 用户id
	private Long userId;

	// 角色id
	private Long roleId;

	// ipc设备id
	private Long cameraId;

	// 创建时间
	private Date createDate;

	// 最近修改时间
	private Date lupDate;

	/**
	 * getter method
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * setter method
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * getter method
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * setter method
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * getter method
	 * @return the cameraId
	 */
	public Long getCameraId() {
		return cameraId;
	}

	/**
	 * setter method
	 * @param cameraId the cameraId to set
	 */
	public void setCameraId(Long cameraId) {
		this.cameraId = cameraId;
	}

	/**
	 * getter method
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * setter method
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * getter method
	 * @return the lupDate
	 */
	public Date getLupDate() {
		return lupDate;
	}

	/**
	 * setter method
	 * @param lupDate the lupDate to set
	 */
	public void setLupDate(Date lupDate) {
		this.lupDate = lupDate;
	}
}