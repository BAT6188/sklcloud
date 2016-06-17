package com.skl.cloud.foundation.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.skl.cloud.common.xml.JAXBUtils;

public class XRemoteResult extends HttpRemoteResult {
	private static XPath XPATH = XPathFactory.newInstance().newXPath();
	protected Document doc;
	
	public XRemoteResult(HttpRemoteContext context) {
		super(context);
	}

	/**
	 * 获得整型值
	 * @param path
	 * @return
	 */
	public Integer getInteger(String path) {
		String content = getString(path);
		return content == null ? null : Integer.parseInt(content);
	}

	/**
	 * 获得字符串值
	 * @param path
	 * @return
	 */
	public String getString(String path) {
		Node node = findNode(doc.getDocumentElement(), path);
		return node == null ? null : node.getTextContent();
	}
	
	/**
	 * 获得statusCode值
	 * @param path
	 * @return
	 */
	public String getStatusCode() {
		return this.getString("/ResponseStatus/statusCode");
	}
	
	/**
	 * 获得statusString值
	 * @param path
	 * @return
	 */
	public String getStatusString() {
		return this.getString("/ResponseStatus/statusString");
	}

	/**
	 * 获得对象值
	 * @param path
	 * @param type
	 * @return
	 */
	public <T> T getObject(String path, Class<T> type) {
		Node node = findNode(doc.getDocumentElement(), path);
		return node == null ? null : convertToObject(node, type);
	}

	/**
	 * 获得返回对象
	 * @param type
	 * @return
	 */
	public <T> T getResultObject(Class<T> type) {
		if(doc == null) {
			throw new IllegalStateException("没有获取请求内容");
		}
		return (T)convertToObject(doc.getDocumentElement(), type);
	}

	protected void initDocument() throws SAXException, IOException, ParserConfigurationException {
		if(doc == null) {
		    DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder=factory.newDocumentBuilder();
	        Reader reader = new InputStreamReader(new ByteArrayInputStream(data), Charset.forName("UTF-8"));
	        doc=builder.parse(new InputSource(reader));
		}
	}

	protected Node findNode(Node root, String path) {
		try {
			return (Node) XPATH.evaluate(path, root, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException("xpath error", e);
		}
	}
	
	protected <T> T convertToObject(Node node, Class<T> type) {
		try {
			return JAXBUtils.convertToObject(node, type);
		} catch (JAXBException e) {
			throw new IllegalArgumentException("can't convert to " + type, e);
		}
	}
}
