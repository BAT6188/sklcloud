package com.skl.cloud.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.EventAlertMapper;
import com.skl.cloud.dao.S3Mapper;
import com.skl.cloud.dao.audio.AudioPlayListMapper;
import com.skl.cloud.foundation.file.S3;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.constants.S3ServiceType;

@Service
public class S3ServiceImpl implements S3Service {
	private static final Logger logger = Logger.getLogger(S3Service.class);

	// patter for {}
	private static final Pattern P_VARIABLE = Pattern.compile("\\{(.*?)\\}");

	@Autowired(required = true)
	public EventAlertMapper eventAlertMapper;

	@Autowired(required = true)
	private AudioPlayListMapper audioPlayListMapper;

	@Autowired(required = true)
	private S3Mapper s3Mapper;
	
	@Autowired(required = true)
    private AppUserService userService;

	/**
	 * Get the S3 key URL by service type and userId in URL for User Space, </p>
	 * for example: http://{ip}:{port}/{bucket}/user/{userId}/video/{year}/{month}/{day}/
	 * @Title: getUrlOfUserSpaceByUserIdAndType
	 * @param userId
	 * @param serviceType
	 * @return String of url
	 * @throws BusinessException (参数说明)
	*/
	@Transactional(readOnly = true)
	public String getUrlOfUserSpaceByUserIdAndType(Long userId, S3ServiceType serviceType) throws BusinessException {
		String retVal = "";
		try {
			retVal = s3Mapper.selectBasicUrlPath(serviceType.getType());
			Map<String, String> paramMap = getCommonParam();
			paramMap.put("userId", String.valueOf(userId));
			retVal = parse(retVal, paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * Get the S3 key URL by service type and sn in URL for User Space, </p>
	 * for example: http://{ip}:{port}/{bucket}/user/{userId}/video/{year}/{month}/{day}/
	 * @Title: getUrlOfUserSpaceBySnAndType
	 * @param sn
	 * @param serviceType
	 * @return String of url
	 * @throws BusinessException (参数说明)
	*/
	@Transactional(readOnly = true)
	public String getUrlOfUserSpaceBySnAndType(String sn, S3ServiceType serviceType) throws BusinessException {
		String retVal = "";
		try {
			retVal = s3Mapper.selectBasicUrlPath(serviceType.getType());
			Map<String, String> paramMap = getCommonParam();
			paramMap.put("deviceSn", sn);
			retVal = parse(retVal, paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * Get the S3 key URL by service type without other parameter for System Space, </p>
	 * for example: http://{ip}:{port}/{bucket}/system/picture/ </p>
	 * if there is {sn} or {userId} in the return URL, it's need you to replace.
	 * @Title: getUrlOfSystemSpaceByType
	 * @param serviceType
	 * @return String of url
	 * @throws BusinessException (参数说明)
	*/
	@Transactional(readOnly = true)
	public String getUrlOfSystemSpaceByType(S3ServiceType serviceType) throws BusinessException {
		String retVal = "";
		try {
			retVal = s3Mapper.selectBasicUrlPath(serviceType.getType());
			retVal = parse(retVal, getCommonParam());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * Get the S3 key URL by service type without other parameter for System Space, </p>
	 * for example: http://{ip}:{port}/{bucket}/system/{SN}}picture/ </p> paramMpa put key = SN, value
	 * if there is {sn} or {userId} in the return URL, it's need you to replace.
	 * @Title: getUrlOfSystemSpaceByType
	 * @param serviceType
	 * @return String of url
	 * @throws BusinessException (参数说明)
	*/
	@Transactional(readOnly = true)
	public String getUrlOfSystemSpaceByType(S3ServiceType serviceType, Map<String, String> paramMap)
			throws BusinessException {
		String retVal = "";
		try {
			retVal = s3Mapper.selectBasicUrlPath(serviceType.getType());
			Map<String, String> valueMap = getCommonParam();
			valueMap.putAll(paramMap);
			retVal = parse(retVal, valueMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * Get the S3 key URL by service type without other parameter for System Space, </p>
	 * for example: http://{ip}:{port}/{bucket}/system/{SN}}picture/ </p> paramMpa put key = SN, value
	 * if there is {sn} or {userId} in the return URL, it's need you to replace.
	 * @Title: getUrlOfSystemSpaceByType
	 * @param serviceType
	 * @return String of url
	 * @throws BusinessException (参数说明)
	*/
	@Transactional(readOnly = true)
	public String getUrlByParam(String serviceType, Map<String, String> paramMap) throws BusinessException {
		String retVal = "";
		try {
			retVal = s3Mapper.selectBasicUrlPath(serviceType);
			Map<String, String> valueMap = getCommonParam();
			valueMap.putAll(paramMap);
			retVal = parse(retVal, valueMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}
	
   /**
    * get the basic url by serviceType and save the s3 file info to table
    * @Title: getUrlAndSaveInfo
    * @param serviceType
    * @return String of url
    * @throws BusinessException (参数说明)
    */
	@Transactional
    public String getUrlAndSaveInfo(String serviceType, String uuid, Map<String,String> paramMap) throws BusinessException{
    	String urlPath = this.getUrlByParam(serviceType, paramMap);
		S3FileData file = new S3FileData();
		file.setUuid(uuid);
		if(paramMap.containsKey("userId")){
			file.setUserId(paramMap.get("userId"));
		}
		if(paramMap.containsKey("deviceSn")){
			file.setDeviceSn(paramMap.get("deviceSn"));
		}
		// 预签名引入
		if(paramMap.containsKey("fileName"))
		{
			file.setFileName(paramMap.get("fileName"));
		}
		file.setFilePath(urlPath);
		file.setServiceType(serviceType);
		this.saveUploadFile(file);
		return urlPath;
    }
    
    /**
	  * isExistServiceType(判断请求参数serviceType是否存在于数据库中)
	  * @Title: isExistServiceType
	  * @return String (返回值说明)
	  * @date 2016年1月22日
	 */
    @Override
    @Transactional(readOnly = true)
    public boolean isExistServiceType(String serviceType) throws Exception{
        boolean retVal = false;
        String url = s3Mapper.selectBasicUrlPath(serviceType);
        if (url != null) {
            return true;
        }
        return retVal;
    }

	/**
	 * 通过sn删除告警类，音频类，离线图片  数据库和S3的数据
	 */
	@Override
	@Transactional
	public void deleteBySN(String sn) throws BusinessException {
		S3 s3 = S3Factory.getDefault();
		// 获得离线图片的key
		String picPath = s3Mapper.selectBasicUrlPath(S3ServiceType.DEVICE_OFFLINE_PICTURE.getType());
		int startIdx = picPath.indexOf("system");
		picPath = picPath.substring(startIdx).replace("{SN}", sn);
		s3.deleteFile(picPath);
		
		// 获得设备微信图片的key
		List<S3FileData> list = s3Mapper.selectUploadFileListBySnAndServiceType(sn, S3ServiceType.SYSTEM_DEVICE_WECHAT_PICTURE.getType());
		if(list != null & list.size()>0){
			for(S3FileData s3FileData:list){
				String path = s3FileData.getFilePath();
				String fileName = s3FileData.getFileName();
				if(!StringUtils.isBlank(path)&&!StringUtils.isBlank(fileName)){
					picPath = path+fileName;
					startIdx = picPath.indexOf("system");
					picPath = picPath.substring(startIdx);
					s3.deleteFile(picPath);
				}
			}
		}
		
		// 获得设备离线视频的key
		list = s3Mapper.selectUploadFileListBySnAndServiceType(sn, S3ServiceType.SYSTEM_DEVICE_ALARM_VIDEO.getType());
		if(list != null & list.size()>0){
			for(S3FileData s3FileData:list){
				String path = s3FileData.getFilePath();
				String fileName = s3FileData.getFileName();
				if(!StringUtils.isBlank(path)&&!StringUtils.isBlank(fileName)){
					picPath = path+fileName;
					startIdx = picPath.indexOf("system");
					picPath = picPath.substring(startIdx);
					s3.deleteFile(picPath);
				}
			}
		}
		
		s3Mapper.deleteUploadFileBySn(sn);
		
		// 删除evet表相关数据
		eventAlertMapper.deleteBySN(sn);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExistUploadFile(String uuid) throws BusinessException {
		S3FileData fileData = this.getUploadFileByUuid(uuid);
		if (fileData == null) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public S3FileData getCheckUploadFile(String uuid, String serviceType, String userIdOrSn) throws BusinessException {
		S3FileData fileData = this.getUploadFileByUuid(uuid);
		if (fileData == null) {
			throw new BusinessException("0x50020022");
		}

		if (serviceType != null && !serviceType.isEmpty()) {
			if (!serviceType.equals(fileData.getServiceType())) {
				throw new BusinessException("0x50020065");
			}
		}

		if (userIdOrSn != null && !userIdOrSn.isEmpty()) {
			if (!userIdOrSn.equals(fileData.getUserId()) && !userIdOrSn.equals(fileData.getDeviceSn())) {
				throw new BusinessException("0x50020066");
			}
		}
		return fileData;
	}

	@Override
	@Transactional
	public void saveUploadFile(S3FileData file) throws BusinessException {
		s3Mapper.insertUploadFile(file);
	}

	@Override
	@Transactional
	public void updateUploadFile(String uuid, String serviceType, String fileName, long fileSize) throws BusinessException {
		s3Mapper.updateUploadFile(uuid, serviceType, fileName, fileSize);
		
		// 根据业务需要更新的额外表信息：自定义头像
		if(S3ServiceType.SYSTEM_USER_PORTRAIT.getType().equals(serviceType))
		{
			AppUser appuser = userService.getUserByPortraintUuid(uuid);
			if(appuser != null && !"-1".equals(appuser.getPortraintId()))
			{
				appuser.setPortraintId("-1");
				userService.updateUser(appuser);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public S3FileData getUploadFileByUuid(String uuid) throws BusinessException {
		List<String> uuids = new ArrayList<String>();
		uuids.add(uuid);
		List<S3FileData> fileDataList = s3Mapper.selectUploadFileListByUuids(uuids);
		if (fileDataList != null && fileDataList.size() > 0) {
			return fileDataList.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<S3FileData> getUploadFileListByUuids(List<String> uuids) throws BusinessException {
		return s3Mapper.selectUploadFileListByUuids(uuids);
	}

	@Override
	@Transactional(readOnly = true)
	public List<S3FileData> getUploadFileListBySn(String sn) throws BusinessException {
		return s3Mapper.selectUploadFileListBySn(sn);
	}

	@Override
	@Transactional(readOnly = true)
	public S3FileData getUploadFileByPathAndName(String filePath, String fileName) throws BusinessException {
		return s3Mapper.selectUploadFileByPathAndName(filePath, fileName);
	}

	@Override
	@Transactional
	public void deleteUploadFileBySn(String sn) throws BusinessException {
		List<S3FileData> fileDataList = this.getUploadFileListBySn(sn);

		// 根据业务需要删除额外的文件列表：
		List<String> delAudioUuids = new ArrayList<String>();
		for (S3FileData fileData : fileDataList) {
			if (S3ServiceType.DEVICE_MUSIC.getType().equals(fileData.getServiceType())) {
				delAudioUuids.add(fileData.getUuid());
			}
		}

		if (delAudioUuids.size() > 0) {
			audioPlayListMapper.deleteAudioPlayByUuids(delAudioUuids);
		}

		s3Mapper.deleteUploadFileBySn(sn);
	}

	@Override
	@Transactional
	public void deleteUploadFileByUuids(List<String> uuids) throws BusinessException {
		List<S3FileData> fileDataList = this.getUploadFileListByUuids(uuids);

		// 根据业务需要删除额外的文件列表：
		List<String> delAudioUuids = new ArrayList<String>();
		for (S3FileData fileData : fileDataList) {
			if (S3ServiceType.DEVICE_MUSIC.getType().equals(fileData.getServiceType())) {
				delAudioUuids.add(fileData.getUuid());
			}
		}

		if (delAudioUuids.size() > 0) {
			audioPlayListMapper.deleteAudioPlayByUuids(delAudioUuids);
		}

		s3Mapper.deleteUploadFileByUuids(uuids);
	}

	// get common param Map
	private static Map<String, String> getCommonParam() {
		Map<String, String> map = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		map.put("year", String.valueOf(year));
		map.put("month", String.valueOf(month));
		map.put("day", String.valueOf(day));

		// config for S3 parameter
		map.put("ip", SystemConfig.getProperty("aws.s3.ip"));
		map.put("port", SystemConfig.getProperty("aws.s3.port"));
		map.put("bucket", SystemConfig.getProperty("aws.s3.bucket"));
		return map;
	}

	// replace parameter
	private static String parse(String path, Map<String, String> map) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		Matcher matcher = P_VARIABLE.matcher(path);
		matcher.reset();
		boolean result = matcher.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String variableName, variableValue;
			do {
				variableName = matcher.group(1);
				//System.out.println(variableName);
				variableValue = (String) map.get(variableName);
				matcher.appendReplacement(sb, variableValue);
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		} else {
			return path;
		}
	}

}
