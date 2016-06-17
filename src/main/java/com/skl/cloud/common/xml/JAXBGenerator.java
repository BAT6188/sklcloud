package com.skl.cloud.common.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.skl.cloud.common.spring.BeanLocator;

public class JAXBGenerator {

	private Document doc;
	private Marshaller marshaller;

	private JAXBGenerator() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");
			this.doc = document;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public JAXBGenerator(String rootName) {
		this();
		if (StringUtils.isBlank(rootName)) {
			throw new IllegalArgumentException("the rootName is blank");
		}
		Element root = doc.createElement(rootName);
		root.setAttribute("version", "1.0");
		root.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(root);
	}
	
	public JAXBGenerator(String rootName, Marshaller marshaller) {
		this();
		if (StringUtils.isBlank(rootName)) {
			throw new IllegalArgumentException("the rootName is blank");
		}
		Element root = doc.createElement(rootName);
		root.setAttribute("version", "1.0");
        root.setAttribute("xmlns", "urn:skylight");
		doc.appendChild(root);
		this.marshaller = marshaller;
	}

	public JAXBGenerator(Object rootObject, String rootName) throws JAXBException {
		this(rootObject, rootName, null);
	}
	
	public JAXBGenerator(Object rootObject, String rootName, Marshaller marshaller) throws JAXBException {
		this();
		if (rootObject == null) {
			throw new IllegalArgumentException("rootObject is null");
		}
		this.marshaller = marshaller;
		marshalObject(doc, rootName, rootObject);
	}	

	public JAXBGenerator(Object rootObject) throws JAXBException {
		this(rootObject, null, null);
	}
	
	public JAXBGenerator(Object rootObject, Marshaller marshaller) throws JAXBException {
		this(rootObject, null, marshaller);
	}
	

	/**
	 * 增加参数节点
	 * 
	 * @param name
	 * @param value
	 * @throws JAXBException
	 */
	public void addParam(String name, Object value) throws JAXBException {
		addSubElement(doc.getDocumentElement(), name, value);
	}
	
	private void addSubElement(Node parent, String name, Object value) throws JAXBException {
		if (value == null) {
			return;
		}
		Class<?> type = value.getClass();
		if (type.isPrimitive() || isPrimitiveWrapper(type) || CharSequence.class.isAssignableFrom(type)) {
			Element element = doc.createElement(name);
			element.setTextContent(value.toString());
			parent.appendChild(element);
		} else if(type.isArray()) {
			Object[] array = (Object[])value;
			Element e = doc.createElement(name);
			for (Object obj : array) {
				marshalObject(e, null, obj);
			}
			parent.appendChild(e);
		} else if(Collection.class.isAssignableFrom(type)) {
			Collection<?> col = (Collection<?>)value;
			Element e = doc.createElement(name);
			for (Object obj : col) {
				marshalObject(e, null, obj);
			}
			parent.appendChild(e);
		} else {
			marshalObject(parent, name, value);
		}
	}
	

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, String value) {
		Element root = doc.getDocumentElement();
		root.setAttribute(name, value);
	}

	/**
	 * 增加参数节点
	 * 
	 * @param value
	 * @throws JAXBException
	 */
	public void addParam(Object value) throws JAXBException {
		if (value == null) {
			return;
		}
		Element root = doc.getDocumentElement();
		marshalObject(root, null, value);
	}

	/**
	 * 转为byte数组
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public byte[] convertToBytes() throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeTo(new StreamResult(os));
		return os.toByteArray();
	}

	/**
	 * 转为输入流
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public InputStream convertToInputStream() throws TransformerException {
		byte[] data = convertToBytes();
		return new ByteArrayInputStream(data);
	}

	/**
	 * 转为xml
	 * 
	 * @return
	 * @throws TransformerException
	 */
	public String convertToXml() throws TransformerException {
		StringWriter writer = new StringWriter();
		writeTo(new StreamResult(writer));
		return writer.toString();
	}

	/**
	 * 写入StreamResult
	 * 
	 * @param result
	 * @throws TransformerException
	 */
	public void writeTo(StreamResult result) throws TransformerException {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer transformer = tfactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
	}

	/**
	 * 写入StreamResult
	 * 
	 * @param result
	 * @throws TransformerException
	 */
	public void writeTo(OutputStream outputStream) throws TransformerException {
		this.writeTo(new StreamResult(outputStream));
	}

	/**
	 * 输出到outputStream
	 * 
	 * @param doc
	 * @param output
	 * @throws TransformerException
	 */
	public static void writeDocument(Document doc, Writer writer, boolean prettyPrint) throws TransformerException {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer transformer = tfactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
	}

	/**
	 * 转为xml
	 * 
	 * @param root
	 * @return
	 * @throws TransformerException
	 */
	public static String toXml(Document doc) throws TransformerException {
		StringWriter sw = new StringWriter();
		writeDocument(doc, sw, false);
		return sw.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void marshalObject(Node parent, String tagName, Object obj) throws JAXBException {
		Object je = obj;
		if (StringUtils.isNotBlank(tagName)) {
			je = new JAXBElement(new QName(tagName), obj.getClass(), obj);
		}
		Marshaller marshaller = getMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		marshaller.marshal(je, parent);
	}

	private Marshaller getMarshaller() throws PropertyException {
		if (marshaller == null) {
			Jaxb2Marshaller jaxb2Marshaller = BeanLocator.getBean(Jaxb2Marshaller.class);
			marshaller = jaxb2Marshaller.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			//marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		}
		return marshaller;
	}

	/**
	 * @param marshaller
	 *            the marshaller to set
	 */
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	private boolean isPrimitiveWrapper(Class<?> clazz) {
		return Boolean.class.equals(clazz) || Byte.class.equals(clazz) || Short.class.equals(clazz)
				|| Integer.class.equals(clazz) || Long.class.equals(clazz) || Float.class.equals(clazz)
				|| Double.class.equals(clazz);
	}
	
}
