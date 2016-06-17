package com.skl.cloud.remote.ipc.dto.ipc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="pictureUrl")
//对属性进行排序输出,添加排序条件，必须要对类的属性添加@XmlElement注解
@XmlType(propOrder = { "urlPath", "uuid", "serviceType"})
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestCurrentPictrueIO {

	// 上传路径
	@XmlElement(name="urlPath")
	private String urlPath;
	// 该上传的唯一标志
	@XmlElement(name="uuid")
	private String uuid;
	// 类型
	@XmlElement(name="serviceType")
	private String serviceType;
	
	/**
	 * @return the urlPath
	 */
	public String getUrlPath() {
		return urlPath;
	}
	/**
	 * @param urlPath the urlPath to set
	 */
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	
}
