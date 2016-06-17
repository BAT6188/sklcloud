package com.skl.cloud.controller.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.test.AppFeedbackInfo;
import com.skl.cloud.service.test.AppFeedbackService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @ClassName: AppFeedbackController
 * @Description: 为公测提供的信息统计接口
 * @author fulin
 * @date 2016年3月18日 
 */

@Controller
@RequestMapping("/skl-cloud/app")
public class AppFeedbackController extends BaseController {

	
	@Autowired
	private AppFeedbackService appFeedbackService;
	
	
	/**
	 * APP用户提交意见反馈
	 * @param req
	 * @return
	 */
	@RequestMapping("/user/submit/feedback.app")
	public ResponseEntity<String> feedbackInfoCount(HttpServletRequest req)
	{
		Long userId = getUserId();
		String sReturn = null;

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

			String networkType = XmlUtil.convertToString(paraMap.get("networkType"), false);
			String submitName = XmlUtil.convertToString(paraMap.get("submitName"), false);
			String featureItem = XmlUtil.convertToString(paraMap.get("featureItem"), false);
			String errorText = XmlUtil.convertToString(paraMap.get("errorText"), false);
			
			//检验networkType参数是否正确
		    if (!AppFeedbackInfo.netType.isExistType(networkType)) {
                throw new BusinessException("0x50000001");
			}
			
            AppFeedbackInfo feedbackInfo = new AppFeedbackInfo();
            feedbackInfo.setUserId(userId);
            feedbackInfo.setNetworkType(AppFeedbackInfo.netType.getNetType(networkType).getIntValue());
            feedbackInfo.setSubmitName(submitName);
            feedbackInfo.setFeatureItem(featureItem);
            feedbackInfo.setErrorText(errorText);
            
            appFeedbackService.insertFeedbackInfo(feedbackInfo);
			
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

}
