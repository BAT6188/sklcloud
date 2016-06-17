package com.skl.cloud.foundation.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.springframework.validation.BindingResult;

import com.skl.cloud.common.xml.JAXBGenerator;
import com.skl.cloud.common.xml.Jaxb2Marshaller;
import com.skl.cloud.controller.app.dto.ResponseStatusAO;
import com.skl.cloud.foundation.mvc.model.SKLModel;

public class SubPathExtensionView extends XmlPathExtensionViewSupport {
    private Jaxb2Marshaller jaxb2Marshaller;
	

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SKLModel sklModel = getSKLModel(model);
		if(sklModel == null) {
			throw new IllegalArgumentException("SKLModel is null");
		}
		// 直接返回流
		if(sklModel.getReturnInputStream() != null) {
			writeToResponse(response, sklModel.getReturnInputStream());
			return;
		}
		// 直接返回xml
		else if(sklModel.getReturnXml() != null) {
			writeToResponse(response, sklModel.getReturnXml());
			return;
		}
		
		String rootName = sklModel.getResponseName();
		// 直接返回对象
		if(sklModel.getReturnObject() != null) {
			JAXBGenerator generator  = new JAXBGenerator(sklModel.getReturnObject(), rootName);
			generator.setMarshaller(jaxb2Marshaller.createMarshaller());
			// 写入状态信息
			writeResponseStatus(generator, sklModel);
			generator.writeTo(response.getOutputStream());
			return;
		}
		// 参数形式
		else {
			JAXBGenerator generator  = new JAXBGenerator(rootName);
			generator.setMarshaller(jaxb2Marshaller.createMarshaller());
			for (Map.Entry<String, Object> entry : sklModel.getAttrMap().entrySet()) {
				String name = entry.getKey();
				Object obj = entry.getValue();
				if (obj instanceof BindingResult) {
					continue;
				}
				if (obj instanceof SKLModel) {
					continue;
				}
				generator.addParam(name, obj);
			}
			// 写入状态信息
			writeResponseStatus(generator, sklModel);
			generator.writeTo(response.getOutputStream());
			return;
		}
	}

	/**
	 * @param jaxb2Marshaller
	 *            the jaxb2Marshaller to set
	 */
	public void setJaxb2Marshaller(Jaxb2Marshaller jaxb2Marshaller) {
		this.jaxb2Marshaller = jaxb2Marshaller;
	}

	private void writeResponseStatus(JAXBGenerator generator, SKLModel model) throws JAXBException {
		int errCode = 0;
		String errMsg = "0";
		ResponseStatusAO rs = new ResponseStatusAO(errCode, errMsg);
		generator.addParam("ResponseStatus", rs);
	}

}
