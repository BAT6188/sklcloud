package com.skl.cloud.foundation.batchjob;

import java.util.List;

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
public interface Job<T> {

	/**
	 * Prepare the data, maybe this data come from files or db.
	 * 
	 * @param jobContext the {@link JobContext} object
	 * @return the data that will be processed
	 */
	public List<T> dataSource(JobContext jobContext);

	/**
	 * Start process business logic job.
	 * 
	 * @param jobContext
	 *            the {@link JobContext} object
	 * @throws JobException
	 */
	public void process(T item, JobContext jobContext);

}
