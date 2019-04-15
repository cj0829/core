/**
 * Project Name:mediaFLV2.0
 * File Name:FileType.java
 * Package Name:org.csr.media.constant
 * Date:2014年5月4日下午12:25:06
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.util.crop;
/**
 * ClassName: FileType.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年5月4日下午12:25:06 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface CodeType {
	/**flv不需要转码的文件*/
	public static int FLV=1;
	/**需要转码的格式*/
	public static int CODING=2;
	/**其他不能转码的格式*/
	public static int OTHER=3;

}

