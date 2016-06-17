package com.skl.cloud.controller.app;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.XmlUtil;

@RequestMapping("skl-cloud/app")
@Controller
public class AppIPCameraController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(AppIPCameraController.class);
    @Autowired
    private IPCameraService ipcameraService;
    
    /**
     * @Title: SetDeviceKind
     * @Description: App向云端设置摄像头种类及名称、时区
     * @param req
     * @return ResponseEntity<String>
     * @author lizhiwei
     * @date 2015年12月28日
     */
    @RequestMapping("/setDeviceKind")
    public ResponseEntity<String> setDeviceKind(HttpServletRequest req) {
        String sReturn = null;
        try
        {
            // 根据请求返回参数map
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
            String deviceKind = XmlUtil.convertToString(paraMap.get("deviceKind"), true);
            String deviceName = XmlUtil.convertToString(paraMap.get("deviceName"), true);
            String timeZone = XmlUtil.convertToString(paraMap.get("timeZone"), true);

            IPCamera ipcamera = ipcameraService.getIPCameraBySN(sn);

            if (ipcamera == null)
            {
                sReturn = XmlUtil.mapToXmlError("0x50010003");
            }

            ipcamera.setSn(sn);
            if (deviceKind != null)
            {
                ipcamera.setKind(deviceKind);
            }
            if (deviceName != null)
            {
                ipcamera.setNickname(deviceName);
            }
            if (timeZone != null)
            {
                ipcamera.setTimeZone(timeZone);
            }

            if ((deviceKind == null || deviceKind.isEmpty())&&(deviceName == null || deviceName.isEmpty())&&(timeZone == null || timeZone.isEmpty()))
            {
                throw new BusinessException("0x50010029");
            }

            ipcameraService.updateIPCamera(ipcamera);

            sReturn = XmlUtil.mapToXmlRight();
        } catch (Exception e)
        {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }
    /**
     * app请求对ipc密码操作（包含校验密码，修改设置密码）
     * @param sn
     * @param id
     * @param inputstream
     * @return
     */
	@RequestMapping("/{sn}/PSIA/Security/AAA/users/{id}")
	public ResponseEntity<String> operateIpcPassword(@PathVariable("sn") String sn,@PathVariable("id") int id,InputStream inputstream){
		try {
			String reqXml = XmlUtil.isChangeToStr(inputstream);
			Map<String, Object> reqMap = XmlUtil.getElementMap(reqXml, null);
			log.info("sn:{}请求ipc密码 报文:{}",sn,reqXml);
			//
			String password = XmlUtil.getDomValue(reqMap, "User/password");
			String userName = XmlUtil.getDomValue(reqMap, "User/userName");
			String idXml = XmlUtil.getDomValue(reqMap, "User/id");
			if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)||StringUtils.isBlank(idXml)){
				throw new BusinessException("0x50000001");
			}
			
			Long userId = getUserId();	
			//id 为0 修改密码
			if(id == 0){
				ipcameraService.modifyIpcPassword(sn,password,userId,id,reqXml);
				return new ResponseEntity<String>(XmlUtil.mapToXmlRight(),HttpStatus.OK);
				
			//id 位1 校验
			} else if(id == 1){
				ipcameraService.verifyIpcPassword(sn,password,userId);
				return new ResponseEntity<String>(XmlUtil.mapToXmlRight(),HttpStatus.OK);
			} else {
				throw new BusinessException("0x50000001");
			}		
		} catch (Exception e) {
			// TODO: handle exception
			log.error("app请求ipc密码操作 sn:{}，id:{}",sn,id,e);
			return new ResponseEntity<String>(getErrorXml(e, this.getClass().getName()),HttpStatus.OK);
		}
				
	}
}
