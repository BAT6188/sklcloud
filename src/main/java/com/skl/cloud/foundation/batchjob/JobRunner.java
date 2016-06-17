package com.skl.cloud.foundation.batchjob;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.skl.cloud.common.spring.SpringContextHolder;

/**
 * <p>Job Runner. 
 *  
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public class JobRunner {
	
	private static final Logger LOGGER = Logger.getLogger(JobRunner.class); 
	
	/**
	 * Start job.
	 *
	 * Example :
	 * java -classpath ./lib/*;./src;./target/classes;./spring-config com.skl.cloud.foundation.batchjob.JobRunner jobBeanName fetchSize=1000
	 * 
	 */
	public static void main(String[] args) {
		
		if (args == null || args.length == 0) {
			LOGGER.error("At least one argument required: jobBeanName.");
			return;
		}
		Map<String, Object> jobParam = new HashMap<String, Object>();
		String jobBeanName = args[0];
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				String[] param = StringUtils.split(args[i], "=");
				jobParam.put(param[0], param[1]);
			}
		}
		
		ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
		Job<?> job = applicationContext.getBean(jobBeanName, Job.class);
		JobExecutor jobExecutor = new SimpleJobExecutor();
		applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(jobExecutor, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
		int exitCode = 0;
		try {
			jobExecutor.execute(job, new JobContext(jobParam));
		} catch (Exception e) {
			LOGGER.error("Execute the job[" + jobBeanName + "] error", e);
			exitCode = 1;
		}
		System.exit(exitCode);
	}
}
