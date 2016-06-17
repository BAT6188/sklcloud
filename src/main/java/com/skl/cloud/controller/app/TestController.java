package com.skl.cloud.controller.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.service.test.TestControllerService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.XmlUtil;

@Controller
@RequestMapping("/skl-cloud/test/app")
public class TestController extends BaseController {
	
	@Autowired(required = true)
	private TestControllerService testControllerService;

	@RequestMapping("/nat/uploadInfo")
	private ResponseEntity<String> testStatus(InputStream inputStream) {
		Map<String, String> map = new HashMap<String, String>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pe) {
			super.LogException(new ManagerException(pe), this.getClass().getName());// 原始代码异常
		}
		Document document = null;
		try {
			document = builder.parse(inputStream);
		} catch (SAXException e) {
			return new ResponseEntity<String>(XmlUtil.responseXml("1", "1"), HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<String>(XmlUtil.responseXml("1", "1"), HttpStatus.OK);
		}
		Element root = document.getDocumentElement();
		String natTypesStr = "";
		// 保存xml中获取的参数
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) nodeList.item(i);
				String sTagName = element.getTagName();
				String sValue = element.getTextContent();
				if ("natType".equals(sTagName)) {
					natTypesStr = sValue;
				}
				if ("inetType".equals(sTagName)) {
					map.put("inetType", sValue);
				}
				if ("inetServer".equals(sTagName)) {
					map.put("inetServer", sValue);
				}
				if ("routeName".equals(sTagName)) {
					map.put("routeName", sValue);
				}
				if ("name".equals(sTagName)) {
					map.put("name", sValue);
				}
				if ("place".equals(sTagName)) {
					map.put("place", sValue);
				}
				if ("devType".equals(sTagName)) {
					map.put("devType", sValue);
				}
			}
		}
		String[] natTypes = natTypesStr.split(";");
		for (int i = 0; i < natTypes.length; i++) {
			map.put("natType", natTypes[i]);
			try {
				int flag = testControllerService.insertNatInfo(map);
			} catch (ManagerException me) {
				super.LogException(me, this.getClass().getName());
				LoggerUtil.error("insert natinfo erro", this.getClass().getName());
				return new ResponseEntity<String>(XmlUtil.responseXml("1", "2"), HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>(XmlUtil.responseXml("0", "0"), HttpStatus.OK);
	}
}
