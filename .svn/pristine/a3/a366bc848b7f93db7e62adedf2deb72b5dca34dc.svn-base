package com.skl.cloud.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.util.common.LoggerUtil;



public class SpringMVCInterceptor implements HandlerInterceptor {
    @Autowired(required = true)
    private DigestService digestService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!StringUtils.equalsIgnoreCase(System.getProperty("skl.profile"), "development")) {
            return digestService.degist(request, response);
        } else {
            return true;
        }
        /*
         * if (null != map) { return true; }
         */
        // this.recordLogs(request, response);//记录用户请求的日志

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        LoggerUtil.info("拦截后。。。。。。。。。。。", this.getClass().getName());

    }

}
