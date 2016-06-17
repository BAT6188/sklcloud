package com.skl.cloud.controller.ipc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.PlatformMapping;
import com.skl.cloud.model.PlatformP2p;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.P2pService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.XmlUtil;

@RequestMapping("/skl-cloud/device")
@Controller
public class IpcReportUpnpController extends IpcController
{
	@Autowired(required = true)
	private IPCameraService iPCameraService;

	@Autowired(required = true)
	private P2pService p2pService;

	/**
	 * <IPC上报upnp信息>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/upnpInfo.ipc", method = RequestMethod.POST)
	public ResponseEntity<String> ipcReportUpnpInfo(HttpServletRequest req, HttpServletResponse resp)
	{
		String sReturn = null;

		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			Map<String, Object> uPNPMap = (Map<String, Object>) ((Map<String, Object>) paraMap.get("NAT")).get("uPNP");
			Map<String, Object> privateMap = (Map<String, Object>) uPNPMap.get("private");

			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);

			String mappingHTTP = XmlUtil.convertToString(uPNPMap.get("Mapping:name='HTTP'"), false);
			String mappingRTSP = XmlUtil.convertToString(uPNPMap.get("Mapping:name='RTSP'"), false);
			String mappingRTP = XmlUtil.convertToString(uPNPMap.get("Mapping:name='RTP'"), false);
			String mappedIP = XmlUtil.convertToString(uPNPMap.get("MappedIP"), false);

			String privateMappingHTTP = XmlUtil.convertToString(privateMap.get("Mapping:name='HTTP'"), false);
			String privateMappingRTSP = XmlUtil.convertToString(privateMap.get("Mapping:name='RTSP'"), false);
			String privateMappingRTP = XmlUtil.convertToString(privateMap.get("Mapping:name='RTP'"), false);
			String privateMappedIP = XmlUtil.convertToString(privateMap.get("MappedIP"), false);
			
			String random = XmlUtil.convertToString(paraMap.get("random"), true);

			// 查询ipc
			IPCamera ipCamera = iPCameraService.getIPCameraBySN(sn);
			if (ipCamera == null)
			{
				throw new BusinessException("0x50000047");
			}

			if (!random.equals(ipCamera.getIpcSub().getSendIpcRandom()))
			{
				throw new BusinessException("0x50000002");
			}

			// 组装数据
			PlatformP2p p2p = new PlatformP2p();
			p2p.setCamera_serialno(sn);
			p2p.setMappingIp(mappedIP);
			p2p.setLocalIp(privateMappedIP);
			p2p.setNatType("9"); //  默认：9
			p2p.setMsgType("1"); //  默认：1

			List<PlatformMapping> mappingList = new ArrayList<PlatformMapping>();
			// 第一条数据
			PlatformMapping mapping = new PlatformMapping();
			mapping.setSn(sn);
			mapping.setLocalPort(mappingHTTP);
			mapping.setMappingPort(privateMappingHTTP);
			mapping.setSocketType("HTTP"); // 默认：HTTP
			mapping.setPortType("HTTP");
			mappingList.add(mapping);

			// 第二条数据
			mapping = new PlatformMapping();
			mapping.setSn(sn);
			mapping.setLocalPort(mappingRTSP);
			mapping.setMappingPort(privateMappingRTSP);
			mapping.setSocketType("HTTP");
			mapping.setPortType("RTSP");
			mappingList.add(mapping);

			// 第三条数据
			mapping = new PlatformMapping();
			mapping.setSn(sn);
			mapping.setLocalPort(mappingRTP);
			mapping.setMappingPort(privateMappingRTP);
			mapping.setSocketType("HTTP");
			mapping.setPortType("RTP");
			mappingList.add(mapping);

			// 执行DB添加操作
			p2pService.insertP2pInfo(p2p);
			p2pService.insertMappingsInfo(mappingList);

			sReturn = XmlUtil.mapToXmlRight();
		}
		catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
