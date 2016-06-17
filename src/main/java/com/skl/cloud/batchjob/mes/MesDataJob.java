package com.skl.cloud.batchjob.mes;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.skl.cloud.controller.app.AppController;
import com.skl.cloud.foundation.batchjob.JobContext;
import com.skl.cloud.foundation.batchjob.SimpleJob;
import com.skl.cloud.service.job.JobManagerService;
import com.skl.cloud.service.mes.MesDataService;
import com.skl.cloud.util.common.LoggerUtil;
import com.skl.cloud.util.constants.Constants.JobId;

/**
 * 
  * @ClassName: MesDataJob
  * @Description: Mes数据入库的Job，定时检测Mes服务器上是否存在新的产品数据
  * @author wangming
  * @date 2016年4月8日
  *
 */
@Component("mesDataJob")
@Lazy
public class MesDataJob extends SimpleJob<Long> {

	protected Logger log = LoggerFactory.getLogger(MesDataJob.class);

	@Autowired
	private MesDataService mesDataService;

	@Autowired
	private JobManagerService jobManagerService;

	@Override
	public List<Long> dataSource(JobContext jobContext) {

		log.info("********MesDataJob****dataSource开始********");

		List<Long> idList = new ArrayList<Long>();
		// 仅为了执行一次process方法
		idList.add(1L);

		return idList;
	}

	@Override
	public void process(Long item, JobContext jobContext) {
		log.info("********MesDataJob****process方法开始********");
		log.info("********Mes入库流程开始********", this.getClass());

		boolean resultFlg = mesDataService.insertMesExcelData();

		// String mesResult = resultFlg == true? "success！！！":"fail！！！";
		// log.info("********Mes入库的结果：" + mesResult + "********");
	}

	@Override
	public long getJobId() {

		log.info("********MesDataJob****getJobId开始********");
		return JobId.MES_DATA;

	}

}
