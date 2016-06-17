package com.skl.cloud.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skl.cloud.util.common.LoggerUtil;

/**
 * HttpClient GET POST PUT 请求
 * 
 * @author huang
 * @date 2013-4-10
 */
public class HttpRequest {

	protected static Log logger = LogFactory.getLog(HttpRequest.class);
	private static HttpRequest httpRequst = null;

	private HttpRequest() {
	}

	public static HttpRequest getInstance() {
		if (httpRequst == null) {
			synchronized (HttpRequest.class) {
				if (httpRequst == null) {
					httpRequst = new HttpRequest();
				}
			}
		}
		return httpRequst;
	}

	/**
	 * HttpClient GET请求
	 * 
	 * @param url
	 * @param encode
	 * @return
	 */
	public String doGet(String url, String encode) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = htpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
				return resStr;
			}
			byte[] responseBody = getMethod.getResponseBody();
			resStr = new String(responseBody, encode);
		} catch (HttpException e) {
			logger.error("Please check your provided http address!", e); // 发生致命的异常，可能是协议不对或者返回的内容有问题
		} catch (Exception e) {
			logger.error("Network anomaly", e); // 发生网络异常
		} finally {
			getMethod.releaseConnection(); // 释放连接
		}
		return resStr;
	}

	/**
	 * 设置有操作连接的
	 * 
	 * @param url
	 * @param encode
	 * @param timeout
	 * @return
	 */
	public String doGet(String url, String encode, int timeout) throws SocketTimeoutException {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try {
			htpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			htpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			int statusCode = htpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
				return resStr;
			}
			byte[] responseBody = getMethod.getResponseBody();
			resStr = new String(responseBody, encode);
		} catch (SocketTimeoutException ex) {
			logger.error("SocketTimeoutException :", ex); // 发生致命的异常，可能是协议不对或者返回的内容有问题
			throw ex;
		} catch (HttpException e) {
			logger.error("Please check your provided http address!", e); // 发生致命的异常，可能是协议不对或者返回的内容有问题
		} catch (Exception e) {
			logger.error("Network anomaly", e); // 发生网络异常
		} finally {
			getMethod.releaseConnection(); // 释放连接
		}
		return resStr;
	}

	/**
	 * HttpClient POST请求
	 * 
	 * @author huang
	 * @date 2013-4-9
	 * @param s_user
	 * @return resStr 请求返回的JSON数据
	 */
	@SuppressWarnings("deprecation")
	public String doPost(String url, String endCode, String xmlContent) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("Admin", "8f3b1cfcd13b7151");
		htpClient.getState().setCredentials(AuthScope.ANY, creds);
		PostMethod postMethod = new PostMethod(url);
		postMethod.addRequestHeader("Content-Type", "application/xml");
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, endCode);
		postMethod.setRequestBody(xmlContent);
		try {
			htpClient.executeMethod(postMethod);

			byte[] responseBody = postMethod.getResponseBody();
			resStr = new String(responseBody, endCode);
		} catch (Exception e) {
			logger.error("与ipc通信失败!", e);
			return null;
		} finally {
			postMethod.releaseConnection();
		}
		return resStr;
	}

	/**
	 * HttpClient POST请求
	 * 
	 * @author huang
	 * @date 2013-4-9
	 * @param s_user
	 * @return resStr 请求返回的JSON数据
	 */
	@SuppressWarnings("deprecation")
	public String doPost(String url, String endCode, String xmlContent, int timeout) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.addRequestHeader("Content-Type", "application/xml");
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, endCode);
		postMethod.setRequestBody(xmlContent);
		try {
			htpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			htpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			htpClient.executeMethod(postMethod);

			byte[] responseBody = postMethod.getResponseBody();
			resStr = new String(responseBody, endCode);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			postMethod.releaseConnection();
		}
		return resStr;
	}

	/**
	 * HttpClient PUT请求
	 * shaoxiong 2015/7/25 修改添加用户名密码验证，用于修改ipc时间,进行Zoom in/out设置
	 * @date 2013-4-10
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String doPut(String url, String encode, String xmlContent) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("administrator", "administrator");
		htpClient.getState().setCredentials(AuthScope.ANY, creds);
		PutMethod putMethod = new PutMethod(url);
		putMethod.addRequestHeader("Content-Type", "application/xml");
		putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		putMethod.setRequestBody(xmlContent);
		// putMethod.set
		try {
			int statusCode = htpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + putMethod.getStatusLine());
				LoggerUtil.error("put request failed", this.getClass().getName());
				return null;
			}
			byte[] responseBody = putMethod.getResponseBody();
			resStr = new String(responseBody, encode);
		} catch (Exception e) {
			LoggerUtil.error("put request erro", this.getClass().getName());
			return null;
		} finally {
			putMethod.releaseConnection();
		}
		return resStr;
	}

	/**
	 * HttpClient PUT请求
	 * 
	 * @date 2013-4-10
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String doPut(String url, String encode) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		PutMethod putMethod = new PutMethod(url);
		putMethod.addRequestHeader("Content-Type", "application/xml");
		putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		try {
			int statusCode = htpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + putMethod.getStatusLine());
				return null;
			}
			byte[] responseBody = putMethod.getResponseBody();
			resStr = new String(responseBody, encode);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			putMethod.releaseConnection();
		}
		return resStr;
	}

	/**
	 * HttpClient PUT请求
	 * 
	 * @date 2013-4-10
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String doPut(String url, String encode, String xmlContent, int timeout) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		PutMethod putMethod = new PutMethod(url);
		putMethod.addRequestHeader("Content-Type", "application/xml");
		putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		putMethod.setRequestBody(xmlContent);
		try {
			htpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			htpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			int statusCode = htpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + putMethod.getStatusLine());
				return null;
			}
			byte[] responseBody = putMethod.getResponseBody();
			resStr = new String(responseBody, encode);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			putMethod.releaseConnection();
		}
		return resStr;
	}

	/**
	  * 
	  * @Title: doGet
	  * @Description: HttpClient返回字节类型的GET请求
	  * @param url
	  * @param timeout
	  * @return HashMap（响应体和响应类型）
	  * @throws SocketTimeoutException
	  * @author leiqiang
	  * @date 2015年7月6日
	 */
	public HashMap doGet(String url, int timeout) throws SocketTimeoutException {

		HashMap map = new HashMap();

		HttpClient htpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try {
			htpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			htpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			int statusCode = htpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
				return map;
			}

			byte[] bt = getMethod.getResponseBody();

			String arryCt[] = getMethod.getResponseHeader("Content-Type").toString().split(":");
			String arryCl[] = getMethod.getResponseHeader("Content-Length").toString().split(":");

			map.put("ResponseBody", bt); // 响应内容
			map.put("Content-Type", arryCt[1].trim()); // 响应类型
			map.put("Content-Length", arryCl[1].trim()); // 响应大小

		} catch (SocketTimeoutException ex) {
			logger.error("SocketTimeoutException :", ex); // 发生致命的异常，可能是协议不对或者返回的内容有问题
			throw ex;
		} catch (HttpException e) {
			logger.error("Please check your provided http address!", e); // 发生致命的异常，可能是协议不对或者返回的内容有问题
		} catch (Exception e) {
			logger.error("Network anomaly", e); // 发生网络异常
		} finally {
			getMethod.releaseConnection(); // 释放连接
		}

		return map;
	}

	public static String obtainXml(String fileName) throws IOException {
		String requestData = "";
		InputStream is = HttpRequest.class.getResourceAsStream("./" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tmp = null;
		while ((tmp = br.readLine()) != null) {
			requestData += tmp;
		}
		br.close();
		return requestData;
	}

}
