package com.skl.cloud.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlNodeUtil
{
	/***
	 * 获取xml的NodeList
	 * 
	 * @param inputstream
	 * @return
	 * @throws Exception
	 */
	public static NodeList getXmlNodeList(InputStream inputstream) throws Exception
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
		Document document = builder.parse(inputstream);
		Element root = document.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		return nodeList;
	}

	/**
	 * 组装Xml文件
	 * 
	 * @throws Exception
	 */
	public static String createXml(Map<String, String> streamMap) throws Exception
	{
		String inputXml = "";
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
		Document doc = builder.newDocument();
		// 创建根元素
		Element root = doc.createElement("server");
		doc.appendChild(root);
		for (Map.Entry<String, String> entry : streamMap.entrySet())
		{
			Element itemKey = doc.createElement(entry.getKey());
			Text textValue = doc.createTextNode(entry.getValue());
			itemKey.appendChild(textValue);
			root.appendChild(itemKey);
		}
		inputXml = docToString(doc);
		return inputXml;
	}

	/***
	 * doc到xml字符串
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String docToString(Document doc) throws Exception
	{
		String retStr = "";
		OutputFormat outputFormat = new OutputFormat(doc, "utf-8", true);
		StringWriter stringWriter = new StringWriter();
		XMLSerializer xmlSerializer = new XMLSerializer(stringWriter, outputFormat);
		xmlSerializer.asDOMSerializer();
		xmlSerializer.serialize(doc.getDocumentElement());
		System.out.println(stringWriter.toString());
		retStr = stringWriter.toString();
		return retStr;
	}
}
