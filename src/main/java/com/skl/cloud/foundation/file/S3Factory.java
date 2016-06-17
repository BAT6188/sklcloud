package com.skl.cloud.foundation.file;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.skl.cloud.util.config.SystemConfig;


public class S3Factory {

	private static S3 defS3 = null;
	private static Map<String, S3> s3Map = new HashMap<String, S3>();
	

	
	/**
	 * 获得缺省的S3
	 * @return
	 */
	public static S3 getDefault() {
		if(defS3 == null) {
            String bucketName = SystemConfig.getProperty("aws.s3.bucket", "skl-developer");
            String strRegion = SystemConfig.getProperty("aws.s3.region", "CN_NORTH_1");
            Region region = Region.getRegion(Regions.valueOf(strRegion));
			defS3 = get(region, bucketName);
		}
		return defS3;
	}
	
	
	private static S3 get(Region region, String bucketName) {
		S3 s3 = s3Map.get(getKey(region, bucketName));
		if(s3 == null) {
			synchronized (S3Factory.class) {
				if(s3 == null) {
					initS3(region, bucketName);
				}
			}
		}
		s3 = s3Map.get(getKey(region, bucketName));
		return s3;
	}
	
	private static void initS3(Region region, String bucketName) {
        String credential = SystemConfig.getProperty("aws.s3.credential.key");
        String password = SystemConfig.getProperty("aws.s3.credential.secret");
        
        ClientConfiguration s3ClientConfiguration = new ClientConfiguration();
        s3ClientConfiguration.setMaxConnections(1000);
		s3ClientConfiguration.setResponseMetadataCacheSize(1000);
        AmazonS3  s3client = new AmazonS3Client(new BasicAWSCredentials(credential, password), s3ClientConfiguration);
        
		s3client.setRegion(region);
		S3 s3 = new S3(s3client, region, bucketName);
		s3Map.put(getKey(region, bucketName), s3);
	}
	
	private static String getKey(Region region, String bucketName) {
		return region.getName() + "#" + bucketName;
	}
	
}
