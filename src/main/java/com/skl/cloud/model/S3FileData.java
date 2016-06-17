package com.skl.cloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Package com.skl.cloud.model
 * @Title: S3FileData
 * @Description: t_platform_file实体类 Copyright: Copyright (c) 2015
 *               Company:西安天睿软件有限公司
 * 
 * @author zhaonao
 * @date 2015年12月30日
 * @version V1.0
 */
public class S3FileData implements Serializable
{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -5957044173506809384L;

	// 唯一标识，主键
	private String uuid;

	// 业务类型
	private String serviceType;

	// 文件名称
	private String fileName;

	// 文件路径
	private String filePath;

	// 文件状态（0：未上传；1：已上传）
	private String fileStatus;
	
	private String userId;
	private String deviceSn;
	private String fileSize;

	// 创建时间
	private Date createTime;

	// 最后修改时间
	private Date datetime;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFileStatus()
	{
		return fileStatus;
	}

	public void setFileStatus(String fileStatus)
	{
		this.fileStatus = fileStatus;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getDatetime()
	{
		return datetime;
	}

	public void setDatetime(Date datetime)
	{
		this.datetime = datetime;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(String fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getDeviceSn()
	{
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}
}
