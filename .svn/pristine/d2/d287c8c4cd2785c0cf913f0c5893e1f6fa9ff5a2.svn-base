package com.skl.cloud.foundation.remote.ipc;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.skl.cloud.common.xml.W3CUtils;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.foundation.remote.exception.CanceledOperationIPCRemoteException;

public class IPCRemoteResult extends XRemoteResult {
	
	public IPCRemoteResult(IPCRemoteContext context) {
		super(context);
	}

	/**
	 * 检查返回状态
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public void checkReturnStatus() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		initDocument();
		NodeList nodeList = doc.getElementsByTagName("ResponseStatus");
		if(nodeList.getLength() == 0) {
			// 无状态节点，默认成功
			return;
		}
		Element statusElement = (Element)nodeList.item(0);
		Node codeNode = W3CUtils.findNode("./statusCode", statusElement);
		Node msgNode = W3CUtils.findNode("./statusString", statusElement);
		String code = codeNode.getTextContent();
		
		if (StringUtils.contains(code, "0x")) {
			code = code.substring(code.indexOf("0x") + 2);
		}
		
		String msg = msgNode.getTextContent();
		//将十六进制字符串转化为十进制整形
		Integer statusCode = Integer.parseInt(code, 16);
		if(statusCode == 40) {
			throw new CanceledOperationIPCRemoteException();
		}
		else if(statusCode != 0) {
			throw new SKLRemoteException(statusCode, msg);
		}
	}
}
