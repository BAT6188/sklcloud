package com.skl.cloud.service.mes.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.mes.MesDataMapper;
import com.skl.cloud.foundation.file.S3;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.mes.PlatformProd;
import com.skl.cloud.service.AppUserAccountMgtService;
import com.skl.cloud.service.LogManageService;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.mes.MesDataService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.ExcelUtil;
import com.skl.cloud.util.common.FileUtil;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.config.SystemConfig;
import com.skl.cloud.util.pattern.StrPattern;
import com.skl.cloud.util.pattern.Toolkits;

/**
 * @Package com.skl.cloud.service.impl
 * @Title: MesDataServiceImpl
 * @Description: MES产品数据导入云端相关操作
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author leiqiang
 * @date 2015年6月10日
 * @version V1.0
 */
@Service
public class MesDataServiceImpl implements MesDataService {

	private static Logger logger = Logger.getLogger(MesDataServiceImpl.class);

	@Autowired(required = true)
	private MesDataMapper mesDataMapper;

	@Autowired(required = true)
	private S3Service s3Service;

	@Autowired
	private LogManageService logService;

	@Autowired(required = true)
	private AppUserAccountMgtService appUserAccountMgtService;

	private static final String TITLEERRORTYPE = "title";
	private static final String CONTENTERRORTYPE = "content";

	int iData = 0; // Excel总行数
	int failData = 0; // 失败的Excel行数
	int insertDBFailCount = 0; // 向云端数据库插入数据失败的次数
	boolean titleFormatFlg = true; // 默认Excel标题格式正确
	String errorExcelPath = ""; // 保存错误信息的EXCEL文件路径
	XSSFSheet sheet = null;
	List errorList = new ArrayList(); // 保存错误的Excel信息

	// Excel数据[Manufacture Time]时间格式验证
	public static final String[] DATE_PARSE_PATTERNS = { "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyyMMddHHmmss" };

	// @Autowired
	// private SimpleMailMessage templateMessage;

	/*
	 * <p>Title: insertMesData</p> <p>Description: </p>
	 * 
	 * @param list
	 * 
	 * @throws ManagerException
	 * 
	 * @see com.skl.cloud.service.MesDataService#insertMesData(java.util.List)
	 */
	@Override
	@Transactional
	public void insertMesData(List<PlatformProd> list) throws ManagerException {
		mesDataMapper.insertMesData(list);
	}

	/*
	 * <p>Title: delMesAll</p> <p>Description: </p>
	 * 
	 * @param list
	 * 
	 * @throws ManagerException
	 * 
	 * @see com.skl.cloud.service.MesDataService#delMesAll(java.util.List)
	 */
	@Override
	@Transactional
	public void deleteMesAll(List<String> list) throws ManagerException {
		mesDataMapper.deleteMesAll(list);
	}

	/**
	 * 
	 * @Title: insertMesExcelData
	 * @Description: 读取EXCEL，导入MES数据到云端
	 * @return boolean
	 * @author wangming
	 * @date 2016年4月8日
	 */
	@Override
	@Transactional
	public boolean insertMesExcelData() throws ManagerException {

		// 服务器上EXCEL文件路径
		String filePath = SystemConfig.getProperty("mes.imp_filePath","/usr/cloud/MES/MES_IMP");
		// String filePath = "D:\\usr\\cloud\\MES\\MES_IMP";

		// EXCEL文件路径+名称
		String fName = "";
		File file = null;
		List<String> excelList = new ArrayList<String>();

		excelList = FileUtil.findFile(filePath);
		if (excelList == null || excelList.size() == 0) {

			LoggerUtil.info("********服务器" + filePath + "目录下没有需要处理的Excel文件。********", this.getClass());
			LoggerUtil.info("********Mes入库失败********", this.getClass());
			return false;
		}

		// 循环处理每一份Excel
		for (int i = 0; i < excelList.size(); i++) {

			fName = excelList.get(i);

			file = new File(fName);

			// 判断是否为后缀名是".xlsx"的Excel文件类型
			if (!file.getName().toLowerCase().endsWith(".xlsx")) {

				LoggerUtil.error("********文件[" + file.getName() + "]非EXCEL2007文件,后缀名不是\".xlsx\"********",
						MesDataServiceImpl.class);

				continue;

			} else {
				// 防止failData、errorList累计，每次处理Excel前清零
				failData = 0;
				errorList = new ArrayList();

				// 处理Excel2007文件
				if (!this.insertMesAndParseExcel(file)) {

					LoggerUtil.info("********文件[" + file.getName() + "]Mes入库流程失败********", this.getClass());

				}
			}

			try {
				// 发送邮件给管理员
				this.sendEmailToManager(file);

				// 记录日志
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L);
				pl.setModuleName("Mes入库:发送邮件");
				pl.setLogContent("解析处理完" + file.getName() + "后，发送邮件成功");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);

			} catch (Exception e) {
				logger.error("********发送邮件失败********", e);

				// 记录日志
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L);
				pl.setModuleName("Mes入库:发送邮件");
				pl.setLogContent("解析处理完" + file.getName() + "后，发送邮件失败");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);

			} finally {

				if (new File(errorExcelPath + file.getName()).exists()) {

					// 邮件发送完不管成功或失败，都删除附件Excel文件
					if (FileUtil.removeFile(errorExcelPath + file.getName())) {
						LoggerUtil.info("********删除生成的错误的Excel成功********", this.getClass());
					} else {
						LoggerUtil.info("********删除生成的错误的Excel失败********", this.getClass());
					}

				}

			}

			if (titleFormatFlg == false) {

				// 记录日志
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L);
				pl.setModuleName("Mes入库");
				pl.setLogContent("Mes入库：" + file.getName() + "文件入库失败");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);

				LoggerUtil.info("********文件[" + file.getName() + "]Mes入库失败!!!********", this.getClass());

			} else {
				// 记录日志
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L);
				pl.setModuleName("Mes入库");
				pl.setLogContent("Mes入库：" + file.getName() + "文件入库成功");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);

				LoggerUtil.info("********文件[" + file.getName() + "]Mes入库完成，ok********", this.getClass());

			}

		}

		LoggerUtil.info("********Mes入库流程已全部处理完成********", this.getClass());
		return true;
	}

	@Override
	@Transactional
	public void deleteExceptionLog() {
		mesDataMapper.deleteExceptionLog();
	}

	@Transactional
	public String getUploadExcelToS3Url() {

		String uploadPath = "";
		String serviceType = "system_mes_excel_file"; // Excel文件的serviceType

		try {
			// 判断数据库中是否存在这种类型的serviceType
			if (!s3Service.isExistServiceType(serviceType)) {
				// 容错处理
				throw new BusinessException(0x50000050, "can not find the information in DB");
			}

			// IPC请求获取文件上传/下载URL
			uploadPath = s3Service.getUrlByParam(serviceType, new HashMap<String, String>());

			S3FileData fileData = new S3FileData();

			String uuId = UUID.randomUUID().toString();
			fileData.setUuid(uuId);
			fileData.setServiceType(serviceType);
			fileData.setFilePath(uploadPath);
			fileData.setFileName(null);

			s3Service.saveUploadFile(fileData);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			ManagerException ex = new ManagerException(e);
			throw new BusinessException(ex.getErrorCode());
		}

		return uploadPath;
	}

	@Transactional
	public void uploadExcelToS3(File file, String uploadPath) throws AmazonServiceException, ConnectException,
			AmazonClientException, Exception {

		S3 s3 = S3Factory.getDefault();
		s3.saveFile(uploadPath + file.getName(), file);
	}

	@SuppressWarnings("resource")
	@Transactional
	public List<PlatformProd> parseExcel2007AndInsertMesData(File file, String errorExcelPath) {

		List<PlatformProd> saveList = new ArrayList<PlatformProd>();
		saveList = this.parseExcel(file, errorExcelPath);

		if (saveList != null && errorList.size() > 0) {

			failData = errorList.size();
			// 把errorList生成errorExcel文件保存起来
			ExcelUtil.createErrorExcel2007(errorList, errorExcelPath, CONTENTERRORTYPE);
		}

		return saveList;

	}

	/**
	 * 
	  * parseExcel(解析Excel)
	  * @Title: parseExcel
	  * @param @param file
	  * @param @param errorExcelPath
	  * @param @return (参数说明)
	  * @return List<PlatformProd> (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月28日
	 */
	@SuppressWarnings("resource")
	@Transactional
	public List<PlatformProd> parseExcel(File file, String errorExcelPath) {

		List<PlatformProd> saveList = new ArrayList<PlatformProd>();

		if (file.exists()) {
			this.parseExcelByXSSFWorkbook(file);

			// 循环获取Excel表格中的内容
			for (int i = 0; i < iData; i++) {

				XSSFRow row;
				int exceptionCount = 0;
				row = sheet.getRow(i);

				PlatformProd platformProd = new PlatformProd();

				if (i == 0) {
					// 检验EXCEL第一行的标题行是否合法
					String[] titleStds = {"SKL PN", "Series Number", "MAC ID", "FW Version", "Manufacture Time"};
					if (!this.validateExcelTitle(file, sheet.getRow(0), titleStds)) {
						return null;
					}
					continue;
				}

				// SKL PN
				String SKLPN = String.valueOf(row.getCell(0));
				// 对SKL PN数据的合法性进行校验
				if (row.getCell(0) != null && StringUtil.isNullOrEmpty(String.valueOf(row.getCell(0)))) {

					platformProd.setProdPn(SKLPN);

				} else {
					String errorContent = "第" + (i + 1) + "行SKL PN的数据为空，数据异常，请检查" + file.getName() + " Excel文件。";
					LoggerUtil.info(errorContent, this.getClass());

					exceptionCount++;
					platformProd.setProdPn(row.getCell(0) == null ? "" : SKLPN);
				}
				// Series Number
				String SeriesNumber = String.valueOf(row.getCell(1));
				// 对Series Number数据的合法性进行校验 不允许有特殊符号输入
				if (row.getCell(1) != null && StringUtil.isNullOrEmpty(String.valueOf(row.getCell(1)))
						&& StringUtil.pattern(StrPattern.patternSKLPN, SeriesNumber)) {
					platformProd.setProdSn(SeriesNumber);
				} else {
					String errorContent = "第" + (i + 1) + "行Series Number的数据为空，数据异常，请检查" + file.getName() + " Excel文件。";
					LoggerUtil.info(errorContent, this.getClass());

					exceptionCount++;
					platformProd.setProdSn(row.getCell(1) == null ? "" : SeriesNumber);
				}
				// MAC ID
				String MACID = String.valueOf(row.getCell(2));
				// 对MAC ID数据的合法性进行校验 限制为12个16进制数
				if (row.getCell(2) != null && StringUtil.isNullOrEmpty(String.valueOf(row.getCell(2)))) {
					platformProd.setProdMac(MACID);
				} else {
					String errorContent = "第" + (i + 1) + "行MAC ID的数据为空，数据异常，请检查" + file.getName() + " Excel文件。";
					LoggerUtil.info(errorContent, this.getClass());

					exceptionCount++;
					platformProd.setProdMac(row.getCell(2) == null ? "" : MACID);
				}
				// FW Version
				String FWVersion = String.valueOf(row.getCell(3));
				// 对FW Version数据的合法性进行校验
				if (row.getCell(3) != null && StringUtil.isNullOrEmpty(String.valueOf(row.getCell(3)))) {
					platformProd.setProdVersion(FWVersion);
				} else {
					String errorContent = "第" + (i + 1) + "行FW Version的数据为空，数据异常，请检查" + file.getName() + " Excel文件。";
					LoggerUtil.info(errorContent, this.getClass());

					exceptionCount++;
					platformProd.setProdVersion(row.getCell(3) == null ? "" : FWVersion);

				}
				// Manufacture Time
				String manufactureTime = String.valueOf(row.getCell(4));
				Date date = null;
				// 对Manufacture Time数据的合法性进行校验
				if (row.getCell(4) != null && StringUtil.isNullOrEmpty(String.valueOf(row.getCell(4)))) {
					try {
						date = DateUtils.parseDate(manufactureTime, DATE_PARSE_PATTERNS);
						platformProd.setProdMakedate(String.valueOf(manufactureTime));

					} catch (ParseException e) {
						e.printStackTrace();

						String errorContent = "第" + (i + 1) + "行Manufacture Time的时间数据格式不对，数据异常，请检查" + file.getName()
								+ " Excel文件。";
						LoggerUtil.info(errorContent, this.getClass());

						exceptionCount++;
						platformProd.setProdMakedate(row.getCell(4) == null ? "" : manufactureTime);
					}
				} else {
					exceptionCount++;

					String errorContent = "第" + (i + 1) + "行Manufacture Time的数据为空，数据异常，请检查" + file.getName()
							+ " Excel文件。";
					platformProd.setProdMakedate(row.getCell(4) == null ? "" : manufactureTime);

				}
				platformProd.setCreatedate(new Date()); // 数据导入日期，默认系统当前日期
				platformProd.setTbbz("0"); // 数据同步状态，新导入数据默认0（未同步到云端）

				if (exceptionCount == 0) {
					// 数据没有解析异常则保存
					saveList.add(platformProd);
				} else {
					errorList.add(platformProd);
				}
				// 够1000条数据就插入云端数据库
				if (saveList.size() >= 1000) {
					try {
						mesDataMapper.insertMesData(saveList);
						LoggerUtil.info(file.getName() + " EXCEL文件中的数据（共" + saveList.size() + "行）执行插入原始数据表的操作",
								this.getClass());
						saveList.clear(); // 清空

					} catch (Exception e) {
						e.printStackTrace();
						LoggerUtil.error("数据保存失败。可能重复插入Series Number", this.getClass());
						insertDBFailCount++;

						saveList.clear(); // 清空

						// 记录日志信息
						PlatformLog pl = new PlatformLog();
						pl.setLogId(Toolkits.getSequenceID18());
						pl.setUserId(6L); // 由于该字段不能为空，故写死
						pl.setModuleName("Mes入库:原始产品数据导入产品原始表");
						pl.setLogContent("原始产品数据" + file.getName() + "导入产品原始表出现异常，失败");
						pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
						logService.saveLog(pl);
						return null;
					}

				}

			}
			return saveList;

		} else {
			return null;
		}
	}

	/**
	 * 
	  * parseExcelByXSSFWorkbook(使用XSSFWorkbook的API解析Excel)
	  * @Title: parseExcelByXSSFWorkbook
	  * @param file (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月28日
	 */
	@Transactional
	public void parseExcelByXSSFWorkbook(File file) {

		XSSFWorkbook xwb = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			xwb = new XSSFWorkbook(fileInputStream);

			fileInputStream.close();

		} catch (IOException e) {
			logger.error("********解析Excel出错********", e);
		}

		sheet = xwb.getSheetAt(0);
		// 总记录数（含标题行）
		iData = sheet.getPhysicalNumberOfRows();
	}

	/**
	 * 
	  * validateExcelTitle(验证Excel第一行的标题是否合法)
	  * @Title: validateExcelTitle
	  * @param file
	  * @param row
	  * @return (参数说明)
	  * @return String (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月28日
	 */
	@Transactional
	public Boolean validateExcelTitle(File file, XSSFRow row, String[] titleStds) {

		for (int i = 0; i < titleStds.length; i++) {
			String titleStd = titleStds[i];
			String title = String.valueOf(row.getCell(i));
			if (!titleStd.equals(title)) {
				String errorContent = file.getName() + " Excel文件错误，第1行的标题不是" + titleStd + "，格式出错";
				LoggerUtil.error(errorContent, this.getClass());
				titleFormatFlg = false;
				errorList.add(errorContent);
				failData = errorList.size();
				ExcelUtil.createErrorExcel2007(errorList, errorExcelPath + file.getName(), TITLEERRORTYPE);
				break;
			}
		}
		return titleFormatFlg;
	}

	/**
	  * sendEmailToManager(发送邮件给管理员)
	  * @Title: sendEmailToManager
	  * @param file
	  * @throws ManagerException (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月28日
	 */
	@Transactional
	public void sendEmailToManager(File file) throws ManagerException {

		// 把errorExcel当作附件，用Email通知管理员
		Calendar calendar = Calendar.getInstance();

		String timeText = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分" + calendar.get(Calendar.SECOND) + "秒";

		String titleErrorText = timeText + "," + file.getName() + " Excel文件中标题格式有错，请参考邮件附件内容，thanks！";

		String successText = timeText + "， mes产品数据" + file.getName() + "文件 入库成功， 有效数据" + (iData - 1 - failData)
				+ "条，如果邮件有附件，那附件的内容是处理该Excel文件出错的部分，请知悉，thanks！";

		String errorText = timeText + "mes产品数据" + file.getName() + "文件 入库失败， 插入不成功，可能插入了重复的SN数据！ 请知悉，thanks！";

		Map<String, String> mailInfo = new HashMap<String, String>();

		String fromWhom = SystemConfig.getProperty("mail.username","cloud_admin@sky-light.com.hk");
		String toWhom = SystemConfig.getProperty("mes.mail.receiver","yangbin@sky-light.com.hk");

		mailInfo.put("fromWhom", fromWhom);
		mailInfo.put("toWhom", toWhom);

		if (insertDBFailCount != 0) {

			// 插入数据库失败时
			mailInfo.put("text", errorText);
		}
		if (titleFormatFlg == false) {
			// Excel标题格式出错
			mailInfo.put("text", titleErrorText);
		}

		if (titleFormatFlg == true && (iData - 1 - failData) > 0 && insertDBFailCount == 0) {
			// 成功情况时
			mailInfo.put("text", successText);
		}

		mailInfo.put("subject", "云端Mes数据入库的结果");

		if (titleFormatFlg == false || failData > 0) {

			mailInfo.put("attachmentPath", errorExcelPath + file.getName());
		}

		appUserAccountMgtService.sendEmailWithAttachment(mailInfo);

		logger.info("*****已成功给管理员发送Email*****");

	}

	@Transactional
	public boolean insertMesAndParseExcel(File file) {

		boolean uploadWithoutError = false; // Excel文件上传S3异常标识，默认失败
		boolean uploadFlg = false; // Excel文件上传到S3判断标识，默认失败
		boolean insertProWithoutError = false; // 数据导入结果标识，默认失败

		errorExcelPath = SystemConfig.getProperty("mes.error_data_filePath","/usr/cloud/MES/MES_IMP/Attachment/");
		// errorExcelPath = "D:\\usr\\cloud\\MES\\MES_IMP\\Attachment\\";

		int uploadCount = 0; // Excel文件上传的次数

		while (!uploadFlg) {

			// 获取Excel上传至S3的Url路径
			String uploadPath = this.getUploadExcelToS3Url();
			uploadPath = uploadPath.substring(uploadPath.indexOf("system"));

			try {
				// 原始Excel文件数据上传至S3上
				this.uploadExcelToS3(file, uploadPath);
				uploadFlg = true; // 代表Excel文件上传成功

				if (uploadCount == 0) {

					uploadWithoutError = true; // 表示第一次就上传成功
				}
				logger.info("********" + file.getName() + " Excel文件成功上传至S3********");

			} catch (IllegalArgumentException | ConnectException | AmazonClientException e) {

				uploadCount++;
				logger.error("********" + file.getName() + " Excel上传至S3失败第" + uploadCount + "次********", e);

				if (uploadCount == 2) { // excel连续上传两次都失败

					uploadFlg = true;
					// 记录失败信息
					logger.info("********" + file.getName() + " excel连续上传两次都失败********");

					PlatformLog pl = new PlatformLog();
					pl.setLogId(Toolkits.getSequenceID18());
					pl.setUserId(6L); // 由于该字段不能为空，故写死
					pl.setModuleName("Mes入库:原始产品Excel数据上传S3");
					pl.setLogContent("原始产品数据" + file.getName() + "连续两次上传S3都失败");
					pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
					logService.saveLog(pl);
				}

			} catch (Exception e) {

				uploadCount++;
				logger.error("********" + file.getName() + " Excel上传至S3失败第" + uploadCount + "次********", e);

				if (uploadCount == 2) { // excel连续上传两次都失败

					uploadFlg = true;
					// 记录失败信息
					logger.info("********" + file.getName() + " excel连续上传两次都失败********");

					PlatformLog pl = new PlatformLog();
					pl.setLogId(Toolkits.getSequenceID18());
					pl.setUserId(6L); // 由于该字段不能为空，故写死
					pl.setModuleName("Mes入库:原始产品Excel数据上传S3");
					pl.setLogContent("原始产品数据" + file.getName() + "连续两次上传S3都失败");
					pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
					logService.saveLog(pl);
				}

			}

		}

		List<PlatformProd> saveList = new ArrayList<PlatformProd>();

		// 解析Excel并导入到产品原始表中
		saveList = this.parseExcel2007AndInsertMesData(file, errorExcelPath + file.getName());

		if (saveList == null) {

			// 把标题出错的Excel移走
			if (FileUtil.moveFile(file.getPath(), SystemConfig.getProperty("mes.fail_import_filePath","/usr/cloud/MES/MES_IMP/Failure"))) {
				logger.info("********" + file.getName() + "文件上传至S3连续两次失败，移走成功********");
			} else {
				logger.info("********" + file.getName() + "文件上传至S3连续两次失败，移走成功********");
			}
			return false;
		}
		if (saveList != null) {

			// 把saveList批量插进DB中，要有回滚操作
			try {
				if (saveList.size() > 0) {

					mesDataMapper.insertMesData(saveList);

					LoggerUtil.info(file.getName() + " EXCEL文件中的数据（共" + saveList.size() + "行）执行插入原始数据表的操作",
							this.getClass());

				}
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtil.error("数据保存失败。可能重复插入Series Number", this.getClass());
				insertDBFailCount++;

				saveList.clear(); // 清空

				// 记录日志信息
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L); // 由于该字段不能为空，故写死
				pl.setModuleName("Mes入库:原始产品数据导入产品运行表");
				pl.setLogContent("原始产品数据" + file.getName() + "导入产品运行表出现异常，失败");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);
				return false;

			}
		}

		if (insertDBFailCount == 0) {
			// Excel数据往产品原始表插入数据没有发生错误时
			insertProWithoutError = true;
		}

		// Excel已上传至S3、数据导入产品原始表成功 必须删除Excel！
		if (uploadCount < 2 && insertProWithoutError && titleFormatFlg) {

			if (FileUtil.removeFile(file.getPath())) {
				logger.info("********" + file.getName() + "文件删除成功********");

				// 记录日志信息
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L); // 由于该字段不能为空，故写死
				pl.setModuleName("Mes入库:原始产品数据导入产品原始表");
				pl.setLogContent(file.getName() + "成功上传至S3，成功完成了原始产品数据插入产品原始表的操作，且成功删除该Excel文件");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);
			} else {
				logger.info("********" + file.getName() + "文件删除失败********");

				// 记录日志信息
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L); // 由于该字段不能为空，故写死
				pl.setModuleName("Mes入库:原始产品数据导入产品原始表");
				pl.setLogContent(file.getName() + "成功上传至S3，成功完成了原始产品数据插入产品原始表的操作，但删除Excel文件失败");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);
			}

		}

		// Excel文件连续两次上传到S3都失败，必须移走Excel！
		if (uploadCount == 2) {
			if (FileUtil.moveFile(file.getPath(), SystemConfig.getProperty("mes.fail_import_filePath","/usr/cloud/MES/MES_IMP/Failure"))) {
				logger.info("********" + file.getName() + "文件上传至S3连续两次失败，移走成功********");

				// 记录日志信息
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L); // 由于该字段不能为空，故写死
				pl.setModuleName("Mes入库:产品原始表上传至S3");
				pl.setLogContent(file.getName() + "连续两次上传到S3都失败，成功移走该文件");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);
			} else {
				logger.info("********" + file.getName() + "文件上传至S3连续两次失败，移走失败********");

				// 记录日志信息
				PlatformLog pl = new PlatformLog();
				pl.setLogId(Toolkits.getSequenceID18());
				pl.setUserId(6L); // 由于该字段不能为空，故写死
				pl.setModuleName("Mes入库:产品原始表上传至S3");
				pl.setLogContent(file.getName() + "连续两次上传到S3都失败，但移走该文件失败");
				pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
				logService.saveLog(pl);
			}
		}

		return true;
	}

	@Override
	@Transactional
	public boolean insertIpcameraWithMesData() {

		String recordCount = SystemConfig.getProperty("dataCount", "10000");
		logger.info("********获取到的每次具体导入到IPC设备表的记录数是：" + recordCount + "条********");

		if (recordCount == null) {
			logger.info("********获取不到每次具体导入到IPC设备表的记录数********");
			return false;
		}

		try {

			Long dataCount = Long.valueOf(recordCount);
			// 产品原始表的数据导入到设备表中
			mesDataMapper.recordsFromProdToIpcamera(dataCount);
		} catch (Exception e) {
			logger.info("********产品原始表的数据导入到设备表失败********");

			// 记录日志信息
			PlatformLog pl = new PlatformLog();
			pl.setLogId(Toolkits.getSequenceID18());
			pl.setUserId(6L); // 由于该字段不能为空，故写死
			pl.setModuleName("Mes入库:数据导入云平台设备表");
			pl.setLogContent("产品原始表部分数据导入云平台设备运行表出现异常，失败");
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			logService.saveLog(pl);
			return false;
		}

		// 记录日志信息
		PlatformLog pl = new PlatformLog();
		pl.setLogId(Toolkits.getSequenceID18());
		pl.setUserId(6L); // 由于该字段不能为空，故写死
		pl.setModuleName("Mes入库:数据导入云平台设备表");
		pl.setLogContent("产品原始表部分数据导入云平台设备运行表，成功");
		pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
		logService.saveLog(pl);
		logger.info("********产品原始表的数据导入到设备表，成功********");
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Long queryAmountOfTheDifference() throws ManagerException {

		return mesDataMapper.queryAmountOfTheDifference();
	}
}
