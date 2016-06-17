package com.skl.cloud.controller.common;

import java.util.Set;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import com.skl.cloud.common.exception.BusinessError;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.exception.common.InvalidParameterException;
import com.skl.cloud.service.common.I18NResourceService;

@Validated
public class SKLController extends BaseController{
	protected Logger log = LoggerFactory.getLogger(SKLController.class);
	
	protected BusinessError handleException(Exception ex) {
		BusinessError err = new BusinessError();
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		
		if (ex instanceof BusinessException) {
			BusinessException be = (BusinessException)ex;
			if(1 == be.getErrCode() && be.getErrMsg().contains("0x")){
			    err.setCode(be.getErrCode());
			    err.setMsg(msgResourceService.getMessage(be.getErrMsg()));
			}else{
			    err.setCode(be.getErrCode());
			    err.setMsg(be.getErrMsg());
			}
		} 
		else if(ex instanceof MethodConstraintViolationException) {
			MethodConstraintViolationException me = (MethodConstraintViolationException)ex;
			Set<MethodConstraintViolation<?>> violations = me.getConstraintViolations();
			if(violations.size() > 0) {
				MethodConstraintViolation<?> violation = violations.iterator().next();

				err.setCode(new InvalidParameterException().getErrCode());
				String msg = violation.getMessage();
				err.setMsg(msg);
			}
		}
		else {
			log.error("发生异常", ex);
			err.setCode(1);
			err.setMsg(ex.getMessage());
		}
		return err;
	}
}
