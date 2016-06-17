package com.skl.cloud.foundation.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

 /***
  * token
  * @author weibin
  *
  */
public class StatelessToken implements AuthenticationToken {

    private Long userId;
    private String digstResponse;
    private HttpServletRequest request;
    
    
      
	public StatelessToken() {
		super();
	}
	public StatelessToken(Long userId, String digstResponse,
			HttpServletRequest request) {
		super();
		this.userId = userId;
		this.digstResponse = digstResponse;
		this.request = request;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDigstResponse() {
		return digstResponse;
	}
	public void setDigstResponse(String digstResponse) {
		this.digstResponse = digstResponse;
	}
	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return userId;
	}
	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return digstResponse;
	}   
}
