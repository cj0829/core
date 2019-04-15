package org.csr.core.tools.initdata.setup;


public class DeployTool{
	/**数据库类型*/
	private static String database="";
	/**文件路径*/
	protected static String filePath="";
	public static String getDatabase(){
		return DeployTool.database;
	}
	public static void setDatabase(String database){
		DeployTool.database=database;
	}
	public static void setFilePath(String filePath){
		DeployTool.filePath=filePath;
	}
	
	public static void deploy() throws Exception {
		System.out.println("创建和初始化数据库\n");
		SystemDataSetup.installDB(database);
	}
}
