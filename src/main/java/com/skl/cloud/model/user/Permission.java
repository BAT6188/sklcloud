package com.skl.cloud.model.user;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Permission extends IdEntity {

	/**
	 * 权限名称
	 */
	private String name;

	/**
	 * 权限类型
	 */
	private String type;

	/**
	 * 对应接口uri
	 */
	private String uri;

	/**
	 * 所属父权限id
	 */
	private Long parentId;

	/**
	 * 权限许可说明
	 */
	private String permission;

	/**
	 * 权限状态：0：拒绝；1：允许
	 */
	private Integer availableFlag;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 最近修改时间
	 */
	private String lupDate;

	/**
	 * getter method
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter method
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter method
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * setter method
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getter method
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * setter method
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * getter method
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * setter method
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * getter method
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * setter method
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * getter method
	 * @return the availableFlag
	 */
	public Integer getAvailableFlag() {
		return availableFlag;
	}

	/**
	 * setter method
	 * @param availableFlag the availableFlag to set
	 */
	public void setAvailableFlag(Integer availableFlag) {
		this.availableFlag = availableFlag;
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
	public String getLupDate() {
		return lupDate;
	}

	/**
	 * setter method
	 * @param lupDate the lupDate to set
	 */
	public void setLupDate(String lupDate) {
		this.lupDate = lupDate;
	}
}