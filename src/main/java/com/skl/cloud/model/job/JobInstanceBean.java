package com.skl.cloud.model.job;

import java.io.Serializable;

/**
 * <p>Job Instance Model. 
 * 
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin </p>
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 */
public class JobInstanceBean implements Serializable {

	private static final long serialVersionUID = -5326234082133311367L;
	
	private Long jobInstanceId;
	
	private Long version;
	
	private Long jobId;
	
	private String jobName;
	
	private String jobKey;
	
	/**
	 * Return the job instance id.
	 */
	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	/**
	 * Set the job instance id.
	 */
	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	/**
	 * Return the job instance version.
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Set the job instance version.
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
	
	/**
	 * Return the job id.
	 */
	public Long getJobId() {
		return jobId;
	}

	/**
	 * Set the job id.
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	/**
	 * Return the job name.
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Set the job name.
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Return the job key.
	 */
	public String getJobKey() {
		return jobKey;
	}

	/**
	 * Set the job key.
	 */
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}

}
