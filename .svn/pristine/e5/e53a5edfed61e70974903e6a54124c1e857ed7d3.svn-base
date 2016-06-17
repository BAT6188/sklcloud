package com.skl.cloud.controller.app;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.AppUserAccountMgtService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.XmlUtil;

/**
 * 用于App登录的Controller
 * 
 * @ClassName: AppLoginController
 * @Description: TODO
 * @author zhaonao
 * @date 2016年1月4日
 *
 */
@Controller
@RequestMapping("/skl-cloud/app/Security/AAA/users")
public class AppLoginController extends BaseController
{
	@Autowired
	private AppUserService userService;

	@Autowired
	private AppUserAccountMgtService userAccountService;

	/**
	 * App用户登录email系列
	 * 
	 * @param inputstream
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/userId", method = RequestMethod.POST)
	public ResponseEntity<String> loginByEmail(HttpServletRequest req, HttpServletResponse resp)
	{
		String sReturn = null;
		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			
			AppUser appUser = userAccountService.updateUserLoginByEmail(paraMap);

			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			backMap.put("userId", appUser.getId());
			backMap.put("random", appUser.getCloudRandom());
			sReturn = XmlUtil.mapToXmlRight("appUserLogin", backMap);
		}
		catch (Exception e)
		{
			sReturn = getErrorXml("appUserLogin", e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <App用户登录第三方账户系列>
	 * 
	 * @param inputstream
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/thirdId", method = RequestMethod.POST)
	public ResponseEntity<String> loginByThirdAccount(HttpServletRequest req, HttpServletResponse resp)
	{
		String sReturn = null;

		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			
			AppUser appUser = userAccountService.updateUserLoginByThirdAccount(paraMap);

			// 返回结果组装（正确）
			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			backMap.put("userId", appUser.getId());
			backMap.put("random", appUser.getCloudRandom());
			sReturn = XmlUtil.mapToXmlRight("appUserLogin", backMap);
		}
		catch (Exception e)
		{
			sReturn = getErrorXml("appUserLogin", e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * App用户登出
	 * 
	 * @param inputstream
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginOut", method = RequestMethod.GET)
	public ResponseEntity<String> LoginOut(HttpServletRequest req, HttpServletResponse resp)
	{
		Long userId = getUserId();
		String sReturn = null;

		try
		{
			// 校验：用户是否存在
			AppUser user = userService.getUserById(userId);
			if (user == null)
			{
				throw new BusinessException("0x50020031");
			}
			else
			{
				// 更改有用户状态（登出）
				user.setStatus(false);
				userService.updateUserById(user);
			}
			sReturn = XmlUtil.mapToXmlRight();
		}
		catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
