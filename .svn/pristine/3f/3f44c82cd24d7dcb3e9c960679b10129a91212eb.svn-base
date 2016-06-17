package com.skl.cloud.foundation.file;

import java.net.URL;
import java.util.Date;

import com.amazonaws.HttpMethod;

public interface SKLFile {

	/**
	 * 获得内容长度
	 * @return
	 */
	long getContentLength();
	
	/**
	 * 上次修改该数据元的日期
	 * @return
	 */
	Date lastModified();
	
	/**
	 * 对象版本
	 * @return
	 */
	String getVersion();
	
	/**
	 * 对象的 base64 编码的 128 位 MD5 摘要
	 * @return
	 */
	String getContentMD5();
	
	/**
	 * 获取预签名url
	 * @param method
	 * @return
	 */
	URL getPresignedUrl(HttpMethod method);
}
