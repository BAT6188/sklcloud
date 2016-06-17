package com.skl.cloud.controller.app;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skl.cloud.common.exception.BusinessError;
import com.skl.cloud.common.xml.JAXBGenerator;
import com.skl.cloud.controller.app.dto.ResponseStatusAO;
import com.skl.cloud.controller.common.SKLController;
import com.skl.cloud.foundation.mvc.model.ModelKeys;
import com.skl.cloud.foundation.mvc.model.SKLModel;

@Controller
public class AppController extends SKLController {
	protected Logger log = LoggerFactory.getLogger(AppController.class);


	@ExceptionHandler(Exception.class)
	public void exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		if(ex instanceof UnauthorizedException){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				response.getWriter().write("DIGEST ERROR");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		SKLModel sklModel = (SKLModel)request.getAttribute(ModelKeys.MODEL_INFO_KEY);
		if(sklModel == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		BusinessError err = super.handleException(ex);
		try {
			JAXBGenerator generator = new JAXBGenerator(sklModel.getResponseName());
			ResponseStatusAO rs = new ResponseStatusAO(err.getCode(), err.getMsg());
			generator.addParam("ResponseStatus", rs);
			generator.writeTo(response.getOutputStream());
		} catch (Exception e) {
			log.error("生成回复xml出错", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}

}
