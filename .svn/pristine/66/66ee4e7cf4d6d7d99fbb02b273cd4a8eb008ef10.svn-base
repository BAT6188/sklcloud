package com.skl.cloud.foundation.remote.ipc;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.TimeoutController.TimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.IOUtils;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.common.xml.JAXBGenerator;
import com.skl.cloud.common.xml.JAXBUtils;
import com.skl.cloud.exception.device.NoDeviceException;
import com.skl.cloud.foundation.remote.HttpRemoteContext;
import com.skl.cloud.foundation.remote.HttpSKLRemoteHandler;
import com.skl.cloud.foundation.remote.RemoteContext;
import com.skl.cloud.foundation.remote.RemoteResult;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.service.common.IPCInstructionCenterService;
import com.skl.cloud.util.config.Configuration;

public class IPCRemoteHandler extends HttpSKLRemoteHandler {
	private static Logger logger = LoggerFactory.getLogger("ipc-remote");
	
	private static final ContentType XML_CONTENT_TYPE = ContentType.create("application/xml", Consts.UTF_8);
	private static final String MOCK_PORT_KEY = "ipc.mock.port";
	private static final String MOCK_HOST_KEY = "ipc.mock.host";

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends RemoteContext> T newRemoteContext() throws SKLRemoteException {
		return (T)(new IPCRemoteContext());
	}
	
	@Override
	protected void concreteRemoteContext(RemoteContext context, Method method, Object[] paramValues)
			throws SKLRemoteException {
		super.concreteRemoteContext(context, method, paramValues);
		IPCRemoteContext ipcContext = (IPCRemoteContext)context;
		IPCRemote ipcRemote = context.getAnnotation(IPCRemote.class);
		String sn = (String)ipcContext.getParamValuePresent(SN.class);
		if(StringUtils.isBlank(sn)) {
			throw new IllegalArgumentException("not specify the IPC SN");
		}
		ipcContext.setHttpMethod(ipcRemote.method());
		ipcContext.setTimeout(ipcRemote.timeout());
		ipcContext.setSn(sn);
		ipcContext.setRootName(ipcRemote.rootName());
		ipcContext.setXmlns(ipcRemote.xmln());
		ipcContext.setUri(ipcRemote.uri());
		
		
	}

	@Override
	protected HttpRequestBase concreteRequest(HttpRemoteContext context) throws Exception {
		IPCRemoteContext ipcContext = (IPCRemoteContext)context;
		initUrl(ipcContext);
		HttpPost post = new HttpPost(ipcContext.getUrl());
		post.setEntity(concreteRequestEntity(ipcContext));
		return post;
	}


	@Override
	protected Object resolveResponse(CloseableHttpResponse response, HttpRemoteContext context) throws Exception {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		IPCResponse ipcResponse = JAXBUtils.convertToObject(inputStream, IPCResponse.class);
		IPCRemoteResult ipcResult = new IPCRemoteResult((IPCRemoteContext)context);
		// 头部信息
		List<IPCHeader> headers = ipcResponse.getHeaders();
		for (IPCHeader ipcHeader : headers) {
			ipcResult.addHeader(ipcHeader.getName(), ipcHeader.getValue());
		}
		// body信息
		String body = ipcResponse.getBody();
		byte[] bodyData = StringUtils.isEmpty(body) ? new byte[0] : Base64.decodeBase64(body);
		ipcResult.setData(bodyData);

		Class<?> resultType = context.getResultType();
		 // 当返回流或字节数组时不需要检查返回状态
		 if (resultType.equals(byte[].class)) {
			 return ipcResult.getData();
		 } else if(resultType.equals(InputStream.class)) {
			 return ipcResult.getResultInputStream();
		 } 
		 ipcResult.checkReturnStatus();
		 if(resultType.equals(String.class)) {
			 return ipcResult.getResultText();
		 } else if (resultType.equals(void.class)) {
			return null;
		} else if (RemoteResult.class.isAssignableFrom(resultType)) {
			return ipcResult;
		} else {
			return ipcResult.getResultObject(resultType);
		}
	}


	@Override
	protected void caughtException(RemoteContext context, Throwable e) {
		if(e instanceof TimeoutException) {
			logger.error("指令中心请求连接超时");
			throw new SKLRemoteException("request ipc time out");
		}
		super.caughtException(context, e);
	}
	
	
	@Override
	protected HttpEntity concreteRequestEntity(HttpRemoteContext context) throws Exception {
		IPCRemoteContext ipcContext = (IPCRemoteContext)context;
		IPCRequest ipcRequest = new IPCRequest();
		// httpInfo
		IPCHttpInfo httpInfo = new IPCHttpInfo();
		httpInfo.setHttpVersion(HttpVersion.HTTP_1_1.toString());
		httpInfo.setMethod(ipcContext.getHttpMethod().name());
		httpInfo.setUri(ipcContext.getUri());
		ipcRequest.setHttpInfo(httpInfo);
		
		DigestService digestService = BeanLocator.getBean(DigestService.class);
		String authorization = digestService.getIPCDigest(ipcContext.getSn());
		
		//context.setContentType(XML_CONTENT_TYPE);
		// header
		for (Header header : ipcContext.getHeaders()) {
			ipcRequest.addHeader(header.getName(), header.getValue());
		}
		// 加认证信息
		ipcRequest.addHeader("Authorization", authorization);
		ipcRequest.addHeader("connection", "Keep-Alive");
		ipcRequest.addHeader("user-agent", "Apache-HttpClient/4.3.1 (java 1.7)");
		ipcRequest.addHeader("host", "127.0.0.1");
		ipcRequest.addHeader("content-type", "text/plain; charset=UTF-8");
		
		// body
		byte[] bodyData = getBodyData(ipcContext);

		String encodedBody = Base64.encodeBase64String(bodyData);
		ipcRequest.setBody(encodedBody);
		
		String bd = new String(bodyData);
		ipcRequest.addHeader("content-length", String.valueOf(bd.length()));
//		HttpEntity temp = new StringEntity(encodedBody);
//		long length = temp.getContentLength();
//		ipcRequest.addHeader("content-length", String.valueOf(length));
//		//ipcRequest.addHeader("Transfer-Encoding", "chunked");
		
		
		String bodyXml = JAXBUtils.convertToXml(ipcRequest);
		HttpEntity httpEntity = new StringEntity(bodyXml, XML_CONTENT_TYPE);
		return httpEntity;
	}

	private byte[] getBodyData(IPCRemoteContext context) throws IOException, JAXBException, TransformerException {
		byte[] data = new byte[0];
		if(context.getRequestInputStream() != null) {
			InputStream is = context.getRequestInputStream();
			data = IOUtils.readByteArray(is);
		}
		else if(context.getRequestText() != null) {
			String text = context.getRequestText();
			data = text.getBytes(Charset.forName("UTF-8"));
		}
		else {
			String rootName = context.getRootName();
			JAXBGenerator jaxbGenerator = null;
			if(context.getRequestObject() != null) {
				Object rootObject = context.getRequestObject();
				jaxbGenerator = new JAXBGenerator(rootObject, rootName);
			}
			if(!context.getParamMap().isEmpty()) {
				if(jaxbGenerator == null && StringUtils.isBlank(rootName)) {
					throw new IllegalArgumentException("not specify IPCRemote rootName");
				}
				if(jaxbGenerator == null) {
					jaxbGenerator = new JAXBGenerator(rootName);
				}
				Map<String, Object> paramMap = context.getParamMap();
				if(!paramMap.isEmpty()) {
					for (Entry<String, Object> entry : paramMap.entrySet()) {
						String name = entry.getKey();
						Object value = entry.getValue();
						jaxbGenerator.addParam(name, value);
					}
				}
			}
			if(jaxbGenerator != null) {
				jaxbGenerator.setAttribute("xmlns", context.getXmlns());
				jaxbGenerator.setAttribute("version", context.getVersion());
			}


			if(jaxbGenerator != null) {
				data = jaxbGenerator.convertToBytes();
			}
		}
		
		return data;
	}

	private void initUrl(IPCRemoteContext context) {
		String uri = context.getUri();
		if (StringUtils.isEmpty(uri)) {
			throw new IllegalArgumentException("request uri is blank");
		}
		String url = uri;
		// 模拟IPC环境
        String mockPort = Configuration.getSystemProperty(MOCK_PORT_KEY);
        String mockHost = Configuration.getSystemProperty(MOCK_HOST_KEY);
        mockHost = StringUtils.isNotBlank(mockHost) ? mockHost : "localhost";
        if (StringUtils.isNotBlank(mockPort)) {
            url = "http://" + mockHost + ":" + mockPort + "/ipc" + uri;
        }
		else {
			String sn = context.getSn();
			IPCInstructionCenterService ipcInstructionCenterService = BeanLocator.getBean(IPCInstructionCenterService.class);
			// 通过Sn查询对应的IPC指令中心子系统的ip地址和端口号。
			Map<String, Object> ipcInstructionInfo = ipcInstructionCenterService.queryIPCInstructionInfo(sn);
			if (ipcInstructionInfo == null) {
				throw new NoDeviceException("can't find ipcInfo for SN[" + sn + "] in InstructionCenter");
			}
			String ip = (String) ipcInstructionInfo.get("Ip");
	    	// 不从数据库获取，写死在代码中
			int port = 29098;
            url = "http://" + ip + ":" + port + "/skl-cloud/appremote/" + context.getSn() + (uri.startsWith("/") ? "" : "/") + uri;
		}
		context.setUrl(url);
	}

}
