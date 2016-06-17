package com.skl.cloud.model;

import java.util.Date;


public class PlatformIpcfw {

private long id;
private String model;
private String decversion;
private Date versiondate;
private String md5check;
private String verpath;
private String newdecversion;
private String newversiondate;
private String newmd5check;
private String newverpath;
private Integer length;

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}

public String getDecversion() {
	return decversion;
}

public void setDecversion(String decversion) {
	this.decversion = decversion;
}

public Date getVersiondate() {
	return versiondate;
}

public void setVersiondate(Date versiondate) {
	this.versiondate = versiondate;
}

public String getMd5check() {
	return md5check;
}

public void setMd5check(String md5check) {
	this.md5check = md5check;
}

public String getVerpath() {
	return verpath;
}

public void setVerpath(String verpath) {
	this.verpath = verpath;
}

public String getNewdecversion() {
	return newdecversion;
}

public void setNewdecversion(String newdecversion) {
	this.newdecversion = newdecversion;
}


public String getNewversiondate() {
	return newversiondate;
}

public void setNewversiondate(String newversiondate) {
	this.newversiondate = newversiondate;
}

public String getNewmd5check() {
	return newmd5check;
}

public void setNewmd5check(String newmd5check) {
	this.newmd5check = newmd5check;
}

public String getNewverpath() {
	return newverpath;
}

public void setNewverpath(String newverpath) {
	this.newverpath = newverpath;
}

public Integer getLength() {
	return length;
}

public void setLength(Integer length) {
	this.length = length;
}
}
