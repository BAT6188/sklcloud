package com.skl.cloud.util.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.skl.cloud.util.pattern.Toolkits;

/**
 * 
 * @ClassName: DateUtil
 * @Description: TODO
 * @author wanggb
 * @date 2015年6月9日
 *
 */
public class DateUtil {
    
    // Date Format
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_FORMAT = "MM";
    public static final String TIME_FORMAT = "HHmm";
    public static final String DATE_TIME_1_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_1_HH_FORMAT = "yyyy-MM-dd HH";
    public static final String DATE_TIME_1_HHMM_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_1_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_2_FORMAT = "yyyy/MM/dd";
    public static final String DATE_TIME_2_HH_FORMAT = "yyyy/MM/dd HH";
    public static final String DATE_TIME_2_HHMM_FORMAT = "yyyy/MM/dd HH:mm";
    public static final String DATE_TIME_2_FULL_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_TIME_3_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_3_HH_FORMAT = "yyyyMMddHH";
    public static final String DATE_TIME_3_HHMM_FORMAT = "yyyyMMddHHmm";
    public static final String DATE_TIME_3_FULL_FORMAT = "yyyyMMddHHmmss";

    public static final String DATE_TIME_ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    
    
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	private static String class_name = "com.dc.portal.system.utils";

	private static String DateDefine_NullDate = "1900-01-01";

	public final static long HOUR = 3600000L;

	public final static long DAY = 24 * HOUR;

	/**
	 * 把时间转换为字符串
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            字符串格式
	 * @return
	 */
	public static String date2string(Date date, String pattern) {
		String sValue;
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		sValue = dateFormat.format(date);

		return sValue;
	}

	public static String iso2String(String isoStr) {
		isoStr = isoStr.substring(0, isoStr.lastIndexOf("."));
		isoStr = isoStr.replace("T", " ");
		return isoStr;
	}

	/**
	 * 把当前时间按日期型字符串输出
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String toCurrentDateStr(String yyyymmdd) {
		SimpleDateFormat df = new SimpleDateFormat(yyyymmdd);
		return df.format(new Date()).trim();
	}

	/**
	 * 把当前时间按日期型字符串输出
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String toDateStr(Date dt, String yyyymmdd) {
		SimpleDateFormat df = new SimpleDateFormat(yyyymmdd);
		return df.format(dt).trim();
	}

	/**
	 * 得到本月的当前日期，格式如：yyyy-MM-dd
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date()).trim();
	}

	/**
	 * 得到上个月的今天，格式如：yyyy-MM-dd
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String getUpMonthNowDate() {
		SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar aGregorianCalendar = new GregorianCalendar();
		aGregorianCalendar.set(Calendar.MONTH,
				aGregorianCalendar.get(Calendar.MONTH) - 1);
		String nowOfLastMonth = aSimpleDateFormat.format(aGregorianCalendar
				.getTime());
		return nowOfLastMonth;
	}

	/**
	 * 变换传入日期字符串 传入日期形式为"YYYY-MM-DD"样式。变换后的值为如"二○○六年一月五日"样式
	 * 
	 * @param forString
	 *            需变换的字符串
	 * @throws java.lang.Exception
	 *             日期变换异常 2006-04-18 add by yangle
	 */
	public static String toChineseDate(String forStr) throws Exception {
		String strValue = "";
		// 判断日期长度,null时返回空;""时返回" 年 月 日"
		if (forStr == null || forStr.length() == 0) {
			return "";
		}
		if (forStr.length() < 10) { // 日期不完整时，暂不处理
			return "      年    月    日";
		}
		// 截取年、月、日
		String strYear = forStr.substring(0, 4);
		String strMonth = forStr.substring(5, 7);
		String strDate = forStr.substring(8, 10);
		// 转换年
		for (int i = 0; i < 4; i++) {
			// 获取年的第X位
			char chrYear = strYear.charAt(i);
			switch (chrYear) {
			case '1':
				strValue += "一";
				break;
			case '2':
				strValue += "二";
				break;
			case '3':
				strValue += "三";
				break;
			case '4':
				strValue += "四";
				break;
			case '5':
				strValue += "五";
				break;
			case '6':
				strValue += "六";
				break;
			case '7':
				strValue += "七";
				break;
			case '8':
				strValue += "八";
				break;
			case '9':
				strValue += "九";
				break;
			case '0':
				strValue += "○";
				break;
			default:
				throw new Exception("转换的日期格式不正确。" + forStr);
			}
		}
		strValue += "年";
		// 转换月的第一位
		char chrMonth0 = strMonth.charAt(0);
		if (chrMonth0 == '1') {
			strValue += "十";
		}
		// 月的第二位
		char chrMonth1 = strMonth.charAt(1);
		switch (chrMonth1) {
		case '1':
			strValue += "一";
			break;
		case '2':
			strValue += "二";
			break;
		case '3':
			strValue += "三";
			break;
		case '4':
			strValue += "四";
			break;
		case '5':
			strValue += "五";
			break;
		case '6':
			strValue += "六";
			break;
		case '7':
			strValue += "七";
			break;
		case '8':
			strValue += "八";
			break;
		case '9':
			strValue += "九";
			break;
		}
		strValue += "月";
		// 天的第一位的转换
		char chrDate0 = strDate.charAt(0);
		switch (chrDate0) {
		case '1':
			strValue += "十";
			break;
		case '2':
			strValue += "二十";
			break;
		case '3':
			strValue += "三十";
		}
		// 天的第二位的转换
		char chrDate1 = strDate.charAt(1);
		switch (chrDate1) {
		case '1':
			strValue += "一";
			break;
		case '2':
			strValue += "二";
			break;
		case '3':
			strValue += "三";
			break;
		case '4':
			strValue += "四";
			break;
		case '5':
			strValue += "五";
			break;
		case '6':
			strValue += "六";
			break;
		case '7':
			strValue += "七";
			break;
		case '8':
			strValue += "八";
			break;
		case '9':
			strValue += "九";
			break;
		}
		strValue += "日";
		return strValue;
	}

	/**
	 * 输出中文日期：传入2003-05-01返回2003年5月1日
	 * 
	 * @param forDate
	 *            英文日期
	 * @return 中文日期表达
	 */
	public static String dateToChineseExpr(String forDate) {
		if (forDate == null || forDate.length() == 0) {
			return "";
		}
		if (forDate.length() < 10) {
			throw new IllegalArgumentException("日期非法：" + forDate);
		}
		String year = forDate.substring(0, 4);
		int mm, dd;
		try {
			mm = (new Integer("1" + forDate.substring(5, 7))).intValue() - 100;
			dd = (new Integer("1" + forDate.substring(8, 10))).intValue() - 100;
		} catch (Exception e) {
			throw new IllegalArgumentException("[日期非法]" + forDate + ":" + e);
		}
		StringBuffer out = new StringBuffer(year);
		out.append("年" + mm);
		out.append("月" + dd);
		out.append("日");
		return out.toString();
	}

	/**
	 * 计算日期偏移
	 * 
	 * @param forDate
	 *            基准日期
	 * @param forDays
	 *            偏移天数；（>0 向后偏 ） （<0向前偏）
	 * @return 偏移日期
	 */
	public static java.util.Date relativeDate(java.util.Date forDate,
			long forDays) {
		if (forDate == null) {
			return null;
		}
		if (forDays == 0) {
			return forDate;
		}

		java.util.Date dd = (java.util.Date) forDate.clone();
		if (forDays > 0) {
			for (long i = 1; i <= forDays; i++) {
				dd = tomorrow(dd);
			}
			return dd;
		}

		forDays = (-1) * forDays;
		for (long i = 1; i <= forDays; i++) {
			dd = yesterday(dd);
		}
		return dd;
	}

	/**
	 * 求@today的第二天
	 * 
	 * @param forToday
	 *            基准日期
	 * @return 第二天
	 */
	public static java.util.Date tomorrow(java.util.Date forToday) {
		long start;
		start = forToday.getTime() + DAY;
		return new java.util.Date(start);
	}

	/**
	 * 求前一条的日期
	 * 
	 * @param forToday
	 *            当天
	 * @return 前一天
	 */
	public static java.util.Date yesterday(java.util.Date forToday) {
		long start;
		start = forToday.getTime() - DAY;
		return new java.util.Date(start);
	}

	/**
	 * 增减分钟数（减则加‘-’）
	 * 
	 * @param date
	 *            原始日期
	 * @param minutes
	 *            增减分钟数
	 * @return
	 */
	public static Date addMinutes(Date date, String minutes) {
		Date rtn = new Date();
		rtn.setTime(date.getTime() + Long.valueOf(minutes).longValue() * 60
				* 1000);
		return rtn;
	}

	/**
	 * 把时间转换为字串
	 * 
	 * @param date
	 *            待转换的时间
	 * @param format
	 *            转换格式：yyyy-MM-dd
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
	 * 把时间转换为字串
	 * 
	 * @param string
	 *            待转换的时间
	 * @param format
	 *            转换格式：yyyy-MM-dd
	 * @return String
	 */
	public static String strDate2str(String sdate) {
		Date date = str2Date(sdate, "yyyy-MM-dd");
		return date2Str(date, "yyyy-MM-dd");
	}

	/**
	 * 把timestamp转换为字串
	 * 
	 * @param ts
	 *            待转换的
	 * @return String 转换timestamp字串（转换格式：yyyy-MM-dd HH24:mi:ss）
	 */
	public static String timestamp2Str(java.sql.Timestamp ts) {
		String timestamp = "";
		if (Toolkits.isNull(ts)) {
			return timestamp;
		}
		if (DateDefine_NullDate.equals(date2Str(ts, "yyyy-MM-dd"))) {
			return timestamp; // 如果是默认的空日期则返回""
		}

		timestamp = date2Str(ts, "yyyy-MM-dd HH:mm:ss"); // 注意这里如果不能用HH24:mi:ss,会报错
		return timestamp;
	}

	/**
	 * 把字串转换为日期
	 * 
	 * @param sdate
	 *            字串形式的日期（最长的格式：yyyy-MM-dd HH:mm:ss.SSS）
	 * @param format
	 *            字串格式
	 * @return Date 转换为日期类型
	 */
	public static java.util.Date str2Date(String sdate, String format) {
		if (StringUtil.isEmpty(sdate)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date new_date = null;
		try {
			new_date = df.parse(sdate);
		} catch (Exception e) {
			logger.error(class_name + "str2Date()" + "把字串转换为日期错误：" + sdate
					+ "，需转换的日期格式为：" + format);
		}
		return new_date;
	}

	/**
	 * 方法用途: 将时间转换为yyyy-MM-dd格式的时间<br>
	 *
	 * @param date
	 * @return 返回java.util.Date类型的数据
	 */
	public static String getYMDByDate(Date date) {
		String dateStr = DateUtil.date2Str(date, "yyyy-MM-dd");
		return dateStr;
	}

	/**
	 * 把字串转换为日期（SqlDate类型）
	 * 
	 * @param sdate
	 *            字串形式的日期（格式：yyyy-MM-dd）
	 * @return sql.Date 转换为日期类型（SqlDate类型）
	 */
	public static java.sql.Date str2SqlDate(String sdate) {
		if (StringUtil.isEmpty(sdate)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date utilDate = null;
		java.sql.Date sqlDate = null;
		try {
			utilDate = df.parse(sdate);
			sqlDate = new java.sql.Date(utilDate.getTime());
		} catch (Exception e) {
			logger.error(class_name + "str2SqlDate()" + "把字串转换为SqlDate日期错误："
					+ sdate);
		}
		return sqlDate;
	}

	/**
	 * 计算指定年月的前后的年月
	 * 
	 * @param yearMon
	 *            传入年月
	 * @param relayMons
	 *            增加、减少的月数
	 * @return String 返回新的年月
	 */
	public static String relayYM(String yearMon, int relayMons) {
		String newYM = "";
		try {
			newYM = date2Str(
					dateAdd(str2Date(yearMon, "yyyyMM"), Calendar.MONTH,
							relayMons), "yyyyMM");
		} catch (Exception e) {
		}
		return newYM;
	}

	/**
	 * 返回两个日期之间的月数
	 * 
	 * @param startDate
	 *            开始日期（前）
	 * @param endDate
	 *            结束日期（后）
	 * @return int 月数
	 */
	public static int monthsBetween(Date startDate, Date endDate) {
		GregorianCalendar gcStart = new GregorianCalendar();
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcStart.setTime(startDate);
		gcEnd.setTime(endDate);
		return (gcEnd.get(Calendar.YEAR) - gcStart.get(Calendar.YEAR)) * 12
				+ gcEnd.get(Calendar.MONTH) - gcStart.get(Calendar.MONTH);
	}

	private static class ElapsedTime {
		public int getDays(GregorianCalendar g1, GregorianCalendar g2) {
			int elapsed = 0;
			GregorianCalendar gc1, gc2;

			if (g2.after(g1)) {
				gc2 = (GregorianCalendar) g2.clone();
				gc1 = (GregorianCalendar) g1.clone();
			} else {
				gc2 = (GregorianCalendar) g1.clone();
				gc1 = (GregorianCalendar) g2.clone();
			}

			gc1.clear(Calendar.MILLISECOND);
			gc1.clear(Calendar.SECOND);
			gc1.clear(Calendar.MINUTE);
			gc1.clear(Calendar.HOUR_OF_DAY);

			gc2.clear(Calendar.MILLISECOND);
			gc2.clear(Calendar.SECOND);
			gc2.clear(Calendar.MINUTE);
			gc2.clear(Calendar.HOUR_OF_DAY);

			while (gc1.before(gc2)) {
				gc1.add(Calendar.DATE, 1);
				elapsed++;
			}
			return elapsed;
		}

		public int getMonths(GregorianCalendar g1, GregorianCalendar g2) {
			int elapsed = 0;
			GregorianCalendar gc1, gc2;

			if (g2.after(g1)) {
				gc2 = (GregorianCalendar) g2.clone();
				gc1 = (GregorianCalendar) g1.clone();
			} else {
				gc2 = (GregorianCalendar) g1.clone();
				gc1 = (GregorianCalendar) g2.clone();
			}

			gc1.clear(Calendar.MILLISECOND);
			gc1.clear(Calendar.SECOND);
			gc1.clear(Calendar.MINUTE);
			gc1.clear(Calendar.HOUR_OF_DAY);
			gc1.clear(Calendar.DATE);

			gc2.clear(Calendar.MILLISECOND);
			gc2.clear(Calendar.SECOND);
			gc2.clear(Calendar.MINUTE);
			gc2.clear(Calendar.HOUR_OF_DAY);
			gc2.clear(Calendar.DATE);

			while (gc1.before(gc2)) {
				gc1.add(Calendar.MONTH, 1);
				elapsed++;
			}
			return elapsed;
		}
	}

	/**
	 * 根据增加or减少的时间得到新的日期
	 * 
	 * @param curDate
	 *            当前日期
	 * @param field
	 *            需操作的'年'or'月'or'日' (Calendar.MONTH Calendar.DATE )
	 * @param addNumber
	 *            增加or减少的时间
	 * @return Date 新的日期
	 */
	public static Date dateAdd(Date curDate, int field, int addNumber) {
		GregorianCalendar curGc = new GregorianCalendar();
		curGc.setTime(curDate);
		curGc.add(field, addNumber);
		return curGc.getTime();
	}

	/**
	 * 判断firstDate是否不在lastDate之后
	 * 
	 * @param lastDate
	 *            开始日期
	 * @param firstDate
	 *            结束日期
	 * @param dateFormat
	 *            eg:"yyyyMM" ; "yyyy-MM-dd"
	 * @return boolean
	 * @throws GeneralException
	 */
	public static boolean notAfter(String firstDate, String lastDate,
			String dateFormat) throws Exception {
		try {
			if (lastDate.equals(firstDate)) {
				return true;
			}
			Date first = str2Date(firstDate, dateFormat);
			Date last = str2Date(lastDate, dateFormat);
			return first.before(last);
		} catch (Exception ex) {
			throw new Exception("err.utility.date.format");
		}
	}

	/**
	 * 判断查询日期是否在指定日期之间
	 * 
	 * @param firstDate
	 *            开始日期
	 * @param lastDate
	 *            结束日期
	 * @param queryDate
	 *            查询日期
	 * @param dateFormat
	 *            eg:"yyyyMM" ; "yyyy-MM-dd"
	 * @return boolean
	 */
	public static boolean isBetweenDate(String firstDate, String lastDate,
			String queryDate, String dateFormat) {
		try {
			if (!StringUtil.isEmpty(queryDate)
					&& (!notAfter(firstDate, queryDate, dateFormat) || !notAfter(
							queryDate, lastDate, dateFormat))) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 根据输入年月得到该年月的包含最后一天的String型日期
	 * 
	 * @param yearMonth
	 *            年月yyyyMM
	 * @return String yyyy-MM-dd
	 */
	public static String getYmEndDay(String yearMonth) {
		return date2Str(
				dateAdd(str2Date(getYmFirstDay(relayYM(yearMonth, 1)),
						"yyyy-MM-dd"), Calendar.DATE, -1), "yyyy-MM-dd");
	}

	/**
	 * 根据输入年月得到该年月的包含第一天的String型日期
	 * 
	 * @param yearMonth
	 *            年月yyyyMM
	 * @return String yyyy-MM-dd
	 */
	public static String getYmFirstDay(String yearMonth) {
		return date2Str(str2Date(yearMonth, "yyyyMM"), "yyyy-MM-dd");
	}

	/**
	 * 根据输入的String日期(yyyy-MM-dd)，得出年月
	 * 
	 * @param strDate
	 *            输入的String日期
	 * @param format
	 *            输入日期的格式（如：yyyy-MM-dd HH:mm:ss.SSS）
	 * @return String 年月
	 */
	public static String getStrDateYM(String strDate, String format) {
		if (StringUtil.isEmpty(format)) {
			format = "yyyy-MM-dd";
		}
		if (DateDefine_NullDate.equals(strDate)) {
			return "190001";
		}
		return date2Str(str2Date(strDate, format), "yyyyMM");
	}

	/**
	 * 取当前月的第一天
	 * 
	 * @return String yyyy-mm-dd
	 */
	public static String getFirstDayOfThisMonth() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		return getYmFirstDay(year + month);
	}

	/**
	 * 取当前月的最后一天
	 * 
	 * @return String yyyy-mm-dd
	 */
	public static String getLastDayOfThisMonth() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);

		return getYmEndDay(year + month);
	}

	/**
	 * 日期格式转换 如果是YYYY-MM-DD格式的字符串则转换成YYYYMMDD 如果是YYYYMMDD格式的字符串则转换成YYYY-MM-DD
	 * 
	 * @param forRq
	 *            String 要转的日期字符串
	 * @return String
	 */
	public static String convertRQ(String forRq) {
		if (forRq.length() == 10) {
			String year = forRq.substring(0, 4);
			String month = forRq.substring(5, 7);
			String day = forRq.substring(8);
			return year + month + day;
		} else if (forRq.length() == 8) {
			String year = forRq.substring(0, 4);
			String month = forRq.substring(4, 6);
			String day = forRq.substring(6);
			return year + "-" + month + "-" + day;
		} else {
			return "";
		}
	}

	/**
	 * <p>
	 * 给定年数和月数，从而获得这个月的天数，如果正好是当年当月，返回现在年月日中日的号码
	 * </p>
	 * 
	 * @param foryear
	 *            传入的年份参数
	 * @param formonth
	 *            出入的月份参数
	 * @return 计算出来的天数
	 */
	public static int getDayNumFromMonth(String foryear, String formonth) {

		int year = Integer.parseInt(foryear);// 转换类型
		int month = Integer.parseInt(formonth);// 转换类型
		int daynum = 0;
		if (Integer.parseInt(foryear) == Calendar.getInstance().get(
				Calendar.YEAR)
				&& Integer.parseInt(formonth) == Calendar.getInstance().get(
						Calendar.MONTH) + 1) {
			return Calendar.getInstance().get(Calendar.DATE);// 如果正好是当年当月，返回现在年月日中日的号码
		}
		if (year % 4 == 0) {// 如果是润年
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				daynum = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				daynum = 30;
				break;
			default:
				daynum = 29;
				break;
			}
		} else {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				daynum = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				daynum = 30;
				break;
			default:
				daynum = 28;
				break;
			}
		}
		return daynum;
	}

	/**
	 * 得到上月的第一天（yyyymmdd）
	 * 
	 */
	public static String getFirstDayofLastMonth() {
		String sssq_q = DateUtil.relayYM(
				DateUtil.date2Str(new Date(), "yyyyMM"), -1);
		sssq_q += "01";
		return sssq_q;
	}

	/**
	 * 得到上月的最后一天（yyyymmdd）
	 * 
	 */
	public static String getEndDayofLastMonth() {
		String sssq_z = DateUtil.relayYM(
				DateUtil.date2Str(new Date(), "yyyyMM"), -1);
		sssq_z = DateUtil.convertRQ(DateUtil.getYmEndDay(sssq_z));
		return sssq_z;
	}

	/**
	 * 得到上月的第一天（yyyymmdd）
	 * 
	 * @param yyyyMM
	 *            当前年月
	 * @return
	 */
	public static String getFirstDayofLastMonth(String yyyyMM) {
		String sssq_q = DateUtil.relayYM(yyyyMM, -1);
		sssq_q += "01";
		return sssq_q;
	}

	/**
	 * 得到上月的最后一天（yyyymmdd）
	 * 
	 * @param yyyyMM
	 *            当前年月
	 * @return
	 */
	public static String getEndDayofLastMonth(String yyyyMM) {
		String sssq_z = DateUtil.relayYM(yyyyMM, -1);
		sssq_z = DateUtil.convertRQ(DateUtil.getYmEndDay(sssq_z));
		return sssq_z;
	}

	/**
	 * 转换日期字符串格式，(yyyymmdd)-->(yyyy-mm-dd) 如果传入的是yyyy-mm-dd类型，直接返回
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String formatToyyyy_mm_dd(String yyyymmdd) {

		if (yyyymmdd.indexOf("-") < 0) {
			if (yyyymmdd.length() < 8) {
				return yyyymmdd;
			}
			return yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6)
					+ "-" + yyyymmdd.substring(6, 8);
		} else {
			return yyyymmdd;
		}
	}

	/**
	 * 转换日期字符串格式，(yyyy-mm-dd)-->(yyyymmdd) 如果传入的是yyyymmdd类型，直接返回
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String formatToyyyymmdd(String yyyy_mm_dd) {
		if (yyyy_mm_dd.indexOf("-") < 0) {
			return yyyy_mm_dd;
		}
		if (yyyy_mm_dd.length() < 10) {
			return yyyy_mm_dd;
		}
		return yyyy_mm_dd.substring(0, 4) + yyyy_mm_dd.substring(5, 7)
				+ yyyy_mm_dd.substring(8, 10);
	}

	/**
	 * 得到下月的第一天（yyyy-mm-dd）
	 * 
	 * @return
	 */
	public static String getFirstDayofNextMonth() {
		String sssq_q = DateUtil.relayYM(
				DateUtil.date2Str(new Date(), "yyyyMM"), +1);
		sssq_q += "01";
		sssq_q = DateUtil.formatToyyyy_mm_dd(sssq_q);
		return sssq_q;
	}

	/**
	 * 月份移动运算函数
	 * 
	 * @param forDate
	 *            起始时间
	 * @param forMoveMonths
	 *            月数量
	 * @return 偏移时间
	 * @throws Exception
	 *             日期处理异常
	 */
	public static String moveMonth(String forDate, int forMoveMonths)
			throws Exception {
		if (!chkYMD(forDate)) {
			throw new Exception("日期格式不符合YYYY-MM-DD格式");
		}
		Calendar cal = toCalendar(forDate);
		cal.add(Calendar.MONTH, forMoveMonths);
		return date2Str(cal.getTime(), "YYYY-MM-DD");
	}

	/**
	 * 检验date参数是否符合YYYY-MM-DD格式
	 * 
	 * @param forDate
	 *            日期
	 * @return true 符合格式
	 */
	public static boolean chkYMD(String forDate) {
		if (forDate.length() != 10) {
			return false;
		}
		if (!forDate.substring(4, 5).equals("-")) {
			return false;
		}
		if (!forDate.substring(7, 8).equals("-")) {
			return false;
		}
		return true;
	}

	/**
	 * 将字符形式的日期转换成为日历
	 * 
	 * @param forDate
	 *            日期
	 * @return 日历
	 */
	public static Calendar toCalendar(String forDate) {
		Calendar cal = Calendar.getInstance();
		int year = getYear(forDate);
		int month = getMonth(forDate);
		int day = getDay(forDate);
		cal.set(year, month - 1, day);
		return cal;
	}

	/**
	 * 取得年
	 * 
	 * @param forDate
	 *            日期
	 * @return 年
	 */
	public static int getYear(String forDate) {
		return Integer.parseInt(forDate.substring(0, 4));
	}

	/**
	 * 取得月
	 * 
	 * @param forDate
	 *            日期
	 * @return 月份
	 */
	public static int getMonth(String forDate) {
		return Integer.parseInt(forDate.substring(5, 7));
	}

	/**
	 * 取得天
	 * 
	 * @param forDate
	 *            日期
	 * @return 天
	 */
	public static int getDay(String forDate) {
		return Integer.parseInt(forDate.substring(8, 10));
	}

	/**
	 * 得到当前年份 @retrun String 当前年份
	 * 
	 */

	public static String getCurrentYear() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.YEAR) + "";
	}

	/**
	 * 通过日期字符串获得年
	 * 
	 * @param forDate
	 *            2011-06-11
	 * @return year
	 */
	public static String getDateYear(String forDate) {
		String year = forDate.substring(0, 4);
		return year;
	}

	/**
	 * 通过日期获得月
	 * 
	 * @param forDate
	 *            yyyy-mm-dd
	 * @return month
	 */
	public static String getDateMonth(String forDate) {
		String month = "";
		try {
			String temp = forDate.substring(5, forDate.length());
			month = temp.substring(0, temp.indexOf("-"));
			if (month.length() == 1) {
				month = '0' + month;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return month;
	}

	/**
	 * 通过日期获得日
	 * 
	 * @param forDate
	 *            yyyy-mm-dd
	 * @return day
	 */
	public static String getDateDay(String forDate) {
		String day = "";
		try {
			String temp = forDate.substring(5, forDate.length());
			day = temp.substring(temp.indexOf("-") + 1, temp.length());
			if (day.length() == 1) {
				day = '0' + day;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 通过上个月的最后一天
	 * 
	 * @param forDate
	 *            yyyy-mm-dd
	 * @return day
	 */
	public static String getLastDayForLastMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return date2Str(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 通过上个月的第一天
	 * 
	 * @param forDate
	 *            yyyy-mm-dd
	 * @return day
	 */
	public static String getFirstDayForLastMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		return date2Str(cal.getTime(), "yyyy-MM-dd");
	}

	// 把一个日历转换成 ISO 8601日期格式
	public static String fromCalendarToISO8601(Calendar calendar) {
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	public static void main(String[] args) {
		Date date = new Date();
		DateUtil dateUtil = new DateUtil();
		System.out.println(date2string(date, "yyyy-MM-dd HH:mm:ss"));

		String str = "2015-06-17T15:35:12.000+08:00";
		System.out.println(iso2String(str));
	}
}