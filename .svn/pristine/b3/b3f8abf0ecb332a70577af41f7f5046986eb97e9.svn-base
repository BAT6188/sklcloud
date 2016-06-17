package com.skl.cloud.util.pattern;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.skl.cloud.util.common.StringUtil;

/**
 * @Package skl.util
 * @Title: 工具类
 * @Description: 系统级工具方法
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author wanggb
 * @date 2015年6月1日
 * @version V1.0
 */
public class Toolkits {
	
	private static String DateDefine_NullDate="1900-01-01";

	// 序号发生器优化
	// 循环计数器
	private static int counter5 = 0;
	
	/**
	 * 取应用服务器当前时间（应用服务器）
	 * 
	 * @param timeFormat 时间的格式，如："yyyy-MM-dd HH:mm:ss.SSS"
	 * @return String
	 */
	public static String getCurTimeAPP(String timeFormat) {
		return date2Str(new Date(), timeFormat);
	}
	
	
	/**
	 * 计数器(5位) 00000 -- 99999
	 * 
	 * @return String 循环计数值（不够六位，在前边补零）
	 */
	synchronized public static String getCounter5() {
		StringBuffer id = new StringBuffer(Integer.toString(counter5));
		StringBuffer zeros = new StringBuffer("");

		// 补零，补够5位
		int n = 5 - id.length();
		for (int i = 0; i < n; i++) {
			zeros.append('0');
		}
		zeros.append(id);

		// 循环累加
		if (counter5 >= 99999) {
			counter5 = 0;

		} else {
			counter5++;
		}
		return zeros.toString();
	}
	
	
	/**
	 * 得到18位序号（13位时间 + 5位循环计数值）
	 * 
	 * @return String 得到18位序列号（递增）
	 */
	public static BigDecimal getSequenceID18() {
		String date = Toolkits.getCurTimeAPP("yyMMddHHmmsss"); // 13位

		String sequenceId18 = date + getCounter5(); // 13位时间 + 5位循环计数值

		return new BigDecimal(sequenceId18);
	}
	
	
	/**
	 * 得到20位序号（15位时间 + 5位循环计数值）
	 * 
	 * @return String 得到20位序列号（递增）
	 */
	public static BigDecimal getSequenceID20() {
		String date = Toolkits.getCurTimeAPP("yyMMddHHmmssSSS"); // 15位

		String sequenceId20 = date + getCounter5(); // 15位时间 + 5位循环计数值

		return new BigDecimal(sequenceId20);
	}
	
	/**
	 * 把时间转换为字串
	 * 
	 * @param date 待转换的时间
	 * @param format 转换格式：yyyy-MM-dd
	 * @return String
	 */
	public static String date2Str(Date date, String format) {
		if (Toolkits.isNull(date)) {
			return "";
		}

		if ("yyyy-MM-dd HH:mm:ss.SSS".equalsIgnoreCase(format)) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}

		if ("yyyy-MM-dd".equalsIgnoreCase(format)) {
			format = "yyyy-MM-dd";
		}

		format = StringUtil.isEmpty(format) ? "yyyy-MM-dd" : format;

		// SimpleDateFormat的转换毫秒的精度最多为3位
		SimpleDateFormat df = new SimpleDateFormat(format);

		if (DateDefine_NullDate.equals(df.format(date))) {
			return ""; // 如果是默认的空日期则返回""
		}

		return df.format(date);
	}
	
	
	/**
	 * 判断Object是否为null
	 * 
	 * @param oValue 对象
	 * @return boolean
	 */
	public static boolean isNull(Object oValue) {
		return oValue == null || "null".equals(oValue) ? true : false;
	}


	/**
	 * 获取客户端文件的类型
	 * 
	 * @param fileItemName
	 *            String 客户端文件完整路径
	 * @return String 文件类型
	 * @throws GeneralException
	 */
	public static String getFileType(String fileItemName) throws Exception {
		// 获得文件名后缀
		String fileType = "";
		int index = fileItemName.lastIndexOf('.');
		// cat.debug("获得文件名后缀.的位置:"+index);
		if (index == -1) {
			throw new Exception("文件类型非法，请确认你上传得文件类型。");
		}

		try {
			fileType = fileItemName.substring(index + 1);
		} catch (Exception e) {
			throw new Exception("文件类型非法，请确认你上传得文件类型。");
		}

		if (fileType.length() != 3) {
			throw new Exception("文件类型非法，请确认你上传得文件类型。");
		}

		return fileType;
	}

	
	/**
	 * 获取客户端文件名称
	 * 
	 * @param fileItemName
	 *            String 客户端文件完整路径
	 * @return String 文件名称
	 * @throws GeneralException
	 */
	public static String getFileRealName(String fileItemName) throws Exception {
		// 获得文件名(纯文件名字)
		String fileRealName = "";
		int indexFileType = fileItemName.lastIndexOf('.');
		int indexFileStart = fileItemName.lastIndexOf('\\');

		fileRealName = fileItemName
				.substring(indexFileStart + 1, indexFileType);
		// cat.debug("获得文件名:"+fileRealName);
		return fileRealName;
	}

	
	/**
	 * 得到客户端IP
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	public static String getClientIP(HttpServletRequest request) {
		if (request == null) {
			return "";
		}

		String ipx = request.getRemoteAddr();
		String ip_xforward = StringUtil.null2Str(request
				.getHeader("X-Forwarded-For"));
		String ip_forward = StringUtil.null2Str(request
				.getHeader("Forwarded-For"));
		String ip = "";

		if (StringUtil.isEmpty(ip_xforward)) {
			if (!StringUtil.isEmpty(ip_forward)) {
				ip = ip_forward;
			} else {
				ip = ipx;
			}
		} else {
			ip = ip_xforward;
		}

		return ip;
	}
}
