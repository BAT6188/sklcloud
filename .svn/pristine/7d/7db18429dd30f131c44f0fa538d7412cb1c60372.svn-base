package com.skl.cloud.controller.app;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.AppDeviceRedistService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.XmlUtil;

@Controller
@RequestMapping("/skl-cloud/app/Security/AAA/users")
public class AppDeviceRedistController extends BaseController
{
    private static final Logger log = Logger.getLogger(AppDeviceRedistController.class);
    
    @Autowired(required = true)
	private AppDeviceRedistService appDeviceRedistService;
    
    @Autowired
    private AppUserService appUserService;

	/**
	 * APP用户向云端发送IPC设备注册请求
	 * @Title: register
	 * @param inputstream
	 * @param
	 * @param ManagerException
	 * @return ResponseEntity<String>
	 * @author yangbin
	 * @date 2015年11月17日
	 */
	@RequestMapping("/deviceRegist")
	public ResponseEntity<String> register(InputStream inputstream) throws ManagerException
	{
		return appDeviceRedistService.register(inputstream);
	}

	/**
     * APP用户通过SN向云端发送IPC设备注册请求
     * @Title: register
     * @param inputstream
     * @param
     * @param ManagerException
     * @return ResponseEntity<String>
     * @author yangbin
     * @date 2015年11月17日
     */
	@RequestMapping("/deviceAuthorizeBySN")
	public ResponseEntity<String> registerBySN(InputStream inputstream) throws ManagerException
	{
		return appDeviceRedistService.registerBySN(inputstream);
	}
	
	/**
     * 上报事件推送的相关内容
     * @param inputstream
     * @return
     * @throws ManagerException
     */
    @RequestMapping("/token")
    public ResponseEntity<String> reportToken(InputStream inputstream) throws ManagerException
    {
        String xml = XmlUtil.mapToXmlError("1");
        long userId = getUserId();
        try {
            //解析参数
            String reqXml = XmlUtil.isChangeToStr(inputstream);
            Map<String, Object> map = XmlUtil.getElementMap(reqXml);
            if(map == null){
                xml = XmlUtil.mapToXmlError("0x50000027");
            }else{
                String system = (String) map.get("systemType");
                String pushToken = (String) map.get("pushToken");
                if(StringUtils.isBlank(pushToken)||StringUtils.isBlank(system)){
                    xml = XmlUtil.mapToXmlError("0x50000001");
                }else{
                    //获取用户
                    AppUser appUser = appUserService.getUserById(userId);
                    if(appUser == null){
                        xml = XmlUtil.mapToXmlError("0x50020031");
                    }else{
                        //修改用户属性
                        appUser.setSystemType(system);
                        appUser.setPushToken(pushToken);
                        appUserService.updateUser(appUser);
                        xml = XmlUtil.mapToXmlRight();
                    }               
                }
                
            }
            
        } catch (BusinessException e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("0x"+e.getErrCode());
        } catch (ManagerException e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("0x50000044");
        } catch (Exception e) {
            log.error(e);
            xml = XmlUtil.mapToXmlError("0x50000027");
        }
        return new ResponseEntity<String>(xml, HttpStatus.OK);
    }

}
