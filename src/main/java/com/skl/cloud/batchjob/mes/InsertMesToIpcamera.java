package com.skl.cloud.batchjob.mes;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.skl.cloud.foundation.batchjob.JobContext;
import com.skl.cloud.foundation.batchjob.SimpleJob;
import com.skl.cloud.service.job.JobManagerService;
import com.skl.cloud.service.mes.MesDataService;
import com.skl.cloud.util.constants.Constants.JobId;

/**
 * 
  * @ClassName: InsertMesToIpcamera
  * @Description: 定期检测，把原始产品表（t_platform_prod）的数据插入到设备表（t_platform_ipcamera）中
  * @author wangming
  * @date 2016年4月8日
  *
 */
@Component("insertMesToIpcamera")
@Lazy
public class InsertMesToIpcamera extends SimpleJob<Long> {

	protected Logger log = LoggerFactory.getLogger(InsertMesToIpcamera.class);

	@Autowired
	private MesDataService mesDataService;

	@Autowired
	private JobManagerService jobManagerService;

	@Override
	public List<Long> dataSource(JobContext jobContext) {
		
		log.info("********insertMesToIpcamera****dataSource开始********");;
		List<Long> idList = new ArrayList<Long>();
		
		Long amount = 0L;
		amount = mesDataService.queryAmountOfTheDifference();
		
		log.info("********查询产品原始表和IPC设备表之间记录不同的条数：" + amount + "条********");
		
		if (amount > 0) {
			
			idList.add(amount);
		}
		return idList;
	}

	@Override
	public void process(Long item, JobContext jobContext) {

		log.info("********insertMesToIpcamera****process开始********");
		log.info("********原始产品数据表往IPC设备表导入数据的流程开始********", this.getClass());

		boolean resultFlg = mesDataService.insertIpcameraWithMesData();

		String mesResult = resultFlg == true ? "success!" : "fail!";

		log.info("********原始产品数据表往IPC设备表导入数据的结果：" + mesResult + "********");
	}

	@Override
	public long getJobId() {
		
		log.info("********getJobId开始********");
		
		return JobId.INSERT_MES_TO_IPCAMERA;
	}

}
