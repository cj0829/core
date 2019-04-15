package org.csr.core.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**]
 * ClassName:ClassBeanFactory.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午2:40:58 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
@Component
@Lazy(value=false)
public class ClassBeanFactory implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
    	System.out.println("================init beanFactory");
        ClassBeanFactory.beanFactory = beanFactory;
    }

    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return beanFactory.getBean(name, clazz);
    }

    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }
}
