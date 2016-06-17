package com.skl.cloud.util.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.service.common.I18NResourceService;
import com.skl.cloud.util.validator.XmlElementValidator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {
	private static final Logger log = Logger.getLogger(XmlUtil.class);

	/**
	 * @author guangbo
	 * @param str
	 * @return 把字符串转化成xml
	 */
	public static String strChangeToXML(String str) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new ByteArrayInputStream(str.getBytes()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UFT-8");
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		try {
			xmlwriter.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 获取文件的document对象，然后获取对应的根节点
	 * 
	 * @author chenleixing
	 */

	/**
	 * 从指定节点开始,递归遍历所有子节点
	 * 
	 * @author chenleixing
	 */
	public static Map getNodes(Element node, Map<String, Object> resultMap) {
		log.info("--------------------");
		// 当前节点的名称、文本内容和属性
		log.info("当前节点名称：" + node.getName());// 当前节点名称
		log.info("当前节点的内容：" + node.getTextTrim());// 当前节点名称
		resultMap.put(node.getName(), node.getTextTrim());

		List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
		for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			String value = attr.getValue();// 属性的值
			resultMap.put(name, value);
			log.info("属性名称：" + name + "属性值：" + value);
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			XmlUtil.getNodes(e, resultMap);// 递归
		}
		return resultMap;
	}

	/**
	 * 根据节点节点的name返回节点value 返回节点value
	 */
	public static String getElement(Element element, String nodeName) throws ManagerException {
		String name = "";
		List list = element.elements();
		// 递归方法
		for (Iterator its = list.iterator(); its.hasNext();) {
			Element chileEle = (Element) its.next();
			name = chileEle.getName();
			if (nodeName.equals(name)) {
				return chileEle.getText();
			}

			log.info("节点：" + chileEle.getName() + ",内容：" + chileEle.attributeValue("id"));
			getElement(chileEle, nodeName);
		}
		return "";

	}

	/**
	 * 递归遍历方法 并将每个元素值取出放入map中，key为元素父节点名称/元素名称名称
	 * 
	 * @param element
	 */
	public static void getElementMap(Element element, Map<String, Object> resultMap, List<String> groupList) {
		if (groupList == null) {
			groupList = new ArrayList<String>();
		}

		List<Object> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			String xpath = element.getParent().getName() + "/" + element.getName();
			// String value = StringUtil.strTrim(element.getTextTrim());
			String value = element.getStringValue();
			resultMap.put(xpath, value);
		} else {
			// 有子元素
			for (Iterator it = elements.iterator(); it.hasNext();) {
				Element elem = (Element) it.next();
				String xpath = elem.getParent().getName() + "/" + elem.getName();
				if (groupList.contains(xpath)) {
					// List<Element> subList = elem.elements();
					String subXml = elem.asXML();
					Document srcdoc;
					try {
						srcdoc = DocumentHelper.parseText(subXml);
						Element root2 = srcdoc.getRootElement();
						Map subMap2 = new HashMap();
						// 递归遍历
						getElementMap(root2, subMap2, groupList);
						List groupMap = (List) resultMap.get(xpath);
						if (groupMap == null) {
							groupMap = new ArrayList();
							resultMap.put(xpath, groupMap);
						}
						groupMap.add(subMap2);
					} catch (DocumentException e) {
					}
				} else {
					// 递归遍历
					getElementMap(elem, resultMap, groupList);
				}
			}
		}
	}

	/**
	 * 解析xml，把信息保存到Map中
	 * 
	 * @param document
	 * @param groupList
	 *            分组的cell[格式如：StreamingChannelList/StreamingChannel]
	 * @return
	 */
	public static Map getElementMap(Document document, List<String> groupList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			Element root = document.getRootElement();
			getElementMap(root, resultMap, groupList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 解析xml格式的字符串，把信息保存到Map中
	 * 
	 * @param strXml
	 * @param groupList
	 *            分组的cell[格式如：StreamingChannelList/StreamingChannel]
	 * @return
	 */
	public static Map<String, Object> getElementMap(String strXml, List<String> groupList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (StringUtil.isEmpty(strXml)) {
			return resultMap;
		}

		try {
			Document srcdoc = DocumentHelper.parseText(strXml);
			return getElementMap(srcdoc, groupList);
		} catch (Exception e) {
			LoggerUtil.error("0x50000030", LoggerUtil.class);
		}

		return resultMap;
	}

	/**
	 * getElementMap(这里用一句话描述这个方法的作用) 
	 *
	 * @Title: getElementMap
	 * @Description: 将ipc返回的xml字符串转化为map
	 * @param @param strXml
	 * @param @return (参数说明)
	 * @return Map<String,Object> (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年7月25日
	 */
	public static Map<String, Object> getElementMap(String strXml) {
		LinkedHashMap<String, Object> resultMap = null;
		if (StringUtil.isEmpty(strXml)) {
			return null;
		}
		try {
			resultMap = new LinkedHashMap<String, Object>();
			Document srcdoc = DocumentHelper.parseText(strXml);
			Element root = srcdoc.getRootElement();
			for (Iterator it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				resultMap.put(element.getName(), element.getStringValue());
			}

		} catch (Exception e) {
			LoggerUtil.error("0x50000030", LoggerUtil.class);
			return null;
		}
		return resultMap;
	}

	/**
	 * 解析指令中心返回的xml报文
	 * 
	 * @Title: getElementMapCommand
	 * @param @param strXml
	 * @param @return (参数说明)
	 * @return Map<String,Object> (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月29日
	 */
	public static Map<String, Object> getElementMapCommand(String strXml) {
		Map<String, Object> resultMap = null;
		if (StringUtil.isEmpty(strXml)) {
			return null;
		}
		try {
			resultMap = new HashMap<String, Object>();
			Document srcdoc = DocumentHelper.parseText(strXml);
			Element root = srcdoc.getRootElement();
			for (Iterator it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				String nodeName = element.getName();
				if (nodeName.equals("httpCmd")) {
					for (Iterator<Element> ite = element.nodeIterator(); ite.hasNext();) {
						Element httpCmd = (Element) ite.next();
						resultMap.put(httpCmd.getName(), httpCmd.getStringValue());
					}
				}
				if (nodeName.equals("headers")) {
					Map<String, Object> headerMap = new HashMap<String, Object>();
					for (Iterator<Element> ite = element.nodeIterator(); ite.hasNext();) {
						Element header = (Element) ite.next();
						String name = "";
						String value = "";
						for (Iterator<Element> ites = header.nodeIterator(); ites.hasNext();) {
							Element ele = (Element) ites.next();
							if (ele.getName().equals("name")) {
								name = ele.getStringValue();
							}
							if (ele.getName().equals("value")) {
								value = ele.getStringValue();
							}
							headerMap.put(name, value);
						}
					}
					resultMap.put("headerMap", headerMap);
				}
				if (nodeName.equals("body")) {
					resultMap.put(element.getName(), element.getStringValue());
				}
				resultMap.put(element.getName(), element.getStringValue());
			}

		} catch (Exception e) {
			LoggerUtil.error("0x50000030", LoggerUtil.class);
			return null;
		}
		return resultMap;
	}

	/**
	 * 返回信息
	 * 
	 * @param domMap
	 * @param cellName
	 * @return
	 */
	public static String getDomValue(Map domMap, String cellName) {
		if (StringUtil.isEmpty(cellName)) {
			return "";
		}

		if (domMap == null || !domMap.containsKey(cellName)) {
			return "";
		}

		Object domValue = domMap.get(cellName);
		if (domValue != null) {
			return domValue.toString();
		}

		return "";
	}

	/**
	 * @author guangbo
	 * @param str
	 * @return 把流转化成字符串
	 */
	public static String isChangeToStr(InputStream is) {
		int i = -1;
		byte[] b = new byte[1024];
		StringBuffer sb = new StringBuffer();
		try {
			while ((i = is.read(b)) != -1) {
				sb.append(new String(b, 0, i));
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		String content = sb.toString();
		return content;
	}

	/**
	 * @author guangbo
	 * @param obj
	 * @return 将POJO转化成XML
	 */

	public static String toXml(Object obj) {
		XStream xstream = new XStream(new DomDriver("utf8"));
		xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
		return xstream.toXML(obj);
	}

	/**
	 * 利用XSD校验XML格式是否正确
	 * 
	 * @param xml
	 * @param xsd
	 * @return 返回XML操作对象
	 * 
	 * @throws ManagerException
	 */
	public static boolean validateXml(InputStream xml, InputStream xsd) {
		try {
			final String schema_uri = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(schema_uri);
			Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator val = schema.newValidator();
			val.validate(new StreamSource(xml));
		} catch (Exception e) {
			// PortalLog.error(e.getMessage(), e, this.getClass().getName());
			return false;
		}
		return true;
	}

	/**
	 * 把输入流转换为字符串
	 * 
	 * @param inputStream
	 * @return
	 */

	/**
	 * 把输入流转换为字符串
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String chInput2Str(InputStream inputStream) {

		StringBuilder contentBD = new StringBuilder();

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				contentBD.append(tmp);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return contentBD.toString();
	}

	/**
	 * 组织返回的xml报文 返回的xml只有ResponseStatus
	 * 
	 * @Title: responseXml
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	public static String responseXml(String statsusCode, String statusString) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		org.w3c.dom.Element root = doc.createElement("ResponseStatus");
		root.setAttribute("version", "1.0");
		root.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(root);
		org.w3c.dom.Element itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		root.appendChild(itemKey);
		itemKey = doc.createElement("statusString");
		if (statusString == null || "".equals(statusString.trim())) {
			I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
			itemKey.appendChild(doc.createTextNode(msgResourceService.getMessage(statsusCode)));
		} else {
			itemKey.appendChild(doc.createTextNode(statusString));
		}
		root.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * 组织返回的xml报文 返回的xml没有ResponseStatus， 只map
	 * 
	 * @Title: responseXml
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	public static String responseXml(String root, LinkedHashMap<String, Object> map) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		org.w3c.dom.Element rootE = doc.createElement(root);
		rootE.setAttribute("version", "1.0");
		rootE.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(rootE);
		org.w3c.dom.Element itemKey;
		for (Entry<String, Object> entry : map.entrySet()) {
			itemKey = doc.createElement(entry.getKey());
			itemKey.appendChild(doc.createTextNode((String) entry.getValue()));
			rootE.appendChild(itemKey);
		}
		return xml2String(doc);
	}

	/**
	 * 组织返回的xml报文 返回的xml有ResponseStatus还有map
	 * 
	 * @Title: responseXml
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	public static String responseXml(String statsusCode, String statusString, String root,
			LinkedHashMap<String, Object> map) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		org.w3c.dom.Element rootE = doc.createElement(root);
		rootE.setAttribute("version", "1.0");
		rootE.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(rootE);
		org.w3c.dom.Element itemKey;
		if (statsusCode.equals("0")) {
			for (Entry<String, Object> entry : map.entrySet()) {
				itemKey = doc.createElement(entry.getKey());
				itemKey.appendChild(doc.createTextNode(entry.getValue() == null ? " " : (String) entry.getValue()));
				rootE.appendChild(itemKey);
			}
		}
		org.w3c.dom.Element status = doc.createElement("ResponseStatus");
		rootE.appendChild(status);
		itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		status.appendChild(itemKey);
		itemKey = doc.createElement("statusString");
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		itemKey.appendChild(doc.createTextNode(msgResourceService.getMessage(statsusCode)));
		status.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * 组织返回的xml报文 返回的xml有ResponseStatus还有map
	 * 
	 * @Title: responseXml
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author weibin
	 * @throws Exception 
	 * @date 2015年12月09日
	 */
	public static String responseXml(String statsusCode, String statusString, String root, Map<String, Object> map)
			throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		org.w3c.dom.Element rootE = doc.createElement(root);
		rootE.setAttribute("version", "1.0");
		rootE.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(rootE);
		org.w3c.dom.Element itemKey;
		if (statsusCode.equals("0")) {
			if (map != null) {
				createElementByMap(doc, rootE, map);
			}

		}
		org.w3c.dom.Element status = doc.createElement("ResponseStatus");
		rootE.appendChild(status);
		itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		status.appendChild(itemKey);
		itemKey = doc.createElement("statusString");
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		itemKey.appendChild(doc.createTextNode(msgResourceService.getMessage(statsusCode)));
		status.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * 添加map里面的map值
	 * @param doc
	 * @param root
	 * @param map
	 * @return
	 */
	private static org.w3c.dom.Element createElementByMap(org.w3c.dom.Document doc, org.w3c.dom.Element root,
			Map<String, Object> map) throws Exception {
		try {
			org.w3c.dom.Element itemKey = null;
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() instanceof Map) {
					Map<String, Object> mapOther = (Map<String, Object>) entry.getValue();
					itemKey = doc.createElement(entry.getKey());
					createElementByMap(doc, itemKey, mapOther);
					root.appendChild(itemKey);
				} else {
					itemKey = doc.createElement(entry.getKey());
					itemKey.appendChild(doc.createTextNode((String) entry.getValue()));
					root.appendChild(itemKey);
				}
			}
			return root;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

	}

	/**
	 * 组织返回的xml报文 返回的xml有ResponseStatus,有list,有map
	 * 
	 * @Title: responseXml
	 * @Description: TODO
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	public static String responseXml(String statsusCode, String statusString, String root,
			ArrayList<LinkedHashMap<String, Object>> list, LinkedHashMap<String, Object> linkedMap) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		String[] roots = root.split("/");
		org.w3c.dom.Element rootE = doc.createElement(roots[0]);
		rootE.setAttribute("version", "1.0");
		rootE.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(rootE);
		org.w3c.dom.Element itemKey;
		if (statsusCode.equals("0")) {
			for (Entry<String, Object> entry : linkedMap.entrySet()) {
				itemKey = doc.createElement(entry.getKey());
				itemKey.appendChild(doc.createTextNode((String) entry.getValue()));
				rootE.appendChild(itemKey);
			}
			itemKey = doc.createElement(roots[1]);
			LinkedHashMap<String, Object> map;
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Element itemKey1 = doc.createElement(roots[2]);
				map = list.get(i);
				for (Entry<String, Object> entry : map.entrySet()) {
					org.w3c.dom.Element itemKey2 = doc.createElement(entry.getKey());
					itemKey2.appendChild(doc.createTextNode((String) entry.getValue()));
					itemKey1.appendChild(itemKey2);
				}
				itemKey.appendChild(itemKey1);
			}
			rootE.appendChild(itemKey);
		}
		org.w3c.dom.Element status = doc.createElement("ResponseStatus");
		rootE.appendChild(status);
		itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		status.appendChild(itemKey);
		itemKey = doc.createElement("statusString");
		itemKey.appendChild(doc.createTextNode(statusString));
		status.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * 组织返回的xml报文 返回的xml有ResponseStatus,有list
	 * 
	 * @Title: responseXml
	 * @param @param statsusCode
	 * @param @param statusString
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	public static String responseXml(String statsusCode, String statusString, String root,
			ArrayList<LinkedHashMap<String, Object>> list) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		String[] roots = root.split("/");
		org.w3c.dom.Element rootE = doc.createElement(roots[0]);
		rootE.setAttribute("version", "1.0");
		rootE.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(rootE);
		org.w3c.dom.Element itemKey;
		if (statsusCode.equals("0")) {
			itemKey = doc.createElement(roots[1]);
			LinkedHashMap<String, Object> map;
			for (int i = 0; i < list.size(); i++) {
				org.w3c.dom.Element itemKey1 = doc.createElement(roots[2]);
				map = list.get(i);
				for (Entry<String, Object> entry : map.entrySet()) {
					org.w3c.dom.Element itemKey2 = doc.createElement(entry.getKey());
					itemKey2.appendChild(doc.createTextNode((String) entry.getValue()));
					itemKey1.appendChild(itemKey2);
				}
				itemKey.appendChild(itemKey1);
			}
			rootE.appendChild(itemKey);

		}
		org.w3c.dom.Element status = doc.createElement("ResponseStatus");
		rootE.appendChild(status);
		itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		status.appendChild(itemKey);
		itemKey = doc.createElement("statusString");
		itemKey.appendChild(doc.createTextNode(statusString));
		status.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * 将xml对象转换为String
	 * 
	 * @Title: xml2String
	 * @Description: 将xml对象转换为String
	 * @param @param doc
	 * @param @return
	 * @param @throws Exception (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年9月11日
	 */
	private static String xml2String(org.w3c.dom.Document doc) {
		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		} catch (TransformerFactoryConfigurationError e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		} catch (TransformerException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		return writer.getBuffer().toString();
	}

	public static void main(String[] args) throws Exception {
		// LinkedHashMap<String, Object> map = new LinkedHashMap<String,
		// Object>();
		// map.put("name", "bob");
		// map.put("age", "21");
		// ArrayList<LinkedHashMap<String, Object>> list = new
		// ArrayList<LinkedHashMap<String, Object>>();
		// list.add(map);
		// list.add(map);
		// log.info(responseXml("0", "0", "sklight/students/student",
		// list, map));
		// String str =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><httpCmd><httpVersion>HTTP/1.1</httpVersion><responseCode>200</responseCode><responseString>OK</responseString></httpCmd><headers><header><name>User-Agent</name><value> PSIAConformanceTest</value></header><header><name>Set-Cookie</name><value> user=it315</value></header><header><name>Content-Type</name><value> application/xml; charset=utf-8</value></header><header><name>Content-Length</name><value> 240</value></header><header><name>Date</name><value> Thu, 01 Jan 1970 04</value></header><header><name>Server</name><value> lighttpd/1.4.35</value></header></headers><body>Cjw/eG1sIHZlcnNpb249IjEuMCIgZW5jb2Rpbmc9IlVURi04IiA/PjxSZXNwb25zZVN0YXR1cyB2ZXJzaW9uPSIxLjAiIHhtbG5zPSJ1cm46cHNpYWxsaWFuY2Utb3JnIj48cmVxdWVzdFVSTD4vcHNpYS9zdHJlYW1pbmcvY2hhbm5lbHMvdGVzdDwvcmVxdWVzdFVSTD48c3RhdHVzQ29kZT40PC9zdGF0dXNDb2RlPjxzdGF0dXNTdHJpbmc+SW52YWxpZCBPcGVyYXRpb248L3N0YXR1c1N0cmluZz48L1Jlc3BvbnNlU3RhdHVzPg0K</body></response>\n";
		// str =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><httpCmd><method>POST</method><uri>/PSIA/Streaming/channels/MAIN</uri><httpVersion>HTTP/1.1</httpVersion></httpCmd><headers><header><name>host</name><value>192.168.141.50:8080</value></header><header><name>user-agent</name><value>Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0</value></header><header><name>accept</name><value>text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8</value></header><header><name>accept-language</name><value>null</value></header><header><name>accept-encoding</name><value>gzip, deflate</value></header><header><name>content-length</name><value>15</value></header><header><name>content-type</name><value>text/plain; charset=UTF-8</value></header><header><name>connection</name><value>keep-alive</value></header><header><name>pragma</name><value>no-cache</value></header><header><name>cache-control</name><value>no-cache</value></header></headers><body>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8+PFJlc3BvbnNlU3RhdHVzIHZlcnNpb249IjEuMCIgeG1sbnM9InVybjpwc2lhbGxpYW5jZS1vcmciPjxyZXF1ZXN0VVJMPi9wc2lhL3N0cmVhbWluZy9jaGFubmVscy9tYWluPC9yZXF1ZXN0VVJMPjxzdGF0dXNDb2RlPjQ8L3N0YXR1c0NvZGU+PHN0YXR1c1N0cmluZz5JbnZhbGlkIE9wZXJhdGlvbjwvc3RhdHVzU3RyaW5nPjwvUmVzcG9uc2VTdGF0dXM+DQo=</body></response>";
		// Map map = getElementMapCommand(str);
		// System.out.print(map.get("body") + "@@@@@@@@@@@@@@" + ((Map) map.get("headerMap")).get("Content-Length"));
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map.put("name", "bob");
		map.put("age", "21");
		map1.put("name", "nihao");
		map1.put("url", "kkk");
		map2.put("name", "wo");
		map2.put("age", "12");
		map1.put("geshou", map2);
		map.put("music", map1);
		log.info(responseXml("0", "0", "app", map));
	}

	/**
	 * 接入子系统响应的消息信息
	 * 
	 * @param xml
	 * @param xsd
	 * @return 返回XML操作对象
	 * 
	 * @throws ManagerException
	 */
	public static String responseXml(String statsusCode) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LoggerUtil.error("xml error:" + e, XmlUtil.class);
			return null;
		}
		org.w3c.dom.Document doc = builder.newDocument();
		// 创建根元素
		org.w3c.dom.Element root = doc.createElement("ResponseStatus");
		root.setAttribute("version", "1.0");
		root.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(root);
		org.w3c.dom.Element itemKey = doc.createElement("statusCode");
		itemKey.appendChild(doc.createTextNode(statsusCode));
		root.appendChild(itemKey);
		return xml2String(doc);
	}

	/**
	 * <返回正确信息(带数据)格式化to xml>
	 * 
	 * @param strRoot
	 *            [根节点名称]
	 * @param backMap
	 *            [子节点map数据]
	 * @return [返回结果]
	 */
	public static final String mapToXmlRight(String sRoot, Map<String, Object> backMap) {
		Map<String, Object> rightMap = new LinkedHashMap<String, Object>();
		rightMap.put("statusCode", "0");
		rightMap.put("statusString", "0");
		backMap.put("ResponseStatus", rightMap);
		return mapToXml(sRoot, backMap);
	}
	
	/**
	 * <返回正确信息（不带数据）格式化to xml>
	 * 
	 * @return [返回结果]
	 */
	public static final String mapToXmlRight() {
		Map<String, Object> rightMap = new LinkedHashMap<String, Object>();
		rightMap.put("statusCode", "0");
		rightMap.put("statusString", "0");
		return mapToXml("ResponseStatus", rightMap);
	}

	/**
	 * <返回错误信息格式化to xml>
	 * 
	 * @param errorCode
	 *            [错误码]
	 * @return [返回结果]
	 */
	public static final String mapToXmlError(String errorCode) {
		Map<String, Object> errorMap = new LinkedHashMap<String, Object>();
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		errorMap.put("statusCode", errorCode);
		errorMap.put("statusString", msgResourceService.getMessage(errorCode));
		return mapToXml("ResponseStatus", errorMap);
	}

	/**
	 * <返回错误信息格式化to xml, 带根节点>
	 * 
	 * @param errorCode
	 *            [错误码]
	 * @return [返回结果]
	 */
	public static final String mapToXmlError(String sRoot, String errorCode) {
		Map<String, Object> backMap = new LinkedHashMap<String, Object>();
		Map<String, Object> errorMap = new LinkedHashMap<String, Object>();
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		errorMap.put("statusCode", errorCode);
		errorMap.put("statusString", msgResourceService.getMessage(errorCode));
		backMap.put("ResponseStatus", errorMap);
		return mapToXml(sRoot, backMap);
	}

	/**
	 * <返回信息格式化map convert to xml>
	 * 
	 * @param strRoot
	 *            [根节点名称]
	 * @param map
	 *            [子节点map]
	 * @return [返回结果]
	 */
	private static final String mapToXml(String sRoot, Map<String, Object> backMap) {
		// 根节点组装
		org.dom4j.Document document = DocumentHelper.createDocument();
		org.dom4j.Element root = document.addElement(sRoot, "urn:skylight");
		root.addAttribute("version", "1.0");
		mapForToXML(root, backMap);
		return ((org.dom4j.Node) document).asXML();
	}

	/**
	 * <循环输出mapForOutput> <递归实现>
	 * 
	 * @param root
	 *            [xml节点]
	 * @param map
	 *            [map集合]
	 */
	@SuppressWarnings("unchecked")
	private static void mapForToXML(org.dom4j.Element root, Map<String, Object> map) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Entry<String, Object> entry : map.entrySet()) {
			// 字符串
			if (entry.getValue() instanceof String) {
				root.addElement(entry.getKey()).addText(String.valueOf(entry.getValue()));
			}
			// 数字
			else if (entry.getValue() instanceof Number) {
				root.addElement(entry.getKey()).addText(String.valueOf(entry.getValue()));
			}
			// 时间
			else if (entry.getValue() instanceof Timestamp) {
				root.addElement(entry.getKey()).addText(sdf.format(entry.getValue()));
			}
			// map集合
			else if (entry.getValue() instanceof Map) {
				org.dom4j.Element node = root.addElement(entry.getKey());
				Map<String, Object> subMap = (Map<String, Object>) entry.getValue();
				mapForToXML(node, subMap);
			}
			// list集合
			else if (entry.getValue() instanceof List) {
				List<Map<String, Object>> subList = (List<Map<String, Object>>) entry.getValue();
				if (subList != null && subList.size() > 0) {
					org.dom4j.Element node = root.addElement(entry.getKey());
					for (Map<String, Object> subMap : subList) {
						// list中的内容是单个map集合
						mapForToXML(node, subMap);
					}
				} else {
					root.addElement(entry.getKey()).addText("");
				}
			}
			// 最后（比如empty的情况）
			else {
				root.addElement(entry.getKey()).addText(String.valueOf(entry.getValue()));
			}
		}
	}

	/**
	 * <初始化请求参数xml to map> 附带校验
	 * 
	 * @param reqXml
	 *            [请求xml]
	 * @return
	 */
	public static Map<String, Object> getRequestXmlParam(String reqXml) throws Exception {
		try {
			// 返回参数map
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			Document document = DocumentHelper.parseText(reqXml);
			Element root = document.getRootElement();

			// 循环遍历获取xml参数
			Iterator<?> it = root.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				paraMap.putAll(listNodes(element));
			}
			return paraMap;
		} catch (BusinessException e) {
			throw e;
		} catch (DocumentException e) {
			log.info("DocumentException by inputStream body xml is: " + reqXml);
			throw new BusinessException("0x50000027", e);
		}
	}

	/**
	 * <初始化请求参数xml to map> 附带校验
	 * 
	 * @param HttpRequest
	 *            [HttpServlet请求]
	 * @return
	 */
	public static Map<String, Object> getRequestXmlParam(HttpServletRequest req) throws Exception {
		Scanner scanner = null;
		try {
			// 返回参数map
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			scanner = new Scanner(req.getInputStream(), "UTF-8");
			String inputStr = scanner.useDelimiter("\\A").next();
			scanner.close();

			Document document = DocumentHelper.parseText(inputStr);
			Element root = document.getRootElement();

			// 循环遍历获取xml参数
			Iterator<?> it = root.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				paraMap.putAll(listNodes(element));
			}
			return paraMap;
		} catch (BusinessException e) {
			throw e;
		} catch (DocumentException e) {
			log.info("DocumentException by inputStream body xml is: " + scanner.useDelimiter("\\A").next());
			throw new BusinessException("0x50000027", e);
		} catch (IOException e) {
			log.info("IOException by inputStream body xml is: " + scanner.useDelimiter("\\A").next());
			throw new BusinessException("0x50000026", e);
		} catch (NoSuchElementException e) {
			log.info("NoSuchElementException by inputStream body xml is: " + scanner.useDelimiter("\\A").next());
			throw new BusinessException("0x50000028", e);
		}
	}

	/**
	 * 专用于解析6.2 APP用户设置过滤条件查询上报事件（告警）信息接口报文
	 * 
	 * @Title: getRequestXmlParamEvent
	 * @Description: TODO
	 * @param @param req
	 * @param @return (参数说明)
	 * @return Map<String,Object> (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年10月19日
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map> getRequestXmlParamEvent(HttpServletRequest req) {
		List<Map> list = new ArrayList<Map>();
		// 返回参数map
		HashMap<String, Object> paraMap = new HashMap<String, Object>();

		try {
			Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
			String inputStr = scanner.useDelimiter("\\A").next();
			scanner.close();

			Document document = DocumentHelper.parseText(inputStr);
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				String userId = element.getName();
				if ("userId".equalsIgnoreCase(userId)) {
					paraMap.put("userId", element.getTextTrim());
				}
				Iterator it1 = element.elementIterator();
				while (it1.hasNext()) {
					Element element1 = (Element) it1.next();
					List<Element> childNodeList = element1.elements();
					paraMap.put(childNodeList.get(0).getTextTrim(), childNodeList.get(1).getTextTrim());
					list.add(paraMap);
				}
			}
		} catch (SecurityException e) {
			LoggerUtil.error("0x50000030", LoggerUtil.class);
			paraMap.put("errorString", "0x50000030");
		} catch (IOException e) {
			LoggerUtil.error("0x50000026", LoggerUtil.class);
			paraMap.put("errorString", "0x50000026");
		} catch (DocumentException e) {
			LoggerUtil.error("0x50000028", LoggerUtil.class);
			paraMap.put("errorString", "0x50000027");
		}

		return list;
	}

	/**
	 * <数据类型转换，目标:String类型，附带为空校验>
	 * 
	 * @param obj
	 *            [参数对象]
	 * @param isNull
	 *            [是否可以为空]
	 * @return
	 * @throws Exception
	 */
	public static String convertToString(Object obj, boolean isNull) throws Exception {
		if (isNull) {
			if (obj == null) {
				return null;
			} else {
				return obj.toString();
			}

		} else {
			if (obj == null) {
				throw new BusinessException("0x50000029");
			} else {
				if (obj.toString().isEmpty()) {
					throw new BusinessException("0x50000029");
				}
			}
			return obj.toString();
		}
	}
	
	/**
	 * <数据类型转换，目标:long类型，附带为空校验，（选择）附带默认值>
	 * 
	 * @param obj
	 *            [参数对象]
	 * @param isNull
	 *            [是否可以为空]
	 * @param defaultLong
	 *            [可为空时的默认值long]
	 * @return
	 * @throws Exception
	 */
	public static long convertToLong(Object obj, boolean isNull) throws Exception
	{
		return convertToLong(obj, isNull, 0L);
	}
	public static long convertToLong(Object obj, boolean isNull, long defaultLong) throws Exception
	{
		String value = convertToString(obj, isNull);
		if (value != null && !value.isEmpty())
		{
			return Long.valueOf(value);
		}
		return defaultLong;
	}
	
	/**
	 * <数据类型转换，目标:int类型，附带为空校验，（选择）附带默认值>
	 * 
	 * @param obj
	 *            [参数对象]
	 * @param isNull
	 *            [是否可以为空]
	 * @param defaultInt
	 *            [可为空时的默认值int]
	 * @return
	 * @throws Exception
	 */
	public static int convertToInt(Object obj, boolean isNull) throws Exception
	{
		return convertToInt(obj, isNull, 0);
	}
	public static int convertToInt(Object obj, boolean isNull, int defaultInt) throws Exception
	{
		String value = convertToString(obj, isNull);
		if (value != null && !value.isEmpty())
		{
			return Integer.valueOf(value);
		}
		return defaultInt;
	}

	/**
	 * <遍历当前节点元素下面的所有(元素的)子节点>
	 * 
	 * @param element
	 *            [节点]
	 * @param paraMap
	 *            [校验map]
	 * @return [返回查询结果]
	 */
	private static Map<String, Object> listNodes(Element element) throws Exception {
		// 返回结果
		Map<String, Object> subMap = new LinkedHashMap<String, Object>();

		// 遍历节点获取需要的临时数据
		String nodeName = getNodeNameAndAttr(element);
		List<Element> childNodeList = element.elements();
		int count = childNodeList.size();

		// 无子元素，则赋值
		if (count == 0) {
			// 获取校验（正则）数组
			Pattern[] patternArr = XmlElementValidator.checkParaMap.get(nodeName);

			// 若校验，则必须符合正则表达式
			if (patternArr != null && patternArr.length > 0) {
				for (Pattern pattern : patternArr) {
					if (StringUtil.pattern(pattern, element.getTextTrim())) {
						// 多次赋值几乎无性能影响
						subMap.put(nodeName, element.getTextTrim());
					} else {
						// 记录后台参数校验日志
						log.error("error：" + "[" + nodeName + "=" + element.getTextTrim() + "]" + "[regex = "
								+ pattern.toString() + "]");
						throw new BusinessException("0x50000030");
					}
				}
			}
			// 若没有匹配到校验规则
			else {
				if (nodeName.endsWith("List")) {
					subMap.put(nodeName, new ArrayList<String>());
				} else {
					subMap.put(nodeName, element.getTextTrim());
				}

			}
		}
		// 有子元素，则遍历
		else {
			List<Object> tempList = new ArrayList<Object>();
			Map<String, Object> tempMap = new HashMap<String, Object>();

			// 判断子元素的节点名称是否相同。
			String firstName = getNodeNameAndAttr(childNodeList.get(0));
			String lastName = getNodeNameAndAttr(childNodeList.get(count - 1));
			boolean flag = firstName.equals(lastName);

			// 循环递归
			for (Element chileElement : childNodeList) {
				// 只有1个子元素，如果当前元素名称为XXXXList时，则以list形式存储；如果不是，则以map形式存储
				if (count == 1) {
					if (nodeName.endsWith("List")) {
						tempList.add(listNodes(chileElement));
					} else {
						tempMap.putAll(listNodes(chileElement));
					}
				}
				// 有大于1个子元素时，如果子元素名称相同，则以list形式存储；如果不同，则以map形式存储
				else {
					if (flag) {
						tempList.add(listNodes(chileElement));
					} else {
						tempMap.putAll(listNodes(chileElement));
					}
				}
			}

			// 组合返回结果
			if (tempMap.size() > 0) {
				subMap.put(nodeName, tempMap);
			}

			if (tempList.size() > 0) {
				subMap.put(nodeName, tempList);
			}
		}
		return subMap;
	}

	private static String getNodeNameAndAttr(Element element) {
		String nodeName = element.getName();
		if (element.attributeCount() > 0) {
			Iterator it = element.attributeIterator();
			nodeName += ":";
			while (it.hasNext()) {
				Attribute noteAttribute = (Attribute) it.next();
				nodeName += noteAttribute.getName() + "='" + noteAttribute.getValue() + "'";
				if (it.hasNext()) {
					nodeName += '&';
				}
			}
		}
		return nodeName;
	}

	/**
	 * <初始化请求参数xml to map> 可选择是否进行正则校验
	 * 
	 * @param HttpRequest
	 *            [HttpServlet请求]
	 * @param Boolean   为true,进行校验；为false,不进行校验 
	 * @return
	 */
	public static Map<String, Object> getRequestXmlParam(HttpServletRequest req, Boolean isCheck) throws Exception {
		try {
			// 返回参数map
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
			String inputStr = scanner.useDelimiter("\\A").next();
			scanner.close();

			Document document = DocumentHelper.parseText(inputStr);
			Element root = document.getRootElement();

			// 循环遍历获取xml参数
			Iterator<?> it = root.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				paraMap.putAll(listNodes(element, isCheck));
			}
			return paraMap;
		} catch (BusinessException e) {
			throw e;
		} catch (DocumentException e) {
			throw new BusinessException("0x50000027", e);
		} catch (IOException e) {
			throw new BusinessException("0x50000026", e);
		} catch (NoSuchElementException e) {
			throw new BusinessException("0x50000028", e);
		}
	}

	/**
	 * <遍历当前节点元素下面的所有(元素的)子节点>
	 * 
	 * @param element
	 *            [节点]
	 * @param Boolean
	 *            [为true,进行校验；为false,不进行校验]
	 * @return [返回查询结果]
	 */
	private static Map<String, Object> listNodes(Element element, Boolean isCheck) throws Exception {
		// 返回结果
		Map<String, Object> subMap = new LinkedHashMap<String, Object>();

		// 遍历节点获取需要的临时数据
		String nodeName = getNodeNameAndAttr(element);
		List<Element> childNodeList = element.elements();
		int count = childNodeList.size();

		// 无子元素，则赋值
		if (count == 0) {
			if (true == isCheck) {
				// 获取校验（正则）数组
				Pattern[] patternArr = XmlElementValidator.checkParaMap.get(nodeName);

				// 若校验，则必须符合正则表达式
				if (patternArr != null && patternArr.length > 0) {
					for (Pattern pattern : patternArr) {
						if (StringUtil.pattern(pattern, element.getTextTrim())) {
							// 多次赋值几乎无性能影响
							subMap.put(nodeName, element.getTextTrim());
						} else {
							// 记录后台参数校验日志
							log.error("error：" + "[" + nodeName + "=" + element.getTextTrim() + "]" + "[regex = "
									+ pattern.toString() + "]");
							throw new BusinessException("0x50000030");
						}
					}
				}
				// 若没有匹配到校验规则
				else {
					if (nodeName.endsWith("List")) {
						subMap.put(nodeName, new ArrayList<String>());
					} else {
						subMap.put(nodeName, element.getTextTrim());
					}

				}

			}

			if (false == isCheck) {
				if (nodeName.endsWith("List")) {
					subMap.put(nodeName, new ArrayList<String>());
				} else {
					subMap.put(nodeName, element.getTextTrim());
				}
			}

		}
		// 有子元素，则遍历
		else {
			List<Object> tempList = new ArrayList<Object>();
			Map<String, Object> tempMap = new HashMap<String, Object>();

			// 判断子元素的节点名称是否相同。
			String firstName = getNodeNameAndAttr(childNodeList.get(0));
			String lastName = getNodeNameAndAttr(childNodeList.get(count - 1));
			boolean flag = firstName.equals(lastName);

			// 循环递归
			for (Element chileElement : childNodeList) {
				// 只有1个子元素，如果当前元素名称为XXXXList时，则以list形式存储；如果不是，则以map形式存储
				if (count == 1) {
					if (nodeName.endsWith("List")) {
						tempList.add(listNodes(chileElement, isCheck));
					} else {
						tempMap.putAll(listNodes(chileElement, isCheck));
					}
				}
				// 有大于1个子元素时，如果子元素名称相同，则以list形式存储；如果不同，则以map形式存储
				else {
					if (flag) {
						tempList.add(listNodes(chileElement, isCheck));
					} else {
						tempMap.putAll(listNodes(chileElement, isCheck));
					}
				}
			}

			// 组合返回结果
			if (tempMap.size() > 0) {
				subMap.put(nodeName, tempMap);
			}

			if (tempList.size() > 0) {
				subMap.put(nodeName, tempList);
			}
		}
		return subMap;
	}
}