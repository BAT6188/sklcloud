package com.skl.cloud.model.user;

import java.util.Date;
import java.util.List;

import com.skl.cloud.common.entity.IdEntity;

@SuppressWarnings("serial")
public class Role extends IdEntity {

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色描述
	 */
	private String description;

	/**
	 * 角色种类
	 */
	private String kind;

	/**
	 * 角色类型
	 */
	private Integer type;

	/**
	 * 角色状态：0：废弃；1：正常
	 */
	private Integer availableFlag;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 最近修改时间
	 */
	private Date lupDate;

	/**
	 * 权限列表
	 */
	private List<Permission> privilegeList;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * setter method
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * setter method
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
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

	/**
	 * getter method
	 * @return the privilegeList
	 */
	public List<Permission> getPrivilegeList() {
		return privilegeList;
	}

	/**
	 * setter method
	 * @param privilegeList the privilegeList to set
	 */
	public void setPrivilegeList(List<Permission> privilegeList) {
		this.privilegeList = privilegeList;
	}
}