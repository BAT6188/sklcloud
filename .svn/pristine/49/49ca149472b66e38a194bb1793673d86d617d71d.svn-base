package com.skl.cloud.foundation.mvc.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.foundation.mvc.JsonResult;

public class FrontPathExtensionView extends AbstractPathExtensionView {
	private static Logger logger = LoggerFactory.getLogger(FrontPathExtensionView.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private JsonEncoding encoding = JsonEncoding.UTF8;
	
	@Override
	public String getContentType() {
		return "application/json";
	}


	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		setResponseContentType(request, response);
		response.setCharacterEncoding(this.encoding.getJavaName());
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		OutputStream stream = response.getOutputStream();
		Object value = filterModel(model);
		writeContent(stream, value);
	}

	/**
	 * Filter out undesired attributes from the given model.
	 * The return value can be either another {@link Map} or a single value object.
	 * <p>The default implementation removes {@link BindingResult} instances and entries
	 * not included in the {@link #setRenderedAttributes renderedAttributes} property.
	 * @param model the model, as passed on to {@link #renderMergedOutputModel}
	 * @return the value to be rendered
	 */
	protected Object filterModel(Map<String, Object> model) {
		JsonResult jsonResult = new JsonResult();
		if(model.containsKey("exception")) {
			Exception e = (Exception)model.get("exception");
			jsonResult.setCode("1");
			if(e instanceof BusinessException) {
				jsonResult.setCode(((BusinessException)e).getErrCode() + "");
				jsonResult.setMsg(e.getMessage());
			}
			else {
				jsonResult.setCode("1");
				jsonResult.setMsg("error");				
			}
			return jsonResult;
		}
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if(entry.getValue() instanceof BindingResult) {
				BindingResult result = (BindingResult)entry.getValue();
				jsonResult.setCode("2");
				jsonResult.setMsg(result.getFieldErrorCount() > 0 ? result.getFieldErrors().iterator().next().getDefaultMessage() : "");
				return jsonResult;
			}
			jsonResult.addAttribute(entry.getKey(), entry.getValue());
		}
		return jsonResult;
	}

	/**
	 * Write the actual JSON content to the stream.
	 * @param stream the output stream to use
	 * @param value the value to be rendered, as returned from {@link #filterModel}
	 * @param jsonPrefix the prefix for this view's JSON output
	 * (as indicated through {@link #setJsonPrefix}/{@link #setPrefixJson})
	 * @throws IOException if writing failed
	 */
	protected void writeContent(OutputStream stream, Object value) throws IOException {
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(stream, this.encoding);
		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}
		this.objectMapper.writeValue(generator, value);
		if(logger.isDebugEnabled()) {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			logger.debug("-------- response -----------\n" + objectMapper.writeValueAsString(value));
		}
	}

}
