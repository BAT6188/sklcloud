package com.skl.cloud.service.common.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.common.CommonService;
import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.HeartMd5Util;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.pattern.StrPattern;

/**
 * 
 * @ClassName: DigestServiceImpl
 * @Description: 用于 Digest认证的实现类
 * @author guangbo
 * @date 2015年6月19日
 *
 */
@Service("digestService")
public class DigestServiceImpl implements DigestService {
    private static Logger log = LoggerFactory.getLogger(DigestServiceImpl.class);

    @Autowired(required = true)
    private CommonService commonService;
    
    @Autowired(required = true)
    private IPCameraService ipcService;

    @Override
    @Transactional
    public boolean degist(HttpServletRequest req, HttpServletResponse resp) {
        Long userId = 0l;
        AppUser appUser = null;
        try {
            String encodedAuth = req.getHeader("Authorization");
            if (encodedAuth == null || !encodedAuth.toUpperCase().startsWith("DIGEST")) {
                log.warn("no Authorization info:  " + req.getRequestURI());
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
                return false;
            }
            Map<String, String> contentMap = getEncodeAuth(encodedAuth);

            // 后面digest认证时使用userId
            String userIdStr = contentMap.get("userId");
            if (!StringUtil.pattern(StrPattern.userIdPattern, userIdStr)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                log.warn("not valid userId");
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
                return false;
            }
            userId = Long.parseLong(userIdStr);
            // 用来判断是开发环境，还是测试环境。本地开发环境不进行Degist认真。
            if (StringUtils.equalsIgnoreCase(System.getProperty("skl.profile"), "development")) {
                req.setAttribute("userId", userId);
                return true;
            }

            String response = contentMap.get("response");

            appUser = commonService.queryAppUserById(userId);
            // 判断用户是否存在和在线
            if (appUser == null) {
                log.warn("不存在用户[{}]", userId);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
                return false;
            }else if ((appUser.getStatus() == null) || (appUser.getStatus() != null && !appUser.getStatus())) {
                log.warn("用户[{}]不在线", userId);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
                return false;
            }
            // 判读用户是否离线？是否需要
            String random = appUser.getCloudRandom();
            String password = appUser.getPassword();
            String uri = req.getRequestURI();
            log.info("*******************uri=" + uri);
            log.info("******************random=" + random);
            String method = req.getMethod();
            log.info("*******************random=" + random);
            String HA1b = HeartMd5Util.getMD5(userId + ":" + random + ":" + password);
            String HA2b = HeartMd5Util.getMD5(method + ":" + uri);
            String HA3b = HeartMd5Util.getMD5(HA1b + ":" + random + ":" + HA2b);
            log.info("********************HA1b=" + HA1b);
            log.info("********************HA2b=" + HA2b);
            log.info("********************HA3b=" + HA3b);
            log.info("===========>HA3b:  " + HA3b + "| " + response.equalsIgnoreCase(HA3b), this.getClass()
                    .getName());
            if (HA3b.equalsIgnoreCase(response)) {
                log.info("userId|" + userId + "|Digest is success", this.getClass().getName());
                req.setAttribute("userId", userId);
                return true;
            } else {
                log.info("userId=" + userId + "|Digest is failure|HA3b=" + HA3b + "|response=" + response, this
                        .getClass().getName());
                log.warn("Digest not match: Authorication[{}], realUri: {}, Method: {}, Random: {}, HA3b: {}",
                        encodedAuth, uri, method, random, HA3b);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
                return false;
            }

        } catch (Exception e) {
            log.error("DIGEST发生错误", e);
            try {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Digest is failure");
                resp.getWriter().flush();
            } catch (IOException e1) {
                log.error("response exception", e1);
            }
            return false;
        }

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

    /*
     * (non-Javadoc)
     * 
     * @see com.skl.cloud.service.DigestService#getIPCDigest()
     */
    @Override
    @Transactional(readOnly = true)
    public String getIPCDigest(String sn) {
        IPCamera ipc = ipcService.getIPCameraBySN(sn);
        String auth = "";
        String ipcModel = ipc.getModel();
        if (Constants.ipcModelType.isHPC03IPC(ipcModel)) {
            // HPC03
            auth = "Admin:8f3b1cfcd13b7151";
        } else if (Constants.ipcModelType.isFamilerIPC(ipcModel)) {
            // Family
            auth = "skyLight_root_user:develop2015";
        }
        // 增加IPC Digest
        String encoded = Base64.encodeBase64String(auth.getBytes(Charset.forName("UTF-8")));
        String authorization = "Basic " + encoded;
        return authorization;
    }

}
