package com.skl.cloud.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformUser implements Serializable{
	
	/**
	  * 
	  */
	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String userName;
	private String userGender;
	private String userTelephone;
	private String userMobile;
	private int userIdentifier;
	private Date userBirth;
	private String userPostalCode;
	private String userEmail;
	private String userAddress;
	private Date userCreateTime;
	private Date userModifyTime;
	
	public int getUserId() 
	{
		return userId;
	}
	public void setUserId(int userId) 
	{
		this.userId = userId;
	}
	
	public String getUserName() 
	{
		return userName;
	}
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	public String getUserGender() 
	{
		return userGender;
	}
	public void setUserGender(String userGender) 
	{
		this.userGender = userGender;
	}
	
	public String getUserTelephone() 
	{
		return userTelephone;
	}
	public void setUserTelephone(String userTelephone) 
	{
		this.userTelephone = userTelephone;
	}
	
	public String getUserMobile() 
	{
		return userMobile;
	}
	public void setUserMobile(String userMobile) 
	{
		this.userMobile = userMobile;
	}
	
	public int getUserIdentifier() 
	{
		return userIdentifier;
	}
	public void setUserIdentifier(int userIdentifier) 
	{
		this.userIdentifier = userIdentifier;
	}
	
	public Date getUserBirth() 
	{
		return userBirth;
	}
	public void setUserBirth(Date userBirth) 
	{
		this.userBirth = userBirth;
	}
	
	public String getUserPostalCode() 
	{
		return userPostalCode;
	}
	public void setUserPostalCode(String userPostalCode) 
	{
		this.userPostalCode = userPostalCode;
	}
	
	public String getUserEmail() 
	{
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public Date getUserCreateTime() {
		return userCreateTime;
	}
	public void setUserCreateTime(Date userCreateTime) {
		this.userCreateTime = userCreateTime;
	}
	public Date getUserModifyTime() {
		return userModifyTime;
	}
	public void setUserModifyTime(Date userModifyTime) {
		this.userModifyTime = userModifyTime;
	}
	
	

}
