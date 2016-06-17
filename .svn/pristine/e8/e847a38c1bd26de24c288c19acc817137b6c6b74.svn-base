package com.skl.cloud.util.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.skl.cloud.model.mes.PlatformProd;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	/**
	 * 
	  * createErrorExcel2007(生成一个Excel)
	  * @Title: createErrorExcel2007
	  * @Description: TODO
	  * @param @param errorList
	  * @param @param pathName
	  * @param @param errorType (参数说明)
	  * @return void (返回值说明)
	  * @throws (异常说明)
	  * @author wangming
	  * @date 2016年3月19日
	 */
	public static void createErrorExcel2007(List errorList, String pathName, String errorType) {

		WritableWorkbook writableWorkbook = null;
		WritableSheet sheet = null;
		WritableFont font = null;
		WritableCellFormat format = null;
		Label titleLabel = null;
		Label contentLabel = null;

		try {

			File file = new File(pathName);
			writableWorkbook = Workbook.createWorkbook(file);

			sheet = writableWorkbook.createSheet("First Sheet", 0);

			if (errorType.equals("title")) {

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(0, 0, "Excel文件出错描述", format);

				sheet.addCell(titleLabel);

				if (errorList != null && errorList.size() > 0) {

					for (int i = 0; i < errorList.size(); i++) {

						contentLabel = new Label(0, i + 1, String.valueOf(errorList.get(i)));
						sheet.addCell(contentLabel);

					}

				}

			}

			if (errorType.equals("content")) {

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(0, 0, "SKL PN", format);

				sheet.addCell(titleLabel);

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(1, 0, "Series Number", format);

				sheet.addCell(titleLabel);

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(2, 0, "MAC ID", format);

				sheet.addCell(titleLabel);

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(3, 0, "FW Version", format);

				sheet.addCell(titleLabel);

				font = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
				format = new WritableCellFormat(font);
				titleLabel = new Label(4, 0, "Manufacture Time", format);

				sheet.addCell(titleLabel);

				if (errorList != null && errorList.size() > 0) {

					for (int i = 0; i < errorList.size(); i++) {

						PlatformProd product = (PlatformProd) errorList.get(i);

						contentLabel = new Label(0, i + 1, String.valueOf(product.getProdPn()));
						sheet.addCell(contentLabel);

						contentLabel = new Label(1, i + 1, String.valueOf(product.getProdSn()));
						sheet.addCell(contentLabel);

						contentLabel = new Label(2, i + 1, String.valueOf(product.getProdMac()));
						sheet.addCell(contentLabel);

						contentLabel = new Label(3, i + 1, String.valueOf(product.getProdVersion()));
						sheet.addCell(contentLabel);

						contentLabel = new Label(4, i + 1, String.valueOf(product.getProdMakedate()));
						sheet.addCell(contentLabel);

					}

				}

			}
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}

	}

}
