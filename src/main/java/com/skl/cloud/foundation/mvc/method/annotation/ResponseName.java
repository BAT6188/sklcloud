package com.skl.cloud.foundation.mvc.method.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseName {
	/**
	 * 响应根节点名称
	 * 
	 * @return
	 */
	String value() default "";
}
