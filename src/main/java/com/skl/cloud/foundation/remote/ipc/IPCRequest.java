package com.skl.cloud.foundation.remote.ipc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class IPCRequest {
	
	@XmlElement(name="httpCmd")
	private IPCHttpInfo httpInfo;
	
	@XmlElement(name="header")
	@XmlElementWrapper(name="headers")
	private List<IPCHeader> headers = new ArrayList<IPCHeader>();
	
	private String body;
	
	
	public void addHeader(String name, String value) {
		headers.add(new IPCHeader(name, value));
	}

	/**
	 * @return the headers
	 */
	public List<IPCHeader> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<IPCHeader> headers) {
		this.headers = headers;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the httpInfo
	 */
	public IPCHttpInfo getHttpInfo() {
		return httpInfo;
	}

	/**
	 * @param httpInfo the httpInfo to set
	 */
	public void setHttpInfo(IPCHttpInfo httpInfo) {
		this.httpInfo = httpInfo;
	}

	
}
