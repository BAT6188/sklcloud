package com.skl.cloud.foundation.sns.dto;

import com.skl.cloud.foundation.sns.model.Message;
import com.skl.cloud.model.EventAlert;
import com.skl.cloud.model.ipc.IPCamera;

/**
 *  
 * @author weibin
 * @date 2016年3月4日
 */
public interface MessageTemplet {
	public String toMessage(IPCamera ipCamera, EventAlert eventAlert);

	public String toMessage(IPCamera ipCamera, EventAlert eventAlert,
			Message message);
}
