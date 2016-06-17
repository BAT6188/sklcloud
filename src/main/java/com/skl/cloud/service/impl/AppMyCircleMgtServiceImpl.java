package com.skl.cloud.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.AppMyCircleMgtMapper;
import com.skl.cloud.model.AppCircle;
import com.skl.cloud.model.AppCircleMember;
import com.skl.cloud.model.AppDeviceShare;
import com.skl.cloud.model.OwnSharedDevices;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.AppMyCircleMgtService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.constants.Constants.MyCircleRole;

/**
 * @ClassName: AppMyCircleMgtService
 * @Description: 家庭组（圈子）管理-service层实现
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
@Service("appMyCircleMgtService")
public class AppMyCircleMgtServiceImpl implements AppMyCircleMgtService {
    private Logger logger = Logger.getLogger(AppMyCircleMgtServiceImpl.class);

    @Autowired(required = true)
    private AppMyCircleMgtMapper appMyCircleMgtMapper;

    @Autowired
    private AppUserService userSerivce;

    @Override
    @Transactional(readOnly = true)
    public void isExistCircle(boolean flag, String circleId) throws Exception {
        AppCircle circle = appMyCircleMgtMapper.getCircleByCId(circleId);
        if (flag) {
            if (circle == null) {
                throw new BusinessException("0x50020026");
            }
        } else {
            if (circle != null) {
                throw new BusinessException("0x50020027");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistCircle(String circleId) throws Exception {
        AppCircle circle = appMyCircleMgtMapper.getCircleByCId(circleId);
        if (circle == null) {
            return false;
        }
        return true;
    }
    
    /**
     * get AppCircle by circleId
     * @param circleId [家庭组id]
     * @throws BusinessException [抛出异常]
     */
    @Override
    @Transactional(readOnly = true)
    public AppCircle getCircleByCId(String circleId) throws Exception{
        return appMyCircleMgtMapper.getCircleByCId(circleId);
    }

    @Override
    @Transactional(readOnly = true)
    public void isCircleMember(boolean flag, String circleId, String memberId) throws Exception {
        isExistCircle(true, circleId);
        AppCircleMember circleMember = this.getCircleMemberByCMId(circleId, memberId);
        if (flag) {
            if (circleMember == null) {
                throw new BusinessException("0x50020033");
            }
        } else {
            if (circleMember != null) {
                throw new BusinessException("0x50020034");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCircleMember(String circleId, String memberId) throws Exception {
        isExistCircle(true, circleId);
        AppCircleMember circleMember = this.getCircleMemberByCMId(circleId, memberId);
        if (circleMember == null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public void isCircleAdmin(boolean flag, String circleId, String memberId) throws Exception {
        isExistCircle(true, circleId);
        AppCircleMember circleMember = this.getCircleMemberByCMId(circleId, memberId);

        if (circleMember != null) {
            if (flag) {
                if (!MyCircleRole.admin.name().equals(circleMember.getMemberCircleRole())) {
                    throw new BusinessException("0x50020035");
                }
            } else {
                if (MyCircleRole.admin.name().equals(circleMember.getMemberCircleRole())) {
                    throw new BusinessException("0x50020036");
                }
            }
        } else {
            throw new BusinessException("0x50020033");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCircleAdmin(String circleId, String memberId) throws Exception {
        isExistCircle(true, circleId);
        AppCircleMember circleMember = this.getCircleMemberByCMId(circleId, memberId);

        if (circleMember != null) {
            if (!MyCircleRole.admin.name().equals(circleMember.getMemberCircleRole())) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void isCircleAdminOnly(boolean flag, String circleId, String memberId) throws Exception {
    	isCircleMember(true, circleId, memberId);
        long count = appMyCircleMgtMapper.getCountCircleMemberRole(circleId, MyCircleRole.admin.toString());
        if (flag) {
            if (count != 1L) {
                throw new BusinessException("0x50020037");
            }
        } else {
            if (count == 1L) {
                throw new BusinessException("0x50020038");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCircleAdminOnly(String circleId, String memberId) throws Exception {
        isCircleMember(true, circleId, memberId);
        long count = appMyCircleMgtMapper.getCountCircleMemberRole(circleId, MyCircleRole.admin.toString());
        if (count == 1L) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public void isHaveShareDevice(boolean flag, String circleId, String memberId) throws Exception {
        long count = appMyCircleMgtMapper.getCountShareDevice(circleId, memberId);
        if (flag) {
            if (count <= 0L) {
                throw new BusinessException("0x50020042");
            }
        } else {
            if (count > 0L) {
                throw new BusinessException("0x50020042");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isHaveShareDevice(String circleId, String memberId) throws Exception {
        long count = appMyCircleMgtMapper.getCountShareDevice(circleId, memberId);
        if (count > 0L) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountAtCircle(String memberId) throws Exception {
        return appMyCircleMgtMapper.getCountAtCircle(memberId);
    }

	@Override
	@Transactional
	public void addCircle(AppCircle appCircle) throws Exception
	{
		appMyCircleMgtMapper.insertCircle(appCircle);
	}

	@Override
	@Transactional
	public void createCircleMember(AppCircleMember appCircleMember) throws Exception
	{
		// 添加家庭组成员
		appMyCircleMgtMapper.insertCircleMember(appCircleMember);
	}

	@Override
	@Transactional
	public void addCircleMember(AppCircleMember appCircleMember) throws Exception
	{
		// 添加家庭组成员
		appMyCircleMgtMapper.insertCircleMember(appCircleMember);
		
		// 只要是加入的成员，默认分享/被分享设备
		if("true".equals(appCircleMember.getIsNew()))
		{
			// 1. 获取别人分享出来的设备（家庭组初始创建不享受别人分享的）
			this.acceptShareDevices(appCircleMember.getCircleId(), appCircleMember.getMemberId());
			
			// 2. 自己的（所有/指定）设备分享设备给（所在/指定）家庭组
			this.sendShareDevices(appCircleMember.getCircleId(), appCircleMember.getMemberId(), null);
		}
	}

	@Override
	@Transactional
	public void sendShareDevices(String circleId, String userId, String deviceId) throws Exception
	{
		// 动态组装参数
		List<String> circleIdList = new ArrayList<String>();
		if (circleId != null && !circleId.isEmpty())
		{
			circleIdList.add(circleId);
		}
		else
		{
			List<Map<String, Object>> circleList = appMyCircleMgtMapper.getCircleListByMberId(userId);
			for (Map<String, Object> circleObj : circleList)
			{
				circleIdList = new ArrayList<String>();
				circleIdList.add(String.valueOf(circleObj.get("circleId")));
			}
		}

		List<String> deviceIdList = new ArrayList<String>();
		if (deviceId != null && !deviceId.isEmpty())
		{
			deviceIdList.add(deviceId);
		}
		else
		{
			deviceIdList = new ArrayList<String>();
			List<IPCamera> ipcList = userSerivce.getUserBindDeviceList(Long.valueOf(userId));
			for (IPCamera ipcamera : ipcList)
			{
				deviceIdList.add(String.valueOf(ipcamera.getId()));
			}
		}

		// 获取相关家庭（多个）组成员信息
		List<AppCircleMember> memberList = appMyCircleMgtMapper.getCircleMemberByCIds(circleIdList);

		// 分享设备组装
		List<AppDeviceShare> deviceShareList = new ArrayList<AppDeviceShare>();

		// 获取需要操作的（所有/指定）家庭组的成员列表,包含自己
		final String trueValue = "true";
		for (String deviceTemp : deviceIdList)
		{
			for (AppCircleMember member : memberList)
			{
				// 信息组装(所有获得此（分享）设备的权限list)
				AppDeviceShare deviceShare = new AppDeviceShare();
				deviceShare.setCircleId(member.getCircleId());
				deviceShare.setMemberId(member.getMemberId());
				deviceShare.setDeviceId(deviceTemp);
				deviceShare.setDeviceStatus(userId.equals(member.getMemberId()) ? "0" : "1");
				deviceShare.setStreamEnable(trueValue);
				deviceShare.setNotificationEnable(trueValue);
				deviceShare.setPlaybackEnable(trueValue);
				deviceShare.setFeedsFilterEnable(trueValue);
				deviceShareList.add(deviceShare);
			}
		}
		
		// 保存分享数据
		if (deviceShareList.size() > 0)
		{
			appMyCircleMgtMapper.insertShareDevices(deviceShareList);
		}
	}

	@Override
	@Transactional
	public void acceptShareDevices(String circleId, String memberId) throws Exception
	{
		appMyCircleMgtMapper.acceptShareDevices(circleId, memberId);
	}

	@Override
	@Transactional
	public void updateCircle(AppCircle appCircle) throws Exception
	{
		appMyCircleMgtMapper.updateCircle(appCircle);
	}

    @Override
    @Transactional
    public void updateCircleMemberRole(String circleId, String memberId, String circleRole) throws Exception {
        appMyCircleMgtMapper.updateCircleMemberRole(circleId, memberId, circleRole);
    }

	@Override
	@Transactional
	public void deleteCircleById(String circleId) throws Exception {
		appMyCircleMgtMapper.deleteCircleById(circleId);
		this.deleteCircleMember(circleId, null);
	}

	@Override
	@Transactional
	public void deleteCircleMember(String circleId, String memberIds) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("circleId", circleId);
		if (memberIds != null && !memberIds.isEmpty()) {
			map.put("memberIds", memberIds.split(","));
		}
		appMyCircleMgtMapper.deleteCircleMember(map);
		this.deleteAcceptAndSendShareDevices(map);
	}

	@Override
	@Transactional
    public void deleteAcceptAndSendShareDevices(Map<String, Object> map) throws BusinessException{
        appMyCircleMgtMapper.deleteAcceptShareDevices(map);
        appMyCircleMgtMapper.deleteSendShareDevices(map);
    }
	
	@Override
	@Transactional
	public void deleteShareDevices(List<String> circleIds, String deviceId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("circleIds", circleIds);
		map.put("deviceId", deviceId);
		appMyCircleMgtMapper.deleteShareDevices(map);
	}

    @Override
    @Transactional(readOnly = true)
    public AppCircleMember getCircleMemberByCMId(String circleId, String memberId) throws Exception {
        AppCircleMember appCircleMamber = new AppCircleMember();
        appCircleMamber.setMemberId(memberId);
        appCircleMamber.setCircleId(circleId);
        List<AppCircleMember> list = appMyCircleMgtMapper.getCircleMemberByCMId(appCircleMamber);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

	@Override
	@Transactional(readOnly = true)
	public List<AppCircleMember> getCircleMemberByCIds(List<String> circleIdList) throws Exception
	{
		return appMyCircleMgtMapper.getCircleMemberByCIds(circleIdList);
	}
	
	@Override
	@Transactional(readOnly = true)
    public Map<String, Object> getOwnDeviceByUserSn(String userId, String sn) throws Exception {
        Map<String, Object> deviceMap = appMyCircleMgtMapper.getDeviceInfoBySn(sn);
        if (deviceMap == null || deviceMap.size() == 0) {
            throw new BusinessException("0x50000047");
        }
        return appMyCircleMgtMapper.getOwnDeviceByUserSn(userId, sn);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDeviceInfoBySn(String sn) throws Exception {
        return appMyCircleMgtMapper.getDeviceInfoBySn(sn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCircleListByMberId(String memberId) throws Exception {
        return appMyCircleMgtMapper.getCircleListByMberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCircleMemberListByCId(String circleId) throws Exception {
        return appMyCircleMgtMapper.getCircleMemberListByCId(circleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserDevices(String circleId, String userId, String isIPC) throws Exception {
        return appMyCircleMgtMapper.getUserDevices(circleId, userId,
                ("true".equals(isIPC) ? Constants.ipcModelType.MODEL_LFM01_0001.getType() : null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getShareDevicesAtCircles(String userId, String deviceId) throws Exception {
        return appMyCircleMgtMapper.getShareDevicesAtCircles(userId, deviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getShareDevicesByCIds(List<String> circleIds) throws Exception {
        return appMyCircleMgtMapper.getShareDevicesByCIds(circleIds);
    }

	@Override
	@Transactional
	public void joinInCircle(HashMap<String, String> queryMap) throws Exception
	{
		appMyCircleMgtMapper.joinInCircle(queryMap);
		
		// 1. 获取别人分享出来的设备（家庭组初始创建不享受别人分享的）
	long count=	appMyCircleMgtMapper.getCountShareDevice(queryMap.get("circleId"), queryMap.get("userId"));//重复分享设备
		if(count<=0){
		this.acceptShareDevices(queryMap.get("circleId"), queryMap.get("userId"));
		// 2. 自己的（所有/指定）设备分享设备给（所在/指定）家庭组
		this.sendShareDevices(queryMap.get("circleId"), queryMap.get("userId"), null);
		}
	}

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryFareDeviceInfoBysn(String sn) {
        return appMyCircleMgtMapper.queryFareDeviceInfoBysn(sn);
    }

    @Override
    @Transactional
    public void updateShareOwnDevices(List<AppDeviceShare> list) {
        appMyCircleMgtMapper.updateShareOwnDevices(list);
    }

    @Override
    @Transactional
    public void updateShareOwnDevice(AppDeviceShare appDeviceShare) {
        appMyCircleMgtMapper.updateShareOwnDevice(appDeviceShare);
    }

    /**
      * addSharedDevices(分享App用户下绑定的设备信息)
      * @Title: addSharedDevices
      * @param @param userId (参数说明)
      * @return void (返回值说明)
      * @throws (异常说明)
      * @author wangming
      * @date 2016年1月11日
     */
    @Override
    @Transactional
    public void addSharedDevices(String userId, String circleId) {

        List<IPCamera> ipcList = null;

        // 查询该userId下绑定的设备信息
        try {
            ipcList = userSerivce.getUserBindDeviceList(Long.valueOf(userId));
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (ipcList.isEmpty()) {
            logger.info("********该用户没有绑定任何设备********");
        }

        if (ipcList != null && !ipcList.isEmpty()) {

            List<AppDeviceShare> list = new ArrayList<AppDeviceShare>();

            for (IPCamera ipcamera : ipcList) {
                AppDeviceShare appDeviceShare = new AppDeviceShare();
                appDeviceShare.setCircleId(circleId);
                appDeviceShare.setMemberId(userId);
                appDeviceShare.setDeviceId(ipcamera.getId().toString());
                appDeviceShare.setFeedsFilterEnable("true");
                appDeviceShare.setNotificationEnable("true");
                appDeviceShare.setStreamEnable("true");
                appDeviceShare.setPlaybackEnable("true");
                appDeviceShare.setDeviceStatus(0 + "");
                appDeviceShare.setCreateTime(new Date());
                appDeviceShare.setChangeTime(new Date());
                list.add(appDeviceShare);
            }

            // 插入该用户分享的设备
            try {
                appMyCircleMgtMapper.insertShareDevices(list);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnSharedDevices> queryOwnSharedDevicesInfo(String userId, String deviceId) throws Exception {

        return appMyCircleMgtMapper.queryOwnSharedDevicesInfo(userId, deviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> queryAllCircleIdByUserId(String userId) {

        return appMyCircleMgtMapper.queryAllCircleIdByUserId(userId);
    }

    /**
     * 把用户关联的SN分享到用户所有所在的家庭组
     * @param userId
     * @param circleId
     */
    @Override
    @Transactional
    public void addSharedDevices(Long userId, Long cameraId) throws BusinessException {
        try {
            List<Map<String, Object>> reCircleList = this.getCircleListByMberId(String.valueOf(userId));
            List<AppDeviceShare> list = new ArrayList<AppDeviceShare>();
            final String trueValue = "true";

            for (Map<String, Object> reCircleMap : reCircleList) {
                // 获取每个家庭组的信息
                Long circleId = (Long) reCircleMap.get("circleId");
                String isNew = (String) reCircleMap.get("memberIsNew");
                // 只返回查询用户已加入的家庭组的信息
                if (StringUtils.isNotEmpty(isNew) && trueValue.equals(isNew)) {
                    AppCircleMember appCircleMamber = new AppCircleMember();
                    appCircleMamber.setCircleId(String.valueOf(circleId));
                    //得到这个家庭组下的所有成员
                    List<AppCircleMember> appCircleMemlist = appMyCircleMgtMapper.getCircleMemberByCMId(appCircleMamber);
                    for(AppCircleMember appCircleMem : appCircleMemlist){
                        if (StringUtils.isNotEmpty(appCircleMem.getIsNew()) && trueValue.equals(appCircleMem.getIsNew())) {
                            AppDeviceShare appDeviceShare = new AppDeviceShare();
                            appDeviceShare.setCircleId(String.valueOf(circleId));
                            appDeviceShare.setMemberId(appCircleMem.getMemberId());
                            appDeviceShare.setDeviceId(String.valueOf(cameraId));
                            appDeviceShare.setFeedsFilterEnable(trueValue);
                            appDeviceShare.setNotificationEnable(trueValue);
                            appDeviceShare.setStreamEnable(trueValue);
                            appDeviceShare.setPlaybackEnable(trueValue);
                            if(String.valueOf(userId).equals(appCircleMem.getMemberId())){
                                appDeviceShare.setDeviceStatus("0");
                            }else{
                                appDeviceShare.setDeviceStatus("1");
                            }
                            appDeviceShare.setCreateTime(new Date());
                            appDeviceShare.setChangeTime(new Date());
                            list.add(appDeviceShare);
                        }
                    }
                }
            }
            //保存分享数据
            if (list.size() > 0) {
                appMyCircleMgtMapper.insertShareDevices(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

	@Override
	@Transactional(readOnly = true)
	public int queryCircleId(Map<String, String> hashMap) {
		return appMyCircleMgtMapper.queryCircleId(hashMap);
	}

	/**
     * <删除分享设备delete>
     * @param map [参数集合]
     * @throws Exception [抛出异常]
     */
	@Override
	@Transactional
    public void deleteShareDeviceById(AppDeviceShare appShareDevice) throws BusinessException{
        appMyCircleMgtMapper.deleteShareDeviceById(appShareDevice);
    }

	@Override
	@Transactional(readOnly = true)
	public AppDeviceShare queryShareDevice(String circleId, String memberId, String deviceId) {
		
		return appMyCircleMgtMapper.queryShareDevice(circleId, memberId, deviceId);
	}

	
}
