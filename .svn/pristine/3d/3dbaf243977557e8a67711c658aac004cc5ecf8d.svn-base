package com.skl.cloud.foundation.sns;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.util.config.SystemConfig;


public class SNSFactory {
	private static SNS defSNS = null;

	/**
	 * 获得缺省的S3
	 * 
	 * @return
	 */
	public static SNS getDefault() {
		if (defSNS == null) {
			defSNS = get();
		}
		return defSNS;
	}

	private static SNS get() {
		if (defSNS == null) {
			synchronized (S3Factory.class) {
				if (defSNS == null) {
					initSNS();
				}
			}
		}
		return defSNS;
	}

	private static void initSNS() {
		String credential = SystemConfig.getProperty("aws.sns.credential.key");
		String password = SystemConfig.getProperty("aws.sns.credential.secret");
		String endpoint = SystemConfig.getProperty("aws.sns.endpoint","https://sns.ap-southeast-1.amazonaws.com");
		AmazonSNS sns = new AmazonSNSClient(new BasicAWSCredentials(credential,	password));
		sns.setEndpoint(endpoint);
		defSNS = new SNS(sns);
	}

}
