package com.skl.cloud.model;

import java.io.Serializable;

public class S3BasicData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320003415422726415L;

	
	private String serviceType;
	
	private String StorageSpace;
	private String DirectoryType;
	
	private String FileType;
	private String url;
	private String Remarks;
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getStorageSpace() {
		return StorageSpace;
	}
	public void setStorageSpace(String storageSpace) {
		StorageSpace = storageSpace;
	}
	public String getDirectoryType() {
		return DirectoryType;
	}
	public void setDirectoryType(String directoryType) {
		DirectoryType = directoryType;
	}
	public String getFileType() {
		return FileType;
	}
	public void setFileType(String fileType) {
		FileType = fileType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	
}
