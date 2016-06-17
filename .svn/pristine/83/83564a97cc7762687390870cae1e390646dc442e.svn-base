package com.skl.cloud.remote.stream.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
  * @ClassName: StreamControlIO
  * @Description: TODO
  * @author wangming
  * @date 2015年12月7日
  *
 */
@XmlRootElement(name="streamControl")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreamControlIO {

	private InputStreamIO inputStream;
	
	@XmlElement(name="outputStream")
	@XmlElementWrapper(name="outputStreamList")
	private List<OutputStreamIO> outputStreamList;

	public InputStreamIO getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStreamIO inputStream) {
		this.inputStream = inputStream;
	}

	public List<OutputStreamIO> getOutputStreamList() {
		return outputStreamList;
	}

	public void setOutputStreamList(List<OutputStreamIO> outputStreamList) {
		this.outputStreamList = outputStreamList;
	}
	
	
}
