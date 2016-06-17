package com.skl.cloud.foundation.file;

import java.net.URL;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;

public class SKLFileImpl implements SKLFile {
	
	private final S3Object s3object;
	private final AmazonS3 s3client;

	public SKLFileImpl(AmazonS3 s3client, S3Object s3object) {
		this.s3client = s3client;
		this.s3object = s3object;
	}

	@Override
	public long getContentLength() {
		return s3object.getObjectMetadata().getContentLength();
	}

	@Override
	public Date lastModified() {
		return s3object.getObjectMetadata().getLastModified();
	}

	@Override
	public String getVersion() {
		return s3object.getObjectMetadata().getVersionId();
	}

	@Override
	public String getContentMD5() {
		return s3object.getObjectMetadata().getContentMD5();
	}


	@Override
	public URL getPresignedUrl(HttpMethod method) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(s3object.getBucketName(), s3object.getKey(), method);
		return s3client.generatePresignedUrl(request);
	}

	
}
