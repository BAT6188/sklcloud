package com.skl.cloud.foundation.mvc;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonResult {

	private String code = "0";
	private String msg = "success";
	private final Map<String, Object> data;

	public JsonResult() {
		this.data = new LinkedHashMap<String, Object>();
	}

	/**
	 * 增加参数
	 * 
	 * @param attrKey
	 * @param attrValue
	 */
	public void addAttribute(String attrKey, Object attrValue) {
		data.put(attrKey, attrValue);
	}

	/**
	 * 增加参数
	 * 
	 * @param modelMap
	 */
	public void addAttributes(Map<String, Object> modelMap) {
		data.putAll(modelMap);
	}

	

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

}
