package com.skl.cloud.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.AppCircle;
import com.skl.cloud.model.AppCircleMember;
import com.skl.cloud.model.AppDeviceShare;
import com.skl.cloud.model.OwnSharedDevices;

/**
 * 
 * @ClassName: AppMyCircleMgtService
 * @Description: 家庭组（圈子）管理-service层
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
public interface AppMyCircleMgtService {
	
	/**
	 * <是否存在此家庭组>
	 * <业务分支用>
	 * @param circleId [家庭组id]
	 * @return [返回结果-boolean]
	 * @throws Exception [抛出异常]
	 */
	public boolean isExistCircle(String circleId) throws Exception;
	
	/**
	 * <是否存在此家庭组>
	 * <业务校验用>
	 * @param flag [判断方向(true：正向<反向抛异常>， false：反向<正向抛异常>)]
	 * @param circleId [家庭组id]
	 * @throws Exception [抛出异常]
	 */
	public void isExistCircle(boolean flag, String circleId) throws Exception;
	
	/**
     * get AppCircle by circleId
     * @param circleId [家庭组id]
     * @throws Exception [抛出异常]
     */
	public AppCircle getCircleByCId(String circleId) throws Exception;
	
	/**
	 * <是否是家庭组成员>
	 * <参数中：有flag时调用业务校验(无返回值)，无flag时调用业务分支(有返回值)>
	 * @param flag [判断方向(true：正向<反向抛异常>， false：反向<正向抛异常>)]
	 * @param circleId [家庭组id]
	 * @param memberId [成员id]
	 * @return [返回结果-boolean]
	 * @throws Exception [抛出异常]
	 */
	public void isCircleMember(boolean flag, String circleId, String memberId) throws Exception;
	public boolean isCircleMember(String circleId, String memberId) throws Exception;
	
	/**
	 * <是否是家庭组成员>
	 * <参数中：有flag时调用业务校验(无返回值)，无flag时调用业务分支(有返回值)>
	 * @param flag [判断方向(true：正向<反向抛异常>， false：反向<正向抛异常>)]
	 * @param circleId [家庭组id]
	 * @param memberId [成员id]
	 * @return [返回结果-boolean]
	 * @throws Exception [抛出异常]
	 */
	public void isCircleAdmin(boolean flag, String circleId, String memberId) throws Exception;
	public boolean isCircleAdmin(String circleId, String memberId) throws Exception;
	
	/**
	 * <是否是家庭组唯一管理员>
	 * <参数中：有flag时调用业务校验(无返回值)，无flag时调用业务分支(有返回值)>
	 * @param flag [判断方向(true：正向<反向抛异常>， false：反向<正向抛异常>)]
	 * @param circleId [家庭组id]
	 * @param memberId [成员id]
	 * @return [返回结果-boolean]
	 * @throws Exception [抛出异常]
	 */
	public void isCircleAdminOnly(boolean flag, String circleId, String memberId) throws Exception;
	public boolean isCircleAdminOnly(String circleId, String memberId) throws Exception;
	
	/**
	 * <家庭组中(此成员)是否有设备分享>
	 * <参数中：有flag时调用业务校验(无返回值)，无flag时调用业务分支(有返回值)>
	 * @param flag [判断方向(true：正向<反向抛异常>， false：反向<正向抛异常>)]
	 * @param circleId [家庭组id]
	 * @param memberId [成员id]
	 * @return [返回结果-boolean]
	 * @throws Exception [抛出异常]
	 */
	public void isHaveShareDevice(boolean flag, String circleId, String memberId) throws Exception;
	public boolean isHaveShareDevice(String circleId, String memberId) throws Exception;
	
	/**
	 * <获取所属家庭组个数>
	 * @param memberId [成员id]
	 * @return [返回结果：long计数]
	 * @throws Exception [抛出异常]
	 */
	public long getCountAtCircle(String memberId) throws Exception;
	
	/**
	 * <新增一个家庭组>
	 * @param appCircle [家庭组对象]
	 * @throws Exception [抛出异常]
	 */
	public void addCircle(AppCircle appCircle) throws Exception;
	
	/**
	 * <创建家庭组成员（新注册的用户）>
	 * @param appCircleMember [家庭组成员对象]
	 * @throws Exception [抛出异常]
	 */
	public void createCircleMember(AppCircleMember appCircleMember) throws Exception;
	
	/**
	 * <添加家庭组成员>
	 * @param appCircleMember [家庭组成员对象]
	 * @throws Exception [抛出异常]
	 */
	public void addCircleMember(AppCircleMember appCircleMember) throws Exception;
	
	/**
	 * <修改家庭组信息>
	 * @param appCircle [家庭组对象]
	 * @throws Exception [抛出异常]
	 */
	public void updateCircle(AppCircle appCircle) throws Exception;
	
	/**
	 * <变更家庭组成员权限>
	 * @param circleId [家庭组id]
	 * @param memberId [家庭组成员id]
	 * @param circleRole [家庭组成员权限]
	 * @throws Exception [抛出异常]
	 */
	public void updateCircleMemberRole(String circleId, String memberId, String circleRole) throws Exception;
	
	/**
	 * <删除一个家庭组>
	 * @param circleId [家庭组id]
	 * @throws Exception [抛出异常]
	 */
	public void deleteCircleById(String circleId) throws Exception;
	
	/**
	 * <删除指定家庭组中部分成员>
	 * @param circleId [家庭组id]
	 * @param memberIds [家庭组成员ids]
	 * @throws Exception [抛出异常]
	 */
	public void deleteCircleMember(String circleId, String memberIds) throws Exception;
	
	/**
	 * <删除分享设备>
	 * @param circleIds [家庭组id列表]
	 * @param deviceId [设备id]
	 * @throws Exception [抛出异常]
	 */
	public void deleteShareDevices(List<String> circleIds, String deviceId) throws Exception;
	
	/**
	 * <查询家庭组指定成员信息>
	 * @param circleId [家庭组id]
	 * @param memberId [家庭组成员id]
	 * @return [返回结果-家庭组成员对象]
	 * @throws Exception [抛出异常]
	 */
	public AppCircleMember getCircleMemberByCMId(String circleId, String memberId) throws Exception;
	
	/**
	 * <获取用户绑定ipc设备信息>
	 * @param userId [用户id]
	 * @param sn [设备sn序列号]
	 * @return [返回结果-拥有设备map对象]
	 * @throws Exception [抛出异常]
	 */
	public Map<String, Object> getOwnDeviceByUserSn(String userId, String sn) throws Exception;
	
	/**
	 * <获取设备信息>
	 * @param sn [设备sn序列号]
	 * @return [返回结果-设备map对象]
	 * @throws Exception [抛出异常]
	 */
	public Map<String, Object> getDeviceInfoBySn(String sn) throws Exception;
	
	/**
	 * <查询用户所属家庭组列表>
	 * @param memberId [家庭组成员id]
	 * @return [返回结果-map对象list]
	 * @throws Exception [抛出异常]
	 */
	public List<Map<String, Object>> getCircleListByMberId(String memberId) throws Exception;

	/**
	 * <查询家庭组成员详情列表>
	 * @param circleId [家庭组id]
	 * @throws Exception [抛出异常]
	 */
	public List<Map<String, Object>> getCircleMemberListByCId(String circleId) throws Exception;
	
	/**
	 * <查询多个家庭组成员信息select>
	 * 
	 * @param circleIdList
	 *            [家庭组id列表]
	 * @return [返回结果-家庭组成员对象列表]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<AppCircleMember> getCircleMemberByCIds(List<String> circleIdList) throws Exception;
	
	/**
	 * <获取用户名下的设备列表>
	 * @param circleId [家庭组id]
	 * @param memberId [成员id]
	 * @param isIPC [是否仅包含IPC设备，false代表全部设备]
	 * @return [返回结果-设备map对象list]
	 * @throws Exception [抛出异常]
	 */
	public List<Map<String, Object>> getUserDevices(String circleId, String memberId, String isIPC) throws Exception;
	
	/**
	 * <获取用户自己的设备分享给所在家庭组组信息>
	 * @param userId [用户id]
	 * @param deviceId [设备id]
	 * @return [返回结果-分享设备map对象list]
	 * @throws Exception [抛出异常]
	 */
	public List<Map<String, Object>> getShareDevicesAtCircles(String userId, String deviceId) throws Exception;
	
	/**
	 * <获取用户在家庭组中能看到的所有设备信息列表>
	 * @param circleIds [家庭组ids]
	 * @return [返回结果-分享设备map对象list]
	 * @throws Exception [抛出异常]
	 */
	public List<Map<String, Object>> getShareDevicesByCIds(List<String> circleIds) throws Exception;
	
	/**
     * 把用户关联的指定(null时：所有)SN分享到用户指定（null时：所在）家庭组
     * @param userId
     * @param circleId
     * @param deviceId
     */
	public void sendShareDevices(String userId, String circleId, String deviceId) throws Exception;
	
	/**
	 * 自己接受别人分享出来的设备
	 * @param circleId [家庭组]
	 * @param memberId [自己]
	 */
	public void acceptShareDevices(String circleId, String memberId) throws Exception;
	
	public void joinInCircle(HashMap<String, String> queryMap) throws Exception;
	
	
	/**
     * 把用户关联的设备都分享到指定的家庭组
     * @param userId
     * @param circleId
     */
	public void addSharedDevices(String userId,String circleId)throws Exception;
	
	/**
     * 把用户关联的SN分享到用户所有所在的家庭组
     * @param userId
     * @param circleId
     */
	public void addSharedDevices(Long userId, Long cameraId) throws BusinessException;
	
	/**
	 * 7.14	用户查询自己的设备分享给所在的家庭组的信息
	 */
	
	public List<Map<String, Object>> queryFareDeviceInfoBysn(String sn);
	/**
	 * 7.15	用户设置将自己的设备分享给所在的家庭组
	 * @param stream
	 * @param playback
	 * @param notification
	 * @param circleId
	 * @param memberId
	 */
	public void updateShareOwnDevices(List<AppDeviceShare> list);
	
	public void updateShareOwnDevice(AppDeviceShare appDeviceShare);
	
	public List<Long> queryAllCircleIdByUserId(String userId);
	
	public List<OwnSharedDevices> queryOwnSharedDevicesInfo(String userId,String deviceId) throws Exception;
	
	public  int queryCircleId(Map<String, String> hashMap);
	
	/**
     * <删除分享设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
    public void deleteShareDeviceById(AppDeviceShare appShareDevice) throws BusinessException;
    
    public AppDeviceShare queryShareDevice(String circleId,String memberId,String deviceId);

    /**
     * <删除别人分享出来和自己分享出去的设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
    public void deleteAcceptAndSendShareDevices(Map<String, Object> map) throws BusinessException;

}
