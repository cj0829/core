package org.csr.core.tools.initdata;

import org.csr.core.tools.initdata.setup.DatabaseConfigUtil;
import org.csr.core.tools.initdata.setup.DeployTool;
import org.csr.core.tools.initdata.setup.SystemDataSetup;

public class Setup {
	public static void main(String[] args) throws Exception {
//		String projPath=System.getProperty("user.dir");
//		int index=projPath.lastIndexOf("\\");
//		String basePath=projPath.substring(0,index);
		String filePath=System.getProperty("filePath");
		
		DeployTool.setDatabase(System.getProperty("database"));
		DeployTool.setFilePath(filePath);
		SystemDataSetup.setMainProp(System.getProperty("mainProp"));
		SystemDataSetup.setIp(System.getProperty("ip"));
		SystemDataSetup.setDeleteDb(System.getProperty("deleteDb"));
		SystemDataSetup.setPort(System.getProperty("port"));
		SystemDataSetup.setDbAdmin(System.getProperty("dbAdmin"));
		SystemDataSetup.setAdminPassword(System.getProperty("AdminPassword"));
		SystemDataSetup.setDbName(System.getProperty("dbName"));
		SystemDataSetup.setLinkId(System.getProperty("linkId"));
		SystemDataSetup.setPassword(System.getProperty("password"));
		SystemDataSetup.setStorepath(System.getProperty("storepath"));
		DatabaseConfigUtil.setFilePath(filePath);
		DatabaseConfigUtil.setCreateTable(System.getProperty("create_table"));
		
		DeployTool.deploy();
	}
}
