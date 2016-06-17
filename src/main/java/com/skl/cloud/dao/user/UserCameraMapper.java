package com.skl.cloud.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.UserCameraQuery;

public interface UserCameraMapper {
    
    /**
     * insert(保存用户与设备的关联信息)
     * @Title insert
     * @param userCamera
    */
   public void insert(UserCamera userCamera);

   /**
    * delete(删除用户与设备的关联信息)
    * @Title delete
    * @param userCamera
   */
   public void delete(UserCamera userCamera);
   
   /**
    * update(更新用户与设备的关联信息)
    * @Title update
    * @param userCamera
   */
   public void update(UserCamera userCamera);
   
   /**
    * queryUserCamera(查询用户与设备的关联信息)
    * @Title queryUserCamera
    * @param sn
    * @param userId
   */
   public List<UserCamera> queryUserCamera(UserCameraQuery userCameraQuery);
   
   /**
    * queryUserBindDeviceList(查询用户与设备的关联信息)
    * @Title queryUserCamera
    * @param userCamera
   */
   public List<IPCamera> queryUserBindDeviceList(UserCameraQuery userCameraQuery);
   
   /**
    * queryIPCameraLinkedUsers(查询设备所有关联的用户信息)
    * @Title queryUserCamera
    * @param userCamera
   */
   public List<AppUser> queryIPCameraLinkedUsers(UserCameraQuery userCameraQuery);
   
   /**
    * 根据微信openId和设备SN获得绑定信息
    * @param openId
    * @param sn 
    * @return
    */
   public UserCamera getUserCameraByOpenIdAndSN(@Param("openId") String openId, @Param("sn") String sn);
   
}