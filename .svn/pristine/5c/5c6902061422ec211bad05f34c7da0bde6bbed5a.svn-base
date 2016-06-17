package com.skl.cloud.foundation.mvc.method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.skl.cloud.foundation.mvc.model.ModelKeys;
import com.skl.cloud.foundation.mvc.model.SKLModel;

public class SKLModelMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(SKLModel.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		ModelMap modelMap = mavContainer.getModel();
		SKLModel sklModel = new SKLModel(modelMap);
		sklModel.setMethod(parameter.getMethod());
		request.setAttribute(ModelKeys.MODEL_INFO_KEY, sklModel);
		return sklModel;
	}
	
}
