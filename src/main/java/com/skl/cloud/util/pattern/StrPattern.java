package com.skl.cloud.util.pattern;

import java.util.regex.Pattern;

public interface StrPattern
{
	static Pattern snPattern = Pattern.compile("[0-9A-Z]*");// 匹配序列号
	static Pattern randomPattern = Pattern.compile("[0-9a-f]*");// 匹配random
	static Pattern userIdPattern = Pattern.compile("\\d+");// 匹配app用户id
	static Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_:-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+");// 匹配邮箱格式
	static Pattern uriPattern = Pattern.compile("(/[1-9a-zA-Z]+)+");// 匹配uri格式

	// 校验：序列号
	static Pattern patternSN = Pattern.compile("[0-9A-Z]*$");
	
	// 校验：UUID
	static Pattern patternUUID = Pattern.compile("^([0-9a-zA-z]{8}-[0-9a-zA-z]{4}-[0-9a-zA-z]{4}-[0-9a-zA-z]{4}-[0-9a-zA-z]{12})*$");

	// 校验：16进制编码
	static Pattern pattern16Band = Pattern.compile("^[0-9a-fA-F]*$");

	// 校验：数字
	static Pattern patternNumber = Pattern.compile("^[0-9]*$");

	// 校验：字母
	static Pattern patternLetter = Pattern.compile("^[a-zA-Z]*$");

	// 校验：字符
	static Pattern patternChar = Pattern.compile("^[0-9a-zA-Z]*$");

	// 校验：邮箱
	static Pattern patternEmail = Pattern.compile("^([a-zA-Z0-9_:-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+");

	// 校验：布尔型
	static Pattern patternBoolean = Pattern.compile("^(true|false)*$");

	// 校验：非法字符
	static Pattern patternIllegal = Pattern.compile("^[^<>'*]*$");

	// 校验：Mes数据的SKL PN
	static Pattern patternSKLPN = Pattern.compile("^[^?!@#$%^&+*/=]*$");

	
	
	// 校验：日期时间（年月日时分秒）
	static Pattern patternDateTime = Pattern
			.compile("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])*$");

	// 校验：日期时间（年月日）
	static Pattern patternDate = Pattern
			.compile("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))*$");

	// 校验：自定义-权限
	static Pattern patternCustomRole = Pattern.compile("^(admin|user)*$");
	
	// 校验：自定义-用户头像id
	static Pattern patternCustomPortraitId = Pattern.compile("^(([0-9]+)|(-1))*$");
	
	// 校验：自定义-触发状态
	static Pattern patternCustomEventState = Pattern.compile("^(active|inactive)*$");
	
	// 校验：自定义-客户端设备类型
	static Pattern patternCustomClientKind = Pattern.compile("^(0001|0002|0103|0104)*$");
	
	// 校验：自定义-(告警)描述
	static Pattern patternEventDescription = Pattern.compile("^(MotionAlarm|HighSoundAlarm|MiddleSoundAlarm|LowSoundAlarm|HighTemperatureAlarm|LowTemperatureAlarm|HighHumidityAlarm|LowHumidityAlarm|ActivityAlarm)*$");
	
	// 校验：自定义-第三方账户登录类型
	static Pattern patternCustomLoginKind = Pattern.compile("^(GOOGLE|FACEBOOK)*$");
	
	// 校验：自定义-audio-控制命令
	static Pattern patternCustomCommand = Pattern.compile("^(Play|Pause)*$");

	// 校验：自定义参数最大位数
	static Pattern patternLengthRange0to8 = Pattern.compile("^.{0,8}$");
	static Pattern patternLengthRange0to10 = Pattern.compile("^.{0,10}$");
	static Pattern patternLengthRange0to16 = Pattern.compile("^.{0,16}$");
	static Pattern patternLengthRange0to20 = Pattern.compile("^.{0,20}$");
	static Pattern patternLengthRange0to32 = Pattern.compile("^.{0,32}$");
	static Pattern patternLengthRange0to36 = Pattern.compile("^.{0,36}$");
	static Pattern patternLengthRange0to64 = Pattern.compile("^.{0,64}$");
}
