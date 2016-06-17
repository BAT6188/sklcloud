package com.skl.cloud.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class BeanLocator implements BeanFactoryAware {
	private static BeanFactory beanFactory;

	/**
	 * 根据提供的bean名称得到相应的Bean类
	 * 
	 * @param servName
	 *            bean名称
	 */
	public static Object getBean(String name) {
		return beanFactory.getBean(name);
	}

	/**
	 * 根据提供的bean名称和类型得到相应的Bean类
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> type) {
		return beanFactory.getBean(name, type);
	}

	/**
	 * 根据类型得到相应的Bean类
	 * 
	 * @param type
	 * @return
	 */
	public static <T> T getBean(Class<T> type) {
		return beanFactory.getBean(type);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BeanLocator.beanFactory = beanFactory;
	}

}
