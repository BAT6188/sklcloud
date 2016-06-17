package com.skl.cloud.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skl.cloud.common.exception.BusinessError;
import com.skl.cloud.controller.common.SKLController;
import com.skl.cloud.controller.web.dto.ResponseStatusFO;
import com.skl.cloud.util.common.JsonUtils;

@Controller
public class FrontController extends SKLController {
	protected Logger log = LoggerFactory.getLogger("FrontController");

	@ExceptionHandler(Exception.class)
	public void exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		try {
			BusinessError err = handleException(ex);
			
			ResponseStatusFO responseStatus = new ResponseStatusFO();
			responseStatus.setCode(err.getCode());
			responseStatus.setMsg(err.getMsg());
			
			String json = JsonUtils.toJSON(responseStatus);
			response.setContentType("application/json");
			response.getWriter().write(json);
		} catch (IOException e1) {
			log.error("生成回复json出错", e1);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
