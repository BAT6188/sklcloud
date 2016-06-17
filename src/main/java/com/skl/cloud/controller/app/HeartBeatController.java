package com.skl.cloud.controller.app;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @ClassName: HeartBeatController
 * @Description: 进行心跳的类
 * @author shaoxiong
 * @date 2015年8月14日
 *
 */
@Controller
@RequestMapping(value = "/skl-cloud/app/Security/AAA")
public class HeartBeatController extends BaseController
{
    
    @Autowired
    private AppUserService userSerivce;

	/**
	 * @Title: appHeratBeat
	 * @Description: 与app用户进行心跳
	 * @param @param req
	 * @param @param resp (参数说明)
	 * @return void (返回值说明)
	 * @throws (异常说明)
	 * @author shaoxiong
	 * @date 2015年8月14日
	 */
	@RequestMapping(value = "/users/heartBeat")
	private ResponseEntity<String> appHeratBeat(HttpServletRequest req, HttpServletResponse resp)
	{
		
		String sReturn = null;
		try
		{
		    Long userId = getUserId();
			// 日志输出
			LoggerUtil.info("*****开始心跳***********userId:"+userId, this.getClass().getName());

			AppUser user = userSerivce.getUserById(userId);

			// 刷新心跳数据(仅时间)
			user.setHeartbeatTime(null);
			userSerivce.updateUserById(user);

			sReturn = XmlUtil.mapToXmlRight();
		}
		catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
