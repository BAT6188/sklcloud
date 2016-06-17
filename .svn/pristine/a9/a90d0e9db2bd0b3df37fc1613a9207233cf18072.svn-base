package com.skl.cloud.service.ipc;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.web.dto.ShareStatusFO;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.ipc.IPCameraSub;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.Share;
import com.skl.cloud.remote.ipc.dto.ipc.RequestCurrentPictrueIO;

/**
 * @ClassName: IPCameraService
 * @Description: IPC Service interface class, 
 * IPC's feature and operation in this class, including IPCamera information and setting.
 * @author yangbin
 * @date 2015年10月8日
*/
public interface IPCameraService {

    /**
     * 更新IPC的信息
     * @param ipcamera
     * @throws BusinessException
    */
    public void updateIPCamera(IPCamera ipcamera) throws BusinessException;

    /**
     * 新增IPC的信息
     * @param ipcamera
     * @throws BusinessException
    */
    public IPCamera createIPCamera(IPCamera ipcamera) throws BusinessException;

    /**
     * 删除IPC的信息
     * @param id
     * @throws BusinessException
    */
    public void deleteIPCamera(Long id) throws BusinessException;

    /**
     * getIPCameraBySN(通过sn查询IPCamera的信息)
     * @Title getIPCameraBySN
     * @param sn
     */
    public IPCamera getIPCameraBySN(String sn) throws BusinessException;

    /**
     * getIPCameraSubBySN(通过sn查询从表IPCameraSub的信息)
     * @Title getIPCameraSubBySN
     * @param sn
     */
    public IPCameraSub getIPCameraSubBySN(String sn) throws BusinessException;

    /**
     * getIPCameraByDeviceId(通过deviceId查询IPCamera的信息)
     * @Title getIPCameraByDeviceId
     * @param deveiceId
     */
    public IPCamera getIPCameraByDeviceId(String deviceId) throws BusinessException;
    
    /**
     * 通过SN得到所有关联的APP用户
     * @Title getIPCameraLinkedUsers
     * @param sn
     */
    public List<AppUser> getIPCameraLinkedUsers(String sn) throws BusinessException;
    
    
    /**
     * 
      * getIPCameraLinkedUsers(通过sn和是否绑定linkType查询与设备sn绑定的用户信息)
      * @Title: getIPCameraLinkedUsers
      * @Description: 通过sn和是否绑定linkType查询与设备sn绑定的用户信息
      * @param @param sn
      * @param @param linkType
      * @param @return
      * @param @throws BusinessException (参数说明)
      * @return List<AppUser> (返回值说明)
      * @throws (异常说明)
      * @author wangming
      * @date 2016年5月13日
     */
    public List<AppUser> getIPCameraLinkedUsers(String sn,int linkType) throws BusinessException;
    
    /**
     * 保存IPC上用户需要分享的信息
     * @param userId
     * @param duration 
     * @param ipcamera
     * @throws BusinessException
     */
    public Share createIPCUserShare(Long userId, Long duration, IPCamera ipcamera) throws BusinessException;

    /**
     * 更新IPC Remote设备的speaker volume信息
     * @param ipcamera
     * @throws BusinessException
    */
    public void updateIPCRemoteVolume(IPCamera ipcamera) throws BusinessException;
    
    /**
     * 获取IPC Remote的音量信息
     * @param ipcamera
     * @throws BusinessException
     */
    public IPCamera getIPCRemoteVolume(IPCamera ipcamera) throws BusinessException;
    
    /**
     * 获取直播分享ID对应的参数
     * @param sid
     * @throws BusinessException
     */
	public ShareStatusFO queryShareStatus(String sid) throws BusinessException;
    
	/**
	 * 通过uuid检验直播流url是否过期
	 * @param uuid
	 * @throws BusinessException
	 */
	public int checkLiveUrlValidity(String uuid) throws BusinessException;
    
	/**
	 * 通过uuid组装有期限的直播流url
	 * @param sid
	 * @throws BusinessException
	 */
	public String[] queryShareLiveUrl(String uuid) throws BusinessException;
    
    /**
	 * 通知ipc实时抓拍图片上传到s3
	 * @param sn
	 * @param requestCurrentPictrueIO
	 * @throws BusinessException
	 */
	public void requestCurrentPictrue(String sn,
			RequestCurrentPictrueIO requestCurrentPictrueIO) throws BusinessException;

	
	/**
	 * 删除有关某ipc的所有分享（但是由于获取ipc的relay url时也会在此表生成一条记录,对应的是start_date与end_date都是NULL，此条记录不删）
	 * @param sn
	 */
	public void deleteShareBySnExceptRelay(String sn);
	
	/**
     * 通过sn删除所有关联的APP用户
     * @Title deleteIPCameraLinkedUsers
     * @param deveiceId
     */
    public void deleteIPCameraLinkedUsers(Long cameraId) throws BusinessException;
    
	/**
	 * 校验用户绑定的ipc的密码
	 * @param sn 
	 * @param password
	 * @param userId
	 */
	void verifyIpcPassword(String sn, String password, Long userId);
	/**
	 * 修改用户绑定的ipc的密码
	 * @param sn
	 * @param password
	 * @param userId
	 * @param id 
	 * @param reqXml 
	 */
	void modifyIpcPassword(String sn, String password, Long userId, int id, String reqXml);
}
