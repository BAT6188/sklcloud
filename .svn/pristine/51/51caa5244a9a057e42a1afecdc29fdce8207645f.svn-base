package com.skl.cloud.service.user;


import java.util.Map;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.WechatUser;

/**
 * @Package com.skl.cloud.service.user.impl
 * @Title: AppUserService
 * @Description: App User Service interface class,
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年11月13日
 * @version V1.0
 */
public interface AppUserService extends UserPermissionService<AppUser> {

    /**
     * getAppUserByEmail(通过userEmail查询APPUser的信息)
     * @Title queryUserByEmail
     * @param userEmail
     */
    public AppUser getAppUserByEmail(String userEmail) throws BusinessException;
    
    /**
     * getUserByPortraintUuid(通过portraintUuid<自定义头像uuid>查询APPUser的信息)
     * @Title getUserByPortraintUuid
     * @param uuid
     */
    public AppUser getUserByPortraintUuid(String uuid) throws BusinessException;
    
    /**
    * APP用户注册时保存AppUser, 用于HPC03不解密直接保存
    * @param appuser
    * @throws BusinessException
    */
    public AppUser createUserNoDecrypt(AppUser appUser) throws BusinessException ;
    
    /**
     * 得到忘记密码的用户
     * @param appuser
     * @throws BusinessException
     */
    public AppUser getForgetPwUserByEmail(String email) throws BusinessException ;
    
    /**
     * 根据用户的id更新用户信息
     * @param appuser
     * @throws BusinessException
     */
    public void updateUserById(AppUser appUser) throws BusinessException ;
    
    /**
     * get user info by userId
     * @return user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    public AppUser getUserById(Long userId) throws BusinessException;
    
    /**
     * 通过userId得到用户信息和用户下的设备信息所绑定的IPC设备
     * @param userId
     * @param sn
     * @return {@link IPCamera}
     * @throws BusinessException
     */
    public AppUser getFullUser(Long userId) throws BusinessException;

    /**
     * APP设置用户头像、昵称、notification使能等信息
     * @param userId
     * @param nickName
     * @param portraitId
     * @param notification
     * @return
     * @throws BusinessException
     */
	public Map<String, Object> updateAppUserInfo(Long userId, String nickName, String portraitId, String notification,String fileName) throws BusinessException;

}
