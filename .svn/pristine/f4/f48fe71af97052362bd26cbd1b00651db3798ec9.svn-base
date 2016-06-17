package com.skl.cloud.common.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	/**
	 * 转为ISO8601格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getISO8601(Date date) {
		DateTime dt = new DateTime(date);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		return fmt.print(dt);
	}
	
	/**
	 * 转为ISO8601格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getISO8601(Date date, String timeZone) {
		DateTime dt = new DateTime(date);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		if (StringUtils.isNotBlank(timeZone)) {
			int timeZoneHour = 0;
			int timeZoneMinutes = 0;
			if (timeZone != null && timeZone.contains(":")) {
				timeZoneHour = Integer.valueOf(timeZone.substring(0, timeZone.indexOf(":"))).intValue();
				timeZoneMinutes = Integer.valueOf(timeZone.substring(timeZone.indexOf(":") + 1, timeZone.length()))
						.intValue();
			}
			DateTimeZone zone = DateTimeZone.forOffsetHoursMinutes(timeZoneHour, timeZoneMinutes);
			dt = dt.withZone(zone);
		} 
		return fmt.print(dt);
	}
	
	
	/**
	 * 转为ISO8601格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getCurrentISO8601(DateTimeZone zone) {
		DateTime dt = new DateTime();
		dt = dt.withZone(zone);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		return fmt.print(dt);
	}
	
	/**
	 * 转为ISO8601格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getCurrentISO8601() {
		DateTime dt = new DateTime(DateTimeZone.UTC);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		return fmt.print(dt);
	}
	
	/**
	 * 把一个日历转换成 ISO 8601日期格式
	 * 
	 * @param date
	 * @return
	 */
    public static String fromCalendarToISO8601(Calendar calendar) {
        Date date = calendar.getTime();
        return getISO8601(date);
    }
	
	public static void main(String[] args) {
	    String timeZone = "+8:00";
        int timeZoneHour = 0;
        int timeZoneMinutes = 0;
        if (timeZone != null && timeZone.contains(":")) {
            timeZoneHour = Integer.valueOf(timeZone.substring(0, timeZone.indexOf(":"))).intValue();
            timeZoneMinutes = Integer.valueOf(timeZone.substring(timeZone.indexOf(":") + 1, timeZone.length()))
                    .intValue();
        }
        String s = DateUtils.getCurrentISO8601(DateTimeZone.forOffsetHoursMinutes(timeZoneHour, timeZoneMinutes));
		System.out.println(s);
		System.out.println(getCurrentISO8601());
		System.out.println(getISO8601(new Date(), "+9:00"));
		System.out.println(fromCalendarToISO8601(Calendar.getInstance()));
		
	}
}
