package com.skl.cloud.model.mes;

import java.io.Serializable;
import java.util.Date;

/**
 * @Package com.skl.cloud.model
 * @Title: PlatformProd
 * @Description: t_platform_prod实体类
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月10日
 * @version V1.0
 */
public class PlatformProd implements Serializable {
	
	private static final long serialVersionUID = 123L;
	
	private int xh = 0;
	private String prodPn;
	private String prodSn;
	private String prodMac;
	private String prodVersion;
	private String prodMakedate;
	private String prodStatus;
	private Date createdate;
	private String createstaff;
	private Date modifydate;
	private String modifystaff;
	private String tbbz;
	
	public int getXh() {
		return xh;
	}
	
	public void setXh(int xh) {
		this.xh = xh;
	}
	
	public String getProdPn() {
		return prodPn;
	}
	
	public void setProdPn(String prodPn) {
		this.prodPn = prodPn;
	}
	
	public String getProdSn() {
		return prodSn;
	}
	
	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}
	
	public String getProdMac() {
		return prodMac;
	}
	
	public void setProdMac(String prodMac) {
		this.prodMac = prodMac;
	}
	
	public String getProdVersion() {
		return prodVersion;
	}
	
	public void setProdVersion(String prodVersion) {
		this.prodVersion = prodVersion;
	}
	
	public String getProdMakedate() {
		return prodMakedate;
	}
	
	public void setProdMakedate(String prodMakedate) {
		this.prodMakedate = prodMakedate;
	}
	
	public String getProdStatus() {
		return prodStatus;
	}
	
	public void setProdStatus(String prodStatus) {
		this.prodStatus = prodStatus;
	}
	
	public Date getCreatedate() {
		return createdate;
	}
	
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	public String getCreatestaff() {
		return createstaff;
	}
	
	public void setCreatestaff(String createstaff) {
		this.createstaff = createstaff;
	}
	
	public Date getModifydate() {
		return modifydate;
	}
	
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	
	public String getModifystaff() {
		return modifystaff;
	}
	
	public void setModifystaff(String modifystaff) {
		this.modifystaff = modifystaff;
	}

	public String getTbbz() {
		return tbbz;
	}

	public void setTbbz(String tbbz) {
		this.tbbz = tbbz;
	}

}
