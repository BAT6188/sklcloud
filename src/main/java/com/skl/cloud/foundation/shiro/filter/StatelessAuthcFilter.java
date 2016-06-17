package com.skl.cloud.foundation.shiro.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skl.cloud.foundation.shiro.realm.StatelessToken;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.pattern.StrPattern;

/**
 * digest 认证
 * @author weibin
 *
 */
public class StatelessAuthcFilter extends AccessControlFilter {
	
	private static Logger log = LoggerFactory.getLogger(StatelessAuthcFilter.class);
	
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	HttpServletRequest req = (HttpServletRequest)request;
    	Long userId = 0L;
//    	HttpServletResponse resp = 
    	String encodedAuth = req.getHeader("Authorization");
        if (encodedAuth == null || !encodedAuth.toUpperCase().startsWith("DIGEST")) {
            log.warn("no Authorization info:  " + req.getRequestURI());
            onLoginFail(response);
            return false;
        }
        Map<String, String> contentMap = getEncodeAuth(encodedAuth);
        // 后面digest认证时使用userId
        String userIdStr = contentMap.get("userId");
        if (!StringUtil.pattern(StrPattern.userIdPattern, userIdStr)) {
        	onLoginFail(response);
            return false;
        }
        userId = Long.parseLong(userIdStr);
        String digestResponse = contentMap.get("response");
        //4、生成无状态Token
        StatelessToken token = new StatelessToken(userId, digestResponse, req);
        try {
            //5、委托给Realm进行登录
            getSubject(request, response).login(token);
            req.setAttribute("userId", userId);
        } catch (Exception e) {
            log.error("digest认证失败 userId:{},response:{},错误信息:{},",userId,digestResponse,e.getMessage(),e);
            onLoginFail(response); //6、登录失败
            return false;
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("DIGEST ERROR");
    }
    
    private Map<String, String> getEncodeAuth(String encodedAuth) {
        Map<String, String> contentMap = new HashMap<String, String>();
        encodedAuth = encodedAuth.trim();
        // Digest字符
        encodedAuth = encodedAuth.substring("Digest".length()).trim();
        log.info("=======>> encodedAuth_1 |" + encodedAuth);
        // 以逗号,分割
        String[] authArr1 = encodedAuth.split(",");
        for (String item1 : authArr1) {
            // 以等号=分割
            String[] authArr2 = item1.split("=");
            if (authArr2.length >= 2) {
                String key = authArr2[0].trim();
                String value = authArr2[1].trim();
                if (value.length() >= 2) {
                    if (value.startsWith("\"")) {
                        value = value.substring(1);
                    }
                    if (value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 1);
                    }
                }
                contentMap.put(key, value);
            }
        }
        log.info("contentMap|" + contentMap);
        return contentMap;
    }
}
