/**
 * Project Name:FileServicesSystem_1.0
 * File Name:WinFileService.java
 * Package Name:org.csr.media.service
 * Date:2014年6月12日下午12:32:57
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.business.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.csr.core.exception.Exceptions;
import org.csr.core.exception.ServiceException;
import org.csr.core.util.FileUtil;
import org.csr.core.util.ObjUtil;
import org.springframework.stereotype.Service;

/**
 * ClassName: WinFileService.java <br/>
 * System Name：    文件上传系统 <br/>
 * Date:     2014年6月12日下午12:32:57 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
@Service("winFilesService")
public class WinFilesService implements FileService{

	
	@Override
	public boolean uploadFileSystem(File storeFile,InputStream inputStream) throws ServiceException {
		if(ObjUtil.isEmpty(storeFile)){
			Exceptions.service("","您要保存的文件不存在！");
		}
		if(storeFile.exists()){
			FileUtil.makeDir(storeFile.getParentFile());
		}
		try {
			BufferedInputStream in =  new BufferedInputStream(inputStream);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(storeFile));
			FileUtil.copy(in, out);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public File createFile(String fileName,String storePath){
		String storeName=FileUtil.reNameasUUID(fileName);
		if(ObjUtil.isEmpty(storePath)){
			Exceptions.service("","没有找到保存路径！");
		}
		File newFile=null;
		if(storePath.indexOf('@') < 0){
			File parent=new File(storePath,FileUtil.createDateDir());
			FileUtil.makeDir(parent);
			newFile=new File(storePath, storeName);
		}else{
			storePath=storePath.substring(1);
			File parent=new File(FileUtil.webRootPath()+storePath,FileUtil.createDateDir());
//			File parent=new File("E:\\"+storePath,FileTool.createDateDir());
			FileUtil.makeDir(parent);
			newFile=new File(parent, storeName);
		}
		return newFile;
	}

	@Override
	public InputStream download(String storePath,String winPath) throws FileNotFoundException {
//		String winPath=PropertiesUtil.getConfigureValue("exam.testPaper.path");
		if(winPath.indexOf('@') < 0){
			File copy=new File(winPath,"\\"+storePath);
			return new FileInputStream(copy);
		}else{
			winPath=winPath.substring(1);
			File copy=new File(FileUtil.webRootPath()+winPath+"\\"+storePath);
//			File copy=new File("E:\\"+winPath+"\\"+tpi.getStorePath());
			return new FileInputStream(copy);
		}
	}
	
}

