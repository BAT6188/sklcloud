package com.skl.cloud.foundation.batchjob;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Job processing context interface. 
 *  
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7143 $ $Date: 2016-03-16 17:01:30 +0800 (Wed, 16 Mar 2016) $
 * 
 */
public class JobContext {
	
	/**
	 * The property name of job. 
	 */
	public static final String FETCH_SIZE_NAME = "fetchSize";
	
	/**
	 * The default fetch size.
	 */
	private static final int DEFAULT_FETCH_SIZE = 500;
	
	/**
	 * The property name of roll poling. 
	 */
	public static final String ROLL_POLING_NAME = "rollPoling";
	
	/**
	 * The default value of roll poling. 
	 */
	private static final boolean DEFAULT_ROLL_POLING = true;
	
	
	/**
	 * Parameter's map
	 */
	private final Map<String, Object> param = new HashMap<String, Object>();

	/**
	 * Default constructor. Initializes a new execution context with an empty internal param.
	 */
	public JobContext() {
	}

	/**
	 * Initializes a new execution context with the contents of another param.
	 *
	 * @param param Initial contents of context.
	 */
	public JobContext(Map<String, Object> param) {
		this.param.putAll(param);;
	}
	
	/**
	 * Get attribute from job context.
	 */
	public final Object getAttribute(String attrName) {
		return param.get(attrName); 
    }
	
	/**
	 * Add attribute for job context.
	 */
	public final void setAttribute(String attrName, Object attrValue) {
		param.put(attrName, attrValue);      
	}

	/**
	 * Get fetch size, default value is {@link DEFAULT_FETCH_SIZE}.
	 * 
	 * @return fetch size
	 */
	public int getFetchSize() {
		String fetchSize = (String) param.get(FETCH_SIZE_NAME);
		return fetchSize == null ? DEFAULT_FETCH_SIZE : Integer.valueOf(fetchSize);
	}
	
	/**
	 * Check if a job is roll poling, default value is {@link DEFAULT_ROLL_POLING}.
	 * 
	 * @return roll poling value
	 */
	public boolean isRollPoling() {
		String rollPoling = (String) param.get(ROLL_POLING_NAME);
		return rollPoling == null ? DEFAULT_ROLL_POLING : Boolean.valueOf(rollPoling);
	}
}
