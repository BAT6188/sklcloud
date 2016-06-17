package com.skl.cloud.foundation.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.skl.cloud.util.config.SystemConfig;

public class S3 {

	private AmazonS3 s3client;
	private String bucketName;
	private Region region;
	
	private static Logger log = Logger.getLogger(S3.class);
	
	S3(AmazonS3 s3client, Region region, String bucketName) {
		this.s3client = s3client;
		this.bucketName = bucketName;
		this.region = region;
	}


	/**
	 * 保存文件
	 * @param targetPath
	 * @param orgFile
	 * @throws FileNotFoundException 
	 */
	public void saveFile(String path, File orgFile) {
		if(StringUtils.isBlank(path) || orgFile == null) {
			throw new IllegalArgumentException("保存文件参数为空");
		}
		s3client.putObject(new PutObjectRequest(bucketName, path, orgFile));
	}
	
	
	/**
	 * 保存文件
	 * @param targetPath
	 * @param inputStream
	 */
	public void saveFile(String path, InputStream inputStream) {
		if(StringUtils.isBlank(path) || inputStream == null) {
			throw new IllegalArgumentException("保存文件参数为空");
		}
		s3client.putObject(new PutObjectRequest(bucketName, path, inputStream, null));
	}
	
	/**
	 * 保存文件
	 * @param targetPath
	 * @param inputStream
	 */
	public void saveFile(String path, byte[] data) {
		if(StringUtils.isBlank(path) || data == null) {
			throw new IllegalArgumentException("保存文件参数为空");
		}
		s3client.putObject(new PutObjectRequest(bucketName, path, new ByteArrayInputStream(data), null));
	}
	
	
	/**
	 * 删除文件
	 * @param path
	 */
	public void deleteFile(String path) {
		s3client.deleteObject(bucketName, path);
	}
	
	
	/**
	 * deleteObject(删除多个文件根据键)
	 * 
	 * @Title: deleteObject
	 * @Description: 刪除存储的对象
	 * @param @param List<KeyVersion> keys
	 * @param @throws AmazonClientException
	 * @param @throws AmazonServiceException (参数说明)
	 * @return void (返回值说明)
	 * @throws (异常说明)
	 * @author weibin
	 * @date 2015年12月17日
	 */
	public void deleteObject(List<KeyVersion> keys) throws AmazonClientException,
			AmazonServiceException {
		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName);
		multiObjectDeleteRequest.setKeys(keys);
		s3client.deleteObjects(multiObjectDeleteRequest);
//		s3client.deleteObject(bucketName, key);
	}
	
	
	
	/**
	 * 获得文件
	 * @param path
	 * @return
	 */
	public SKLFile getFile(String path) {
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, path));
		try {
			s3object.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SKLFileImpl(s3client, s3object);
	}

	/**
	 * url预签名
	 * @param s3Key
	 * @param method
	 * @return
	 */
	public URL getPresignedUrl(String s3Key, HttpMethod method) {
		//设置签名url过期时间
		final long sec = Long.valueOf(SystemConfig.getProperty("aws.s3.expire.millis","3600000"));
		Date expireDate = new Date(System.currentTimeMillis() + sec);
		return this.getPresignedUrl(s3Key, method, expireDate);
	}
	
	/**
	 * url预签名
	 * @param s3Key
	 * @param method
	 * @return
	 */
	public URL getPresignedUrl(String s3Key, HttpMethod method, Date expireDate) {
		try {
			s3Key = URLDecoder.decode(s3Key,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
		log.info("s3Key is "+s3Key);
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, s3Key, method);
		request.setExpiration(expireDate);
		return s3client.generatePresignedUrl(request);
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}


	/**
	 * @return the bucketName
	 */
	public String getBucketName() {
		return bucketName;
	}
	
	
	
	
}
