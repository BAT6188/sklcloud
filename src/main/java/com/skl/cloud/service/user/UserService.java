package com.skl.cloud.service.user;

import java.util.List;

import com.skl.cloud.common.entity.IdEntity;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.WechatUser;

/**
 * @ClassName: UserService
 * @Description: User Service interface class, 
 * user's feature and operation in this class, including WechatUser, AppUser, UserCamera.
 * @author yangbin
 * @date 2015/11/13
*/
public interface UserService<T extends IdEntity> {

    /**
     * Create a new user
     * @param user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
     */
    public T createUser(T user) throws BusinessException;

    /**
      * Delete an user
      * @param user is the {@link AppUser} {@link WechatUser} object
      * @throws BusinessException
     */
    public void deleteUser(Long userId) throws BusinessException;

    /**
     * update for an user
     * @param user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    public void updateUser(T user) throws BusinessException;

    /**
     * check the user is exits, exist is true, else is false
     * @param userId
     * @return Boolean
     * @throws BusinessException
    */
    public Boolean isExistUser(Long userId) throws BusinessException;

    /**
     * get user info by user
     * @return user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    public T getUser(T user) throws BusinessException;

    /**
     * 绑定某个设备到用户,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    public void bindOneDevice(UserCamera userCamera) throws BusinessException;

    /**
     * 解绑用户下某个设备,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    public void unbindDevice(Long userId, Long cameraId) throws BusinessException;

    /**
     * 解绑用户下所有的设备,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    public void unbindDevice(Long userId) throws BusinessException;

    /**
     * 通过userId得到用户所绑定的所有IPC设备
     * @param userId
     * @throws BusinessException
     */
    public List<IPCamera> getUserBindDeviceList(Long userId) throws BusinessException;

    /**
     * 通过userId和camera SN得到用户所绑定的IPC设备
     * @param userId
     * @param sn
     * @return {@link IPCamera}
     * @throws BusinessException
     */
    public IPCamera getUserBindDevice(Long userId, String sn) throws BusinessException;
    
    /**
     * 通过userId和camera SN得到UserCamera信息
     * @param userId
     * @param sn
     * @return {@link UserCamera}
     * @throws BusinessException
     */
    public UserCamera getUserCamera(Long userId, String sn) throws BusinessException;
    
    /**
     * 更新UserCamera的信息
     * @param userCamera {@link UserCamera}
     * @throws BusinessException
     */
    public void updateUserCamera(UserCamera userCamera) throws BusinessException;
    
    /**
     * 
     * 获取用户关联的所有设备列表
     * <p>Creation Date: 2016年5月12日 and by Author: fulin </p>
     * @param userId
     * @return
     * @throws BusinessException
     * @return List<UserCamera>
     * @throws
     *
     */
    public List<UserCamera> getUserCameraList(Long userId) throws BusinessException;
}
