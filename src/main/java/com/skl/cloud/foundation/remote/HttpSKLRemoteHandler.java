package com.skl.cloud.foundation.remote;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.foundation.remote.exception.RequestErrorRemoteException;

public abstract class HttpSKLRemoteHandler extends AbstractSKLRemoteHandler {
	private static Logger logger = LoggerFactory.getLogger(HttpSKLRemoteHandler.class);

	@Override
	protected Object handle(RemoteContext context) throws Exception {
		HttpRemoteContext httpContext = (HttpRemoteContext) context;
		// 设置超时
		RequestConfig requestConfig = null;
		if (httpContext.getTimeout() > 0) {
			int timeout = httpContext.getTimeout() * 1000;
			requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).build();
		}
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		CloseableHttpResponse response = null;
		try {
			HttpRequestBase request = concreteRequest(httpContext);
			logger.debug("-----------remote request method: {}, request url: {}", httpContext.getHttpMethod(),
					httpContext.getUri());
			response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.debug("-----------remote response statusCode: {}", statusCode);
			if (statusCode != HttpStatus.OK.value()) {
				throw new RequestErrorRemoteException(
						"request remote address [" + httpContext.getUri() + "] error, status code: " + statusCode);
			}
			return resolveResponse(response, httpContext);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	protected void caughtException(RemoteContext context, Throwable e) {
		if(e instanceof BusinessException) {
			throw (BusinessException)e;
		}
		if(e instanceof ClientProtocolException) {
			logger.error("不能连接到远程地址[" + ((HttpRemoteContext)context).getUrl() + "]", e);
			throw new SKLRemoteException("can't connect to target address");
		}
		logger.error("处理远程地址的返回结果发生错误", e);
		throw new SKLRemoteException("get remote content error");
	}

	/**
	 * 构造Request
	 * 
	 * @param context
	 * @return
	 * @throws SKLRemoteException
	 */
	protected HttpRequestBase concreteRequest(HttpRemoteContext context) throws Exception {
		HttpEntityEnclosingRequestBase httpRequest = null;
		HttpMethod httpMethod = context.getHttpMethod();
		String requestUrl = context.getUrl();
		switch (httpMethod) {
		case GET:
			return new HttpGet(requestUrl);
		case DELETE:
			return new HttpDelete(requestUrl);
		case POST:
			httpRequest = new HttpPost(requestUrl);
			break;
		case PUT:
			httpRequest = new HttpPut(requestUrl);
			break;
		default:
			throw new UnsupportedOperationException("http method " + httpMethod + " is not supported");
		}
		HttpEntity httpEntity = concreteRequestEntity(context);
		if (httpEntity != null) {
			httpRequest.setEntity(httpEntity);
		}
		return httpRequest;
	
	}

	/**
	 * 解析Response
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract Object resolveResponse(CloseableHttpResponse response, HttpRemoteContext context)
			throws Exception;

	/**
	 * 构造请求Entity
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected abstract HttpEntity concreteRequestEntity(HttpRemoteContext context) throws Exception;
	
}
