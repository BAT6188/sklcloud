package com.skl.cloud.util.constants;

public enum S3ServiceType {
    USER_VIDEO("user_video","定义用于存放用户的视频文件"),
	USER_MUSIC("user_music","定义用于存放用户的音乐文件"),
	USER_PICTURE("user_picture","定义用于存放用户的图片文件"),
	USER_TEMP("user_temp","定义用于存放用户的临时文件"),
	DEVICE_VIDEO("device_video","定义用于存放设备的视频文件，此目录下面包含视频录制截图的图片目录，分别存放头中尾三张图片"),
	DEVICE_MUSIC("device_music","定义用于存放设备的音乐文件"),
	DEVICE_PICTURE("device_picture","定义用于存放设备的图片文件"),
	DEVICE_OFFLINE_PICTURE("device_offline_picture","定义用于存放系统的设备离线图片文件"),
	DEVICE_TEMP("device_temp","定义用于存放设备的临时文件"),
	DEVICE_STORY("device_story","定义用于存放设备的故事文件"),
	DEVICE_NOTICE("device_notice","定义用于存放设备的提醒文件"),
	SYSTEM_SHARE_PICTURE("system_share_picture","定义用于存放系统的分享图片文件"),
	SYSTEM_VIDEO("system_video","定义用于存放系统的视频文件"),
	SYSTEM_MUSIC("system_music","定义用于存放系统的音乐文件"),
	SYSTEM_STORY("system_story","定义用于存放系统的故事文件"),
	SYSTEM_NOTICE("system_notice","定义用于存放系统的提醒文件"),	
	SYSTEM_PICTURE("system_picture","定义用于存放系统的图片文件"),
	SYSTEM_WEB_CONTENT("system_web_content","定义用于存放系统的静态网页文件"),	
	SYSTEM_DEVICE_UPGRADE("system_device_upgrade","定义用于存放系统的设备升级文件"),
	SYSTEM_DEVICE_ALARM_PICTURE("system_device_alarm_picture","定义用于存放系统的设备离线图片文件"),
	SYSTEM_DEVICE_ALARM_VIDEO("system_device_alarm_video","定义用于存放系统的设备离线视频文件"),
	SYSTEM_USER_PORTRAIT("system_user_portrait","定义用于存放用户头像文件"),
	SYSTEM_TEMP("system_temp","定义用于存放系统的临时文件"),
	SYSTEM_DEVICE_WECHAT_PICTURE("system_device_wechat_picture","定义用于存放系统的设备微信图片文件");
	
	private String type;
	private String desc;
	
    private S3ServiceType(String type, String desc){
	    this.type = type;
	    this.desc = desc;
	}
    
    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
    
    public static S3ServiceType getS3ServiceType(String  serviceType){
        S3ServiceType[] types = S3ServiceType.values();
        for(S3ServiceType type : types){
            if(type.getType().equals(serviceType)){
                return type;
            }
        }
        return null;
    }
}
