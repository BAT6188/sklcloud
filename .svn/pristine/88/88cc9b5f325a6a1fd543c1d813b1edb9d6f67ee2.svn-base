package com.skl.cloud.dao.user;

import com.skl.cloud.common.entity.IdEntity;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.AppUser;

/**
 * @ClassName: UserMapper
 * @Description: Wechat User dao
 * @author yangbin
 * @date 2015/11/13
*/
public interface BaseUserMapper<T extends IdEntity> {

    /**
     * insert(关注公众号时保存User的信息)
     * @Title insert
     * @param wechatUser
    */
    public void insert(T user);

    /**
     * delete(取关时删除User的信息)
     * @Title delete
     * @param wechatUser
    */
    public void delete(Long id);

    /**
     * update(更新WechatUser的信息)
     * @Title update
     * @param wechatUser
    */
    public void update(T user);

    /**
     * @Title: isExistUser
     * @Description: 判断用户是否存在
     * @param  userId
     * @return Boolean
    */
    public Boolean isExistUser(Long userId);

    /**
     * get user info by user
     * @return user is the {@link AppUser} {@link WechatUser} object
     * @throws BusinessException
    */
    public T getUser(T user) throws BusinessException;
}
