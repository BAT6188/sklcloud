package com.skl.cloud.dao.job;

import java.util.List;

import com.skl.cloud.model.job.JobDefinitionBean;
import com.skl.cloud.model.job.JobExecutionBean;
import com.skl.cloud.model.job.JobInstanceBean;

/**
 * <p>JobManagerMapper. 
 *  
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public interface JobManagerMapper {
	
	/**
	 * Query a job definition.
	 * 
	 * @param jobId the {@link JobDefinitionBean} object
	 */
	public JobDefinitionBean queryJobDefinition(long jobId); 
	
	/**
	 * Update the job definition.
	 * 
	 * @param jobId the {@link JobDefinitionBean} object
	 */
	public void updateJobDefinition(JobDefinitionBean jobDefinition); 
	
	/**
	 * Create a new job instance.
	 * 
	 * @param jobInstance the {@link JobInstanceBean} object
	 */
	public void insertJobInstance(JobInstanceBean jobInstance);   
	
	/**
	 * Update the job instance.
	 * 
	 * @param jobInstance the {@link JobInstanceBean} object
	 */
	public void updateJobInstance(JobInstanceBean jobInstance);

	/**
	 * Create a new job execution.
	 * 
	 * @param jobExecution the {@link JobExecutionBean} object
	 */
	public void insertJobExecution(JobExecutionBean jobExecution);

	/**
	 * Update the job execution.
	 * 
	 * @param jobExecution the {@link JobExecutionBean} object
	 */
	public void updateJobExecution(JobExecutionBean jobExecution);  

	/**
	 * Delete the job instance.
	 * 
	 * @param jobInstanceId
	 */
	public void deleteJobInstance(long jobInstanceId);
	
	/**
	 * Delete the job Execution.
	 * 
	 * @param jobInstanceId
	 */
	public void deleteJobExecution(long jobInstanceId);
	
	/**
	 * Query the historical job instance ids.
	 * 
	 * @param daysAgo some days ago
	 * @return job instance ids
	 */
	public List<Long> queryHistoricalJobInstanceIds(int daysAgo);
}
