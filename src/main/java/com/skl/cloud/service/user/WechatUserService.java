package com.skl.cloud.service.user;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.WechatUser;

/**
 * @Package com.skl.cloud.service.user.impl
 * @Title: WechatUserService
 * @Description: Wechat User Service interface class,
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年11月13日
 * @version V1.0
 */
public interface WechatUserService extends UserService<WechatUser> {

    /**
     * 用户关注微信公众号(for wechat)
     * @param WechatUser
     * @throws BusinessException (参数说明)
    */
    public void subscribe(WechatUser wechatUser) throws BusinessException;

    /**
     * 用户取消关注微信公众号(for wechat)
     * @param WechatUser
     * @throws BusinessException (参数说明)
    */
    public void unsubscribe(WechatUser wechatUser) throws BusinessException;

    /**
     * 通过openId得到用户所绑定的所有IPC设备(for wechat)
     * @param opendId
     * @throws BusinessException
     */
    public List<IPCamera> getUserBindDeviceList(String openId) throws BusinessException;

    /**
     * 通过opendId和devieceId得到用户所绑定的IPC设备(for wechat)
     * @param openId
     * @param deviceId
     * @throws BusinessException
     */
    public IPCamera getUserBindDevice(String openId, String deviceId) throws BusinessException;

    /**
     * 检查微信用户是否存在, 存在则为true,否则为false
     * @param openId
     * @return Boolean
     * @throws BusinessException (参数说明)
    */
    public Boolean isExistWechatUser(String openId) throws BusinessException;

    /**
     * 检查微信用户和设备的绑定情况
     * @param openId
     * @param sn
     * @return
     */
    public boolean checkWebchatBind(String openId, String sn);

    /**
     * 检查设备是否被任何微信用户绑定了
     * @param deviceId
     * @return boolean
     */
    public boolean checkIPCIsBindByDeviceId(String deviceId);
    
    
    /**
     * 通过openId查询WechatUser的信息
     * @param openId
     * @return WechatUser
     * @throws BusinessException (参数说明)
    */
    public WechatUser getUserByOpenId(String openId) throws BusinessException;
    
    /**
     * 通过绑定IPC设备的SN查询WechatUser的信息
     * @param sn
     * @return WechatUser
     * @throws BusinessException (参数说明)
    */
    public WechatUser getUserByBindSn(String sn) throws BusinessException;

}
