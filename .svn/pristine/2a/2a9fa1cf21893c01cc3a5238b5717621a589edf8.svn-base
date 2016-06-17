package com.skl.cloud.foundation.remote;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;

import com.skl.cloud.foundation.remote.annotation.RInputStream;
import com.skl.cloud.foundation.remote.annotation.RParam;
import com.skl.cloud.foundation.remote.annotation.RRoot;
import com.skl.cloud.foundation.remote.annotation.RText;
import com.skl.cloud.foundation.remote.annotation.RVariable;
import com.skl.cloud.util.common.ReflectionUtils;

public abstract class AbstractSKLRemoteHandler implements SKLRemoteHandler {

	@Override
	public Object handle(Method method, Object[] paramValues) throws SKLRemoteException {
		RemoteContext context = newRemoteContext();
		concreteRemoteContext(context, method, paramValues);
		try {
			Object result = handle(context);
			return result;
		} catch (Exception e) {
			caughtException(context, e);
			return null;
		}
	}

	/**
	 * 构造RemoteContext
	 * @param context
	 * @param method
	 * @param paramValues
	 * @throws SKLRemoteException
	 */
	protected void concreteRemoteContext(RemoteContext context, Method method, Object[] paramValues)
			throws SKLRemoteException {
		context.setMethod(method);
		context.setParamValues(paramValues);

		MethodParameter[] params = ReflectionUtils.getMethodParameters(method);
		context.setParams(params);
		for (MethodParameter param : params) {
			Object paramValue = paramValues[param.getParameterIndex()];
			if(param.hasParameterAnnotation(RParam.class)) {
				RParam rparam = param.getParameterAnnotation(RParam.class);
				context.addParameter(rparam.value(), paramValue);
			}	
			if(param.hasParameterAnnotation(RVariable.class)) {
				RVariable rvar = param.getParameterAnnotation(RVariable.class);
				context.addVariable(rvar.value(), paramValue);				
			}
			if(param.hasParameterAnnotation(RRoot.class)) {
				context.setRequestObject(paramValue);
			}
			if(param.hasParameterAnnotation(RInputStream.class)) {
				context.setRequestInputStream((InputStream)paramValue);				
			}
			if(param.hasParameterAnnotation(RText.class)) {
				context.setRequestText(((String)paramValue));				
			}
		}
	}
	
	
	/**
	 * 执行操作
	 * 
	 * @param context
	 * @return
	 * @throws SKLRemoteException
	 */
	protected abstract Object handle(RemoteContext context) throws Exception;


	/**
	 * 新建RemoteContext
	 * @return
	 * @throws SKLRemoteException
	 */
	protected abstract <T extends RemoteContext> T newRemoteContext()
			throws SKLRemoteException;
	

	/**
	 * 异常处理
	 * @param e
	 */
	protected abstract void caughtException(RemoteContext context, Throwable e);

}
