package com.skl.cloud.model.test;

import java.util.Date;

import com.skl.cloud.common.entity.IdEntity;


@SuppressWarnings("serial")
public class AppFeedbackInfo extends IdEntity {
	
	//用户id
    private Long userId;
    //用户的网络类型：1表示Wifi，2表示2G，3表示3G，4表示4G
	private Integer networkType;
	//提交名字
	private String submitName;
	//功能项
	private String featureItem;
	//反馈信息详细描述
	private String errorText;
	//创建数据时间
	private Date createDate;
	//最后一次修改数据时间
	private Date lUpdDate;
	
	//networkType的枚举类
	public enum netType {
		NETWORK_TYPE_WIFI(1, "Wifi"),
		NETWORK_TYPE_2G(2, "2G"),
		NETWORK_TYPE_3G(3, "3G"),
		NETWORK_TYPE_4G(4, "4G");

        private Integer intValue;
        private String stringValue;
  
        private netType(Integer intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }
        
        /**
         * 判断是否存在该网络类型，存在返回true
         * @param inputType
         * @return
         */
        public static boolean isExistType(String inputType){
            boolean ret = false;
            netType[] types = netType.values();
            for(netType type: types){
                if(type.getStringValue().equals(inputType)){
                    return true;
                }
            }
            return ret;
        }
        
        /**
         * 根据网络类型返回对应的枚举类
         * @param typeString
         * @return
         */
        public static netType getNetType(String typeString){
        	 netType[] types = netType.values();
        	 for(netType type: types){
                 if(type.getStringValue().equals(typeString)){
                     return type;
                 }
             }
			return null;
        }
        
        /**
         * 获取Integer类型的值
         * @return
         */
        public Integer getIntValue() {
            return intValue;
        }

        /**
         * 获取String类型的值
         * @return
         */
        public String getStringValue() {
            return stringValue;
        }
    }
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the networkType
	 */
	public Integer getNetworkType() {
		return networkType;
	}
	/**
	 * @param networkType the networkType to set
	 */
	public void setNetworkType(Integer networkType) {
		this.networkType = networkType;
	}
	/**
	 * @return the submitName
	 */
	public String getSubmitName() {
		return submitName;
	}
	/**
	 * @param submitName the submitName to set
	 */
	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}
	/**
	 * @return the featureItem
	 */
	public String getFeatureItem() {
		return featureItem;
	}
	/**
	 * @param featureItem the featureItem to set
	 */
	public void setFeatureItem(String featureItem) {
		this.featureItem = featureItem;
	}
	/**
	 * @return the errorText
	 */
	public String getErrorText() {
		return errorText;
	}
	/**
	 * @param errorText the errorText to set
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the lUpdDate
	 */
	public Date getlUpdDate() {
		return lUpdDate;
	}
	/**
	 * @param lUpdDate the lUpdDate to set
	 */
	public void setlUpdDate(Date lUpdDate) {
		this.lUpdDate = lUpdDate;
	}
	

}
