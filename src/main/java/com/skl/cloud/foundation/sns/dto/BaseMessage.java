package com.skl.cloud.foundation.sns.dto;

import com.skl.cloud.foundation.sns.model.Message;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.util.config.SystemConfig;

/**
 *  
 * @author weibin
 * @date 2016年3月4日
 */
public class BaseMessage implements MessageTemplet {
	
	public String showMessage(IPCamera ipCamera, EventAlert eventAlert){
		String tmpl = "%s %s在%s有%s发生！";
		String country = System.getProperty("user.country");
		if("CN".equals(country)){
			tmpl = SystemConfig.getProperty("tmplCH", "%s %s在%s有%s发生！");
			tmpl = String.format(tmpl, ipCamera.getKind(),ipCamera.getNickname(),eventAlert.getDateTime(),eventAlert.getEventId());
		}else{
			tmpl = SystemConfig.getProperty("tmplEN", "%s %s has happend %s on %s！");
			tmpl = String.format(tmpl, ipCamera.getKind(),ipCamera.getNickname(),eventAlert.getEventId(),eventAlert.getDateTime());
		}
		return tmpl;
	}
	
	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert,
			Message message) {
		// TODO Auto-generated method stub
		return null;
	}

}
