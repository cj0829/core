/**
 * Project Name:FileServicesSystem_1.0
 * File Name:FileService.java
 * Package Name:com.fss.service
 * Date:2014年6月10日上午12:08:28
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.business.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.csr.core.exception.ServiceException;

/**
 * ClassName: FileService.java <br/>
 * System Name： 文件上传系统 <br/>
 * Date: 2014年6月10日上午12:08:28 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public interface FileService {

	/**
	 * 
	 */
	boolean uploadFileSystem(File storeFile, InputStream inputStream) throws ServiceException;

	File createFile(String fileName, String storePath);

	InputStream download(String storePath, String winPath) throws FileNotFoundException;


}