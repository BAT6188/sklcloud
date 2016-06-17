package com.skl.cloud.foundation.mvc.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class PathExtensionContentNegotiatingViewResolver extends WebApplicationObjectSupport implements ViewResolver, Ordered {
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private int order = Ordered.HIGHEST_PRECEDENCE;
	// 经
	private Map<String, AbstractPathExtensionView> pathExtensionViews = new HashMap<String, AbstractPathExtensionView>();

	@Override
	public int getOrder() {
		return order;
	}
	

	/**
	 * @param pathExtensionViews the pathExtensionViews to set
	 */
	public void setPathExtensionViews(Map<String, AbstractPathExtensionView> pathExtensionViews) {
		this.pathExtensionViews.putAll(pathExtensionViews);
	}



	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View pathExtensionView = lookupPathExtensionView(viewName, locale);
		if(pathExtensionView != null) {
			return pathExtensionView;
		}
		return null;
	}
	
	private View lookupPathExtensionView(String viewName, Locale locale) {
		if(pathExtensionViews.isEmpty()) {
			return null;
		}
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
		String path = urlPathHelper.getLookupPathForRequest(request);
		String filename = WebUtils.extractFullFilenameFromUrlPath(path);
		String extension = StringUtils.getFilenameExtension(filename);
		if(!StringUtils.hasText(extension)) {
			return null;
		}
		for (String pathExtension : pathExtensionViews.keySet()) {
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(pathExtension, extension)) {
				return pathExtensionViews.get(pathExtension);
			}
		}
		return null;
	}
}
