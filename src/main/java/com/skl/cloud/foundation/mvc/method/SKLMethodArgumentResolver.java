package com.skl.cloud.foundation.mvc.method;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.common.xml.Jaxb2Marshaller;
import com.skl.cloud.common.xml.W3CUtils;
import com.skl.cloud.exception.common.InvalidParameterException;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.foundation.mvc.method.annotation.Root;
import com.skl.cloud.foundation.mvc.method.annotation.Text;

public class SKLMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private static Logger logger = LoggerFactory.getLogger(SKLMethodArgumentResolver.class);
	
	private static final String ROOT_XML_NODE = "__root_xml_node";
	private static final String ROOT_JSON_NODE = "__root_json_node";

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	private Map<String, String> pathExtensionMapping = new HashMap<String, String>();

	public SKLMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.hasParameterAnnotation(Param.class) || parameter.hasParameterAnnotation(Root.class));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String path = urlPathHelper.getLookupPathForRequest(request);
		String filename = WebUtils.extractFullFilenameFromUrlPath(path);
		String extension = StringUtils.getFilenameExtension(filename);
		if (!pathExtensionMapping.containsKey(extension)) {
			throw new IllegalStateException("请求链接后缀[" + extension + "]不支持@Param或@Root");
		}
		String type = pathExtensionMapping.get(extension);
		if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(type, "xml")) {
			return resolveArgumentXml(parameter, mavContainer, webRequest, binderFactory);
		} else if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(type, "json")) {
			return resolveArgumentJson(parameter, mavContainer, webRequest, binderFactory);
		}
		return null;
	}

	private Object resolveArgumentXml(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws ParserConfigurationException,
			SAXException, IOException, JAXBException, XPathExpressionException, TransformerException {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		// 获得xml的根元素
		Document doc = getDocument(request, parameter);
		// xml字符串
		if(parameter.hasParameterAnnotation(Text.class)) {
			String xml = W3CUtils.toXml(doc);
			return xml;
		}
		else {
			Jaxb2Marshaller jaxb2Marshaller = BeanLocator.getBean(Jaxb2Marshaller.class);
			Unmarshaller unmarshaller = jaxb2Marshaller.createUnmarshaller();
			Object obj = null;
			// 根对象
			if (parameter.hasParameterAnnotation(Root.class)) {
				JAXBElement<?> je = unmarshaller.unmarshal(doc, parameter.getParameterType());
				obj = je.getValue();
			} 
			// 参数对象
			else if (parameter.hasParameterAnnotation(Param.class)) {
				Element root = doc.getDocumentElement();
				Param param = parameter.getParameterAnnotation(Param.class);
				String path = param.value();
				if(!StringUtils.hasText(path)) {
					path = "/" +root.getTagName() +"/" + parameter.getParameterName();
				}
				// 判断类型
				Node node = W3CUtils.findNode(path, root);
				if(node == null) {
					return null;
				}
				JAXBElement<?> je =  unmarshaller.unmarshal(node, parameter.getParameterType());			
				obj = je.getValue();
			}
			//validate(obj);
			return obj;
		}
	}
	
	
	/*
	private Object resolveXmlParam(Document doc, Param param, MethodParameter parameter) {
		Element root = doc.getDocumentElement();
		String path = param.value();
		if(!StringUtils.hasText(path)) {
			path = "/" +root.getTagName() +"/" + parameter.getParameterName();
		}
		Class<?> parameterClass = parameter.getParameterType();
		// 集合类型
		if(Collection.class.isAssignableFrom(parameterClass)) {
			Type parameterType = parameter.getGenericParameterType();
			if(parameterType instanceof ParameterizedType) {
				Type elemType = ((ParameterizedType) parameterType).getActualTypeArguments()[0];
				
			}
		}
	}
	*/

	private Document getDocument(HttpServletRequest request, MethodParameter parameter) throws SAXException, IOException,
			ParserConfigurationException {
		Document doc = (Document) request.getAttribute(ROOT_XML_NODE);
		if (doc == null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(request.getInputStream());
			request.setAttribute(ROOT_XML_NODE, doc);
		}
		return doc;
	}

	private Object resolveArgumentJson(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		JsonNode root = getJsonRoot(request, parameter);
		JsonNode node = null;
		if(parameter.hasParameterAnnotation(Root.class)) {
			node = root;
		}
		else if(parameter.hasParameterAnnotation(Param.class)) {
			Param param = parameter.getParameterAnnotation(Param.class);
			String path = parameter.getParameterName();
			if(org.apache.commons.lang3.StringUtils.isNotBlank(param.value())) {
				path = param.value();
			}
			node = findNode(root, path);
		}
		Class<?> parameterType = parameter.getParameterType();
		Object obj = getJsonValue(node, parameterType);
		//validate(obj);
		return obj;
	}

	private JsonNode getJsonRoot(HttpServletRequest request, MethodParameter parameter) throws IOException {
		if(request.getAttribute(ROOT_JSON_NODE) == null) {
			String json = IOUtils.toString(request.getInputStream(), "UTF-8");
			if(org.apache.commons.lang3.StringUtils.isBlank(json)) {
				throw new IllegalArgumentException(getParameterInfo(parameter) + ", the json content is empty.");
			}
			try {
				JsonNode root = objectMapper.readTree(json);
				request.setAttribute(ROOT_JSON_NODE, root);
				if(logger.isDebugEnabled()) {
					objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
					logger.debug("-------- request -----------\n" + objectMapper.writeValueAsString(root));
				}
			} catch (JsonParseException e) {
				throw  new InvalidParameterException("the format of json parameter is wrong ");
			}
		}
		return (JsonNode)request.getAttribute(ROOT_JSON_NODE);
	}

	/**
	 * @return the pathExtensionMapping
	 */
	public Map<String, String> getPathExtensionMapping() {
		return pathExtensionMapping;
	}

	/**
	 * @param pathExtensionMapping
	 *            the pathExtensionMapping to set
	 */
	public void setPathExtensionMapping(Map<String, String> pathExtensionMapping) {
		this.pathExtensionMapping.putAll(pathExtensionMapping);
	}
	
	/**
	 * 获得参数信息
	 * @param methodParameter
	 * @return
	 */
	protected String getParameterInfo(MethodParameter methodParameter) {
		Method method = methodParameter.getMethod();
		StringBuilder sb = new StringBuilder(method.toGenericString());
		sb.append(": the parameter[")
			.append(methodParameter.getParameterName())
			.append("] ");
		return sb.toString();
	}
	

	private Object getJsonValue(JsonNode node, Class<?> type) {
		return node == null ? null : objectMapper.convertValue(node, type);
	}
	
	private JsonNode findNode(JsonNode root, String path) {
		String[] paths = org.apache.commons.lang3.StringUtils.split(path, ".");
		JsonNode node = root;
		if (paths.length > 0) {
			for (String p : paths) {
				node = node.get(p);
			}
		}
		return node;
	}
	
	




}