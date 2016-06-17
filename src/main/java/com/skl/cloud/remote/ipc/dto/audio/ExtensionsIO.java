package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="extensions")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtensionsIO {
	@XmlElement(name="action_parameter")
	private ActionParameterIO actionParameter;
	

	/**
	 * @return the actionParameter
	 */
	public ActionParameterIO getActionParameter() {
		return actionParameter;
	}

	/**
	 * @param actionParameter the actionParameter to set
	 */
	public void setActionParameter(ActionParameterIO actionParameter) {
		this.actionParameter = actionParameter;
	}
	
	
	
}
