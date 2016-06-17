package com.skl.cloud.dao.user;

import java.util.List;

import com.skl.cloud.model.user.WechatUser;

/**
  * @ClassName: WechatUserMapper
  * @Description: Wechat User dao
  * @author yangbin
  * @date 2015年10月7日
 */
public interface WechatUserMapper extends BaseUserMapper<WechatUser>{

    /**
      * queryUserByOpenId(通过openId查询WechatUser的信息)
      * @Title queryUserByOpenId
      * @param openId
     */
    public WechatUser queryUserByOpenId(String openId);
    
    /**
     * 通过绑定的IPC设备的SN查询WechatUser的信息
     * @Title queryUserByBindSn
     * @param sn
    */
   public List<WechatUser> queryUserByBindSn(String sn);
    
    /**
      * @Title: isExistUser
      * @Description: 判断微信用户是否存在
      * @param  openId
      * @return Boolean
     */
    public Boolean isExistUser(String openId) ;
    
    /**
     * queryUserByUserId(通过userId查询WechatUser的信息)
     * @Title queryUserByUserId
     * @param userId
    */
    public WechatUser queryUserByUserId(Long userId);
}