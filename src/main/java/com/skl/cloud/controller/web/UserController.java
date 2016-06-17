package com.skl.cloud.controller.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.validator.ValidationUtils;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.controller.web.dto.UserIPCameraFO;
import com.skl.cloud.foundation.mvc.method.annotation.Param;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.WechatUserService;

/**
 * @ClassName: UserController
 * @Description: User request from wechat
 * @author yangbin
 * @date 2015年10月7日
*/

@Controller
@RequestMapping("/skl-cloud/wechat")
public class UserController extends FrontController {
	private static final Logger logger = Logger.getLogger(UserController.class);
    // 关注
    private static final String TYPE_SUBSCRIBE = "subscribe";
    // 取消关注
    private static final String TYPE_UNSUBSCRIBE = "unSubscribe";
    // 绑定一个设备
    private static final String TYPE_BINDONE = "bindOne";
    // 取消某个设备的绑定
    private static final String TYPE_UNBINDONE = "unBindOne";
    // 取消用户所有设备的绑定
    private static final String TYPE_UNBINDALL = "unBindAll";

    @Autowired
    private WechatUserService userService;

    @Autowired
    private IPCameraService ipcService;

	@Autowired
	private S3Service s3Service;
    
    /**
     * 用户关注/取消微信公众号
     * @param openId
     * @param type
     */
    @RequestMapping("/user/subscribe.json")
    public void subscribe(@Param String openId, @Param String type, Model model) {
        AssertUtils.isNotBlank(openId, new String[] { "openId" });
        if (!ValidationUtils.checkOpenId(openId)) {
			AssertUtils.formatNotCorrect(new String[] { "openId" }); 
		}
        AssertUtils.isNotBlank(type, new String[] { "type" });

        WechatUser wechatUser = new WechatUser();
        wechatUser.setOpenId(openId);
        // 用户关注微信公众号
        if (TYPE_SUBSCRIBE.equals(type)) {
            userService.subscribe(wechatUser);
            // 取消关注微信公众号
        } else if (TYPE_UNSUBSCRIBE.equals(type)) {
        	// 检查用户是否存在并关注公众号
            validateParam(openId);
            // 取消关注
            userService.unsubscribe(wechatUser);
        } else {
            AssertUtils.formatNotCorrect(new String[] { "type:"+type }); 
        }
    }

    /**
     * 获取与用户绑定的IPC信息
     * @param openId
     */
    @RequestMapping("/user/devicelist.json")
    public void deviceList(@Param String openId, Model model) {
        // 检查用户是否存在并关注公众号
        validateParam(openId);

        // 通过opendId得到用户所绑定的所有IPC设备
        List<IPCamera> ipcList = userService.getUserBindDeviceList(openId);
        List<UserIPCameraFO> foList = new ArrayList<UserIPCameraFO>();
        for (IPCamera ipcamera : ipcList) {
            UserIPCameraFO fo = new UserIPCameraFO();
            fo.setStatus( ipcamera.getIsOnline() != null ? (ipcamera.getIsOnline() ? Constants.Code.CODE_1.getStringValue()
                    : Constants.Code.CODE_0.getStringValue()) : Constants.Code.CODE_0.getStringValue());
            fo.setName(ipcamera.getNickname());
            fo.setDeviceId(ipcamera.getDeviceId());
            // 查询预览图片的S3下载url
            if (StringUtils.isBlank(ipcamera.getSnapshotUuid())) {
				fo.setImgUrl("");
			}else {
				String imgUrl = "";
				try {
					S3FileData file = new S3FileData();
					file = s3Service.getUploadFileByUuid(ipcamera.getSnapshotUuid());
					imgUrl = file.getFilePath() + file.getFileName();
				} catch (Exception e) {
					logger.info("**********获取url失败，请检查表t_platform_s3_file_data是否有service_uuid:" + ipcamera.getSnapshotUuid() + "相关的值*********");
					AssertUtils.throwBusinessEx(0x50000002, new String[] { "imgUrl" });
				}
				
				fo.setImgUrl(imgUrl);
			}
            // 如果云端没有数据则从IPC Remote端获取
            if (ipcamera.getSpeakerVolume() == null) {
                IPCamera remoteCamera = ipcService.getIPCRemoteVolume(ipcamera);
                fo.setSpeakerVolume(remoteCamera.getSpeakerVolume());
                // 同步设置到云端
                ipcService.updateIPCamera(remoteCamera);
            } else {
                fo.setSpeakerVolume(ipcamera.getSpeakerVolume());
            }
            fo.setSn(ipcamera.getSn());
            foList.add(fo);
        }
        model.addAttribute("ipcList", foList);
    }

    /**
     * 微信用户绑定/取消绑定设备
     * @param openId
     * @param type
     * @param deviceId
     */
    @RequestMapping("/device/bind.json")
    public void bindDevice(@Param String openId, @Param String type, @Param String sn, @Param String deviceId,
            Model model) {
        // 1> 检查用户是否存在并关注公众号
        WechatUser wechatUser = validateParam(openId);
        AssertUtils.isNotBlank(type, new String[] { "type" });
        AssertUtils.isNotBlank(sn, new String[] { "sn" });
        AssertUtils.isNotBlank(deviceId, new String[] {"deviceId"});
        IPCamera ipcamera = null;
        if (TYPE_BINDONE.equals(type) || TYPE_UNBINDONE.equals(type)) {
            // 2> 通过deviceId得到IPC设备的信息
            ipcamera = ipcService.getIPCameraByDeviceId(deviceId);
            AssertUtils.existDB(ipcamera, new String[] {"deviceId:" + deviceId});
            // 检查SN与这个deviceId绑定的SN是否对应
            if (!ipcamera.getSn().equals(sn)) {
                throw new BusinessException(0x50000001, "sn [" + sn + "] not match to the deviceId.");
            }
        }

        // 3-1> 绑定某个设备到用户
        if (TYPE_BINDONE.equals(type)) {
            // 检查IPC是否已经被微信用户绑定
        	if (!userService.checkWebchatBind(openId, sn)) {//检查设备是否被当前用户绑定
        		if (userService.checkIPCIsBindByDeviceId(deviceId)) {//检查设备是否被其他用户绑定
                    AssertUtils.throwBusinessEx(0x50030003, new String[] {deviceId}); 
                }
        		// 设置需要绑定的数值到model
                UserCamera userCamera = new UserCamera();
                userCamera.setUserId(wechatUser.getUserId());
                userCamera.setCameraId(ipcamera.getId());
                userCamera.setCameraSerialno(ipcamera.getSn());
                userCamera.setConfirm(Boolean.TRUE);
                userCamera.setLocalTime(new Date());
                userService.bindOneDevice(userCamera);
			}
            // 3-2> 解绑用户下某个设备
        } else if (TYPE_UNBINDONE.equals(type)) {
            userService.unbindDevice(wechatUser.getUserId(), ipcamera.getId());
            // 3-3> 解绑用户下所有的设备
        } else if (TYPE_UNBINDALL.equals(type)) {
            userService.unbindDevice(wechatUser.getUserId());
        } else { 
            AssertUtils.formatNotCorrect(new String[] { "type:"+type });
        }
    }

    /**
     * 验证请求过来的参数是否合法并正确
     */
    private WechatUser validateParam(String openId) {
        AssertUtils.isNotBlank(openId, new String[] {"openId"});
        WechatUser wechatUser = userService.getUserByOpenId(openId);
        if (wechatUser == null) {
        	AssertUtils.throwBusinessEx(0x50030000, new String[] {"openId:" + openId});
	    }

        boolean subscribeFlag = WechatUser.SUBSCRIBE_FLAG_YES == wechatUser.getSubscribeFlag();
        AssertUtils.isSubscribe(subscribeFlag, new String[] {openId});

        return wechatUser;
    }
}
