package org.csr.core.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanFactoryUtil {
	
	static Map instances = new HashMap();
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static BeanFactory getBeanFactory(String resource) {
		BeanFactory factory = null;
		synchronized (instances) {
			if (factory == null) {
				try {
					factory = (BeanFactory) new ClassPathXmlApplicationContext(
							"/" + resource);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(" Spring������ʾ��" + e.getMessage());
				}
			}
		}
		return factory;
	}

	/**
	 * ֧�ֶ�������ļ���ʵ��
	 * 
	 * @param resource
	 * @return
	 */
	public static BeanFactory getBeansFactory(String resource) {
		BeanFactory factory = null;
		synchronized (instances) {
			if (resource == null) {
				resource = "/spring.properties";
			}
			factory = (BeanFactory) instances.get(resource);
			if (factory == null) {
				try {
					InputStream in = SpringBeanFactoryUtil.class.getResourceAsStream(resource);
					Properties props = new Properties();
					props.load(in);
					String path = props.getProperty("path");
					String[] paths = path.split(",");
					factory = (BeanFactory) new ClassPathXmlApplicationContext(paths);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(" Spring������ʾ��" + e.getMessage());
				}
			}
			instances.put(resource, factory);
		}
		return factory;
	}

	public static void main(String[] args) {
		getBeansFactory("/spring.properties");
	}
}