package com.skl.cloud.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.service.LogManageService;
import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.pattern.Toolkits;

/**
 * Controller类的基础类。所有控制器类必须基继承该类。
 * 
 * @ClassName: BaseController
 * @Description: Controller类基类
 * @author guangbo
 * @date 2015年6月1日
 *
 */
public class BaseController
{
	protected Logger log = LoggerFactory.getLogger("controller");

	@Autowired(required = true)
	private DigestService digestService;

	private LogManageService lgs;

	public LogManageService getLgs()
	{
		return lgs;
	}

	@Autowired(required = true)
	public void setLgs(LogManageService lgs)
	{
		this.lgs = lgs;
	}

	/**
	 * 
	 * @Title: saveLog
	 * @Description: 记录日志公共方法（使用此方法，必须try-catch异常）
	 * @param pl
	 *            :PlatformLog
	 * @throws Exception
	 * @author leiqiang
	 * @date 2015年7月10日
	 */
	public final void saveLog(PlatformLog pl) throws Exception
	{

		Boolean flg = false; // 非空标识

		// 用户ID、模块/功能名称、日志内容、日志时间4项内容不允许为空
		if (StringUtil.isNullOrEmpty(pl.getUserId().toString()) && StringUtil.isNullOrEmpty(pl.getModuleName()) && StringUtil.isNullOrEmpty(pl.getLogContent())
				&& StringUtil.isNullOrEmpty(pl.getLogTime().toString()))
		{
			flg = true;
		}

		if (flg)
		{
			pl.setLogId(Toolkits.getSequenceID18());
			lgs.saveLog(pl);
		}
		else
		{
			throw new ManagerException("990001");
		}
	}

	/**
	 * 该方法可以输出异常到日志文件及设定属性值到request
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param e
	 *            异常对象
	 * @param forClssName
	 *            捕捉到该异常的对象
	 * @author guangbo
	 * @since
	 */
	public final void LogException(HttpServletRequest request, ManagerException me, String forClssName)
	{
		if (me.getMyException() != null)
		{
			LoggerUtil.error(me.getMyExceptionMessage() + "[原始异常]", me.getMyException(), forClssName);
		}

		LoggerUtil.error(me.getErrMsg() + "[包装异常]", me, forClssName);

		// request.setAttribute("MException", me);
	}

	/**
	 * 
	 * @Title: LogException
	 * @Description: TODO
	 * @author guangbo
	 * @param @param me
	 * @param @param forClssName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public final void LogException(ManagerException me, String forClssName)
	{
		if (me.getMyException() != null)
		{
			LoggerUtil.error(me.getMyExceptionMessage() + "[原始异常]", me.getMyException(), forClssName);
		}
		LoggerUtil.error(me.getErrMsg() + "[包装异常]", me, forClssName);

		// request.setAttribute("MException", me);
	}

	/**
	 * <异常记录日志及返回错误xml>
	 * 
	 * @param ex
	 *            [异常]
	 * @param forClssName
	 *            [类名]
	 * @return [返回错误xml]
	 */
	protected String getErrorXml(Exception e, String forClssName)
	{
		ManagerException ex = new ManagerException(e);

		// 假如是纯业务异常，则不输出日志，仅仅返回错误xml给前台
		if (!(e instanceof BusinessException && e.getCause() == null))
		{
			// 记录日志
			LogException(ex, forClssName);
		}
		// 返回错误xml
		return XmlUtil.mapToXmlError(ex.getErrorCode());
	}

	/**
	 * <异常记录日志及返回错误xml>
	 * 
	 * @param root
	 *            [根节点]
	 * @param ex
	 *            [异常]
	 * @param forClssName
	 *            [类名]
	 * @return [返回错误xml]
	 */
	protected String getErrorXml(String sRoot, Exception e, String forClssName)
	{
		ManagerException ex = new ManagerException(e);

		// 假如是纯业务异常，则不输出日志，仅仅返回错误xml给前台
		if (!(e instanceof BusinessException && e.getCause() == null))
		{
			// 记录日志
			LogException(ex, forClssName);
		}
		// 返回错误xml
		return XmlUtil.mapToXmlError(sRoot, ex.getErrorCode());
	}

	/**
	 * digest(这里用一句话描述这个方法的作用)
	 * 
	 * @Title: digest
	 * @Description: digest认证
	 * @param req
	 * @param resp
	 * @return Map<String,Object> (返回值说明)
	 * @author shaoxiong
	 * @date 2015年9月8日
	 */
	/*
	 * public final Map<String, Object> digest(HttpServletRequest req,
	 * HttpServletResponse resp) { return digestService.degist(req, resp); }
	 */

	protected Long getUserId()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Long userId = (Long) request.getAttribute("userId");
		if (userId == null)
		{
			// 从header去获取
			String auth = request.getHeader("Authorization");
			if (StringUtils.isBlank(auth) || !StringUtils.startsWith(auth, "Digest") || auth.indexOf("userId") <= 0)
			{
				userId = 0L;
			}
			else
			{
				String alice = auth.substring("Digest ".length());
				String[] array = alice.split(",");
				for (String item1 : array)
				{
					// 以等号=分割
					String[] authArr2 = item1.split("=");
					if (authArr2.length >= 2)
					{
						String key = authArr2[0].trim();
						String value = authArr2[1].trim();
						if (value.length() >= 2 && StringUtils.startsWith(key, "userId"))
						{
							if (value.startsWith("\""))
							{
								value = value.substring(1);
							}
							if (value.endsWith("\""))
							{
								value = value.substring(0, value.length() - 1);
							}
							userId = Long.parseLong(value);
							break;
						}
					}
				}

			}
		}
		return userId == null ? 0L : userId;
	}

	@ExceptionHandler(Exception.class)
	public void exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response)
	{
		log.error("发生异常", ex);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

}