package com.skl.cloud.service.test.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.dao.test.AppFeedbackMapper;
import com.skl.cloud.model.test.AppFeedbackInfo;
import com.skl.cloud.service.test.AppFeedbackService;

@Service("infoCountService")
public class AppFeedbackServiceImpl implements AppFeedbackService{

	@Autowired
	private AppFeedbackMapper infoCountMapper;
	
	/**
	 * 新增反馈信息
	 */
	@Override
	@Transactional
	public void insertFeedbackInfo(AppFeedbackInfo feedbackInfo) throws Exception {
		
		infoCountMapper.insert(feedbackInfo);
	}

	
	
}
