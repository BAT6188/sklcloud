package com.skl.cloud.common.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <p>Base implementation of {@link ApplicationContextAware}.
 * 
 * <p>Creation Date and by: 2016/03/12 Author: liyangbin </p>
 * 
 * @author $Author: liyangbin $
 * @version $Revision: 7177 $ $Date: 2016-03-18 12:31:38 +0800 (Fri, 18 Mar 2016) $
 * 
 */
@Component("uniqueSpringContextHolder")
public class SpringContextHolder implements ApplicationContextAware {

	private static final Logger LOG = Logger.getLogger(SpringContextHolder.class);

	private final static String APPPATH = "classpath:/spring-batch.xml";

	private static volatile ApplicationContext applicationContext;

	/**
	 * Default Constructor
	 */
	private SpringContextHolder() {

	}

	/**
	 * Set the ApplicationContext that this object runs in.
	 * Normally this call will be used to initialize the object.
	 *
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		synchronized (SpringContextHolder.class) {
			if (SpringContextHolder.applicationContext == null) {
				LOG.info("Injected the application context with spring.");
				SpringContextHolder.applicationContext = applicationContext;
			}
		}
	}

	/**
	 * Get the spring application context.
	 * 
	 * @return ApplicationContext 
	 */
	public static ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			synchronized (SpringContextHolder.class) {
				if (applicationContext == null) {
					LOG.info("Initialized the application context.");
					applicationContext = new ClassPathXmlApplicationContext(APPPATH);
				}
			}
		}
		return applicationContext;
	}

	/**
	 * get an instance by bean name, which may be shared or independent, of the specified bean. 
	 * 
	 * @param beanName the name of the bean to retrieve
	 * @return an instance of the bean 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBizComponent(String beanName) {
		return (T)getApplicationContext().getBean(beanName);
	}

	/**
	 * get an instance by bean type, which may be shared or independent, of the specified bean. 
	 * 
	 * @param requiredType the type of the bean to retrieve
	 * @return an instance of the bean 
	 */
	public static <T> T getBizComponent(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}
	
	/**
	 * get an instance by bean type, which may be shared or independent, of the specified bean. 
	 * 
	 * @param requiredType the type of the bean to retrieve
	 * @return an instance of the bean 
	 */
	public static <T> T getBizComponent(String beanName, Class<T> requiredType) {
		return getApplicationContext().getBean(beanName, requiredType);
	}
}
