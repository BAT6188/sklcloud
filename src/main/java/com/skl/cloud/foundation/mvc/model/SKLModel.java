package com.skl.cloud.foundation.mvc.model;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

import com.skl.cloud.foundation.mvc.method.annotation.ResponseName;

public class SKLModel {
	// 方法
	private Method method;
	// 返回的xml
	private String returnXml;
	// 返回的输入流
	private InputStream returnInputStream;
	// 返回对象
	private Object returnObject;

	private final ModelMap modelMap;
	
	private Map<String, Object> attrMap = new LinkedHashMap<String, Object>();

	public SKLModel(ModelMap modelMap) {
		this.modelMap = modelMap;
		this.modelMap.put(ModelKeys.MODEL_INFO_KEY, this);
	}

	/**
	 * 获得参数
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		return modelMap.get(key);
	}

	/**
	 * 设置参数
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 */
	public SKLModel addAttribute(String key, Object value) {
		modelMap.addAttribute(key, value);
		attrMap.put(key, value);
		return this;
	}

	/**
	 * 设置参数
	 * 
	 * @param attributes
	 * @return
	 */
	public SKLModel addAllAttributes(Map<String, ?> attributes) {
		modelMap.addAllAttributes(attributes);
		attrMap.putAll(attributes);
		return this;
	}

	/**
	 * 混合参数
	 * 
	 * @param attributes
	 * @return
	 */
	public SKLModel mergeAttributes(Map<String, ?> attributes) {
		modelMap.mergeAttributes(attributes);
		if (attributes != null) {
			for (String key : attributes.keySet()) {
				if (!attrMap.containsKey(key)) {
					attrMap.put(key, attributes.get(key));
				}
			}
		}
		return this;
	}

	public Map<String, Object> asMap() {
		return modelMap;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return the returnName
	 */
	public String getResponseName() {
		String responseName = null;
		if (method.isAnnotationPresent(ResponseName.class)) {
			responseName = method.getAnnotation(ResponseName.class).value();
		}
		if (StringUtils.isBlank(responseName)) {
			responseName = method.getName();
		}
		return responseName;
	}

	/**
	 * @return the returnXml
	 */
	public String getReturnXml() {
		return returnXml;
	}

	/**
	 * @param returnXml
	 *            the returnXml to set
	 */
	public void setReturnXml(String returnXml) {
		this.returnXml = returnXml;
	}

	/**
	 * @return the returnInputStream
	 */
	public InputStream getReturnInputStream() {
		return returnInputStream;
	}

	/**
	 * @param returnInputStream
	 *            the returnInputStream to set
	 */
	public void setReturnInputStream(InputStream returnInputStream) {
		this.returnInputStream = returnInputStream;
	}

	/**
	 * @return the returnObject
	 */
	public Object getReturnObject() {
		return returnObject;
	}

	/**
	 * @param returnObject
	 *            the returnObject to set
	 */
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	/**
	 * @return the modelMap
	 */
	public ModelMap getModelMap() {
		return modelMap;
	}

	/**
	 * @return the attrMap
	 */
	public Map<String, Object> getAttrMap() {
		return attrMap;
	}

	/**
	 * @param attrMap the attrMap to set
	 */
	public void setAttrMap(Map<String, Object> attrMap) {
		this.attrMap = attrMap;
	}

	
	
	
}
