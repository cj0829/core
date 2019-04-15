package org.csr.core.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuxin on 16/9/10.
 */
@Target({ ElementType.METHOD }) // 注解使用对象
@Retention(RetentionPolicy.RUNTIME) // 注解使用时机
@Documented
@Inherited // 继承
public @interface MonitorLog {

	String desc() default "";

	String[] param() default {};
}