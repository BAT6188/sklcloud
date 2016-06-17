package com.skl.cloud.dao.user;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.AppUser;

public interface AppUserMapper  extends BaseUserMapper<AppUser>{

    /**
     * getForgetPwUserByEmail(通过userEmail查询AppUser的信息)
     * @Title queryUserByEmail
     * @param userEmail
    */
    public AppUser getForgetPwUserByEmail(String email);
    
    /**
     * 根据用户的id更新用户信息
     * @param appuser
     * @throws BusinessException
     */
    public void updateUserWithId(AppUser appUser);
}