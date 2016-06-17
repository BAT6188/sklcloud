package com.skl.cloud.foundation.remote.ipc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IPCRemote {
	/**
	 * 请求uri
	 * @return
	 */
	String uri();
	
	/**
	 * 请求Root name
	 * @return
	 */
	String rootName() default "";
	
	/**
	 * 请求方式
	 * @return
	 */
	HttpMethod method() default HttpMethod.POST;
	
	/**
	 * 请求版本
	 * @return
	 */
	String version() default "1.0";
	
	
	/**
	 * xmln设置
	 * @return
	 */
	String xmln() default "urn:sky-light";
	
	/**
	 * 超时时间
	 * @return
	 */
	int timeout() default 30;
}
