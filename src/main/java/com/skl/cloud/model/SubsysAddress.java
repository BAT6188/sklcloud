package com.skl.cloud.model;

public class SubsysAddress {
	
	private String serverUuid;
	private String ip;
	private String port;
	private int sysId;
	private String publicIp;
	
	/**
	 * getter method
	 * @return the publicIp
	 */
	public String getPublicIp() {
		return publicIp;
	}
	
	/**
	 * setter method
	 * @param publicIp the publicIp to set
	 */
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	
	public String getServerUuid() 
	{
		return serverUuid;
	}
	public void setServerUuid(String serverUuid) 
	{
		this.serverUuid = serverUuid;
	}
	public String getIp() 
	{
		return ip;
	}
	public void setIp(String ip) 
	{
		this.ip = ip;
	}
	public String getPort() 
	{
		return port;
	}
	public void setPort(String port)
	{
		this.port = port;
	}
	
	public int getSysId() 
	{
		return sysId;
	}
	public void setSysId(int sysId) 
	{
		this.sysId = sysId;
	}
	
}
