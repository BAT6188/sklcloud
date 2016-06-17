package com.skl.cloud.util.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;


public class HttpClientUtil
{
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
	
	private static final Logger log = Logger.getLogger(HttpClientUtil.class);
	//httpPostAsStream改为httpPostBackgroundSubsystem
	public static InputStream httpPostBackgroundSubsystem(String para,String url) throws Exception  
	{ 
		HttpClient client=new HttpClient();
		PostMethod postMethod = new PostMethod(url); 
		RequestEntity entity = new StringRequestEntity(para, "text/xml","utf-8");  
	    postMethod.setRequestEntity(entity);  
	    
	    int statusCode = client.executeMethod(postMethod);  
        
	    logger.info("********请求URL:"+url+"********");
	    logger.info("********返回的状态码:"+statusCode+"********");
	         
        if (statusCode != HttpStatus.SC_OK) 
        {  
        	log.error(postMethod.getStatusLine());
        }  
            
        byte[] responseBody = postMethod.getResponseBody();  
        InputStream inputStream = new ByteArrayInputStream(responseBody); 
		
		return inputStream;
	}
	
	public static InputStream httpPostAsStream(String url) throws Exception  
	{
		HttpClient client=new HttpClient();
		PostMethod postMethod = new PostMethod(url); 
		RequestEntity entity = new StringRequestEntity("", "application/xml","utf-8");  
	    postMethod.setRequestEntity(entity);  
	    
	    int statusCode = client.executeMethod(postMethod);  
          
        if (statusCode != HttpStatus.SC_OK) 
        {  
            System.out.println("Method failed: " + postMethod.getStatusLine());  
        }  
           
        byte[] responseBody = postMethod.getResponseBody();  
        InputStream inputStream = new ByteArrayInputStream(responseBody); 
         
		return inputStream;
	}
	
	public static InputStream httpGetAsStream(String url) throws Exception  
	{
		HttpClient client=new HttpClient();
		GetMethod postMethod = new GetMethod(url); 
		
	    int statusCode = client.executeMethod(postMethod);  
          
        if (statusCode != HttpStatus.SC_OK) 
        {  
            System.out.println("Method failed: " + postMethod.getStatusLine());  
        }  
           
        byte[] responseBody = postMethod.getResponseBody();  
        InputStream inputStream = new ByteArrayInputStream(responseBody); 
         
		return inputStream;
	}
	
	
	
	
	
	
}
