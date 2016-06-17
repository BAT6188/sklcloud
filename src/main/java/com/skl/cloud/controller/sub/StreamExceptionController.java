package com.skl.cloud.controller.sub;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.sub.SubException;
import com.skl.cloud.service.sub.StreamExceptionService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @ClassName: StreamExceptionController
 * @Description: sub system report the stream exception
 * <p>Creation Date: 2016年3月26日 and by Author: yangbin </p>
 *
 * @author $Author$
 * @date $Date$
 * @version  $Revision$
 */
@Controller
public class StreamExceptionController extends BaseController {

	@Autowired
	private StreamExceptionService streamExceptionService;

	@RequestMapping("/skl-cloud/exception")
	public ResponseEntity<String> exception(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, String> hashMap = new HashMap<String, String>();
		String sReturn = null;
		final String root = "exceptionInfo";
		try {
			Document doc = null;
			try {
				doc = DocumentHelper.parseText(XmlUtil.isChangeToStr(req.getInputStream()));
				Element element = doc.getRootElement();
				this.getNodes(element, hashMap);
			} catch (DocumentException e) {
				LoggerUtil.error(e.getMessage(), e, StreamExceptionController.class);
				throw new BusinessException("0x50000027");
			}

			if (StringUtil.isEmpty(hashMap.get("subsysType")) || StringUtil.isEmpty(hashMap.get("exceptionType"))
					|| StringUtil.isEmpty(hashMap.get("streamSn"))
					|| StringUtil.isEmpty(hashMap.get("subsysPrivateIp"))
					|| StringUtil.isEmpty(hashMap.get("subsysPrivateIp"))
					|| StringUtil.isEmpty(hashMap.get("subsysPublicIp"))
					|| StringUtil.isEmpty(hashMap.get("exceptionNotification"))
					|| StringUtil.isEmpty(hashMap.get("exceptionContent"))) {
				throw new BusinessException("0x50000048");
			}
			
			SubException subException = new SubException();
			subException.setUuid(UUID.randomUUID().toString());
			subException.setExceptionContent(XmlUtil.convertToString(hashMap.get("exceptionContent"), false));
			subException.setExceptionNotification(XmlUtil.convertToString(hashMap.get("exceptionNotification"), false));
			subException.setStreamSn(XmlUtil.convertToString(hashMap.get("streamSn"), false));
			subException.setStreamType(XmlUtil.convertToString(hashMap.get("streamType"), false));
			subException.setSubsysType(XmlUtil.convertToString(hashMap.get("subsysType"), false));
			subException.setSubsysUuid(XmlUtil.convertToString(hashMap.get("subsysUuid"), false));
			subException.setSubsysPublicIp(XmlUtil.convertToString(hashMap.get("subsysPublicIp"), false));
			subException.setSubsysPrivateIp(XmlUtil.convertToString(hashMap.get("subsysPrivateIp"), false));
			subException.setExceptionType(XmlUtil.convertToString(hashMap.get("exceptionType"), false));
			//异步进行相应处理
			streamExceptionService.handleSubSysReportExp(subException);
			
			LinkedHashMap<String, Object> valueMap = new LinkedHashMap<String, Object>();
			sReturn = XmlUtil.mapToXmlRight(root, valueMap);
		} catch (Exception e) {
			sReturn = getErrorXml(root, e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}
	
	/**
	 * 从指定节点开始,递归遍历所有子节点
	 * 
	 * @author
	 */
	private void getNodes(Element node, Map<String, String> map) {
		map.put(node.getName(), node.getTextTrim());
		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodes(e, map);// 递归
		}
	}

}
