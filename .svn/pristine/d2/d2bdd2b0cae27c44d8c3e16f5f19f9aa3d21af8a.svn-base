package com.skl.cloud.service.user.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.entity.IdEntity;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.user.BaseUserMapper;
import com.skl.cloud.dao.user.UserCameraMapper;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.UserCameraQuery;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.service.user.UserService;

/**
 * @ClassName: AbstractUserServiceImpl
 * @Description: User Service interface class, 
 * user's feature and operation in this class, including WechatUser, AppUser, UserCamera.
 * @author yangbin
 * @date 2015/11/13
*/
public abstract class AbstractUserServiceImpl<T extends IdEntity> implements UserService<T> {

    // sIV
    protected static final String SIV = "0000000000000000";

    protected abstract BaseUserMapper<T> getUserMapper();

    protected abstract UserCameraMapper getUserCameraMapper();

    /**
     * Create a new user
     * @param user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
     */
    @Override
	@Transactional
    public T createUser(T user) throws BusinessException {
        getUserMapper().insert(user);
        return getUser(user);
    }

    /**
      * Delete an user
      * @param user is the {@link AppUser} {@link WechatUser} object
      * @throws BusinessException
     */
    @Override
	@Transactional
    public void deleteUser(Long userId) throws BusinessException {
        getUserMapper().delete(userId);
    }

    /**
     * update for an user
     * @param user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    @Override
	@Transactional
    public void updateUser(T user) throws BusinessException {
        getUserMapper().update(user);
    }

    /**
     * check the user is exits, exist is true, else is false
     * @param userId
     * @return Boolean
     * @throws BusinessException
    */
    @Override
	@Transactional(readOnly = true)
    public Boolean isExistUser(Long userId) throws BusinessException {
        return getUserMapper().isExistUser(userId);
    }

    /**
     * get user info by user
     * @return user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    @Override
    @Transactional(readOnly = true)
    public T getUser(T user) throws BusinessException {
        return getUserMapper().getUser(user);
    }

    /**
     * 绑定某个设备到用户,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void bindOneDevice(UserCamera userCamera) throws BusinessException {
    	if(userCamera != null && userCamera.getEnable() == null){
    		userCamera.setEnable("true");
    	}
    	if(userCamera != null && userCamera.getUserNotification() == null){
    		userCamera.setUserNotification("0");
    	}
        getUserCameraMapper().insert(userCamera);
    }

    /**
     * 解绑用户下某个设备,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void unbindDevice(Long userId, Long cameraId) throws BusinessException {
        UserCamera userCamera = new UserCamera();
        userCamera.setUserId(userId);
        if (cameraId != null) {
            userCamera.setCameraId(cameraId);
        }
        getUserCameraMapper().delete(userCamera);
    }

    /**
     * 解绑用户下所有的设备,包括APP用户与Wechat用户
     * @param userCamera
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void unbindDevice(Long userId) throws BusinessException {
        unbindDevice(userId, null);
    }

    /**
     * 通过userId得到用户所绑定的所有IPC设备
     * @param userId
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public List<IPCamera> getUserBindDeviceList(Long userId) throws BusinessException {
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(userId);
        return getUserCameraMapper().queryUserBindDeviceList(userCameraQuery);
    }

    /**
     * 通过userId和camera SN得到用户所绑定的IPC设备
     * @param userId
     * @param sn
     * @return {@link IPCamera}
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public IPCamera getUserBindDevice(Long userId, String sn) throws BusinessException {
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(userId);
        userCameraQuery.setSn(sn);

        List<IPCamera> ipcList = getUserCameraMapper().queryUserBindDeviceList(userCameraQuery);
        if (ipcList != null && ipcList.size() > 0) {
            return ipcList.get(0);
        }
        return null;
    }
    
    /**
     * 通过userId和camera SN得到UserCamera信息
     * @param userId
     * @param sn
     * @return {@link UserCamera}
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public UserCamera getUserCamera(Long userId, String sn) throws BusinessException{
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(userId);
        userCameraQuery.setSn(sn);

        List<UserCamera> userCameraList = getUserCameraMapper().queryUserCamera(userCameraQuery);
        if (userCameraList != null && userCameraList.size() > 0) {
            return userCameraList.get(0);
        }
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserCamera>  getUserCameraList(Long userId) throws BusinessException{
        UserCameraQuery userCameraQuery = new UserCameraQuery();
        userCameraQuery.setUserId(userId);
        return getUserCameraMapper().queryUserCamera(userCameraQuery);
    }
    
    /**
     * 更新UserCamera的信息
     * @param userCamera {@link UserCamera}
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void updateUserCamera(UserCamera userCamera) throws BusinessException{
        getUserCameraMapper().update(userCamera);
    }

    /**
     * 重新组合解密需要的skey
     * @param sKey
     * @return String skey
     */
    protected String combEncryptKey(String sKey) {
        String ret = sKey;
        if (sKey.length() >= 16) {
            ret = sKey.substring(0, 16);
        } else {
            for (int i = ret.length(); i < 16; i++) {
                ret += "0";
            }
        }
        return ret;
    }
}
