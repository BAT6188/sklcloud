package com.skl.cloud.service;

import java.util.List;
import java.util.Map;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.util.constants.S3ServiceType;

public interface S3Service {

	/**
	  * Get the S3 key URL by service type and userId in URL for User Space, </p>
	  * for example: http://{ip}:{port}/{bucket}/user/{userId}/video/{year}/{month}/{day}/
	  * @Title: getUrlOfUserSpaceByUserIdAndType
	  * @param userId
	  * @param serviceType
	  * @return String of url
	  * @throws BusinessException (参数说明)
	 */
	public String getUrlOfUserSpaceByUserIdAndType(Long userId, S3ServiceType serviceType) throws BusinessException;
	
	/**
     * Get the S3 key URL by service type and sn in URL for User Space, </p>
     * for example: http://{ip}:{port}/{bucket}/user/{userId}/video/{year}/{month}/{day}/
     * @Title: getUrlOfUserSpaceBySnAndType
     * @param sn
     * @param serviceType
     * @return String of url
     * @throws BusinessException (参数说明)
    */
	public String getUrlOfUserSpaceBySnAndType(String sn, S3ServiceType serviceType) throws BusinessException;
	
	/**
     * Get the S3 key URL by service type without other parameter for System Space, </p>
     * for example: http://{ip}:{port}/{bucket}/system/picture/
     * @Title: getUrlOfSystemSpaceByType
     * @param serviceType
     * @return String of url
     * @throws BusinessException (参数说明)
    */
	public String getUrlOfSystemSpaceByType(S3ServiceType serviceType) throws BusinessException;
	
   /**
    * Get the S3 key URL by service type without other parameter for System Space, </p>
    * for example: http://{ip}:{port}/{bucket}/system/{SN}}picture/ </p> paramMpa put key = SN, value
    * if there is {sn} or {userId} in the return URL, it's need you to replace.
    * @Title: getUrlOfSystemSpaceByType
    * @param serviceType
    * @return String of url
    * @throws BusinessException (参数说明)
   */
	public String getUrlOfSystemSpaceByType(S3ServiceType serviceType, Map<String,String> paramMap) throws BusinessException;
	
   /**
    * Get the S3 key URL by service type without other parameter for System Space, </p>
    * for example: http://{ip}:{port}/{bucket}/system/{SN}}picture/ </p> paramMpa put key = SN, value
    * if there is {sn} or {userId} in the return URL, it's need you to replace.
    * @Title: getUrlByParam
    * @param serviceType
    * @return String of url
    * @throws BusinessException (参数说明)
   */
    public String getUrlByParam(String serviceType, Map<String,String> paramMap) throws BusinessException;
    
   /**
    * get the basic url by serviceType and save the s3 file info to table
    * @Title: getUrlAndSaveInfo
    * @param serviceType
    * @return String of url
    * @throws BusinessException (参数说明)
    */
    public String getUrlAndSaveInfo(String serviceType, String uuid, Map<String,String> paramMap) throws BusinessException;
    
    /**
	  * isExistServiceType(判断请求参数serviceType是否存在于数据库中)
	  * @Title: isExistServiceType
	  * @Description: TODO
	  * @param @param serviceType
	  * @param @return (参数说明)
	  * @return String (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年1月22日
	 */
	public boolean isExistServiceType(String serviceType) throws Exception;
    
    /**
     * delete s3 data by user unbind ipcamera.
     * @Title: deleteBySN
     * @param sn
     * @throws Exception (参数说明)
    */
	void deleteBySN(String sn) throws BusinessException;
	
    /**
	 * <上传文件是否存在>
	 * 
	 * @param uuid
	 *            [唯一标识]
	 * @throws Exception
	 */
	public boolean isExistUploadFile(String uuid) throws BusinessException;
	
	/**
	 * <获取此文件，存在且合法 >
	 * @param uuid
	 * @param serviceType(可以为null)
	 * @param userIdOrSn(可以为null)
	 * @throws Exception
	 */
	public S3FileData getCheckUploadFile(String uuid, String serviceType, String userIdOrSn) throws BusinessException;

	/**
	 * <通过uuid获取上传文件的信息(单个)>
	 * 
	 * @param uuid
	 *            [唯一标识]
	 * @throws Exception
	 */
	public S3FileData getUploadFileByUuid(String uuid) throws BusinessException;

	/**
	 * <通过uuid获取上传文件的信息（多个）>
	 * 
	 * @param uuids
	 *            [唯一标识]
	 * @throws Exception
	 */
	public List<S3FileData> getUploadFileListByUuids(List<String> uuids) throws BusinessException;

	/**
	 * <通过sn获取该设备上传文件的信息（多个）>
	 * 
	 * @param sn
	 *            [设备sn号]
	 * @throws Exception
	 */
	public List<S3FileData> getUploadFileListBySn(String sn) throws BusinessException;

	/**
	 * <通过filePath和fileName获取上传文件的信息（单个）>
	 * 
	 * @param filePath
	 *            [文件路径]
	 * @param fileName
	 *            [文件名称]
	 * @throws Exception
	 */
	public S3FileData getUploadFileByPathAndName(String filePath, String fileName) throws BusinessException;

	/**
	 * <保存用户上传文件>
	 * 
	 * @param file
	 *            [上传文件信息]
	 * @throws Exception
	 */
	public void saveUploadFile(S3FileData file) throws BusinessException;

	/**
	 * <更新用户上传文件>
	 * 
	 * @param uuid
	 *            [上传文件唯一标识]
	 * @param fileName
	 *            [上传文件名称]
	 * @throws Exception
	 */
	public void updateUploadFile(String uuid, String serviceType, String fileName, long fileSize) throws BusinessException;

	/**
	 * <删除设备sn上传的文件>
	 * 
	 * @param sn
	 *            [设备sn]
	 * @throws Exception
	 */
	public void deleteUploadFileBySn(String sn) throws BusinessException;

	/**
	 * <通过唯一标识列表删除上传的文件>
	 * 
	 * @param uuids
	 *            [文件唯一标列表]
	 * @throws Exception
	 */
	public void deleteUploadFileByUuids(List<String> uuids) throws BusinessException;
	
}
