package com.skl.cloud.controller.app;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.P2pService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * 
  * @ClassName: AppQueryP2PInfoContorller
  * @Description: APP获取P2P信息
  * @author guangbo
  * @date 2015年6月16日
  *
 */

@Controller
@RequestMapping("skl-cloud/app")
public class AppQueryP2PInfoContorller extends BaseController {
    @Autowired
    private IPCameraService ipcService;

    @Autowired
	private P2pService p2pService;

    /**
     * APP获取P2P信息
     */
    @RequestMapping("/queryP2PInfo/{SN}")
    public ResponseEntity<String> queryP2PInfo(@PathVariable String SN, InputStream inputstream)
            throws ManagerException {
        String sReturn = "";
        StringBuffer sblocal = new StringBuffer();
        StringBuffer sbtotal = new StringBuffer();
        StringBuffer sbMapping = new StringBuffer();
        LinkedHashMap<String, Object> repMap = new LinkedHashMap<String, Object>();
        try {
            IPCamera ipcamera = ipcService.getIPCameraBySN(SN);
            if (ipcamera == null) {
                throw new BusinessException("0x50000047");
            }

            if ((null != ipcamera.getIsLive() && ipcamera.getIsLive())
                    && (null != ipcamera.getStatus() && ipcamera.getStatus())) {

                PlatformP2p platformP2p = p2pService.getP2pInfoBySn(SN);

                List<PlatformMapping> list = p2pService.getMappingsInfoBySn(SN);
                if (list != null && !list.isEmpty()) {
                    sblocal.append("<private>");
                    for (PlatformMapping platFormmapping : list) {
                        sblocal.append("<Mapping name=\"");
                        sblocal.append(platFormmapping.getPortType());
                        sblocal.append("\">");
                        sblocal.append(platFormmapping.getLocalPort());
                        sblocal.append("</Mapping>");

                        sbMapping.append("<Mapping name=\"");
                        sbMapping.append(platFormmapping.getPortType());
                        sbMapping.append("\">");
                        sbMapping.append(platFormmapping.getMappingPort());
                        sbMapping.append("</Mapping>");
                    }
                    //add private local ip
                    sblocal.append("<MappedIP>");
                    sblocal.append(platformP2p != null ? platformP2p.getLocalIp() : "");
                    sblocal.append("</MappedIP>");
                    sblocal.append("</private>");
                    //add mapping ip
                    sbMapping.append("<MappedIP>");
                    sbMapping.append(platformP2p != null ? platformP2p.getMappingIp() : "");
                    sbMapping.append("</MappedIP>");
                    //组装返回报文
                    sbtotal.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    sbtotal.append("<queryP2PInfo version=\"1.0\" xmlns=\"urn:skylight\"><NAT><mode>uPNP</mode><uPNP>");
                    sbtotal.append(sbMapping.toString());
                    sbtotal.append(sblocal.toString());
                    sbtotal.append("</uPNP>");
                    sbtotal.append("</NAT>");
                    sbtotal.append("<ResponseStatus><statusCode>0</statusCode><statusString>0</statusString></ResponseStatus>");
                    sbtotal.append("</queryP2PInfo>");
                    sReturn = sbtotal.toString();
                } else {
                    sReturn = XmlUtil.responseXml("0x50000002", "", "queryP2PInfo", repMap);
                }
            } else {
                sReturn = XmlUtil.responseXml("0x50020062", "", "queryP2PInfo", repMap);
            }
        } catch (Exception e) {
            sReturn = getErrorXml("queryP2PInfo", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }
}
