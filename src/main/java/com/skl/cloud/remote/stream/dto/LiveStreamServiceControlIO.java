package com.skl.cloud.remote.stream.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
  * @ClassName: LiveStreamServiceControlIO
  * @Description: TODO
  * @author wangming
  * @date 2015年12月9日
  *
 */
@XmlRootElement(name = "streamControl")
@XmlAccessorType(XmlAccessType.FIELD)
public class LiveStreamServiceControlIO {

	private InputStreamIO inputStream;
	private StreamSourceIO streamSource;

	public InputStreamIO getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStreamIO inputStream) {
		this.inputStream = inputStream;
	}

	public StreamSourceIO getStreamSource() {
		return streamSource;
	}

	public void setStreamSource(StreamSourceIO streamSource) {
		this.streamSource = streamSource;
	}

}
