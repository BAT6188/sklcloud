package com.skl.cloud.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.model.StreamingChannel;

public interface StreamResourcesService {

    /**
      * getStreamRemotePrefixUrl
      *
      * @Title: getStreamRemotePrefixUrl
      * @param  streamType
      * @throws ManagerException (参数说明)
      * @author yangbin
      * @date 2015年12月16日
     */
	public String getStreamRemotePrefixUrl(String streamType) throws ManagerException;
	
	/**
     * 对RTP流类型请求进行流资源调度
     * @param map
     * @param ipcMap
     * @return String
     */
	public String RTPLiveStreamControl(Map<String, String> map, Map<String, String> ipcMap);

	/**
     * 获取指定IPC的流通道的信息
     * @param cameraSerialno
     * @param id
     * @return StreamingChannel
     */
	public StreamingChannel getStreamChannelBySn(String cameraSerialno, String id);
	
	/**
     * 获取摄像头的局域网uri
     * @param cameraSerialno
     * @param id
     * @param streamType
     * @return String
     */
	public String getStreamUri(String cameraSerialno, String id, String streamType);
	
    /**
     * 获取stun的公有ip和端口,云端的环境信息配置
     * @param name
     * @return
     */
	public ResponseEntity<String> getAdress();
	
	/**
     * getLiveRelayURL(获取视频直播Relay的URL)
     * @Title: getLiveRelayURL
     * @param sn,userId,streamType
     * @return url
     * @throws ManagerException
     * @author fulin
     * @date 2015/12/11
     */
    public String getLiveRelayURL(String sN, Long userId, String streamType) throws ManagerException;
    
    /**
     * 根据请求进行流资源调度
     * @param name
     * @return String
     */
    public String StreamResourceSchedule(Map<String, String> xmlMap) ;
    
    /**
     * 获取组装Relay url需要的流服务ip信息
     * @param sn
     * @return
     */
    public String getRelayServerIp(String sn) throws ManagerException;
    
    /**
     * 获取组装Relay url需要的流服务ip信息
     * @param sn
     * @param channelId
     * @return String
     */
    public String getRelayServerIp(String sn, String channelId, int serverId) throws ManagerException;
    
    /**
	 * 通过uuid检验直播流url是否过期
	 * @param uuid
	 * @throws BusinessException
	 */
	public int checkLiveUrlValidity(String uuid) throws BusinessException;
	
	/**
	 * 
	  * insertIPCameraInfo(插入IPC相关信息)
	  * @Title: insertIPCameraInfo
	  * @Description: TODO
	  * @param @param cameraSerialno
	  * @param @param id (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月24日
	 */
	public void insertOrUpdateIPCameraInfo(String cameraSerialno, String id);
	
   /**
	 * @Description: 查询流处理子系统对应服务器的IP和Port
	 * @param uuid
	 * @return SubsysAddress
	 */
	public com.skl.cloud.model.SubsysAddress querySubsysIpPort(String uuid) throws ManagerException;

	/**
	 * 
	 * @Description: 查询接入子系统返回状态
	 * @param sn
	 * @param channelId
	 * @param eventID
	 * @param dateTime
	 * @return String
	 */
	public String queryEventStatus(String sn, String channelId, String eventID, String dateTime) throws ManagerException;
}
