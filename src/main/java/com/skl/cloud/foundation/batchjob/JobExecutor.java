package com.skl.cloud.foundation.batchjob;

/**
 * <p>Job executor interface. 
 *  
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 */
public interface JobExecutor {
	
	/**
	 * start a new job.
	 * @throws JobException 
	 */
	public void execute(Job<?> job, JobContext jobContext); 

}
