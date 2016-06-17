package com.skl.cloud.dao;

import java.util.List;

import com.skl.cloud.model.S3FileData;

public interface S3Mapper
{
	public String selectBasicUrlPath(String serviceType) ;
	
	public List<S3FileData> selectUploadFileListByUuids(List<String> uuids) ;
	
	public List<S3FileData> selectUploadFileListBySn(String sn) ;
	/**
	 * 通过sn和存储类型获得s3文件
	 * TODO(这里用一句话描述这个方法的作用)
	 * <p>Creation Date: 2016年5月4日 and by Author: weibin </p>
	 * @param sn
	 * @param serviceType
	 * @return
	 * @return List<S3FileData>
	 * @throws
	 *
	 */
	public List<S3FileData> selectUploadFileListBySnAndServiceType(String sn, String serviceType) ;
	
	public S3FileData selectUploadFileByPathAndName(String filePath, String fileName) ;

	public void insertUploadFile(S3FileData file) ;
	
	public void deleteUploadFileBySn(String sn) ;
	
	public void deleteUploadFileByUuids(List<String> uuids) ;

	public void updateUploadFile(String uuid, String serviceType, String fileName, long fileSize) ;
}
