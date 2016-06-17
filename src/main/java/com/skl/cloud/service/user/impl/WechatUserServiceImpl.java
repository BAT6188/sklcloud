package com.skl.cloud.service.user.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.user.BaseUserMapper;
import com.skl.cloud.dao.user.UserCameraMapper;
import com.skl.cloud.dao.user.WechatUserMapper;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.UserCameraQuery;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.service.user.WechatUserService;
import com.skl.cloud.util.encrypt.EncrytorUtil;

/**
 * @Package com.skl.cloud.service.user.impl
 * @Title: WechatUserServiceImpl
 * @Description: user's feature and operation in this class, including WechatUser, UserCamera.
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年11月13日
 * @version V1.0
 */
@Service
public class WechatUserServiceImpl extends AbstractUserServiceImpl<WechatUser> implements WechatUserService {
    private static Logger logger = LoggerFactory.getLogger(WechatUserServiceImpl.class);

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Autowired
    private UserCameraMapper userCameraMapper;

    @Autowired
    private AppUserService appUserService;

    @Override
    protected BaseUserMapper<WechatUser> getUserMapper() {
        return wechatUserMapper;
    }

    @Override
    protected UserCameraMapper getUserCameraMapper() {
        return userCameraMapper;
    }

    /**
     * 用户关注微信公众号
     * @param WechatUser
     * @throws BusinessException (参数说明)
    */
    @Override
    @Transactional
    public void subscribe(WechatUser wechatUser) throws BusinessException {
        wechatUser.setSubscribeFlag(WechatUser.SUBSCRIBE_FLAG_YES);
        // 1>检查用户是否存在
        if (this.isExistWechatUser(wechatUser.getOpenId())) {// 2>如果存在则更新为关注
            super.updateUser(wechatUser);
        } else {// 3>如果不存在则添加新用户
            this.createUser(wechatUser);
        }
    }

    /**
     * 用户取消关注微信公众号
     * @param WechatUser
     * @throws BusinessException (参数说明)
    */
    @Override
    @Transactional
    public void unsubscribe(WechatUser wechatUser) throws BusinessException {
        wechatUser.setSubscribeFlag(WechatUser.SUBSCRIBE_FLAG_NO);
        // 获取微信用户对应的userId
        Long userId = wechatUserMapper.queryUserByOpenId(wechatUser.getOpenId()).getUserId();
        // 1> 更新用户关注标识为0
        super.updateUser(wechatUser);
        // 2> 解绑用户下所有的IPC设备
        super.unbindDevice(userId);
    }

    /**
     * 关注公众号时保存WechatUser的信息
     * @param sn
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public WechatUser createUser(WechatUser wechatUser) throws BusinessException {

    	String emailPrifix = UUID.randomUUID().toString().replace("-", "");
        final String userName = "wc";
        String password = "123456";
        String userEmail = emailPrifix + "@hpc03.com";
        AppUser appUser = null;

        // 1> 添加一条信息到主User表，虚拟数据。
        try {
            String sKey = userName + emailPrifix;
            // 对密钥做组合
            sKey = combEncryptKey(sKey);
            // 转成Base64后的密码
            password = EncrytorUtil.Encrypt(password, sKey, SIV);

            appUser = new AppUser();
            appUser.setEmail(userEmail);
            appUser.setName(userName);
            appUser.setPassword(password);
            appUser.setUserType(1);
            appUser.setBindFlag(0);
            appUserService.createUser(appUser);
        } catch (Exception e) {
            logger.error("appUser Service insertAppUserRegister error.", e);
        }

        // 2> 得到保存的APP用户信息
        appUser = appUserService.getAppUserByEmail(userEmail);

        // 3> 添加微信用户信息到Wechat User表
        wechatUser.setUserId(appUser.getId());
        wechatUserMapper.insert(wechatUser);

        return wechatUser;
    }

    /**
     * 删除WechatUser的信息
     * @param wechatUser
     * @throws BusinessException (参数说明)
    */
    @Override
    @Transactional
    public void deleteUser(Long userId) throws BusinessException {
        // 删除Wechat user的数据
        super.deleteUser(userId);
        // 删除Wechat User对应的APP user的数据
        appUserService.deleteUser(userId);
        // 删除绑定的设备
        super.unbindDevice(userId);
    }

    /**
     * 通过opendId得到用户所绑定的所有IPC设备(for wechat)
     * @param openId
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public List<IPCamera> getUserBindDeviceList(String openId) throws BusinessException {
        WechatUser wechatUser = wechatUserMapper.queryUserByOpenId(openId);
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(wechatUser.getUserId());
        return userCameraMapper.queryUserBindDeviceList(userCameraQuery);
    }

    /**
     * 通过opendId和devieceId得到用户所绑定的IPC设备(for wechat)
     * @param openId
     * @param deviceId
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public IPCamera getUserBindDevice(String openId, String deviceId) throws BusinessException {
        WechatUser wechatUser = wechatUserMapper.queryUserByOpenId(openId);
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(wechatUser.getUserId());
        userCameraQuery.setDeviceId(deviceId);

        List<IPCamera> ipcList = userCameraMapper.queryUserBindDeviceList(userCameraQuery);
        if (ipcList != null && ipcList.size() > 0) {
            return ipcList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isExistWechatUser(String openId) throws BusinessException {
        return wechatUserMapper.isExistUser(openId);
    }

    /**
     * 检查微信用户和设备的绑定情况
     * @param openId
     * @param sn
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkWebchatBind(String openId, String sn) {
        UserCamera uc = userCameraMapper.getUserCameraByOpenIdAndSN(openId, sn);
        return uc != null;
    }

    /**
     * 检查设备是否被任何微信用户绑定了
     * @param deviceId
     * @return boolean
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkIPCIsBindByDeviceId(String deviceId) {
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setDeviceId(deviceId);

        List<IPCamera> ipcList = userCameraMapper.queryUserBindDeviceList(userCameraQuery);
        if (ipcList != null && ipcList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 通过openId得到WechatUser的信息
     * @param openId
     * @return WechatUser
     * @throws BusinessException (参数说明)
    */
    @Override
    @Transactional(readOnly = true)
    public WechatUser getUserByOpenId(String openId) throws BusinessException {
        return wechatUserMapper.queryUserByOpenId(openId);
    }

    /**
     * 通过绑定IPC设备的SN查询WechatUser的信息
     * @param sn
     * @return WechatUser
     * @throws BusinessException (参数说明)
    */
    @Override
    @Transactional(readOnly = true)
    public WechatUser getUserByBindSn(String sn) throws BusinessException {
        List<WechatUser> list = wechatUserMapper.queryUserByBindSn(sn);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
