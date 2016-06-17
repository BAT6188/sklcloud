package com.skl.cloud.controller.ipc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.EventScheduleService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.validator.XmlElementValidator;

/**
 * @Package com.skl.cloud.controller
 * @Title: EventScheduleController
 * @Description: Event Schedule Copyright: Copyright (c) 2015
 *               Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年7月17日
 * @version V1.0
 */
@Controller
@RequestMapping("/skl-cloud")
public class EventScheduleController extends BaseController {

    private static final Logger logger = Logger.getLogger(EventScheduleController.class);

    @Autowired(required = true)
    private EventScheduleService eventScheduleService;

    @Autowired(required = true)
    private IPCameraService ipcameraService;

    /**
     * 
     * @Title: reportOnLineNotification
     * @Description: IPC设备上报（在线告警）事件（IPC_v2.2_2.15）
     * @param response
     * @param request
     * @param inputStream
     * @param SN
     * @param EventID
     * @return String
     * @throws ManagerException
     * @author leiqiang
     * @date 2015年7月27日
     */
    @RequestMapping(value = "/device/{SN}/notification/{EventID}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> reportOnLineNotification(HttpServletResponse response, HttpServletRequest request,
            InputStream inputStream, @PathVariable
            final String SN, @PathVariable String EventID) {
        // 获取当前用户id，（diges认证通过后）
        String sReturn = XmlUtil.mapToXmlError("1");
        List<EventAlert> list = new ArrayList<EventAlert>();
        String sXml = ""; // 请求报文XML字符串
        String cameraModel = "";
        // 校验：SN格式合法
        if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("SN"), SN)) {
            sReturn = XmlUtil.mapToXmlError("0x50000033");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }

        try {

            IPCamera ipcamera = ipcameraService.getIPCameraBySN(SN);

            // 校验：SN是否存在
            if (ipcamera == null) {
                sReturn = XmlUtil.mapToXmlError("0x50000047");
                return new ResponseEntity<String>(sReturn, HttpStatus.OK);
            }
            cameraModel = ipcamera.getModel();
        } catch (Exception e) {
            sReturn = XmlUtil.mapToXmlError("0x50010027");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }

        try {
            sXml = XmlUtil.isChangeToStr(inputStream);
            LoggerUtil.info("IPC发送的报文：" + sXml, this.getClass());
        } catch (Exception e) {
            // 请求报文解析失败
            sReturn = XmlUtil.mapToXmlError("0x50010026");
            return new ResponseEntity<String>(sReturn, HttpStatus.BAD_REQUEST);
        }

        Document doc = null;
        try {
            doc = DocumentHelper.parseText(sXml);
        } catch (DocumentException de) {
            // 解析XML报文出错
            sReturn = XmlUtil.mapToXmlError("0x50010020");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }
        EventAlert ea = new EventAlert(); // 事件告警信息对象
        Element root = doc.getRootElement(); // 获取根节点

        String sn = root.elementTextTrim("SN");

        if (!sn.equals(SN)) {
            sReturn = XmlUtil.mapToXmlError("0x50000033");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }

        String model = root.elementTextTrim("model");

		if ("".equals(model) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("cameraModel"), model))
		{
			sReturn = XmlUtil.mapToXmlError("0x50010028");
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}
		
		 if (!model.equals(cameraModel)) {
	            sReturn = XmlUtil.mapToXmlError("0x50000033");
	            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	        }

        String id = root.elementTextTrim("id");
        if (!StringUtil.isNullOrEmpty(id)) {
            sReturn = XmlUtil.mapToXmlError("0x50010021");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }

		String dateTime = root.elementTextTrim("dateTime");
		if (!StringUtil.isNullOrEmpty(dateTime) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("dateTime"), dateTime.replace("T", " ")))
		{
			sReturn = XmlUtil.mapToXmlError("0x50010022");
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}

		String activePostCount = root.elementTextTrim("activePostCount");
		if (!StringUtil.isNullOrEmpty(activePostCount) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("activePostCount"), activePostCount))
		{
			sReturn = XmlUtil.mapToXmlError("0x50010023");
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}

        String eventId = root.elementTextTrim("eventId");
        if (!eventId.equals(EventID)) {
            sReturn = XmlUtil.mapToXmlError("0x50000033");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }
        // 校验：eventId格式合法
        if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventId"), eventId)) {
            sReturn = XmlUtil.mapToXmlError("0x50000033");
            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }

        String eventState = root.elementTextTrim("eventState");

		// 校验：eventState格式合法
		if ("".equals(eventState) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventState"), eventState))
		{
			sReturn = XmlUtil.mapToXmlError("0x50000033");
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}

        String eventDescription = root.elementTextTrim("eventDescription");

		// 校验：eventDescription格式合法
		if ("".equals(eventDescription) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventDescription"), eventDescription))
		{
			sReturn = XmlUtil.mapToXmlError("0x50000033");
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}

		// DetectionRegionList节点
		StringBuffer RegionID = new StringBuffer();

        Element node = null;
        try {
            node = root.element("regionCoordinatesList"); // "DetectionRegionList"是节点名

            if (node != null) {
                List nodes = node.elements("regionId"); // 获取RegionID节点

                for (int i = 0; i < nodes.size(); i++) {

                    Element elm = (Element) nodes.get(i);
                    if (i != 0) {
                        RegionID.append(",");
                    }

                    RegionID.append(elm.getText());
                }
            }

        } catch (Exception e) {
            // 获取区域信息异常
            sReturn = XmlUtil.mapToXmlError("0x50010019");

            return new ResponseEntity<String>(sReturn, HttpStatus.OK);
        }
        ea.setCameraSerialno(sn);
        ea.setCameraModel(model);
        ea.setId(id);
        ea.setEventId(EventID);
        ea.setDateTime(dateTime);
        ea.setActivePostCount(activePostCount);
        ea.setEventState(eventState);
        ea.setEventDescription(eventDescription);
        ea.setRegionId(RegionID.toString());
        ea.setChannelId("MAIN");
        list.add(ea);

		String chanId = "MAIN";
		EventAlert eventalert = eventScheduleService.queryEventAlert(sn, chanId, EventID, dateTime);
		if (eventalert == null)
		{
			try 
			{
				for (int i = 0; i < list.size(); i++) 
				{
					eventScheduleService.saveEventAlert(list.get(i));
				}
				eventScheduleService.addAlarmPush(sn, EventID, dateTime, list, model);
				eventScheduleService.addRecordVideo(sn, EventID, dateTime, list, model);
			} catch (Exception e)
			{
				LoggerUtil.error("保存在线告警消息失败。", e.getMessage());
			}

		} else {
			LoggerUtil.info("该告警信息已存在，请核实。", this.getClass());
		}
		
        return new ResponseEntity<String>(XmlUtil.mapToXmlRight(), HttpStatus.OK);
    }
    /**
     * 
     * @Title: reportOffLineNotification
     * @Description: IPC设备上报（离线告警）事件（IPC_v2.2_2.15）
     * @param response
     * @param request
     * @param inputStream
     * @param SN
     * @param EventID
     * @return String
     * @throws ManagerException
     * @author leiqiang
     * @date 2016年2月25日       
     */
    @RequestMapping(value = "/device/{SN}/offLine/notification/{EventID}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> reportOffLineNotification(HttpServletResponse response, HttpServletRequest request,
    		InputStream inputStream, @PathVariable
    		final String SN, @PathVariable String EventID) {
    	// 获取当前用户id，（diges认证通过后）
    	String sReturn = XmlUtil.mapToXmlError("1");
    	List<EventAlert> list = new ArrayList<EventAlert>();
    	String sXml = ""; // 请求报文XML字符串
    	String cameraModel = "";
    	// 校验：SN格式合法
    	if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("SN"), SN)) {
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	try {
    		
    		IPCamera ipcamera = ipcameraService.getIPCameraBySN(SN);
    		
    		// 校验：SN是否存在
    		if (ipcamera == null) {
    			sReturn = XmlUtil.mapToXmlError("0x50000047");
    			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    		}
    		cameraModel = ipcamera.getModel();
    	} catch (Exception e) {
    		sReturn = XmlUtil.mapToXmlError("0x50010027");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	try {
    		sXml = XmlUtil.isChangeToStr(inputStream);
    		LoggerUtil.info("IPC发送的报文：" + sXml, this.getClass());
    	} catch (Exception e) {
    		// 请求报文解析失败
    		sReturn = XmlUtil.mapToXmlError("0x50010026");
    		return new ResponseEntity<String>(sReturn, HttpStatus.BAD_REQUEST);
    	}
    	
    	Document doc = null;
    	try {
    		doc = DocumentHelper.parseText(sXml);
    	} catch (DocumentException de) {
    		// 解析XML报文出错
    		sReturn = XmlUtil.mapToXmlError("0x50010020");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	EventAlert ea = new EventAlert(); // 事件告警信息对象
    	Element root = doc.getRootElement(); // 获取根节点
    	
    	String sn = root.elementTextTrim("SN");
    	
    	if (!sn.equals(SN)) {
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String model = root.elementTextTrim("model");
    	
    	if ("".equals(model) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("cameraModel"), model))
    	{
    		sReturn = XmlUtil.mapToXmlError("0x50010028");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	if (!model.equals(cameraModel)) {
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String id = root.elementTextTrim("id");
    	if (!StringUtil.isNullOrEmpty(id)) {
    		sReturn = XmlUtil.mapToXmlError("0x50010021");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String dateTime = root.elementTextTrim("dateTime");
    	if (!StringUtil.isNullOrEmpty(dateTime) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("dateTime"), dateTime.replace("T", " ")))
    	{
    		sReturn = XmlUtil.mapToXmlError("0x50010022");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String activePostCount = root.elementTextTrim("activePostCount");
    	if (!StringUtil.isNullOrEmpty(activePostCount) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("activePostCount"), activePostCount))
    	{
    		sReturn = XmlUtil.mapToXmlError("0x50010023");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String eventId = root.elementTextTrim("eventId");
    	if (!eventId.equals(EventID)) {
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	// 校验：eventId格式合法
    	if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventId"), eventId)) {
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String eventState = root.elementTextTrim("eventState");
    	
    	// 校验：eventState格式合法
    	if ("".equals(eventState) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventState"), eventState))
    	{
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	String photoUrl = "";
    	String videoUrl = "";
		Element memberElm = root.element("extensions");
		
		Element nodeUrl = memberElm.element("eventUrl"); // "messageUrlList"是节点名
		
		photoUrl = nodeUrl.elementTextTrim("photoUrl");
		
		videoUrl = nodeUrl.elementTextTrim("videoUrl");
		
    	String eventDescription = root.elementTextTrim("eventDescription");
    	
    	// 校验：eventDescription格式合法
    	if ("".equals(eventDescription) || !StringUtil.pattern(XmlElementValidator.checkParaMap.get("eventDescription"), eventDescription))
    	{
    		sReturn = XmlUtil.mapToXmlError("0x50000033");
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	
    	// DetectionRegionList节点
    	StringBuffer RegionID = new StringBuffer();
    	
    	Element node = null;
    	try {
    		node = root.element("regionCoordinatesList"); // "DetectionRegionList"是节点名
    		
    		if (node != null) {
    			List nodes = node.elements("regionId"); // 获取RegionID节点
    			
    			for (int i = 0; i < nodes.size(); i++) {
    				
    				Element elm = (Element) nodes.get(i);
    				if (i != 0) {
    					RegionID.append(",");
    				}
    				
    				RegionID.append(elm.getText());
    			}
    		}
    		
    	} catch (Exception e) {
    		// 获取区域信息异常
    		sReturn = XmlUtil.mapToXmlError("0x50010019");
    		
    		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    	}
    	ea.setCameraSerialno(sn);
    	ea.setCameraModel(model);
    	ea.setId(id);
    	ea.setEventId(EventID);
    	ea.setDateTime(dateTime);
    	ea.setActivePostCount(activePostCount);
    	ea.setEventState(eventState);
    	ea.setEventDescription(eventDescription);
    	ea.setRegionId(RegionID.toString());
    	ea.setChannelId("MAIN");
    	
    	if (videoUrl != null)
		{
    		if(!videoUrl.isEmpty())
    		{
    			String strCut = "/" + videoUrl.split("/")[2] + "/";
    			int startIdx = videoUrl.indexOf(strCut) + strCut.length();
    			videoUrl = videoUrl.substring(startIdx);
    		}
    		ea.setStoreUrl(videoUrl);
		}
    	
    	ea.setPhotoUrl(photoUrl);
    	list.add(ea);
    	
    	String chanId = "MAIN";
		EventAlert eventalert = eventScheduleService.queryEventAlert(sn, chanId, EventID, dateTime);
		if (eventalert == null)
		{
			try
			{
				for (int i = 0; i < list.size(); i++) 
				{
					eventScheduleService.saveEventAlert(list.get(i));
				}
				eventScheduleService.addAlarmPush(sn, EventID, dateTime, list, model);
			} catch (Exception e) 
			{
				LoggerUtil.error("保存离线告警消息失败。", e.getMessage());
			}

		} else {
			LoggerUtil.info("该告警信息已存在，请核实。", this.getClass());
		}
    	
    	return new ResponseEntity<String>(XmlUtil.mapToXmlRight(), HttpStatus.OK);
    }

    
}
