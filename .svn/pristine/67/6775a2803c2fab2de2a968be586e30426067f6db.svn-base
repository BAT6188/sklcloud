package com.skl.cloud.util.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

	private static String fs = System.getProperties().getProperty("file.separator"); // 系统文件分隔符

	/**
	 * 
	  * findFile(查找指定目录下Excel文件)
	  * @Title: findFile
	  * @Description: TODO
	  * @param @param path
	  * @param @return (参数说明)
	  * @return String (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月18日
	 */
	public static List<String> findFile(String path) {
		String fName = path;
		List<String> list = new ArrayList<String>();
		String[] arryFilter = { ".xls", ".xlsx" };

		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();

				for (int i = 0; i < fileArray.length; i++) {
					if (!fileArray[i].isDirectory()) {

						// 文件类型判断
						if (fileArray[i].getName().toLowerCase().endsWith(arryFilter[0])) {
							fName = path + fs + fileArray[i].getName(); // 通用路径
							list.add(fName);
						} else if (fileArray[i].getName().toLowerCase().endsWith(arryFilter[1])) {
							fName = path + fs + fileArray[i].getName(); // 通用路径
							list.add(fName);
						} else {
							LoggerUtil.error("文件[" + fileArray[i].getName() + "]非EXCEL文件。", FileUtil.class);
						}

					} else {// 子目录不处理
						continue;
					}
				}

				// 每次只取目录中一份Excel文件进行处理
				// if (list.size() >= 1) {
				// fName = list.get(0);
				// } else {
				// fName = "";
				// }

			} else {
				LoggerUtil.error("路径[" + path + "]不是目录。", FileUtil.class);
			}
		} else {
			LoggerUtil.error("路径[" + path + "]不存在。", FileUtil.class);
		}

		return list;
	}

	/**
	 * 
	  * removeFile(删除文件 )
	  * @Title: removeFile
	  * @Description: TODO
	  * @param @param strFilePath
	  * @param @return (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月16日
	 */
	public static boolean removeFile(String strFilePath) {
		boolean result = false;
		if (strFilePath == null || "".equals(strFilePath)) {
			return result;
		}
		File file = new File(strFilePath);
		if (file.isFile() && file.exists()) { // 判断是不是文件和判断该路径是否存在
			result = file.delete();
		}
		return result;
	}

	/**
	 * 
	  * moveFile(把一个文件移动到另一个目录下)
	  * @Title: moveFile
	  * @Description: TODO
	  * @param @param sourcePath
	  * @param @param newPath
	  * @param @return (参数说明)
	  * @return boolean (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年4月22日
	 */
	public static boolean moveFile(String sourcePath, String newPath) {

		File file = new File(sourcePath);
		if (file.renameTo(new File(newPath + file.getName()))) {
			return true;
		} else {
			return false;
		}
	}
}
