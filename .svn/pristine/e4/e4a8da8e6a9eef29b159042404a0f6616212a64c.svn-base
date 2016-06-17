package com.skl.cloud.util.common;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.pattern.StrPattern;
import com.skl.cloud.util.pattern.Toolkits;

/**
 * 
 * <p>Title: 天彩云平台2011</p>
 * <p>Description:字符串处理单元类。</p>
 * <p>Copyright: sky light Copyright (c) 2015</p>
 * <p>Company:天彩电子(深圳)有限公司</p>
 * @author wgb
 * @version V1.0
 */
public class StringUtil {

	// 过滤通过页面表单提交的字符
	private static String[][] FilterChars = { { "<", "&lt;" }, { ">", "&gt;" }, { " ", "&nbsp;" }, { "\"", "&quot;" },
			{ "&", "&amp;" }, { "/", "&#47;" }, { "\\", "&#92;" }, { "\n", "<br>" } };
	// 过滤通过javascript脚本处理并提交的字符
	private static String[][] FilterScriptChars = { { "\n", "\'+\'\\n\'+\'" }, { "\r", " " },
			{ "\\", "\'+\'\\\\\'+\'" }, { "\'", "\'+\'\\\'\'+\'" } };

	/**
	 * 
	  * isEmpty2(这里用一句话描述这个方法的作用)
	  * TODO(这里描述这个方法适用条件 – 可选)
	  * TODO(这里描述这个方法的执行流程 – 可选)
	  * TODO(这里描述这个方法的使用方法 – 可选)
	  * TODO(这里描述这个方法的注意事项 – 可选)
	  *
	  * @Title: isEmpty2
	  * @Description: TODO
	  * @param @param sValue
	  * @param @return (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author guangbo
	  * @date 2015年6月8日
	 */
	public static boolean isEmpty2(String sValue) {
		boolean isTrue = false;
		if (Toolkits.isNull(sValue) || "".equals(sValue.trim())) {
			isTrue = true;
		}
		return isTrue;
	}

	/**
	 * 去掉字符串的前后空格,并使用""代替空对象或者空字符串
	 * 
	 * @param strValue
	 * @return
	 */
	public static String strTrim(String strValue) {

        if (!StringUtil.isEmpty(strValue)) {
            return strValue.trim();
        }
		return "";

	}

	/**
	 * 方法用途: 将特殊字符&转换成全角的＆<br>
	 *
	 * @param str
	 * @return
	 */
	public static String specialStrToXml(String str) {
		String str1 = str.replaceAll("&", "＆");
		return str1;
	}

	/**
	 * 方法用途: 美国编码转换成中文编码<br>
	 *
	 * @param ustr
	 * @return
	 * @throws Exception
	 */
	public static String toU2C(String ustr) throws Exception {
		if (ustr == null) {
			return null;
		}
		return new String(ustr.getBytes("ISO8859_1"), "GB2312");
	}

	/**
	 * 方法用途: 中文编码转换成美国编码<br>
	 *
	 * @param cstr
	 * @return
	 * @throws Exception
	 */
	public static String toC2U(String cstr) throws Exception {
		if (cstr == null) {
			return null;
		}
		return new String(cstr.getBytes("GB2312"), "ISO8859_1");
	}

	/**
	 * 获得串s的字节长度
	 * 
	 * @param src
	 *            串s
	 * @return int 字节长度
	 */
	public static int getByteSize(String src) {
		int ret = 0;
		if (Toolkits.isNull(src)) {
			return ret;
		}
		try {
			ret = getBytes_encode(src).length;
		} catch (Exception e) {
		}
		return ret;
	}

	public static byte[] getBytes_encode(String sourceStr) {
		byte[] newByte = null;
		try {
			newByte = sourceStr.getBytes("GBK");
		} catch (Exception e) {
			newByte = sourceStr.getBytes();
		}
		return newByte;
	}

	/**
	 * 传入字符串头尾trim
	 * 
	 * @param str
	 *            字符串
	 * @return String
	 */
	public static String trim(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 从一个字符表达式中抽取出来的一段字符串
	 * 
	 * @param str
	 *            字符表达式
	 * @param offset
	 *            开始偏移量（从0开始）
	 * @param length
	 *            截取长度
	 * @return String 抽取出来的一段字符串
	 * @throws GeneralException
	 */
	public static String subStringByByte(String str, int offset, int length) { // throws
		// GeneralException{
		String newStr = "";
		int skipLen = 0;
		int newLength = 0;

		int byteLength = 0;

		// 输入校验
		if (str == null || length < 1 || offset < 0) {
			return newStr;
		}
		int strLengthByByte = getBytes_encode(str).length;
		if (strLengthByByte < offset + 1)
			return newStr;
		if (strLengthByByte - offset < length) {
			length = strLengthByByte - offset;
		}

		// 按字节取子串
		byte[] subBytes = null;
		ByteArrayInputStream bytesStream = new ByteArrayInputStream(getBytes_encode(str));
		bytesStream.skip(offset + skipLen);

		try {
			subBytes = new byte[strLengthByByte - offset];
			// （1）判断开始的偏移量
			byteLength = bytesStream.read(subBytes, 0, strLengthByByte - offset);
			if (byteLength == -1)
				return "";
			newStr = new String(subBytes);
			if (newStr == null || newStr.length() < 1 || getBytes_encode(newStr).length < byteLength) { // 如果开始偏移量往后的不可转变为string,则表示截取的第一位是半个汉字
				skipLen = skipLen + 1;
			}
			// cat.debug(newStr);

			bytesStream.reset();
			bytesStream.skip(offset + skipLen);
			newLength = length - skipLen; // 新的截取长度
			if (newLength < 1)
				return "";
			subBytes = new byte[newLength];
			// （2）判断结束的偏移量
			byteLength = bytesStream.read(subBytes, 0, newLength);
			newStr = new String(subBytes);
			if (newStr == null || newStr.length() < 1 || getBytes_encode(newStr).length < byteLength) { // 如果重新截取的不可转变为string,则表示截取的最后一位是半个汉字
				newLength = newLength - 1;
			} else {
				return newStr;
			}
			// cat.debug(newStr);

			bytesStream.reset();
			bytesStream.skip(offset + skipLen);
			if (newLength < 1)
				return "";
			subBytes = new byte[newLength];
			// （3）
			byteLength = bytesStream.read(subBytes, 0, newLength);
			newStr = new String(subBytes);
			if (newStr == null || newStr.length() < 1 || getBytes_encode(newStr).length < byteLength)
				return "";

			return newStr;
		} catch (Exception e) {
			LoggerUtil.error("   抽取字符串错误：", e, StringUtil.class);
			return "";
		}
	}

	/**
	 * 返回一个指定字串右边length个字节组成的字符串
	 * 
	 * @param str
	 *            原字串
	 * @param strLen
	 *            截取的长度
	 * @return String
	 * @throws GeneralException
	 */
	public static String rightStrByByte(String str, int strLen) throws Exception {
		if (Toolkits.isNull(str))
			return "";
		int offset = getByteSize(str) - strLen;
		if (offset < 0)
			return str;
		return subStringByByte(str, offset, strLen);
	}

	/**
	 * 返回一个指定字串左边length个字节组成的字符串
	 * 
	 * @param str
	 *            原字串
	 * @param strLen
	 *            截取的长度
	 * @return String
	 * @throws GeneralException
	 */
	public static String leftStrByByte(String str, int strLen) {
		if (Toolkits.isNull(str))
			return "";
		if (getByteSize(str) <= strLen)
			return str;
		return subStringByByte(str, 0, strLen);
	}

	/**
	 * 给字串加上前缀（按字符）
	 * 
	 * @param srcStr
	 *            需要加入前缀的字串
	 * @param length
	 *            返回字串总长度
	 * @param fixChar
	 *            前缀字符
	 * @return String 加上前缀后的字串
	 */
	public static String fixPrefixStr(String srcStr, int length, String fixChar) {
		if (Toolkits.isNull(srcStr)) {
			srcStr = "";
		}
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length - srcStr.length(); i++) {
			sb.append(fixChar);
		}
		return new String(sb) + srcStr;
	}

	/**
	 * 给字串加上前缀（按字节）
	 * 
	 * @param srcStr
	 *            需要加入前缀的字串
	 * @param length
	 *            返回字串总长度
	 * @param fixChar
	 *            前缀字符
	 * @return String 加上前缀后的字串
	 */
	public static String fixPrefixStrb(String srcStr, int length, String fixChar) {
		if (Toolkits.isNull(srcStr)) {
			srcStr = "";
		}
		StringBuffer sb = new StringBuffer(length);

		int srcSize = 0;
		srcSize = getBytes_encode(srcStr).length;

		for (int i = 0; i < length - srcSize; i++) {
			sb.append(fixChar);
		}
		return new String(sb) + srcStr;
	}

	/**
	 * 给字串加上后缀（按字符）
	 * 
	 * @param srcStr
	 *            需要加入后缀的字串
	 * @param length
	 *            返回字串总长度
	 * @param fixChar
	 *            后缀字符
	 * @return String 加上后缀后的字串
	 */
	public static String fixSuffixStr(String srcStr, int length, String fixChar) {
		if (Toolkits.isNull(srcStr)) {
			srcStr = "";
		}
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length - srcStr.length(); i++) {
			sb.append(fixChar);
		}
		return srcStr + new String(sb);
	}

	/**
	 * 给字串加上后缀（按字节）
	 * 
	 * @param srcStr
	 *            需要加入后缀的字串
	 * @param length
	 *            返回字串总长度
	 * @param fixChar
	 *            后缀字符
	 * @return String 加上后缀后的字串
	 */
	public static String fixSuffixStrb(String srcStr, int length, String fixChar) {
		String encoding = "GBK";
		if (Toolkits.isNull(srcStr)) {
			srcStr = "";
		}
		StringBuffer sb = new StringBuffer(length);
		int srcSize = 0;
		try {
			srcSize = srcStr.getBytes(encoding).length;
		} catch (UnsupportedEncodingException ue) {
		}
		for (int i = 0; i < length - srcSize; i++) {
			sb.append(fixChar);
		}
		return srcStr + new String(sb);
	}

	/**
	 * 把可能为null的字符串变量转换为0长度字串
	 * 
	 * @param str
	 *            字符串变量
	 * @return String ""
	 */
	public static String null2Str(String str) {
		if (isEmpty(str)) {
			return "";
		}
		// return str.trim() ;
		return str;
	}

	/**
	 * 把可能为null的字符串变量转换为0长度字串
	 * 
	 * @param str
	 *            字符串变量
	 * @return String ""
	 */
	public static String null2Str(Object obj) {
		if (isEmpty(obj)) {
			return "";
		}
		// return str.trim() ;
		return obj.toString();
	}

	/**
	 * 把可能为null的字符串变量转换为空格字串
	 * 
	 * @param str
	 *            字符串变量
	 * @return String ""
	 */
	public static String null2Space(String str) {
		if (Toolkits.isNull(str)) {
			return " ";
		}
		return str;
	}

	/**
	 * 方法用途: 将空字符转换为0,<br>
	 * 实现步骤: 判断，然后做处理
	 *
	 * @param str 传入字符串
	 * @return
	 */
	public static String null2Zero(String str) {
		if (Toolkits.isNull(str) || str.equals("") || str.equals("null"))
			return "0";
		return str.trim();
	}

	/**
	 * 方法用途: 将空字符转换为0,<br>
	 * 实现步骤: 判断，然后做处理
	 *
	 * @param str 传入字符串
	 * @return
	 */
	public static String null2Zero(Object str) {
		String retStr = "0";
		if (str != null) {
			retStr = null2Zero(str.toString());
		}
		return retStr.trim();
	}

	/**
	 * 替换出现的字串
	 * 
	 * @param oldString
	 *            原子串
	 * @param strSearch
	 *            查询子串
	 * @param strReplace
	 *            替换子串
	 * @return Sring 新的子串
	 */
	public static String replaceString(String oldString, String strSearch, String strReplace) {
		/*
		 * String ret = oldString; int pos = oldString.indexOf(strSearch); if(pos >= 0) ret = oldString.substring(0,pos)
		 * + strReplace + oldString.substring(pos + strSearch.length()); return ret;
		 */
		int pos = oldString.indexOf(strSearch);
		while (pos > 0) {
			oldString = oldString.substring(0, pos) + strReplace + oldString.substring(pos + strSearch.length());
			pos = oldString.indexOf(strSearch);
		}
		return oldString;
	}

	/**
	 * 判断字符串是否为空串""
	 * 
	 * @param sValue
	 *            字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String sValue) {
		if (Toolkits.isNull(sValue))
			return true;
		return sValue.trim().equals("") ? true : false;
	}

	/**
	 * 判断字符串是否为空串""
	 * 
	 * @param sValue
	 *            字符串
	 * @return boolean
	 */
	public static boolean isEmpty(Object sValue) {
		if (Toolkits.isNull(sValue))
			return true;
		else
			return false;
	}

	/**
	 * 按长度要求截取字符串
	 * 
	 * @param oldString
	 *            原字符串
	 * @param length
	 *            截取长度
	 * @return String 截取后字符串
	 */
	public static String stringToConstantLength(String oldString, int length) {
		// 该方法需要测试 todo : zhm
		String re = "";
		String encoding = "GBK";
		byte[] byteArray = new byte[length];
		byte[] b;
		int len = length;
		try {
			for (int i = 0; i < length; i++) {
				byteArray[i] = (byte) ' ';
			}
			if (!isEmpty(oldString)) {
				b = oldString.getBytes(encoding);

				if (b.length < len) {
					len = b.length;
				}
				for (int i = 0; i < len; i++) {
					byteArray[i] = b[i];
				}
			}
			re = new String(byteArray, encoding);
			if (!isEmpty(re)) {
				byteArray[length - 1] = (byte) ' ';
				re = new String(byteArray, encoding);
			}
			return re;
		} catch (Exception e) {
			LoggerUtil.error("   抽取字符串错误：", e, StringUtil.class);
			return "";
		}
	}

	/**
	 * 如果str不为null也不为""则返回true
	 * 
	 * @param str
	 *            String
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(String str) {
		boolean flag = false;
		if (str != null && str.length() > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * java数组对象转换为字符串
	 * 
	 * @param array
	 *            Object java数组对象转
	 * @param separator
	 *            String 分隔符
	 * @return String
	 */
	public static String arrayToString(Object array, String separator) {
		separator = null2Str(separator);

		if (array == null) {
			return "";
		}

		Object obj = null;
		if (array instanceof Hashtable) {
			array = ((Hashtable) array).entrySet().toArray();
		} else if (array instanceof HashSet) {
			array = ((HashSet) array).toArray();
		} else if (array instanceof Collection) {
			array = ((Collection) array).toArray();
		}
		int length = Array.getLength(array);
		int lastItem = length - 1;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			obj = Array.get(array, i);
			if (obj != null) {
				sb.append(obj);
				// } else {
				// sb.append("[NULL]");
			}
			if (i < lastItem) {
				sb.append(separator);
			}
		}
		sb.append(separator);
		return sb.toString();
	}

	/**
	 * 把null转变为空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String sNull(Object obj) {
		return obj != null ? obj.toString() : "";
	}

	/**
	 * 把null转变为空串
	 * 
	 * @param obj
	 * @param isconvert
	 * @return
	 */
	public static String sNull(Object obj, boolean isconvert) {
		if (isconvert)
			return obj != null ? obj.toString() : "";
		else
			return obj != null ? obj.toString() : null;
	}

	/**
	 * 将字符串按特定分隔符拆分为一个String数组 格式：abc^dddddd^5555555^aaa^bbbb^
	 * 
	 * @param str
	 *            以固定符号分割的字符串
	 * @param splitChar
	 *            分隔符
	 * @return 找不到时返回一个空的数组，判断数组的size
	 */
	public static String[] splitStr(String str, String splitChar) {
		if (str == null || str.length() == 0) {
			return new String[0];
		}
		if (splitChar == null || splitChar.length() == 0) {
			return new String[0];
		}
		int count = 1, pos = 0;

		while ((pos = str.indexOf(splitChar, pos)) >= 0) {
			count++;
			if (pos + splitChar.length() >= str.length()) {
				break;
			} else {
				pos = pos + splitChar.length();
			}
		}
		if (count == 1)
			return new String[0];
		String arrSplit[] = new String[count];

		if (count == 1) {
			arrSplit[0] = str;
		} else {
			int i = 0;
			while (i < count) {
				if (str.indexOf(splitChar) >= 0) {
					arrSplit[i] = str.substring(0, str.indexOf(splitChar));
				} else {
					arrSplit[i] = str;
					break;
				}
				str = str.substring(str.indexOf(splitChar) + splitChar.length());
				i++;
			}
		}
		return arrSplit;
	}

	/**
	 * 把双字节数字字符串转化成单子节
	 * 如果有单字节数字字符，该位保持不变
	 * @param s
	 * @return
	 * @version cliuyk 2008-10-27 WSSB_JXH_NB787_cliuyk
	 */
	public static String formatNumberToByteCharString(String s) {
		char[] c = s.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			if (c[i] >= 65248) {
				int t = c[i] - 65248;
				c[i] = (char) t;
			}
		}
		return new String(c);
	}

	/**
	 * 以下代码是将数字字符串转换为大写的中文字符串
	 * @param s
	 * @return
	 * @version cxiefeng 2010-10-27 DZGK211_CXIEFENG_NSWZZM
	 */

	private static final String[] hanzi = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	private static String strToChinese(String digit) {
		int value = 0;
		int exponent = 0;
		boolean hasZero = false;

		StringBuffer chineseDigit = new StringBuffer();
		value = Integer.parseInt(digit);

		for (exponent = 4; exponent >= 0; exponent--) {
			String[] unit = { "", "拾", "佰", "仟" };
			int divisor = (int) Math.pow(10, exponent);
			int result = value / divisor;
			if (result != 0) {
				chineseDigit.append(hanzi[result]);
				chineseDigit.append(unit[exponent]);
				hasZero = false;
			} else if (result == 0 && !hasZero) {
				chineseDigit.append(hanzi[0]);
				hasZero = true;
			} else {

			}
			value = value % divisor;
		}
		if (chineseDigit.substring(0, 1).equals("零")) {
			// return chineseDigit.deleteCharAt(0).toString();
			return chineseDigit.substring(0, 1) + "圆";
		} else {
			return chineseDigit.toString() + "圆";
		}
	}

	/**
	 * 在使用时一般是调用这个方法，来实现数字字符串转换为中文大写字符串
	 * @param digit
	 * @return
	 */
	public static String numStrToChinesBig(String digit) {
		String[] unit = { "圆", "万", "亿" };

		String chineseDigitWan = new String(); // 存储万位数字

		String chineseDigitYi = new String(); // 存储亿位数字

		StringBuffer chineseDigit = new StringBuffer();

		if (digit.length() <= 5) {
			return strToChinese(digit);
		} else {
			if (digit.length() <= 8) {
				chineseDigitWan = digit.substring(0, digit.length() - 4);
				chineseDigit.append(strToChinese(chineseDigitWan) + unit[1]
						+ strToChinese(digit.substring(digit.length() - 4, digit.length())));
			} else {
				chineseDigitYi = digit.substring(0, digit.length() - 8);
				chineseDigit.append(strToChinese(chineseDigitYi) + unit[2]
						+ strToChinese(digit.substring(digit.length() - 8, digit.length() - 4)) + unit[1]
						+ strToChinese(digit.substring(digit.length() - 4, digit.length())) + unit[0]);
			}
		}
		return chineseDigit.toString();
	}

	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String subModdleString(String totalStr, String startStr, String endStr) {
		int i1 = totalStr.indexOf(startStr);
		String str1 = totalStr.substring(0, i1);

		String str2 = StringUtil.replace(totalStr, str1, "");
		int i2 = str2.indexOf(endStr);
		String str3 = str2.substring(i2, str2.length());
		String str4 = StringUtil.replace(str2, str3, "");

		String str5 = StringUtil.replace(str4, startStr, "");
		return str5;
	}

	/**
	 * 将字符串内的NULL转换为空
	 * @param sXml
	 * @return
	 * @throws Exception
	 */
	public static String formatXmlString(String sXml) {
		return sXml.replaceAll(">null<", "><").replaceAll(">NULL<", "><");
	}

	/**
	 * 用特殊的字符连接字符串
	 * 
	 * @param strings
	 *            要连接的字符串数组
	 * @param spilit_sign
	 *            连接字符
	 * @return 连接字符串
	 */
	public static String stringConnect(String[] strings, String spilit_sign) {
		String str = "";
		for (int i = 0; i < strings.length; i++) {
			str += strings[i] + spilit_sign;
		}
		return str;
	}

	/**
	 * 过滤字符串里的的特殊字符
	 * 
	 * @param str 要过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String stringFilter(String str) {
		String[] str_arr = stringSpilit(str, "");
		for (int i = 0; i < str_arr.length; i++) {
			for (int j = 0; j < FilterChars.length; j++) {
				if (FilterChars[j][0].equals(str_arr[i]))
					str_arr[i] = FilterChars[j][1];
			}
		}
		return (stringConnect(str_arr, "")).trim();
	}

	/**
	 * 过滤脚本中的特殊字符（包括回车符(\n)和换行符(\r)）
	 * 
	 * @param str
	 *            要进行过滤的字符串
	 * @return 过滤后的字符串 2004-12-21 闫
	 */
	public static String stringFilterScriptChar(String str) {

		String[] str_arr = stringSpilit(str, "");
		for (int i = 0; i < str_arr.length; i++) {
			for (int j = 0; j < FilterScriptChars.length; j++) {
				if (FilterScriptChars[j][0].equals(str_arr[i]))
					str_arr[i] = FilterScriptChars[j][1];
			}
		}
		return (stringConnect(str_arr, "")).trim();
	}

	/**  
	 * 分割字符串  
	 * @param str 要分割的字符串  
	 * @param spilit_sign 字符串的分割标志  
	 * @return 分割后得到的字符串数组  
	 */
	public static String[] stringSpilit(String str, String spilit_sign) {
		String[] spilit_string = str.split(spilit_sign);
		if (spilit_string[0].equals("")) {
			String[] new_string = new String[spilit_string.length - 1];
			for (int i = 1; i < spilit_string.length; i++)
				new_string[i - 1] = spilit_string[i];
			return new_string;
		} else
			return spilit_string;
	}

	/**  
	 * 字符串字符集转换  
	 * @param str 要转换的字符串  
	 * @return 转换过的字符串  
	 */
	public static String stringTransCharset(String str) {
		String new_str = null;
		try {
			new_str = new String(str.getBytes("iso-8859-1"), "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_str;
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * @param value 指定的字符串
	 * @return 字符串的长度
	 */
	public static long getStrlength(String value) {
		long valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 
	 * 方法用途:判断字符串的长度是否大于最大限制
	 * 实现步骤:
	 *
	 * @param val
	 * @param maxLength
	 * @return
	 */
	public static boolean validateStringLength(String val, long maxLength) {
		boolean isPassValidate = true;
		long strLength = getStrlength(val);
		if (strLength > maxLength) {
			isPassValidate = false;
		}
		return isPassValidate;
	}

	/**
	  * 使用正则表达式判断字符串的合法性
	  * @Title: pattre
	  * @Description: TODO
	  * @param @param pattern
	  * @param @param str
	  * @param @return (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author shaoxiong
	  * @date 2015年9月15日
	  */
	public static boolean pattern(Pattern pattern, String str) {
		if (str == null) {
			return false;
		}
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * <批量正则校验>
	 * @param patternArr [正则数组]
	 * @param str [带校验字符]
	 * @return [返回结果boolean]
	 */
	public static boolean pattern(Pattern[] patternArr, String str)
	{
		boolean flag = true;
		for (Pattern pattern : patternArr)
		{
			flag = pattern(pattern, str);
			if (!flag)
			{
				break;
			}
		}
		return flag;
	}

	/**
     * 把字节码转换成16进制
     */
    public static String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
                    .substring(1).toUpperCase());
        }
        return retString.toString();
    }
	public static void main(String[] args) {
		// String str = "48B1517B142B5";
		// System.out.println(isSn(StrPattern.snPattern, str));
		// String str = "e83431f70e18cf7e";
		// System.out.println(isSn(StrPattern.randomPattern, str));
		String str = "FCDBB_340E5-4:1@HPC03.com";
//		str = null;
		System.out.println(pattern(StrPattern.emailPattern, str));
		// String str = "/test/12fds34/EW";
		// System.out.println(Pattre(StrPattern.uriPattern, str));
	}
	
	/**
	 * 获取S3所需的key
	 * @param fullPath
	 * @return
	 */
	public static String convertToS3Key(String fullPath)
	{
		String bucket = "/" + SystemConfig.getProperty("aws.s3.bucket") + "/";
		int startIdx = fullPath.indexOf(bucket) + bucket.length();
		String reStr = fullPath.substring(startIdx);
		return reStr;
	}
}
