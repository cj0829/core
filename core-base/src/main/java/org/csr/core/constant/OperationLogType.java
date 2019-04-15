/**
 * Project Name:common
 * File Name:Snippet.java
 * Package Name:org.csr.core.constant
 * Date:2014-2-20下午3:50:53
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.constant;
/**
 * ClassName: Snippet.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-2-20下午3:50:53 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  1 增；2 删；3 改；4 查
 */

public interface OperationLogType {
	/**
	 * 添加
	 */
	Byte ADD = 1;
	/**
	 * 修改
	 */
	Byte UPDATE = 2;
	/**
	 * 删除操作
	 */
	Byte DELETE = 3;
	/**
	 * 浏览
	 */
	Byte BROWSE = 4;
}

