package com.skl.cloud.foundation.batchjob;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.skl.cloud.model.job.JobDefinitionBean;

/**
 * <p>
 * Job processing business logic interface.
 * 
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public abstract class SimpleJob<T> implements Job<T> {

	private static final Logger LOGGER = Logger.getLogger(SimpleJobExecutor.class);
	
	/**
	 * The job definition.
	 */
	private JobDefinitionBean jobDefinition;

	/**
	 * Return this job definition.
	 */
	public JobDefinitionBean getJobDefinition() {
		return jobDefinition;
	}

	/**
	 * Set job definition.
	 */
	public void setJobDefinition(JobDefinitionBean jobDefinition) {
		this.jobDefinition = jobDefinition;
	}

	/**
	 * If throws an exception when process data, this method will be callback. 
	 * After invoke this method, program will check the returned value whether continue to process next item or exit this job.
	 * 
	 * @param item the process data
	 * @param jobContext Job context
	 * @param exception One exception
	 * @return If continue to process next item.
	 */
	public boolean processAfterException(T item, JobContext jobContext, Exception exception) {
		LOGGER.error("The job[" + getJobId() + "] process item[" + ToStringBuilder.reflectionToString(item) + "] failed.", exception);
		return true;
	}
	
	/**
	 * Return the job id.
	 */
	public abstract long getJobId();
	
}
