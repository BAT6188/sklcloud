package com.skl.cloud.model;
/**
 *  
 * @author weibin
 * @date 2016年2月29日
 */
public class AppSnsInfo {
	private long id;
	private String model;
	private String systemType;
	private String platform;
	private String apiKey;
	private String secretKey;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	
	
}
