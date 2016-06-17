package com.skl.cloud.service.job;

import java.util.List;

import com.skl.cloud.model.job.JobDefinitionBean;
import com.skl.cloud.model.job.JobExecutionBean;
import com.skl.cloud.model.job.JobInstanceBean;

/**
 * <p>Job Manager Business Service Interface.
 * 
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin </p>
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public interface JobManagerService {
	
	/**
	 * Get a job definition.
	 * 
	 * @param jobId the {@link JobDefinitionBean} object
	 */
	public JobDefinitionBean getJobDefinition(long jobId); 
	
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
	public void createJobInstance(JobInstanceBean jobInstance); 
	
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
	public void createJobExecution(JobExecutionBean jobExecution);

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
	 * Get the historical job instance ids.
	 * 
	 * @param daysAgo some days ago
	 * @return job instance ids
	 */
	public List<Long> getHistoricalJobInstanceIds(int daysAgo);	
}
