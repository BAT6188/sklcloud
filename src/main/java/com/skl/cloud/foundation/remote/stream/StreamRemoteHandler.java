package com.skl.cloud.foundation.remote.stream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.util.TimeoutController.TimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
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
import com.skl.cloud.foundation.remote.HttpRemoteContext;
import com.skl.cloud.foundation.remote.HttpSKLRemoteHandler;
import com.skl.cloud.foundation.remote.RemoteContext;
import com.skl.cloud.foundation.remote.RemoteResult;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.util.config.Configuration;

public class StreamRemoteHandler extends HttpSKLRemoteHandler {
    private static Logger logger = LoggerFactory.getLogger(StreamRemoteHandler.class);

    private static final ContentType XML_CONTENT_TYPE = ContentType.create("application/xml", Consts.UTF_8);
    private static final String MOCK_PORT_KEY = "ipc.mock.port";
    private static final String MOCK_HOST_KEY = "ipc.mock.host";

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends RemoteContext> T newRemoteContext() throws SKLRemoteException {
        return (T) (new StreamRemoteContext());
    }

    @Override
    protected void concreteRemoteContext(RemoteContext context, Method method, Object[] paramValues)
            throws SKLRemoteException {
        super.concreteRemoteContext(context, method, paramValues);
        StreamRemoteContext streamRemoteContext = (StreamRemoteContext) context;
        StreamRemote streamRemote = context.getAnnotation(StreamRemote.class);
        streamRemoteContext.setHttpMethod(streamRemote.method());
        streamRemoteContext.setTimeout(streamRemote.timeout());
        streamRemoteContext.setRootName(streamRemote.rootName());
        streamRemoteContext.setXmlns(streamRemote.xmln());
        streamRemoteContext.setUri(streamRemote.uri());
        if(StringUtils.isBlank(streamRemote.streamType())) {
            throw new IllegalArgumentException("not specify the stream type");
        }
        streamRemoteContext.setStreamType(streamRemote.streamType());

    }

    @Override
    protected HttpRequestBase concreteRequest(HttpRemoteContext context) throws Exception {
        StreamRemoteContext streamRemoteContext = (StreamRemoteContext) context;
        initUrl(streamRemoteContext);
        HttpPost post = new HttpPost(streamRemoteContext.getUrl());
        post.setEntity(concreteRequestEntity(streamRemoteContext));
        return post;
    }

    // construct the response for by http remote
    @Override
    protected Object resolveResponse(CloseableHttpResponse response, HttpRemoteContext context) throws Exception {
        logger.info(response.toString());
        HttpEntity entity = response.getEntity();
        logger.info(entity.toString());
        InputStream inputStream = entity.getContent();

        String reponseBody = IOUtils.read(inputStream);
        // StreamResponse streamResponse = JAXBUtils.convertToObject(inputStream, StreamResponse.class);
        StreamRemoteResult streamRemoteResult = new StreamRemoteResult((StreamRemoteContext) context);

        // body信息
        // String body = streamResponse.getBody();
        streamRemoteResult.setData(reponseBody.getBytes());

        Class<?> resultType = context.getResultType();
        // 当返回流或字节数组时不需要检查返回状态
        if (resultType.equals(byte[].class)) {
            return streamRemoteResult.getData();
        } else if (resultType.equals(InputStream.class)) {
            return streamRemoteResult.getResultInputStream();
        }
        streamRemoteResult.checkReturnStatus();
        if (resultType.equals(String.class)) {
            return streamRemoteResult.getResultText();
        } else if (resultType.equals(void.class)) {
            return null;
        } else if (RemoteResult.class.isAssignableFrom(resultType)) {
            return streamRemoteResult;
        } else {
            return streamRemoteResult.getResultObject(resultType);
        }
    }

    @Override
    protected void caughtException(RemoteContext context, Throwable e) {
        if (e instanceof TimeoutException) {
            logger.error("请求连接超时");
            throw new SKLRemoteException("request ipc time out");
        }
        super.caughtException(context, e);
    }

    @Override
    protected HttpEntity concreteRequestEntity(HttpRemoteContext context) throws Exception {
        StreamRemoteContext streamRemoteContext = (StreamRemoteContext) context;
        // body
        byte[] bodyData = getBodyData(streamRemoteContext);
        String encodedBody = new String(bodyData);
        HttpEntity httpEntity = new StringEntity(encodedBody, XML_CONTENT_TYPE);
        return httpEntity;
    }

    private byte[] getBodyData(StreamRemoteContext context) throws IOException, JAXBException, TransformerException {
        byte[] data = new byte[0];
        if (context.getRequestInputStream() != null) {
            InputStream is = context.getRequestInputStream();
            data = IOUtils.readByteArray(is);
        } else if (context.getRequestText() != null) {
            String text = context.getRequestText();
            data = text.getBytes(Charset.forName("UTF-8"));
        } else {
            String rootName = context.getRootName();
            JAXBGenerator jaxbGenerator = null;
            if (context.getRequestObject() != null) {
                Object rootObject = context.getRequestObject();
                jaxbGenerator = new JAXBGenerator(rootObject, rootName);
            }
            if (!context.getParamMap().isEmpty()) {
                if (jaxbGenerator == null && StringUtils.isBlank(rootName)) {
                    throw new IllegalArgumentException("not specify StreamRemote rootName");
                }
                if (jaxbGenerator == null) {
                    jaxbGenerator = new JAXBGenerator(rootName);
                }
                Map<String, Object> paramMap = context.getParamMap();
                if (!paramMap.isEmpty()) {
                    for (Entry<String, Object> entry : paramMap.entrySet()) {
                        String name = entry.getKey();
                        Object value = entry.getValue();
                        jaxbGenerator.addParam(name, value);
                    }
                }
            }
            if (jaxbGenerator != null) {
                jaxbGenerator.setAttribute("xmlns", context.getXmlns());
                jaxbGenerator.setAttribute("version", context.getVersion());
            }

            if (jaxbGenerator != null) {
                data = jaxbGenerator.convertToBytes();
            }
        }

        return data;
    }

    private void initUrl(StreamRemoteContext context) {
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
            // TODO finish stream mock
            url = "http://" + mockHost + ":" + mockPort + "/stream" + uri;
            // url = "http://localhost:8080/sklcloud/skl-cloud/cloud/stream/post/control";
        } else {
            StreamResourcesService streamResourcesService = BeanLocator.getBean(StreamResourcesService.class);
            // 通过stream type得到流子系统的ip地址和端口号。
            String prefixUrl = streamResourcesService.getStreamRemotePrefixUrl(context.getStreamType());
            if(null == prefixUrl) {
                throw new IllegalArgumentException("cannot not get prefixUrl for the stream type "+context.getStreamType());
            }
            url = prefixUrl + uri;
        }
        context.setUrl(url);
    }

}
