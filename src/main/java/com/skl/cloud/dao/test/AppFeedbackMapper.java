package com.skl.cloud.dao.test;

import com.skl.cloud.model.test.AppFeedbackInfo;

public interface AppFeedbackMapper {
 
	/**
	 * 插入反馈信息
	 * @param feedbackInfo
	 * @throws Exception
	 */
	public void insert(AppFeedbackInfo feedbackInfo) throws Exception;

	
}
