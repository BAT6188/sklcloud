package com.skl.cloud.controller.web.dto;

public class ResponseStatusFO {

	public String code = "0";
	public String msg = "";
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		if (code == 1) {
			this.code = "1";
		} else {
//			this.code = "0x" + code;
		    this.code = "0x" + Integer.toHexString(code);
		}
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}


	
}
