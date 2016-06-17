package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;

@RequestMapping("/skl-cloud/app")
@Controller
public class AppToS3Controller extends BaseController {
	@Autowired(required = true)
	private S3Service s3Service;

	@Autowired(required = true)
	private IPCameraService iPCameraService;

	/**
	 * <9.3 APP请求获取设备文件上传URL>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/device/file/url/request.app", method = RequestMethod.POST)
	public ResponseEntity<String> getDeviceBasicUrlPathToS3(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;
		String root = "requestUrl";

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);
			String serviceType = XmlUtil.convertToString(paraMap.get("serviceType"), false);

			// 校验：设备是否存在
			IPCamera ipCamera = iPCameraService.getIPCameraBySN(sn);
			if (ipCamera == null) {
				throw new BusinessException("0x50000047");
			}
			
			// 判断数据库中是否存在这种类型的serviceType
			if (!s3Service.isExistServiceType(serviceType)) {
				// 容错处理
				throw new BusinessException("0x50000050");
			}

			// 取文件上传URL
			String uuid = UUID.randomUUID().toString();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("deviceSn", sn);
			paramMap.put("userId", String.valueOf(super.getUserId()));
			paramMap.put("SN", sn);
			paramMap.put("fileName", fileName);
			String urlPath = s3Service.getUrlAndSaveInfo(serviceType, uuid, paramMap);

			// 返回xml
			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			backMap.put("uuid", uuid);
			backMap.put("url", S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(urlPath + fileName), HttpMethod.PUT));
			sReturn = XmlUtil.mapToXmlRight(root, backMap);
		} catch (Exception e) {
			sReturn = getErrorXml(root, e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <APP请求获取用户文件上传URL>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/user/file/url/request.app", method = RequestMethod.POST)
	public ResponseEntity<String> getUserBasicUrlPathToS3(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;
		String root = "requestUrl";

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String serviceType = XmlUtil.convertToString(paraMap.get("serviceType"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);

			// 判断数据库中是否存在这种类型的serviceType
			if (!s3Service.isExistServiceType(serviceType)) {
				// 容错处理
				throw new BusinessException("0x50000050");
			}

			// 取文件上传URL
			String uuid = UUID.randomUUID().toString();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId", String.valueOf(super.getUserId()));
			String urlPath = s3Service.getUrlAndSaveInfo(serviceType, uuid, paramMap);

			// 返回xml
			Map<String, Object> backMap = new LinkedHashMap<String, Object>();
			backMap.put("uuid", uuid);
			backMap.put("url", S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(urlPath + fileName), HttpMethod.PUT));
			sReturn = XmlUtil.mapToXmlRight(root, backMap);
		} catch (Exception e) {
			sReturn = getErrorXml(root, e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <9.4 APP进行设备文件上传完成确认>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/device/file/upload/success/confirm.app", method = RequestMethod.POST)
	public ResponseEntity<String> uploadFileDeviceConfirm(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
			String serviceType = XmlUtil.convertToString(paraMap.get("serviceType"), false);
			String uuid = XmlUtil.convertToString(paraMap.get("uuid"), false);

			// 校验：设备是否存在
			IPCamera ipCamera = iPCameraService.getIPCameraBySN(sn);
			if (ipCamera == null) {
				throw new BusinessException("0x50000047");
			}

			S3FileData fileData = s3Service.getCheckUploadFile(uuid, serviceType, sn);
			String s3Key = StringUtil.convertToS3Key(fileData.getFilePath() + fileData.getFileName());

			// 获取文件大小
			long fileSize = S3Factory.getDefault().getFile(s3Key).getContentLength();

			// 刷新db
			s3Service.updateUploadFile(uuid, serviceType, fileData.getFileName(), fileSize);

			// 返回xml
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <9.2 APP进行用户文件上传完成确认>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/user/file/upload/success/confirm.app", method = RequestMethod.POST)
	public ResponseEntity<String> uploadFileUserConfirm(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;
		String userId = String.valueOf(getUserId());
		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			String uuid = XmlUtil.convertToString(paraMap.get("uuid"), false);
			String serviceType = XmlUtil.convertToString(paraMap.get("serviceType"), false);
			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);

			S3FileData fileData = s3Service.getCheckUploadFile(uuid, serviceType, userId);
			String s3Key = StringUtil.convertToS3Key(fileData.getFilePath() + fileName);

			// 获取文件大小
			long fileSize = S3Factory.getDefault().getFile(s3Key).getContentLength();

			// 刷新db
			s3Service.updateUploadFile(uuid, serviceType, fileName, fileSize);

			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * <APP请求删除文件>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/file/delete.app", method = RequestMethod.POST)
	public ResponseEntity<String> deleteUploadFileFromS3(HttpServletRequest req, HttpServletResponse resp) {
		String sReturn = null;

		try {
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

			List<String> delUuids = new ArrayList<String>();
			List<KeyVersion> delS3Keys = new ArrayList<KeyVersion>();

			for (Object musicFileObj : (List) paraMap.get("musicFileList")) {
				Map<String, Object> musicFileMap = (Map<String, Object>) ((Map<String, Object>) musicFileObj)
						.get("musicFile");
				String uuid = XmlUtil.convertToString(musicFileMap.get("uuid"), false);
				String fileName = XmlUtil.convertToString(musicFileMap.get("fileName"), false);

				// 校验，uuid和fileName是否匹配
				S3FileData fileData = s3Service.getCheckUploadFile(uuid, null, null);

				if (!fileName.equals(fileData.getFileName())) {
					throw new BusinessException("0x50000022");
				}

				// 组装合法数据
				delS3Keys.add(new KeyVersion(StringUtil.convertToS3Key(fileData.getFilePath() + fileName)));
				delUuids.add(uuid);
			}

			if (delS3Keys.size() > 0) {
				// 删除S3服务器上的文件
				S3Factory.getDefault().deleteObject(delS3Keys);
			}

			if (delUuids.size() > 0) {
				// 删除DB数据
				s3Service.deleteUploadFileByUuids(delUuids);
			}

			// 返回xml
			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e) {
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
}
