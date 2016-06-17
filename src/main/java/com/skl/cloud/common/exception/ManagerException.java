package com.skl.cloud.common.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.rmi.NoSuchObjectException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.service.common.I18NResourceService;


/**
 * 
 * @ClassName: ManagerException
 * @Description:是所有在框架端异常类的父类，其他任何业务异常必须继承这个父类
 * @author guangbo
 * @date 2015年6月11日
 *
 */
public class ManagerException extends DataAccessException
{
	 
	private static final long serialVersionUID = 7960612132133356541L;

	// 错误代码
	protected String errorCode = "";

	// 返回客户端概要信息（返回前台的中文）
	protected String errMsg = "";

	// 错误的辅助信息，主要包括异常产生的时间（精确到秒）+ weblogicDomianName
	// 20070907231256App1
	protected String errHelp = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new java.util.Date()) + System.getProperty("weblogic.Name");

	// 原始异常 exception和message
	protected Throwable myException;
	protected String myExceptionMessage;

	// 全局错误码，为错误提示信息提供
	private static String GLOBALERROR = "0x50000005";

	/**
	 * 所有底层抛出的异常信息，供系统级别异常调用 注意：开发人员请勿调用
	 * 
	 * @param ex
	 *            Throwable 底层异常
	 */
	public ManagerException(Throwable ex)
	{
		super(ex.toString());
		// 包装异常
		String errCode = this.handleSystemException(ex);
		this.setErrorCode(errCode);
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		this.errMsg = msgResourceService.getMessage(errCode);
		// 底层异常
		this.myException = ex.getCause() == null ? ex : ex.getCause();
		this.myExceptionMessage = ex.getCause() == null ? this.errMsg : msgResourceService.getMessage(this.handleSystemException(ex.getCause()));
	}

	/**
	 * 所有开发人员抛出的业务异常都必须调用该构造函数 包括：提示信息、已知异常信息
	 * 
	 * @param errCode
	 *            String 错误代码
	 */
	public ManagerException(String errCode)
	{
		super(errCode);

		this.setErrorCode(errCode);
		
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		this.errMsg =msgResourceService.getMessage(errCode);
	}
	

	/**
	 * 扩展的构造函数，可以动态的修改错误描述信息 如：10001={0},后面{0}可以替换成任何自己的信息
	 * 
	 * @param errCode
	 *            错误代码
	 * @param errMsgs
	 *            需要替换的错误提示信息
	 */
	public ManagerException(String errCode, String[] errMsgs)
	{
		super(errCode == null ? GLOBALERROR : errCode);

		if (errCode == null)
		{
			errCode = GLOBALERROR;
		}
		this.setErrorCode(errCode);
		// 根据错误码获得错误提示信息
		I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
		String message = msgResourceService.getMessage(errCode);
		for (int i = 0; i < errMsgs.length; i++)
		{
			message = message.replace("{" + i + "}", errMsgs[i]);
		}

		this.errMsg = message;
	}

	/**
	 * 适配系统异常方法，根据异常类型来找到对应的错误码，
	 * 
	 * @param throwable
	 *            底层异常
	 * 
	 * @return String 系统错误代码
	 */
	private String handleSystemException(Throwable throwable)
	{
		// 错误代码
		String errorCode = "0x50000005";

		// 其它java不许捕获异常、框架异常等
		if (throwable instanceof NullPointerException)
		{
			errorCode = "0x50000006";
		}
		else if (throwable instanceof ClassCastException)
		{
			errorCode = "0x50000007";
		}
		else if (throwable instanceof NumberFormatException)
		{
			errorCode = "0x50000008";
		}
		else if (throwable instanceof FileNotFoundException)
		{
			errorCode = "0x50000009";
		}
		else if (throwable instanceof IOException)
		{
			errorCode = "0x50000010";
		}
		else if (throwable instanceof ArrayIndexOutOfBoundsException)
		{
			errorCode = "0x50000011";
		}
		else if (throwable instanceof IndexOutOfBoundsException)
		{
			errorCode = "0x50000012";
		}
		else if (throwable instanceof ClassNotFoundException)
		{
			errorCode = "0x50000013";
		}
		else if (throwable instanceof NoSuchMethodException)
		{
			errorCode = "0x50000014";
		}
		else if (throwable instanceof SecurityException)
		{
			errorCode = "0x50000015";
		}
		else if (throwable instanceof SQLWarning)
		{
			errorCode = "0x50000016";
		}
		else if (throwable instanceof BatchUpdateException)
		{
			errorCode = "0x50000017";
		}
		else if (throwable instanceof NoSuchObjectException)
		{
			errorCode = "0x50000018";
		}
		else if (throwable instanceof UnsupportedEncodingException)
		{
			errorCode = "0x50000018";
		}
		else if (throwable instanceof DocumentException)
		{
			errorCode = "0x50000027";
		}
		else if (throwable instanceof ParserConfigurationException)
		{
			errorCode = "0x50000019";
		}
		else if (throwable instanceof ParserConfigurationException)
		{
			errorCode = "0x50000032";
		}
		else if (throwable instanceof SocketTimeoutException)
		{
			errorCode = "0x50000020";
		}
		else if (throwable instanceof DuplicateKeyException)
		{
			errorCode = "0x50000021";
		}
		else if (throwable instanceof SQLException)
		{
			errorCode = "0x50000022";
		}
		else if (throwable instanceof UncategorizedSQLException)
		{
			errorCode = "0x50000024";
		}
		else if (throwable instanceof NoSuchElementException)
		{
			errorCode = "0x50000025";
		}
		else if (throwable instanceof BusinessException)
		{
			errorCode = throwable.getMessage() != null ? throwable.getMessage() : "0x50020015";
		}
		return errorCode;
	}

	private void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorCode()
	{
		return this.errorCode;
	}

	public void setErrMsg(String errMsg)
	{
		this.errMsg = errMsg;
	}

	public String getErrMsg()
	{
		return this.errMsg;
	}

	public String getErrHelp()
	{
		return errHelp;
	}

	public Throwable getMyException()
	{
		return myException;
	}

	public String getMyExceptionMessage()
	{
		return myExceptionMessage;
	}
	public static void main(String[] args) {
		new ManagerException("0x50000005");
	}

}
