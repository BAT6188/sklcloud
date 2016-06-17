package com.skl.cloud.dao;

import com.skl.cloud.model.user.UserThirdAccount;


public interface AppUserAccountMgtMapper
{
	public int reSetPw(String userId, String cloudRandom, String password, String newCloudRandom) throws Exception;
	
	public void insertUserThirdAccount(UserThirdAccount thirdAccount) throws Exception;

	public UserThirdAccount selectUserThirdAccount(String openKind, String openID) throws Exception;
}
