package com.skl.cloud.foundation.mvc;

import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public abstract class MethodArgumentParser {

	/**
	 * 对参数进行解析
	 * @param parameter
	 * @param macContainer
	 * @param nwRequest
	 * @param bindFactory
	 * @return
	 */
	abstract Object parse(MethodParameter parameter, ModelAndViewContainer macContainer,
			NativeWebRequest nwRequest, WebDataBinderFactory bindFactory)  throws Exception;
	
	/**
	 * 获得参数信息
	 * @param methodParameter
	 * @return
	 */
	protected String getParameterInfo(MethodParameter methodParameter) {
		Method method = methodParameter.getMethod();
		StringBuilder sb = new StringBuilder(method.toGenericString());
		sb.append(": the parameter[")
			.append(methodParameter.getParameterName())
			.append("] ");
		return sb.toString();
	}
}
