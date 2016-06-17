package com.skl.cloud.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
  * @ClassName: DigestService
  * @Description: 用于Digest认证的接口
  * @author guangbo
  * @date 2015年6月19日
  *
 */

public interface DigestService {

	
	public boolean  degist(HttpServletRequest req, HttpServletResponse resp);
	
	
	public String getIPCDigest(String sn);
}
