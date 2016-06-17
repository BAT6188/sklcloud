package com.skl.cloud.service.ipc.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.AssertUtils;
import com.skl.cloud.model.ipc.IPCameraSub;
import com.skl.cloud.controller.web.dto.ShareStatusFO;
import com.skl.cloud.dao.ipc.IPCMapper;
import com.skl.cloud.dao.user.ShareMapper;
import com.skl.cloud.dao.user.UserCameraMapper;
import com.skl.cloud.dao.user.WechatUserMapper;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.Share;
import com.skl.cloud.model.user.UserCamera;
import com.skl.cloud.model.user.UserCameraQuery;
import com.skl.cloud.model.user.WechatUser;
import com.skl.cloud.remote.ipc.IPCameraRemote;
import com.skl.cloud.remote.ipc.dto.ipc.AudioChannelIO;
import com.skl.cloud.remote.ipc.dto.ipc.RequestCurrentPictrueIO;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.constants.Constants;

/**
 * @ClassName: IPCameraService
 * @Description: IPC Service implement class, 
 * IPC's feature and operation in this class, including IPCamera information and setting.
 * @author yangbin
 * @date 2015年10月8日
*/
@Service
public class IPCameraServiceImpl implements IPCameraService {
	private static Logger logger = LoggerFactory.getLogger(IPCameraServiceImpl.class);

	@Autowired
	private IPCMapper ipcMapper;

	@Autowired
	private UserCameraMapper userCameraMapper;

	@Remote
	private IPCameraRemote ipcRemote;

	@Autowired
	private ShareMapper shareMapper;

	@Autowired
	private WechatUserMapper wechatUserMapper;

	@Autowired
	private StreamResourcesService streamResourcesService;

	@Autowired
	private S3Service s3Service;
	
	@Autowired
    private AppUserService appUserService;
	/**
	 * 更新IPC的信息
	 * @param ipcamera
	 * @throws BusinessException
	*/
	@Override
	@Transactional
	public void updateIPCamera(IPCamera ipcamera) throws BusinessException {
		ipcMapper.updateIpc(ipcamera);
		ipcMapper.updateIpcSub(ipcamera.getIpcSub());
	}

	/**
	 * 新增IPC的信息
	 * @param ipcamera
	 * @throws BusinessException
	*/
	@Override
	@Transactional
	public IPCamera createIPCamera(IPCamera ipcamera) throws BusinessException {
		ipcMapper.insertIpc(ipcamera);
		ipcMapper.insertIpcSub(ipcamera.getIpcSub());
		return ipcamera;
	}

	/**
	 * 删除IPC的信息
	 * @param id
	 * @throws BusinessException
	*/
	@Override
	@Transactional
	public void deleteIPCamera(Long id) throws BusinessException {
		ipcMapper.deleteIpc(id);
		ipcMapper.deleteIpcSub(id);
	}

	/**
	 * getIPCameraBySN(通过sn查询IPCamera的信息)
	 * @Title getIPCameraBySN
	 * @param sn
	 */
	@Override
	@Transactional(readOnly = true)
	public IPCamera getIPCameraBySN(String sn) throws BusinessException {
		IPCamera ipcamera = new IPCamera();
		ipcamera.setSn(sn);

		ipcamera = ipcMapper.queryIPCamera(ipcamera);
		if (ipcamera != null) {
			IPCameraSub ipcSub = this.getIPCameraSubBySN(sn);
			if (ipcSub != null) {
				ipcamera.setIpcSub(ipcSub);
				return ipcamera;
			}
		}
		return null;
	}

	/**
	 * getIPCameraSubBySN(通过sn查询IPCameraSub的信息)
	 * @Title getIPCameraBySN
	 * @param sn
	 */
	@Override
	@Transactional(readOnly = true)
	public IPCameraSub getIPCameraSubBySN(String sn) throws BusinessException {
		IPCameraSub ipcameraSub = new IPCameraSub();
		ipcameraSub.setSn(sn);
		return ipcMapper.queryIPCameraSub(ipcameraSub);
	}

	/**
	 * getIPCameraByDeviceId(通过deviceId查询IPCamera的信息)
	 * @Title getIPCameraByDeviceId
	 * @param opendId
	 */
	@Override
	@Transactional(readOnly = true)
	public IPCamera getIPCameraByDeviceId(String deviceId) throws BusinessException {
		IPCamera ipcamera = new IPCamera();
		ipcamera.setDeviceId(deviceId);
		ipcamera = ipcMapper.queryIPCamera(ipcamera);
		if (ipcamera != null) {
			IPCameraSub ipcSub = this.getIPCameraSubBySN(ipcamera.getSn());
			if (ipcSub != null) {
				ipcamera.setIpcSub(ipcSub);
				return ipcamera;
			}
		}
		return null;
	}

	/**
	 * 通过SN得到所有关联的APP用户信息
	 * @Title getIPCameraByOpenId
	 * @param deveiceId
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AppUser> getIPCameraLinkedUsers(String sn) throws BusinessException {
		UserCameraQuery userCameraQuery = new UserCameraQuery();
		userCameraQuery.setSn(sn);
		return userCameraMapper.queryIPCameraLinkedUsers(userCameraQuery);
	}

	/**
	 * 保存IPC上用户需要分享的信息
	 * @param userId
	 * @param ipcamera
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public Share createIPCUserShare(Long userId, Long duration, IPCamera ipcamera) throws BusinessException {

		// 判断用户ipc的分享使能是否打开
		if (ipcamera.getLiveShareStatus() != 1) {
			AssertUtils.throwBusinessEx(0x50010037);
		}
		// 保存分享的信息到云端
		Share share = new Share();
		share.setUserId(userId);
		share.setCameraId(ipcamera.getId());
		share.setCameraSn(ipcamera.getSn());
		// 生成uuid
		String uuid = UUID.randomUUID().toString();
		share.setUuid(uuid);

		// 生成linkUrl
		String linkUrl = SystemConfig.getProperty("wechat.link.url");
		// linkUrl加上uuid参数，以便后期判断是否过期
		share.setLinkUrl(linkUrl + "?id=" + uuid);

		// 生成imgUrl
		if (StringUtils.isBlank(ipcamera.getSnapshotUuid())) {
			share.setImgUrl("");
		} else {
			S3FileData file;
			String imgUrl = "";
			try {
				file = s3Service.getUploadFileByUuid(ipcamera.getSnapshotUuid());
				imgUrl = file.getFilePath() + file.getFileName();
			} catch (Exception e) {
				logger.info("**********获取url失败，请检查表t_platform_s3_file_data是否有file_id:" + ipcamera.getSnapshotUuid()
						+ "相关的值*********");
				AssertUtils.throwBusinessEx(0x50000002, new String[] { "imgUrl" });
			}

			share.setImgUrl(imgUrl);
		}

		share.setShareType(Share.SHARE_TYPE_VIDEO);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 分享开始时间
		share.setStartDate(cal.getTime());

		// 分享时间有期限时，share将设置截止时间；当duration为-1，代表永久时，该share有开始时间，但截止时间将为null
		if (duration > 0) {
			// 计算分享结果时间, 由秒换算成毫秒
			Long endTime = cal.getTimeInMillis() + duration * 1000;
			cal.setTimeInMillis(endTime);
			// 分享结束时间
			share.setEndDate(cal.getTime());
		}

		// 得到永不过期的视频直播的URL
		String liveDataUrl = streamResourcesService.getLiveRelayURL(ipcamera.getSn(), userId, "hls");

		// 替换视频直播的URL上的uuid参数，以便后期判断是否过期
		liveDataUrl = liveDataUrl.substring(0, liveDataUrl.indexOf("?id="));
		share.setDataUrl(liveDataUrl + "?id=" + uuid);

		// 保存到数据库
		shareMapper.insert(share);
		return share;
	}

	/**
	 * 更新IPC Remote设备的speaker volume信息,异步处理。
	 * @param ipcamera
	 * @throws BusinessException
	*/
	@Override
	@Transactional
	public void updateIPCRemoteVolume(IPCamera ipcamera) throws BusinessException {
		AudioChannelIO audioIO = new AudioChannelIO();
		audioIO.setSpeakerEnabled(Boolean.TRUE.toString());
		audioIO.setSpeakerVolume(ipcamera.getSpeakerVolume().toString());
		// 更新IPC设备remote端的音量
		ipcRemote.updateSpeakerVolume(ipcamera.getSn(), Constants.CHANNEL_ID_AIC3101, audioIO);
	}

	/**
	 * 获取IPC Remote的音量信息
	 * @param ipcamera
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public IPCamera getIPCRemoteVolume(IPCamera ipcamera) throws BusinessException {
		// 1> 从IPC remote获得IPC设备上的音量信息
		AudioChannelIO audioIO = ipcRemote.getSpeakerVolume(ipcamera.getSn(), Constants.CHANNEL_ID_AIC3101);
		String speakerVolume = audioIO.getSpeakerVolume();
		if (logger.isInfoEnabled()) {
			logger.info("get IPC's speaker volume from remote is: " + speakerVolume);
		}
		ipcamera.setSpeakerVolume(speakerVolume != null ? Long.valueOf(speakerVolume) : null);
		return ipcamera;
	}

	/**
	 * 获取直播分享ID对应的参数
	 * @param ipcamera
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public ShareStatusFO queryShareStatus(String sid) throws BusinessException {
		ShareStatusFO shareStatusFO = new ShareStatusFO();
		// 查询对应uuid的share信息
		Share share = shareMapper.queryShareByUUID(sid);
		// 数据库不存在该uuid的share信息，则将isExpire值设为2
		if (share == null) {
			shareStatusFO.setIsExpire(ShareStatusFO.EXPIRE_NOTEXIST);
			return shareStatusFO;
		} else if (share.getEndDate() == null) {// 如果结束时间为null,表示永不过期
			shareStatusFO.setIsExpire(ShareStatusFO.EXPIRE_ENABLED);
			// 永不过期时，将过期时间设为"-1"
			shareStatusFO.setExpireAt("-1");
		} else {
			// 判断是否过期；未过期，将isExpire值设为0; 过期，将isExpire值设为1;
			if (share.getEndDate().getTime() > new Date().getTime()) {
				shareStatusFO.setIsExpire(ShareStatusFO.EXPIRE_ENABLED);
			} else {
				shareStatusFO.setIsExpire(ShareStatusFO.EXPIRE_DISABLED);
			}
			shareStatusFO.setExpireAt(DateUtil.date2string(share.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
		}

		shareStatusFO.setLinkUrl(share.getLinkUrl());
		shareStatusFO.setImgUrl(share.getImgUrl());
		// 获取分享对应的ipc的信息
		IPCamera ipc = this.getIPCameraBySN(share.getCameraSn());
		AssertUtils.existDB(ipc, new String[] { "ipcInfo" });

		shareStatusFO.setSn(share.getCameraSn());
		shareStatusFO.setDeviceId(ipc.getDeviceId());
		shareStatusFO.setDeviceName(ipc.getNickname());
		// 获取发出分享对应的wechat用户的信息
		WechatUser wechatUser = wechatUserMapper.queryUserByUserId(share.getUserId());
		AssertUtils.existDB(wechatUser, new String[] { "wechatUserInfo" });

		shareStatusFO.setOpenId(wechatUser.getOpenId());
		return shareStatusFO;
	}

	/**
	 * 通过uuid检验直播流url是否过期
	 * @param uuid
	 * @throws BusinessException
	 */
	@Override
	@Transactional(readOnly = true)
	public int checkLiveUrlValidity(String uuid) throws BusinessException {
		int isExpire = 1;
		// 查询对应uuid的share信息
		Share share = shareMapper.queryShareByUUID(uuid);
		// 数据库不存在该uuid的share信息，则将isExpire值设为2
		if (share == null) {
			isExpire = 2;
		} else if (share.getEndDate() == null) {// 如果结束时间为null,表示永不过期
			isExpire = 0;
		} else {
			// 判断是否过期；未过期，将isExpire值设为0; 过期，将isExpire值设为1;
			isExpire = share.getEndDate().getTime() > new Date().getTime() ? 0 : 1;
		}
		return isExpire;
	}

	/**
	 * 通过uuid组装有期限的直播流url
	 * @param sid
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public String[] queryShareLiveUrl(String uuid) throws BusinessException {
		// 查询对应uuid的share信息
		Share share = shareMapper.queryShareByUUID(uuid);
		AssertUtils.existDB(share, new String[] { "shareInfo" });

		// 得到永不过期的视频直播的hls URL
		String liveHlsUrl = streamResourcesService.getLiveRelayURL(share.getCameraSn(), share.getUserId(), "hls");
		// 得到永不过期的视频直播的rtsp URL
		String liveRtspUrl = streamResourcesService.getLiveRelayURL(share.getCameraSn(), share.getUserId(), "rtsp");
		// 替换视频直播的URL上的uuid参数，以便后期判断是否过期
		String[] liveUrls = new String[2];
		liveHlsUrl = liveHlsUrl.substring(0, liveHlsUrl.indexOf("?id=")) + "?id=" + uuid;
		liveRtspUrl = liveRtspUrl.substring(0, liveRtspUrl.indexOf("?id=")) + "?id=" + uuid;

		liveUrls[0] = liveHlsUrl;
		liveUrls[1] = liveRtspUrl;

		return liveUrls;
	}

	/**
	 * 通知ipc实时抓拍图片上传到s3
	 * @param sn
	 * @param requestCurrentPictrueIO
	 * @throws BusinessException
	 */
	@Override
	public void requestCurrentPictrue(String sn, RequestCurrentPictrueIO requestCurrentPictrueIO)
			throws BusinessException {
		ipcRemote.requestCurrentPictrue(sn, requestCurrentPictrueIO);

	}

	/**
	 * 删除有关某ipc的所有分享（但是由于获取ipc的relay url时也会在此表生成一条记录,对应的是start_date与end_date都是NULL，此条记录不删）
	 * @param sn
	 */
	@Override
	@Transactional
	public void deleteShareBySnExceptRelay(String sn) {
		shareMapper.deleteBySnExceptRelay(sn);

	}

	@Override
	@Transactional
	public void deleteIPCameraLinkedUsers(Long cameraId) throws BusinessException {
		UserCamera userCamera = new UserCamera();
		userCamera.setCameraId(cameraId);
		userCameraMapper.delete(userCamera);
	}

	@Override
	@Transactional
	public List<AppUser> getIPCameraLinkedUsers(String sn, int linkType) throws BusinessException {
		UserCameraQuery userCameraQuery = new UserCameraQuery();
		userCameraQuery.setSn(sn);
		// 0:设备与用户已经绑定
		userCameraQuery.setLinkType(linkType);
		return userCameraMapper.queryIPCameraLinkedUsers(userCameraQuery);

	}
	
		
	@Override
	@Transactional(readOnly=true)
	public void verifyIpcPassword(String sn, String password, Long userId) {
		try {
			IPCamera ipCamera = appUserService.getUserBindDevice(userId, sn);
			if(ipCamera == null){
				throw new BusinessException("0x50020041");
			}
			if(!password.equals(ipCamera.getPassword())){
				logger.info("ipc 数据库 密码{}",ipCamera.getPassword());
				throw new BusinessException("0x50020110");
			}
		} catch (Exception e) {
			logger.error("校验失败 参数 sn:{},password:{},userId:{}", sn,password,userId ,e);
			throw e;
		}
		
	}

	@Override
	@Transactional
	public void modifyIpcPassword(String sn, String password, Long userId,int id,String reqXml) {
		// TODO Auto-generated method stub
		try {
			IPCamera ipCamera = appUserService.getUserBindDevice(userId, sn);
			if(ipCamera == null){
				throw new BusinessException("0x50020041");
			}
			//修改数据库的密码
			ipCamera.setPassword(password);
			updateIPCamera(ipCamera);
			
			ipcRemote.modifyDevicePassword(id, sn, reqXml);
			
		} catch (Exception e) {
			logger.error("修改密码失败 sn:{},password:{},userId:{},id:{}", sn,password,userId ,id,e);
			throw e;
		}
	}
}
