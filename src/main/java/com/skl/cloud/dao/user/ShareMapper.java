package com.skl.cloud.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.user.Share;

public interface ShareMapper {
    
    /**
     * insert(保存IPC分享的信息)
     * @Title insert
     * @param Share
    */
   public void insert(Share share);

   /**
    * delete(删除IPC分享的信息)
    * @Title delete
    * @param id
   */
   public void delete(Long id);
   
   /**
    * update(更新IPC分享的信息)
    * @Title update
    * @param wechatUser
   */
   public void update(Share share);

   /**
     * queryShareById(通过id查询IPC Share的信息)
     * @Title queryShareById
     * @param id
    */
   public Share queryShareById(Long id);
   
   /**
    * queryShareList(通过条件查询IPC设备所有Share的信息)
    * @Title queryShareById
    * @param id
   */
  public List<Share> queryShareList(Share share);
  
  /**
   * queryShareByUUID(通过uuid查询IPC Share的信息)
   * @Title queryShareByUUID
   * @param sid
  */
  public Share queryShareByUUID(String sid);
  
  /**
   * 获取直播流服务子系统的EC2的外网IP
   * @param sn
   * @return
   */
  public String getServerPublicIP(String sn);
  
  /**
   * 获取永不过期的直播流分享的uuid
   * @param sn
   * @return
   */
  public String getUuidBySn(String sn);
  
  /**
   * 获取组装Relay url需要的流服务ip信息
   * @param sn
 * @param serverId 
   * @return
   */
  public String getCreatRelayUrlInfo (@Param("sn") String sn, @Param("channelId") String channelId, @Param("serverId") int serverId);
  
  /**
   * 更新表的uuid和userId
   * @param sn,userId,uuid
   * @return 
   */
  public void updateUuidUserId(@Param("sn") String sn, @Param("userId") Long userId, @Param("uuid") String uuid);
  
   /**
    * 
    * getLiveStreamServiceInfo(获取直播流服务子系统的状态信息)
    * @Title: getLiveStreamServiceInfo
    * @Description: TODO
    * @param @param sn
    * @param @return (参数说明)
    * @return String (返回值说明)
    * @throws (异常说明)
    * @author wangming
    * @date 2016年3月26日
   */
  public String getLiveStreamServiceInfo(String sn);
  
  /**
   * 删除有关某ipc的所有分享（但是由于获取ipc的relay url时也会在此表生成一条记录,
   * 对应的是start_date与end_date都是NULL，此条记录不删）
   * @param sn
   */
  public void deleteBySnExceptRelay (String sn);
}