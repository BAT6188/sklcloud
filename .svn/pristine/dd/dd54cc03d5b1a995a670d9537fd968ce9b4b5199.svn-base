package com.skl.cloud.controller.sub.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResponseStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseStatusSO {

	private String statusCode;
	private String statusString;

	
	public ResponseStatusSO(int statusCode, String statusString) {
		this.statusCode = Integer.toHexString(statusCode);
		this.statusString = statusString;
	}
	
	public ResponseStatusSO() {}



	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusString
	 */
	public String getStatusString() {
		return statusString;
	}

	/**
	 * @param statusString
	 *            the statusString to set
	 */
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
}
