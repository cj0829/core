package org.csr.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface Comment {
	/**
	 * en: 英文字段名称 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
    String en() default "";
    /**
     * ch: 中文字的名称 <br/>
     * @author caijin
     * @return
     * @since JDK 1.7
     */
    String ch() default "";
    /**
     * vtype: 验证规则 <br/>
     * @author caijin
     * @return
     * @since JDK 1.7
     */
    String vtype() default "";
    /**
     * len: 字段长度 <br/>
     * @author caijin
     * @return
     * @since JDK 1.7
     */
    int len()  default 0;
    /**
     * search: 是否作为查询条件，true：为需要有查询条件 <br/>
     * @author caijin
     * @return
     * @since JDK 1.7
     */
    boolean search() default false;
}
