package com.skl.cloud.service.sub;

import com.skl.cloud.model.sub.SubException;

public interface StreamExceptionService {

	/**
	 * 保存上报的subException数据
	 */
	public void insert(SubException subException);
	
	/**
	 * 对子系统上报的Exception按type进行相应高度处理
	 */
	public void handleSubSysReportExp(SubException bean);
}
