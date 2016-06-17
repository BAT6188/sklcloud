package com.skl.cloud.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.AppCircle;
import com.skl.cloud.model.AppCircleMember;
import com.skl.cloud.model.AppDeviceShare;
import com.skl.cloud.model.OwnSharedDevices;

/**
 * @ClassName: AppMyCircleMgtMapper
 * @Description: 家庭组（圈子）管理-dao层
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
public interface AppMyCircleMgtMapper
{

	/**
	 * <增加一个家庭组insert>
	 * 
	 * @param appCircle
	 *            [家庭组对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void insertCircle(AppCircle appCircle) throws Exception;

	/**
	 * <增加家庭组成员insert>
	 * 
	 * @param appCircleMember
	 *            [家庭组成员对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void insertCircleMember(AppCircleMember appCircleMember) throws Exception;

	/**
	 * <增加分享设备insert>
	 * 
	 * @param deviceList
	 *            [设备列表]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void insertShareDevices(List<AppDeviceShare> deviceShareList) throws Exception;

	/**
	 * <修改家庭组信息update>
	 * 
	 * @param appCircle
	 *            [家庭组对象]
	 * @return [返回结果-boolean]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void updateCircle(AppCircle appCircle) throws Exception;

	/**
	 * <修改家庭组成员权限update>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @param memberId
	 *            [家庭组成员id]
	 * @param circleRole
	 *            [家庭组成员权限]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void updateCircleMemberRole(String circleId, String memberId, String circleRole) throws Exception;

	/**
	 * <删除一个家庭组delete>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void deleteCircleById(String circleId) throws Exception;

	/**
	 * <删除指定家庭组中部分成员delete>
	 * 
	 * @param map
	 *            [参数集合]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void deleteCircleMember(Map<String, Object> map) throws Exception;

	/**
	 * <删除分享设备delete>
	 * 
	 * @param map
	 *            [参数集合]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public void deleteShareDevices(Map<String, Object> map) throws Exception;

	/**
	 * <查询家庭组中具有circleRole权限的家庭组成员总数select>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @param circleRole
	 *            [家庭组成员权限]
	 * @return [返回结果-计数]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public long getCountCircleMemberRole(String circleId, String circleRole) throws Exception;

	/**
	 * <获取家庭组中(此成员)分享设备的个数>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @param memberId
	 *            [成员id]
	 * @return [返回结果-计数]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public long getCountShareDevice(String circleId, String memberId) throws Exception;

	/**
	 * <获取所属家庭组个数>
	 * 
	 * @param memberId
	 *            [成员id]
	 * @return [返回结果：long计数]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public long getCountAtCircle(String memberId) throws Exception;

	/**
	 * <查询家庭组信息列表select>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @return [返回结果-家庭组对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public AppCircle getCircleByCId(String circleId) throws Exception;

	/**
	 * <查询家庭组指定成员信息select>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @param memberId
	 *            [成员id]
	 * @return [返回结果-家庭组成员对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<AppCircleMember> getCircleMemberByCMId(AppCircleMember appCircleMamber) throws Exception;

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
	 * 自己接受别人分享出来的设备
	 * @param circleId [家庭组]
	 * @param memberId [自己]
	 */
	public void acceptShareDevices(String circleId, String memberId) throws Exception;

	/**
	 * <获取用户绑定ipc设备信息select>
	 * 
	 * @param userId
	 *            [用户id]
	 * @param sn
	 *            [设备sn序列号]
	 * @return [返回结果-设备map对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public Map<String, Object> getOwnDeviceByUserSn(String userId, String sn) throws Exception;

	/**
	 * <获取设备信息select>
	 * 
	 * @param sn
	 *            [设备sn序列号]
	 * @return [返回结果-设备map对象]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public Map<String, Object> getDeviceInfoBySn(String sn) throws Exception;

	/**
	 * <查询家庭组信息详情列表select>
	 * 
	 * @param memberId
	 *            [家庭组成员id]
	 * @return [返回结果-map对象list]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<Map<String, Object>> getCircleListByMberId(String memberId) throws Exception;

	/**
	 * <查询家庭组成员详情列表>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<Map<String, Object>> getCircleMemberListByCId(String circleId) throws Exception;

	/**
	 * <获取用户名下的设备列表select>
	 * 
	 * @param circleId
	 *            [家庭组id]
	 * @param memberId
	 *            [成员id]
	 * @param deviceModel
	 *            [设备型号]
	 * @return [返回结果-设备map对象list]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<Map<String, Object>> getUserDevices(String circleId, String userId, String deviceModel) throws Exception;

	/**
	 * <获取用户自己的设备分享给所在家庭组组信息select>
	 * 
	 * @param userId
	 *            [用户id]
	 * @param deviceId
	 *            [设备id]
	 * @return [返回结果-分享设备map对象list]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<Map<String, Object>> getShareDevicesAtCircles(String userId, String deviceId) throws Exception;

	/**
	 * <获取用户在家庭组中能看到的所有设备信息列表select>
	 * 
	 * @param circleIds
	 *            [家庭组ids]
	 * @return [返回结果-分享设备map对象list]
	 * @throws Exception
	 *             [抛出异常]
	 */
	public List<Map<String, Object>> getShareDevicesByCIds(List<String> circleIds) throws Exception;

	public void joinInCircle(HashMap<String, String> queryMap) throws Exception;

	/**
	 * 7.14 用户查询自己的设备分享给所在的家庭组的信息
	 */

	public List<Map<String, Object>> queryFareDeviceInfoBysn(String sn);

	public void updateShareOwnDevices(List<AppDeviceShare> list);

	public void updateShareOwnDevice(AppDeviceShare appDeviceShare);
	
	/**
	 * 
	  * queryAllCircleIdByUserId(根据用户id查询所在的所有的家庭组的id)
	  * @Title: queryAllCircleIdByUserId
	  * @Description: TODO
	  * @param @param userId
	  * @param @return (参数说明)
	  * @return List<Integer> (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年1月14日
	 */
	public List<Long> queryAllCircleIdByUserId(String userId);
	 
	/**
	 * 
	  * queryOwnSharedDevicesInfo(7.14 根据用户id和设备id查询自己的设备分享给所在的家庭组的信息)
	  * @Title: queryOwnSharedDevicesInfo
	  * @Description: TODO
	  * @param @param userId
	  * @param @param deviceId
	  * @param @return
	  * @param @throws Exception (参数说明)
	  * @return List<OwnSharedDevices> (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年1月14日
	 */
	public List<OwnSharedDevices> queryOwnSharedDevicesInfo(String userId,String deviceId) throws Exception;
	
	public  int  queryCircleId(Map<String, String> hashMap);
	
	
	/**
     * <删除分享设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
    public void deleteShareDeviceById(AppDeviceShare appShareDevice) throws BusinessException;
    /**
     * <删除指定家庭组中别人分享出来的设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
    public void deleteAcceptShareDevices(Map<String, Object> map) throws BusinessException;
	
	/**
     * <删除指定家庭组中自己分享出去的设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
    public void deleteSendShareDevices(Map<String, Object> map) throws BusinessException;
	
    
    public AppDeviceShare queryShareDevice(String circleId,String memberId,String deviceId);
}
