package com.skl.cloud.util.validator;

import java.util.regex.Pattern;

public class ValidationUtils {

	private static final Pattern P_EMAIL = Pattern.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");
	//时间格式为"MM-dd HH:mm"
	private static final Pattern P_DATETIME01 = Pattern.compile("^((((0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01]))|((0[2469]|11)-(0[1-9]|1[0-9]|2[0-9]|30))|(02-(0[1-9]|1[0-9]|2[0-9]))) ([01][0-9]|2[0-3]):[0-5][0-9])$");
	//时间格式为"HH:mm"
	private static final Pattern P_DATETIME02 = Pattern.compile("^(([01][0-9]|2[0-3]):[0-5][0-9])$");
	//wechat--story/alarm列表的显示名字,最长允许30个字符，包括允许中英文字符，不允许包含 \ / : * ? “ < > | 等字符
	private static final Pattern P_LISTNAME = Pattern.compile("[\\w\u4e00-\u9fa5]{1,30}");
	//wechat--openId，包含字母、数字和-
	private static final Pattern P_OPENID = Pattern.compile("[\\w-_]{1,30}");
	/**
	 * 检查是否为邮件
	 * @param email
	 * @return 符合返回true,否则返回false
	 */
	public static boolean checkEmail(String email) {
		if(email == null) {
			return false;
		}
		return P_EMAIL.matcher(email).matches();
	}

	/**
	 * 检查时间格式为"MM-dd HH:mm"的有效性
	 * @param dateTime
	 * @return 符合返回true,否则返回false
	 */
	public static boolean checkDateTime01(String dateTime) {
		if(dateTime == null) {
			return false;
		}
		return P_DATETIME01.matcher(dateTime).matches();
	}
	/**
	 * 检查时间格式为"HH:mm"的有效性
	 * @param dateTime
	 * @return 符合返回true,否则返回false
	 */
	public static boolean checkDateTime02(String dateTime) {
		if(dateTime == null) {
			return false;
		}
		return P_DATETIME02.matcher(dateTime).matches();
	}
	/**
	 * 检验story/alarm列表的显示名字,最长允许30个字符，包括允许中英文字符，不允许包含 \ / : * ? “ < > | 等字符
	 * @param listName 
	 * @return 符合返回true,否则返回false
	 */
	public static boolean checkListName(String listName) {
		if(listName == null) {
			return false;
		}
		return P_LISTNAME.matcher(listName).matches();
	}
	/**
	 * 检验wechat的openId，openId包含字母、数字和-
	 * @param openId 
	 * @return 符合返回true,否则返回false
	 */
	public static boolean checkOpenId(String openId) {
		if(openId == null) {
			return false;
		}
		return P_OPENID.matcher(openId).matches();
	}
}
