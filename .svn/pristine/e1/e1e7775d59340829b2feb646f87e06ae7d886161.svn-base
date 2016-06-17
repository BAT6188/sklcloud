package com.skl.cloud.util.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.pattern.StrPattern;

/**
 * @Package com.skl.cloud.util.validator
 * @Title: [类名称]
 * @Description: [类说明]
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年12月15日
 * @version V1.0
 */
public class XmlElementValidator {
    
    /** xml请求参数校验 */
    public static Map<String, Pattern[]> checkParaMap = new HashMap<String, Pattern[]>()
    {
        {
            // 系列：序列号
            put("SN", new Pattern[]{StrPattern.patternSN, StrPattern.patternLengthRange0to32});
            
            // 系列：uuid
            put("uuid", new Pattern[] { StrPattern.patternUUID, StrPattern.patternLengthRange0to36 });

            // 系列：数字
            put("userId", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to20});
            put("circleId", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to20});
            put("portraitId", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to16});
            put("regionID", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to16});
            put("activePostCount", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to10});
            put("alertId", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to32});
			put("fileLen", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to16});
            put("delay", new Pattern[]{StrPattern.patternNumber, StrPattern.patternLengthRange0to8});

            // 系列：16进制字符
            put("random", new Pattern[]{StrPattern.pattern16Band, StrPattern.patternLengthRange0to16});
            put("openID", new Pattern[]{StrPattern.pattern16Band, StrPattern.patternLengthRange0to32});
            put("oldPassword ", new Pattern[]{StrPattern.pattern16Band, StrPattern.patternLengthRange0to32});
            put("newPassword ", new Pattern[]{StrPattern.pattern16Band, StrPattern.patternLengthRange0to32});
            
            // 系列：字符
            //put("id", new Pattern[]{StrPattern.patternLetter});

            // 系列：布尔
            put("isIPC", new Pattern[]{StrPattern.patternBoolean});
            put("stream", new Pattern[]{StrPattern.patternBoolean});
            put("playback", new Pattern[]{StrPattern.patternBoolean});
            put("notification", new Pattern[]{StrPattern.patternBoolean});
            put("enable", new Pattern[]{StrPattern.patternBoolean});
            put("execute", new Pattern[]{StrPattern.patternBoolean});

            // 系列：邮箱
            put("email", new Pattern[]{StrPattern.patternEmail, StrPattern.patternLengthRange0to64});

            // 系列：中文字符（非法字符过滤）
            put("circleName", new Pattern[]{StrPattern.patternIllegal, StrPattern.patternLengthRange0to64});
            put("newCircleName", new Pattern[]{StrPattern.patternIllegal, StrPattern.patternLengthRange0to64});
            put("nickName", new Pattern[]{StrPattern.patternIllegal, StrPattern.patternLengthRange0to64});
            put("fileName", new Pattern[]{StrPattern.patternIllegal, StrPattern.patternLengthRange0to64});
            
            // 系列：日期时间
            put("recordDate", new Pattern[]{StrPattern.patternDate});
            put("eventTime", new Pattern[]{StrPattern.patternDateTime});
            put("dateTime", new Pattern[]{StrPattern.patternDateTime});
            
            // 系列：自定义（权限）
            put("circleRole", new Pattern[]{StrPattern.patternCustomRole});
            
            // 系列：自定义（用户头像id）
            put("portraitId", new Pattern[]{StrPattern.patternCustomPortraitId, StrPattern.patternLengthRange0to16});
            
            // 系列：自定义（触发状态）
            put("eventState", new Pattern[]{StrPattern.patternCustomEventState});
            
            // 系列：自定义（用户对应设备类型）
            put("clientKind", new Pattern[]{StrPattern.patternCustomClientKind});
            
            // 系列：自定义（第三方账户登录类型）
            put("loginKind", new Pattern[]{StrPattern.patternCustomLoginKind});
            
            // 系列：自定义（产品型号）
            put("cameraModel", new Pattern[]{StrPattern.patternCustomClientKind});
            
            // 系列：自定义（(告警)描述）
            put("eventDescription", new Pattern[]{StrPattern.patternEventDescription});
            
			// 系列：自定义（audio-控制命令）
            put("command", new Pattern[]{StrPattern.patternCustomCommand});
			
            // 系列：字符
            put("eventId", new Pattern[]{Constants.patternCustomEvent});
        }
    };
    
}
