package com.skl.cloud.foundation.remote;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;

public class RemoteContext {
	private static final Pattern P_VARIABLE = Pattern.compile("\\{(.*?)\\}");
	
	// 方法
	private Method method;
	// 参数信息
	private MethodParameter[] params;
	// 参数值
	private Object[] paramValues;
	// 版本
	private String version = "1.0";
	// 请求根对象
	private Object requestObject;
	// 请求流
	private InputStream requestInputStream;
	// 请求文本
	private String requestText;
	// 参数值
	private Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
	// 变量值
	private Map<String, String> varMap = new LinkedHashMap<String, String>();
	
	/**
	 * 与另外一个context进行混合
	 * @param context
	 */
	public void merge(RemoteContext context) {
		if(context.getRequestObject() != null) {
			setRequestObject(context.getRequestObject());
		}
		if(context.getRequestInputStream() != null) {
			setRequestInputStream(context.getRequestInputStream());
		}
		if(context.getRequestText() != null) {
			setRequestText(context.getRequestText());			
		}
		if(context.getVersion() != null) {
			setVersion(context.getVersion());
		}
		this.getParamMap().putAll(context.getParamMap());
	}
	
	protected String parse(String raw) {
		if(StringUtils.isBlank(raw)) {
			return raw;
		}
		if(!raw.startsWith("/")) {
			raw = "/" + raw;
		}
		// 替换参数
		Matcher matcher = P_VARIABLE.matcher(raw);
		matcher.reset();
		boolean result = matcher.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String variableName, variableValue;
			do {
				variableName = matcher.group(1);
				variableValue = varMap.get(variableName);
				matcher.appendReplacement(sb, variableValue);
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		else {
			return raw;
		}
	}
	
	/**
	 * 增加参数
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, Object value) {
		paramMap.put(name, value);
	}
	
	/**
	 * 增加变量
	 * @param name
	 * @param value
	 */
	public void addVariable(String name, Object value) {
		varMap.put(name, value == null ? "" : value.toString());
	}
	
	/**
	 * 获得Annotation
	 * @param annotationType
	 * @return
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return method == null ? null : method.getAnnotation(annotationType);
	}
	
	/**
	 * 获得注解了指定注解的参数值
	 * @param annotationType
	 * @return
	 */
	public Object getParamValuePresent(Class<? extends Annotation> annotationType) {
		for (MethodParameter param : params) {
			if(param.hasParameterAnnotation(annotationType)) {
				return paramValues[param.getParameterIndex()];
			}
		}
		return null;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return the params
	 */
	public MethodParameter[] getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	void setParams(MethodParameter[] params) {
		this.params = params;
	}

	/**
	 * @return the paramValues
	 */
	public Object[] getParamValues() {
		return paramValues;
	}

	/**
	 * @param paramValues the paramValues to set
	 */
	void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}

	/**
	 * @return the resultType
	 */
	public Class<?> getResultType() {
		return method == null ? null : method.getReturnType();
	}

	/**
	 * @return the paramMap
	 */
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the requestObject
	 */
	public Object getRequestObject() {
		return requestObject;
	}

	/**
	 * @param requestObject the requestObject to set
	 */
	public void setRequestObject(Object requestObject) {
		this.requestObject = requestObject;
	}

	/**
	 * @return the requestInputStream
	 */
	public InputStream getRequestInputStream() {
		return requestInputStream;
	}

	/**
	 * @param requestInputStream the requestInputStream to set
	 */
	public void setRequestInputStream(InputStream requestInputStream) {
		this.requestInputStream = requestInputStream;
	}

	/**
	 * @return the requestText
	 */
	public String getRequestText() {
		return requestText;
	}

	/**
	 * @param requestText the requestText to set
	 */
	public void setRequestText(String requestText) {
		this.requestText = requestText;
	}

	
	
	
}
