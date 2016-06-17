package com.skl.cloud.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.AppdeviceLinkIpcMapper;
import com.skl.cloud.model.AppDeviceShare;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.UserCameraQuery;
import com.skl.cloud.service.AppMyCircleMgtService;
import com.skl.cloud.service.AppdeviceLinkIpcservice;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.service.user.UserPermissionService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants;

@Service("appdeviceLinkIpcservice")
public class AppdeviceLinkIpcImpl implements AppdeviceLinkIpcservice {

	private static final Logger logger = Logger
			.getLogger(AppdeviceLinkIpcImpl.class);
	private final static String MODULE_NAME = "APP用户关联IPC设备"; // 模块名称
	@Autowired(required = true)
	public AppdeviceLinkIpcMapper appdeviceLinkIpcMapper;

	@Autowired
	private IPCameraService ipcService;

	@Autowired
	private AppUserService userService;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private AppMyCircleMgtService familyCircleService;
	
	@Autowired
	private UserPermissionService<AppUser> userPermissionService;

	/**
	 * 取得这个设备被用户关联的总数
	 * 
	 * @Title: querySum
	 * @param userCameraQuery
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer querySum(UserCameraQuery userCameraQuery) {
		return appdeviceLinkIpcMapper.querySum(userCameraQuery);
	}

	/**
	 * 查询最早关联而且不在线用户的ID
	 * 
	 * @Title: querySum
	 * @param cameraId
	 */
	@Override
	@Transactional(readOnly = true)
	public Long queryminId(Long cameraId) {
		return appdeviceLinkIpcMapper.queryminId(cameraId);
	}

	/**
	 * 绑定设备
	 * 
	 * @param inputstream
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public void addDeviceLink(InputStream inputstream, Long userId)
			throws BusinessException {
		boolean bindFlag = false;
		try {
			PlatformLog pl = new PlatformLog();

			// 解析报文
			String reqXml = XmlUtil.isChangeToStr(inputstream);
			Map<String, Object> reqMap = XmlUtil.getElementMap(reqXml, null);

			// 获取SN参数
			String strSN = XmlUtil.getDomValue(reqMap, "appDeviceLink/SN");
			if (!StringUtil.isNullOrEmpty(strSN)) {
				// 查不到设备
				throw new BusinessException("0x50010003");
			}
			// 从header获得userId

			// 获取SN参数
			String deviceKind = XmlUtil.getDomValue(reqMap,
					"appDeviceLink/deviceKind");
			String deviceName = XmlUtil.getDomValue(reqMap,
					"appDeviceLink/deviceName");

			// 记录日志־
			pl.setUserId(userId);
			pl.setModuleName(MODULE_NAME);
			pl.setLogContent("云端收到APP用户关联IPC设备请求。");
			pl.setLogTime(DateUtil.date2string(new Date(),
					DateUtil.DATE_TIME_1_FULL_FORMAT));
			// super.saveLog(pl);
			logger.info(pl);

			IPCamera ipcamera = ipcService.getIPCameraBySN(strSN);
			if (ipcamera == null) {
				logger.info("这个设备不存在--" + strSN);
				throw new BusinessException("0x50000047");
			}

			UserCamera userCamera = new UserCamera();
			userCamera.setUserId(userId);
			userCamera.setCameraId(ipcamera.getId());
			userCamera.setCameraSerialno(ipcamera.getSn());
			userCamera.setConfirm(Boolean.TRUE);
			userCamera.setLocalTime(new Date());
			userCamera.setEnable("true");
			userCamera.setLinkType(0);
			// 默认关联用户可以使用
			userCamera.setIsUsedToShareUser(0);

			String cameraModel = ipcamera.getModel();
			// 检查是否已经关联过
			IPCamera ipcamera1 = userService.getUserBindDevice(userId, strSN);
			if (ipcamera1 != null) {
				logger.info("这个设备" + strSN + "和用户" + userId + "已经绑定过");
				throw new BusinessException("0x50020060");
			}
			
			AppUser loginUser = userService.getUserById(userId);
			String userKind = loginUser.getKind() != null ? loginUser
					.getKind() : "";

			if (!userKind.equals(cameraModel)) {
				// user kind not match camera model
				throw new BusinessException("0x50020064");
			}

			// 取得被关联的总数
			UserCameraQuery userCameraQuery = new UserCameraQuery();
			userCameraQuery.setCameraId(ipcamera.getId());
			int linkedCount = querySum(userCameraQuery);
			// HPC03, IPC can linked by HPC03 or HPC03B user is 32, rather
			// than 32 will unbind the not live and
			// linked in
			// front.
			if (Constants.ipcModelType.isHPC03IPC(cameraModel)) {
				if (linkedCount < 32) {
					userService.bindOneDevice(userCamera);
					bindFlag = true;
				} else {
					// query if exits user bind ipC but not live and linked
					// in front
					Long minUserId = queryminId(ipcamera.getId());
					if (minUserId != null) {
						userService.unbindDevice(minUserId,
								ipcamera.getId());
						userService.bindOneDevice(userCamera);
						bindFlag = true;
					} else {
						throw new BusinessException("0x50020073");
					}
				}
			} else if (Constants.ipcModelType.isHPC03BIPC(cameraModel)) {
				// query if exits user bind ipC
				if (linkedCount == 0) {
					// 判断用户是否被关联
					if (loginUser.getLinkUserId() != null) {
						userCamera.setIsUsedToShareUser(1);
					}
					userService.bindOneDevice(userCamera);
					bindFlag = true;
				} else {
					throw new BusinessException("0x50020063");
				}

			} else if (Constants.ipcModelType.isFamilerIPC(cameraModel)) {
				// Familer, IPC only can bind by one familer user.
				List<IPCamera> ipcList = userService
						.getUserBindDeviceList(userId);
				final int maxLinkCnt = 99;
				if (linkedCount == 0
						&& (ipcList != null && ipcList.size() < maxLinkCnt)) {
					userService.bindOneDevice(userCamera);
					// 把sn分享到用户已经确认的家庭组中
					familyCircleService.addSharedDevices(userId,
							ipcamera.getId());
					bindFlag = true;
				} else if (linkedCount > 0) {
					throw new BusinessException("0x50020063");
				} else if (ipcList != null && ipcList.size() >= maxLinkCnt) {
					throw new BusinessException("0x50020072");
				}
			} else {
				// user kind not match camera model
				throw new BusinessException("0x50020064");
			}
			// update ipcamera name and kind
			if (bindFlag) {
				if (StringUtils.isNotBlank(deviceName)) {
					ipcamera.setNickname(deviceName);
				}
				if (StringUtils.isNotBlank(deviceKind)) {
					ipcamera.setKind(deviceKind);
				}
				ipcService.updateIPCamera(ipcamera);
			}
			
			// 智能赋予绑定角色
			userPermissionService.assignRole(userId, loginUser.getKind(), Constants.roleType.BIND.getId(), ipcamera.getId());

			// 记录日志
			pl.setLogContent("云端处理APP用户关联IPC设备请求成功。");
			pl.setLogTime(DateUtil.date2string(new Date(),
					"yyyy-MM-dd HH:mm:ss"));
			// super.saveLog(pl);
			logger.info(pl);
		} catch (BusinessException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw new BusinessException("0x50000005");
		}
	}

	/**
	 * 解除绑定的设备
	 * 
	 * @param sn
	 * @return
	 */
	@Override
	@Transactional
	public void deleteLinkIpc(String sn, Long userId) {
		try {
			AppUser loginUser = userService.getUserById(userId);
			IPCamera ipcamera = ipcService.getIPCameraBySN(sn);
			if (ipcamera == null) {
				logger.info("这个设备不存在--" + sn);
				throw new BusinessException("0x50000047");
			}
			UserCamera userCamera = userService.getUserCamera(userId, sn);
			// 检查关联的数据是否存在

			if (Constants.ipcModelType.isFamilerIPC(ipcamera.getModel())) {
				// 删除用户与IPC关联的数据
				if (null == userCamera) {
					logger.warn("不是当前用户" + userId + "绑定的设备" + sn
							+ "并且该用户没有关联其他用户");
					throw new BusinessException("0x50020041");
				}
				userService.unbindDevice(userId, ipcamera.getId());
				//删除家庭组的相关消息
				AppDeviceShare appShareDevice = new AppDeviceShare();
				appShareDevice.setDeviceId(String.valueOf(ipcamera.getId()));
				appShareDevice.setMemberId(String.valueOf(userId));
				familyCircleService.deleteShareDeviceById(appShareDevice);
				// 清除S3相关的数据
				s3Service.deleteBySN(sn);
			} else if (Constants.ipcModelType.isHPC03IPC(ipcamera.getModel())) {
				// 删除用户与IPC关联的数据
				if (null == userCamera) {
					logger.warn("不是当前用户" + userId + "绑定的设备" + sn
							+ "并且该用户没有关联其他用户");
					throw new BusinessException("0x50020041");
				}
				userService.unbindDevice(userId, ipcamera.getId());
				//判断还有其他用户绑定没有
				List<AppUser> list = ipcService.getIPCameraLinkedUsers(sn);
				if (list.size() == 0) {
					s3Service.deleteBySN(sn);
				}
			} else if (Constants.ipcModelType.isHPC03BIPC(ipcamera.getModel())) {
				//删除hpc03b
				deleteLinkHpc03b(loginUser,userCamera,ipcamera);
				
			} else {
				logger.warn("当前设备" + sn + "没有支持的model" + ipcamera.getModel());
				throw new BusinessException("0x50020041");
			}

		} catch (ManagerException e) {
			logger.error(e);
			throw new BusinessException("0x50000044");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	@Transactional
	public void installDevice(Long userId, String sn) throws BusinessException {
		// 1> 校验：设备是否存在
		IPCamera ipCamera = ipcService.getIPCameraBySN(sn);
		if (ipCamera == null) {
			throw new BusinessException("0x50000047");
		}

		// 2> 检查该sn是否与该用户已经进行过绑定或者install
		IPCamera ipcamera1 = userService.getUserBindDevice(userId, sn);
		if (ipcamera1 != null) {
			throw new BusinessException("0x50020060");
		}

		// 3> 查询该SN被install的用户是否已达到32个
		UserCameraQuery userCameraQuery = new UserCameraQuery();
		userCameraQuery.setCameraId(ipCamera.getId());
		userCameraQuery.setLinkType(1);
		// 取得被install的总数
		int linkedCount = querySum(userCameraQuery);
		if (linkedCount >= 32) {
			throw new BusinessException("0x50020073");
		}

		// 4> 插入数据库
		UserCamera userCamera = new UserCamera();
		userCamera.setUserId(userId);
		userCamera.setCameraId(ipCamera.getId());
		userCamera.setCameraSerialno(ipCamera.getSn());
		userCamera.setConfirm(Boolean.TRUE);
		userCamera.setLocalTime(new Date());
		// 判断用户是否被关联
		AppUser appUser = userService.getUserById(userId);
		if (appUser.getBindFlag() == 1) {
			// 有被关联，IsUsedToShareUser设为1
			userCamera.setIsUsedToShareUser(1);
		}else {
			// 没有被关联，IsUsedToShareUser设为0
			userCamera.setIsUsedToShareUser(0);
		}
		// 设置用户与ipc关系为install
		userCamera.setLinkType(1);
		// 用户与ipc的关系为install时，允许ipc的告警信息也推送到用户
		userCamera.setEnable("true");
		userService.bindOneDevice(userCamera);
		
		// 智能赋予安装角色
		userPermissionService.assignRole(userId, appUser.getKind(), Constants.roleType.INSTALL.getId(), ipCamera.getId());
	}
	/**
	 * 解绑hpc03b的流程
	 * @param loginUser
	 * @param userCamera
	 * @param ipcamera
	 * @author weibin
	 */
	@Transactional
	private void deleteLinkHpc03b(AppUser loginUser,UserCamera userCamera,IPCamera ipcamera){
		if (null != userCamera || loginUser.getLinkUserId() != null) {
			if (userCamera != null) {
				logger.info("这个设备" + ipcamera.getSn() + "是这个用户" + loginUser.getId() + "绑定");
				if (userCamera.getLinkType() == 0) {
					// 清除S3相关的数据
					ipcService.deleteIPCameraLinkedUsers(userCamera
							.getCameraId());
					s3Service.deleteBySN(ipcamera.getSn());
				} else {
					userService.unbindDevice(loginUser.getId(), ipcamera.getId());
				}
			} else {
				// 获取关联用户的userID
				Long ralateUserId = loginUser.getLinkUserId();
				UserCamera relateUserCamera = userService
						.getUserCamera(ralateUserId, ipcamera.getSn());
				
				if (relateUserCamera != null) {
					logger.info("这个设备" + ipcamera.getSn() + "是这个用户" + loginUser.getId() + "关联用户"
							+ ralateUserId + "绑定的");
					relateUserCamera.setIsUsedToShareUser(0);
					userService.updateUserCamera(relateUserCamera);
				} else {
					logger.warn("不是当前用户" + loginUser.getId() + "绑定的设备" + ipcamera.getSn()
							+ "也不是他关联app用户绑定的设备");
					throw new BusinessException("0x50020041");
				}

			}
		} else {
			logger.warn("不是当前用户" + loginUser.getId() + "绑定的设备" + ipcamera.getSn()
					+ "并且该用户没有关联其他用户");
			throw new BusinessException("0x50020041");
		}
	}
	
}
