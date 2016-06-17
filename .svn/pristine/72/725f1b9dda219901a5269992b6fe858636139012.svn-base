package com.skl.cloud.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.AppOwnedByOtherService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.constants.Constants;

@Service("appOwnedByOtherService")
public class AppOwnedByOtherImpl implements AppOwnedByOtherService {

	@Autowired
	private IPCameraService ipcService;

	@Autowired(required = true)
	private IPCameraService iPCameraService;

	@Autowired(required = true)
	private AppUserService appUserService;

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> queryownedByOther(Long userId, String sn) {

		Map<String, String> retMap = new HashMap<String, String>();

		String isOwnedByOther = Constants.Code.CODE_0.getStringValue();

		IPCamera ipcamera = iPCameraService.getIPCameraBySN(sn);
		String cameraModel = ipcamera.getModel();
		
		// 0:设备与用户已经绑定
		List<AppUser> users = ipcService.getIPCameraLinkedUsers(sn,0);

		if (users != null && users.size() > 0) {
			List<Long> list = new ArrayList<Long>();

			for (AppUser user : users) {
				list.add(user.getId());
			}

			if (list.contains(userId)) {

				isOwnedByOther = Constants.Code.CODE_1.getStringValue();

			} else {

				isOwnedByOther = Constants.Code.CODE_2.getStringValue();
				userId = list.get(0);

			}

			retMap.put("isOwnedByOther", isOwnedByOther);

			// 区别产品：当产品是HPC03B时
			if (Constants.ipcModelType.isHPC03BIPC(cameraModel)) {

				AppUser appUser = appUserService.getUserById(userId);
				retMap.put("email", appUser.getUserType() == 0? appUser.getEmail(): "NULL");
				retMap.put("nickName", appUser.getUserType() == 1? appUser.getName(): "NULL");

				return retMap;
			} else {

				// HPC03A和family产品
				retMap.put("email", "NULL");
				retMap.put("nickName", "NULL");
				return retMap;
			}

		} else {
			// 所有产品统一处理
			retMap.put("isOwnedByOther", isOwnedByOther);
			retMap.put("email", "NULL");
			retMap.put("nickName", "NULL");
			return retMap;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean queryIpcIslive(String sn) {
		boolean ret = false;
		IPCamera ipcamera = ipcService.getIPCameraBySN(sn);
		if (ipcamera != null && ipcamera.getIsLive() != null && ipcamera.getIsLive()) {
			ret = true;
		}
		return ret;
	}

}
