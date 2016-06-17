package com.skl.cloud.foundation.remote;

import java.lang.reflect.Method;

public class RemoteResult {
	
	// 请求方法
	private Method method;
	// 请求参数
	private Object[] paramValues;
	
	
	public RemoteResult(RemoteContext context) {
		this.method = context.getMethod();
		this.paramValues = context.getParamValues();
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
	public void setMethod(Method method) {
		this.method = method;
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
	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}
	
	
	
}
