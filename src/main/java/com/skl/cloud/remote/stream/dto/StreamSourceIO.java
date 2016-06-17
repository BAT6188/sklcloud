package com.skl.cloud.remote.stream.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
  * @ClassName: StreamSource
  * @Description: TODO
  * @author wangming
  * @date 2015年12月9日
  *
 */
@XmlRootElement(name="streamSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreamSourceIO {

	private String sourceServerIp; 
	private String sourceServerType;
	
	public String getSourceServerIp() {
		return sourceServerIp;
	}
	public void setSourceServerIp(String sourceServerIp) {
		this.sourceServerIp = sourceServerIp;
	}
	public String getSourceServerType() {
		return sourceServerType;
	}
	public void setSourceServerType(String sourceServerType) {
		this.sourceServerType = sourceServerType;
	} 
	
}
