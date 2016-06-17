package com.skl.cloud.model.job;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Job Execution Model. </p>
 * 
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin </p>
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public class JobExecutionBean implements Serializable {
	
	private static final long serialVersionUID = 8345732271943062404L;

	private Long jobExecutionId;
	
	private Long version;
	
	private Long jobInstanceId;
	
	private Date createTime;
	
	private Date startTime;
	
	private Date endTime;
	
	private String status;
	
	private String exitCode;
	
	private String exitMessage;
	
	private Date lastUpdated;
	
	private String serverName;
	
	/**
	 * Return the job excution id.
	 */
	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	/**
	 * Set the job execution id.
	 */
	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	/**
	 * Return the job execution version.
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Set the job execution version.
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

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
	 * Return the job execution create time.
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Set the job execution create time.
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Return the job execution start time.
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set the job execution start time.
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Return the job execution end time.
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set the execution end time.
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Return the job exeution status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set the job execution status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Return the job execution exit code.
	 */
	public String getExitCode() {
		return exitCode;
	}

	/**
	 * Set the job execution exit code.
	 */
	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	/**
	 * Return the job execution exit message.
	 */
	public String getExitMessage() {
		return exitMessage;
	}

	/**
	 * Set the job execution exit message.
	 */
	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	/**
	 * Return the job execution last updated.
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Set the job execution last updated.
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * Return the job execution server name.
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Set the job execution server name.
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
