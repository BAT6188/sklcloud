package com.skl.cloud.foundation.remote;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.foundation.remote.ipc.IPCRemote;
import com.skl.cloud.foundation.remote.ipc.IPCRemoteHandler;
import com.skl.cloud.foundation.remote.stream.StreamRemote;
import com.skl.cloud.foundation.remote.stream.StreamRemoteHandler;

public class AutowiredSKLRemoteAnnotationProcessor implements BeanPostProcessor {
 
	@Override
	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
		Class<?> clazz = getRealClass(bean);
		if (!clazz.isAnnotationPresent(Service.class)) {
			return bean;
		}
		// 处理Service里的Remote接口
		if (clazz.isAnnotationPresent(Service.class)) {
			ReflectionUtils.doWithFields(clazz, new FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					Class<?> fieldType = field.getType();
					if (SKLRemote.class.isAssignableFrom(fieldType)) {
						ReflectionUtils.makeAccessible(field);
						Object fieldBean = ReflectionUtils.getField(field, bean);
						fieldBean = fieldBean == null ? getProxyByClass(fieldType) : getProxyByObject(fieldBean);
						ReflectionUtils.setField(field, bean, fieldBean);
					}
				}
			}, new FieldFilter() {
				@Override
				public boolean matches(Field field) {
					return field.isAnnotationPresent(Remote.class)
							&& SKLRemote.class.isAssignableFrom( field.getType());
				}
			});
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
		return bean;
	}

	private Class<?> getRealClass(Object bean) {
		return AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
	}

	private Object getProxyByClass(Class<?> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(new SKLRemoteMethodInterceptor(null));
		return enhancer.create();
	}

	private Object getProxyByObject(Object obj) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(obj.getClass());
		enhancer.setCallback(new SKLRemoteMethodInterceptor(obj));
		return enhancer.create();
	}

	private class SKLRemoteMethodInterceptor implements MethodInterceptor {
		private Object targetObject;

		public SKLRemoteMethodInterceptor(Object targetObject) {
			this.targetObject = targetObject;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (method.isAnnotationPresent(IPCRemote.class)) {
				IPCRemoteHandler ipcRemoteHandler = new IPCRemoteHandler();
				return ipcRemoteHandler.handle(method, args);
			}else if (method.isAnnotationPresent(StreamRemote.class)) {
                StreamRemoteHandler streamRemoteHandler = new StreamRemoteHandler();
                return streamRemoteHandler.handle(method, args);
            } else {
				return targetObject == null ? proxy.invokeSuper(obj, args) : proxy.invokeSuper(targetObject, args);
			}
		}
	}

}
