package com.skl.cloud.service.user.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.user.AppUserMapper;
import com.skl.cloud.dao.user.BaseUserMapper;
import com.skl.cloud.dao.user.UserCameraMapper;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.constants.S3ServiceType;
import com.skl.cloud.util.encrypt.AESUtil;
import com.skl.cloud.util.encrypt.EncrytorUtil;
import com.skl.cloud.util.validator.XmlElementValidator;

/**
 * @Package com.skl.cloud.service.user.impl
 * @Title: AppUserServiceImpl
 * @Description: user's feature and operation in this class, including AppUser, UserCamera.
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年11月13日
 * @version V1.0
 */
@Service
public class AppUserServiceImpl extends AbstractUserPermissionServiceImpl<AppUser> implements AppUserService {

	@Autowired
	private AppUserMapper appUserMapper;

	@Autowired
	private UserCameraMapper userCameraMapper;

	@Autowired
	private S3Service s3Service;

	@Override
	protected BaseUserMapper<AppUser> getUserMapper() {
		return appUserMapper;
	}

	@Override
	protected UserCameraMapper getUserCameraMapper() {
		return userCameraMapper;
	}

	/**
	 * queryUserByEmail(通过userEmail查询APPUser的信息)
	 * @Title queryUserByEmail
	 * @param userEmail
	 */
	@Override
	@Transactional(readOnly = true)
	public AppUser getAppUserByEmail(String userEmail) throws BusinessException {
		AppUser user = new AppUser();
		user.setEmail(userEmail);
		return super.getUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public AppUser getUserByPortraintUuid(String uuid) throws BusinessException {
		AppUser user = new AppUser();
		user.setPortraintUuid(uuid);
		return super.getUser(user);
	}

	/**
	 * APP用户注册时保存AppUser的信息
	 * @param appuser
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public AppUser createUser(AppUser appUser) throws BusinessException {
		// 对密码进行转换
		// 获取邮箱@字符前的字符

		String preEmail = appUser.getEmail().substring(0, appUser.getEmail().indexOf("@"));
		String sKey = StringUtils.trim(appUser.getName()) + preEmail;
		// 对密钥做组合
		sKey = combEncryptKey(sKey);
		String prePwd = appUser.getPassword();
		String decodPwd;
		try {
			decodPwd = EncrytorUtil.Decrypt(prePwd, sKey, SIV);
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), this.getClass().getName());
			throw new ManagerException(e);
		}
		decodPwd = StringUtils.trim(decodPwd);
		String md5Pwd = AESUtil.string2MD5(decodPwd);
		appUser.setPassword(md5Pwd);

		// 用户注册，默认用户状态为false(不在线)
		if (appUser.getStatus() == null) {
			appUser.setStatus(false);
		}
		appUser.setNotification("true");

		long currentTimeMillis = System.currentTimeMillis();
		java.sql.Timestamp currentTime = new java.sql.Timestamp(currentTimeMillis);
		// 设置用户的创建时间
		appUser.setCreateDate(currentTime);
		// 设置用户的登录时间
		appUser.setLoginTime(currentTime);
		super.createUser(appUser);

		return appUser;
	}

	/**
	 * APP用户注册时保存AppUser, 用于HPC03不解密直接保存
	 * @param appuser
	 * @throws BusinessException
	 */
	@Transactional
	public AppUser createUserNoDecrypt(AppUser appUser) throws BusinessException {
		if (appUser.getStatus() == null) {
			appUser.setStatus(false);
		}
		appUser.setNotification("true");
		appUser.setCreateDate(new Date());
		return super.createUser(appUser);
	}

	/**
	 * 得到忘记密码的用户
	 * @param appuser
	 * @throws BusinessException
	 */
	@Transactional
	public AppUser getForgetPwUserByEmail(String email) throws BusinessException {
		return appUserMapper.getForgetPwUserByEmail(email);
	}

	/**
	 * 根据用户的id更新用户信息
	 * @param appuser
	 * @throws BusinessException
	 */
	@Transactional
	public void updateUserById(AppUser appUser) throws BusinessException {
		appUserMapper.updateUserWithId(appUser);
	}

	/**
	 * get user info by userId
	 * @return user is the {@link AppUser} {@link WechatUser} object
	 * @throws BusinessException
	*/
	@Transactional(readOnly = true)
	public AppUser getUserById(Long userId) throws BusinessException {
		AppUser user = new AppUser();
		user.setId(userId);
		return super.getUser(user);
	}

	/**
	 * 通过userId得到用户信息和用户下的设备信息所绑定的IPC设备
	 * @param userId
	 * @param sn
	 * @return {@link IPCamera}
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public AppUser getFullUser(Long userId) throws BusinessException {
		AppUser user = this.getUserById(userId);
		List<IPCamera> ipcameras = super.getUserBindDeviceList(userId);
		user.setIpcameras(ipcameras);
		return user;
	}

	/**
	 * APP设置用户头像、昵称、notification使能等信息
	 */
	@Override
	@Transactional
	public Map<String, Object> updateAppUserInfo(Long userId, String nickName, String portraitId, String notification,
			String fileName) throws BusinessException {

		if (notification != null) {
			// 校验：notification格式合法
			if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("notification"), notification)) {
				throw new BusinessException("0x50000030");
			}
		}

		AppUser appuser = this.getUserById(userId);

		if (appuser == null) {
			throw new BusinessException("0x50020031");
		}

		int count = 0;
		if (nickName != null) {
			appuser.setName(nickName);
			count++;
		}
		if (portraitId != null) {
			if (!portraitId.equals("-1")) {
				appuser.setPortraintId(portraitId);
			}
			count++;
		}
		if (notification != null) {
			appuser.setNotification(notification);
			count++;
		}

		if (count <= 0) {
			throw new BusinessException("0x50010031");
		}

		Map<String, Object> backMap = new LinkedHashMap<String, Object>();

		if ("-1".equals(portraitId)) {
			// portraitId为-1时，fileName必填
			if (fileName == null || fileName.isEmpty()) {
				throw new BusinessException("0x50000029");
			}

			String uuid = appuser.getPortraintUuid();
			String urlPath;
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId", String.valueOf(userId));

			if (uuid != null && !uuid.isEmpty()) {
				S3FileData file = s3Service.getUploadFileByUuid(uuid);
				if (file == null) {
					urlPath = s3Service.getUrlAndSaveInfo(S3ServiceType.SYSTEM_USER_PORTRAIT.getType(), uuid, paramMap);
				} else {
					urlPath = file.getFilePath();
				}
			} else {
				uuid = UUID.randomUUID().toString();
				urlPath = s3Service.getUrlAndSaveInfo(S3ServiceType.SYSTEM_USER_PORTRAIT.getType(), uuid, paramMap);
			}
			appuser.setPortraintUuid(uuid);
			backMap.put("uuid", uuid);
			backMap.put(
					"url",
					S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(urlPath + fileName),
							HttpMethod.PUT));
			backMap.put("serviceType", S3ServiceType.SYSTEM_USER_PORTRAIT.getType());
		}

		this.updateUser(appuser);

		return backMap;
	}

}
