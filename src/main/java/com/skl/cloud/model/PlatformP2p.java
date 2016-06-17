package com.skl.cloud.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformP2p implements Serializable {
	private static final long serialVersionUID = 1L;
	private String camera_serialno;
	private String localIp;
	private String mappingIp;
	private String natType;
	private String msgType;
	private Date createtime;
	private Date modifytime;
	
	private PlatformMapping platformMapping;

	public String getCamera_serialno() {
		return camera_serialno;
	}

	public void setCamera_serialno(String camera_serialno) {
		this.camera_serialno = camera_serialno;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getMappingIp() {
		return mappingIp;
	}

	public void setMappingIp(String mappingIp) {
		this.mappingIp = mappingIp;
	}

	public String getNatType() {
		return natType;
	}

	public void setNatType(String natType) {
		this.natType = natType;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public PlatformMapping getPlatformMapping() {
		return platformMapping;
	}

	public void setPlatformMapping(PlatformMapping platformMapping) {
		this.platformMapping = platformMapping;
	}

}
