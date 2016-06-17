package com.skl.cloud.foundation.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;

public class HttpRemoteContext extends RemoteContext {
	
	private static final String CONTENT_TYPE_NAME = "Content-Type";

	// 请求URI
	private String uri;
	// 请求URL
	private String url;
	// 请求方法
	private HttpMethod httpMethod;
	// 请求头信息
	private List<Header> headers = new ArrayList<Header>();
	// 超时时间
	private Integer timeout = 30;

	
	
	@Override
	public void merge(RemoteContext context) {
		super.merge(context);
		if(context instanceof HttpRemoteContext) {
			HttpRemoteContext httpContext = (HttpRemoteContext)context;
			if(httpContext.getUri() != null) {
				setUri(httpContext.getUri());
			}
			if(httpContext.getHttpMethod() != null) {
				setHttpMethod(httpContext.getHttpMethod());
			}
			if(httpContext.getTimeout() != null) {
				setTimeout(httpContext.getTimeout());
			}
		}
	}

	/**
	 * 增加头信息
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}
	
	/**
	 * 增加头信息
	 * @param header
	 */
	public void addHeader(Header header) {
		headers.add(header);
	}
	
	
	
	/**
	 * @return the headers
	 */
	public List<Header> getHeaders() {
		return headers;
	}

	/**
	 * 设置ContentType
	 * @param contentType
	 */
	public void setContentType(ContentType contentType) {
		addHeader(CONTENT_TYPE_NAME, contentType.toString());
	}
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = parse(uri);
	}
	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}


	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}


	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	
	
	
}
