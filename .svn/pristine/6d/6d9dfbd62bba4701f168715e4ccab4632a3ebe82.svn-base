package com.skl.cloud.foundation.mvc.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.skl.cloud.foundation.mvc.model.ModelKeys;
import com.skl.cloud.foundation.mvc.model.SKLModel;

public abstract class AbstractPathExtensionView extends AbstractView {

	/**
	 * 获得返回的内容类型
	 */
	public abstract String getContentType();

	/**
	 * 获得model信息
	 * 
	 * @param model
	 * @return
	 */
	protected SKLModel getSKLModel(Map<String, Object> model) {
		return (SKLModel) model.get(ModelKeys.MODEL_INFO_KEY);
	}

	/**
	 * 写入流至response
	 * 
	 * @param input
	 * @param response
	 * @throws IOException
	 */
	protected void writeToResponse(HttpServletResponse response, InputStream input) throws IOException {
		IOUtils.copy(input, response.getOutputStream());
	}

	/**
	 * 写入流至response
	 * 
	 * @param input
	 * @param response
	 * @throws IOException
	 */
	protected void writeToResponse(HttpServletResponse response, String content) throws IOException {
		IOUtils.copy(new StringReader(content), response.getOutputStream());
	}

}
