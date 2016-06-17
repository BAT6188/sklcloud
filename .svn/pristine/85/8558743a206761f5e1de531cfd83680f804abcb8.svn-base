package com.skl.cloud.foundation.remote;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HttpRemoteResult extends RemoteResult {

	protected List<Header> headers = new ArrayList<Header>();
	protected byte[] data;
	
	public HttpRemoteResult(HttpRemoteContext context) {
		super(context);
	}

	/**
	 * 增加Header
	 * @param header
	 */
	public void addHeader(Header header) {
		headers.add(header);
	}
	
	/**
	 * 增加Header
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}
	
	/**
	 * 获得头部信息
	 * 
	 * @return
	 */
	public List<Header> getHeaders() {
		return headers;
	}

	/**
	 * 获得返回的内容
	 * 
	 * @return
	 */
	public String getResultText() {
		return new String(data, Charset.forName("UTF-8"));
	}

	/**
	 * 获得结果的字节数组
	 * 
	 * @return
	 */
	public byte[] getResultBytes() {
		return data;
	}

	/**
	 * 获得请求返回的流
	 * 
	 * @return
	 */
	public InputStream getResultInputStream() {
		return new ByteArrayInputStream(data);
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
	
	
}
