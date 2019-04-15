package org.csr.core;

import java.io.Serializable;
import java.util.List;

/**
 * 访问资源
 * 
 * @author caijin
 * 
 */
public interface SecurityResource extends Serializable {
	
	Long getId();
	/**
	 * 访问链接
	 * 
	 * @return
	 */
	String getName();
	/**
	 * 访问链接
	 * @return
	 */
	String getForwardUrl();
	
	/**
	 * getUrlRule: 校验规则 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	String getUrlRule();
	
	/**
	 *  访问资源编码
	 * @return
	 */
	String getCode();
	
	/**
	 * getBrowseLogLevel: 浏览日志<br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Byte getBrowseLogLevel();
	
	/**
	 * getOperationLogLevel: 操作日志 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	Byte getOperationLogLevel();
	/**
	 * 获取子
	 * @return
	 */
	List<? extends SecurityResource> getChildren();
	
	String getRemark();
}
