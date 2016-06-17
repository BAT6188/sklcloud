package com.skl.cloud.service;

import java.util.Map;

import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.UserThirdAccount;

public interface AppUserAccountMgtService
{
	
	/**
	 * <用户忘记密码>
	 * <忘记密码专用>
	 * @param email [用户email]
	 * @throws Exception
	 */
	public void forgetPw(String email) throws Exception;
	
	/**
	 * <用户重置密码>
	 * @param userId [用户id]
	 * @param cloudRandom [云端随机数]
	 * @param password [新密码]
	 * @throws Exception
	 */
	public int reSetPw(String userId, String cloudRandom, String password) throws Exception;
	
	/**
	 * <验证第三方账户的合法性>
	 * @param openKind [账户类型]
	 * @param openID [账户openID]
	 * @param accessToken [账户accessToken]
	 * @throws Exception
	 */
	public void checkThirdAccount(String openKind, String openID, String accessToken) throws Exception;
	
	/**
	 * <创建第三方账号>
	 * @param thirdUser [第三方账号]
	 * @throws Exception
	 */
	public void createUserThirdAccount(UserThirdAccount thirdAccount) throws Exception;
	
	/**
	 * <获取第三方账户信息>
	 * @param openKind [账户类型]
	 * @param openID [账户openID]
	 * @throws Exception
	 */
	public UserThirdAccount getUserThirdAccount(String openKind, String openID) throws Exception;
	
	/**
	 * <app用户注册账号email类型>
	 * @param appUser [用户信息]
	 * @param fileName [自定义头像文件名]
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addUserByEmail(AppUser appUser, String fileName) throws Exception;
	
	/**
	 * <app用户登录>
	 * @param paraMap [请求参数]
	 * @return
	 * @throws Exception
	 */
	public AppUser updateUserLoginByEmail(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * <app用户登录,第三方账户>
	 * @param paraMap [请求参数]
	 * @return
	 * @throws Exception
	 */
	public AppUser updateUserLoginByThirdAccount(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 
	  * sendEmailWithAttachment(发送带附件的邮件)
	  * @Title: sendEmailWithAttachment
	  * @Description: TODO
	  * @param @param mailInfo (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月8日
	 */
	public void sendEmailWithAttachment(Map<String, String> mailInfo);
}
