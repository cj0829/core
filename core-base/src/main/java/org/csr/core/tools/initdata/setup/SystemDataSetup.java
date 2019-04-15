package org.csr.core.tools.initdata.setup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:SystemDataSetup.java <br/>
 * System Name：    在线学习系统 <br/>
 * Date:     2016年6月29日下午5:38:09 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class SystemDataSetup extends DeployTool {
	private static final String CONFIG_PATH = "../conf";
	private static final String INSTALL_PATH = "../../";

	private static String mainProp = "";
	
	/**ip*/
	private static String ip="localhost";
	/**是否删除数据库（"Y"或"N"，其它值终止操作）*/
	private static String deleteDb="N";
	/**数据库端口号*/
	private static String port="";
	/**数据库管理员账号*/
	private static String dbAdmin="";
	/**管理员密码*/
	private static String adminPassword="";
	/**将创建的数据库的名称*/
	private static String dbName="";
	/**连接数据库的标识符（oracle用）*/
	private static String linkId="";
	/**新建数据库密码（oracle用）*/
	private static String password="";
	/**数据库文件存放路径（oracle用）*/
	private static String storepath="/";
	public static String getMainProp() {
		return mainProp;
	}
	public static void setMainProp(String mainProp) {
		SystemDataSetup.mainProp = mainProp;
	}
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		SystemDataSetup.ip = ip;
	}
	public static String getDeleteDb() {
		return deleteDb;
	}
	public static void setDeleteDb(String deleteDb) {
		SystemDataSetup.deleteDb = deleteDb;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		SystemDataSetup.port = port;
	}
	public static String getDbAdmin() {
		return dbAdmin;
	}
	public static void setDbAdmin(String dbAdmin) {
		SystemDataSetup.dbAdmin = dbAdmin;
	}
	public static String getAdminPassword() {
		return adminPassword;
	}
	public static void setAdminPassword(String adminPassword) {
		SystemDataSetup.adminPassword = adminPassword;
	}
	public static String getDbName() {
		return dbName;
	}
	public static void setDbName(String dbName) {
		SystemDataSetup.dbName = dbName;
	}
	public static String getLinkId() {
		return linkId;
	}
	public static void setLinkId(String linkId) {
		SystemDataSetup.linkId = linkId;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		SystemDataSetup.password = password;
	}
	public static String getStorepath() {
		return storepath;
	}
	public static void setStorepath(String storepath) {
		SystemDataSetup.storepath = storepath;
	}
	public static void installDB(String type) throws Exception {
		Map<String, String> map = getInputedDBInfo(type);
		if (map == null){
			return;
		}
		DatabaseConfigUtil config = DatabaseConfigUtil.getInstance(type);
		config.setServer(map.get("server"));
		config.setPort(map.get("port"));
		config.setUser(map.get("user"));
		config.setPassword(map.get("password"));
		config.setSchema(map.get("schema"));
		config.setSchemapass(map.get("schemapass"));
		config.setLinkId(map.get("linkId"));
		config.setStorepath(map.get("storepath"));

		System.out.println("\r\n正在检查数据库...");
		boolean createdb = true;
		JdbcDomain adminJdbc= new JdbcDomain(config.getJdbcDriver(), config.getAdminURL(), config.getAdminUser(),  config.getAdminPassword());
		List<?> list = DatabaseSetup.queryOnce(adminJdbc , config.getCheckDBExistSQL());
		boolean dbexist = (list != null && list.size() > 0);
		if (dbexist == true) {
			String str = SystemDataSetup.deleteDb;
			DatabaseSetup.setOper(str);
			if ("Y".equalsIgnoreCase(str)) {
				System.out.println("\r\n正在删除数据库...");
				DatabaseSetup.executeOnce(adminJdbc, config.getDeleteDBSQL());
			} else if ("N".equalsIgnoreCase(str)) {
				createdb = false;
			} else {
				return;
			}
		}
		if (createdb == true) {
			System.out.println("\r\n正在创建数据库...");
			DatabaseSetup.executeOnce(adminJdbc, config.getCreateDBSQL());
		}
		System.out.println("\r\n正在创建表结构...");
		/*String[] blobSQL = dbi.getCreateBlobTableSQL();
		if (blobSQL != null) {
			DatabaseSetup.executeOnce(prop, dbi.getDeleteTblobSql());
			DatabaseSetup.executeOnce(prop, blobSQL);
		}*/
		JdbcDomain dbJdbc= new JdbcDomain(config.getJdbcDriver(),config.getCommonURL(), config.getDBUser(), config.getDBPassword());
		DatabaseSetup.initDB(dbJdbc, config.getSchemaFile());
		System.out.println("\r\n正在初始化数据库...");
		System.setProperty("dir", CONFIG_PATH);
		System.setProperty("instdir", INSTALL_PATH);
		System.out.println("\r\n正在创建Generator...");
		DatabaseSetup.executeOnce(dbJdbc,getGenerator());
		System.out.println("\r\n操作成功!");
		System.exit(0);
	}



	private static Map<String, String> getInputedDBInfo(String type) throws Exception {
		String server = "";
		String port = "";
		String user = "";
		String password = "";
		String schema = "";
		String linkId = "";
		String schemapass = "";
		String storepath = "";
		DatabaseConfigUtil dbi = DatabaseConfigUtil.getInstance(type);

		String str = SystemDataSetup.ip;
		server = ("".equals(str) ? "localhost" : str);
		str = SystemDataSetup.port;
		port = ("".equals(str) ? dbi.getDefaultPort() : str);
		str = SystemDataSetup.dbAdmin;
		user = ("".equals(str) ? dbi.getDefaultAdmin() : str);
		str = SystemDataSetup.adminPassword;
		password = str;

		if (DatabaseConfigUtil.MSSQL_TYPE.equalsIgnoreCase(type)) {
			str = SystemDataSetup.dbName;
			schema = str;
		} else if (DatabaseConfigUtil.ORACLE_TYPE.equalsIgnoreCase(type)) {
			str = SystemDataSetup.linkId;
			linkId = str;
			str = SystemDataSetup.dbName;
			schema = str.toUpperCase();
			str = SystemDataSetup.password;
			schemapass = ("".equals(str) ? schema : str);
			str = SystemDataSetup.storepath;
			storepath = str;
		} else if (DatabaseConfigUtil.DB2_TYPE.equalsIgnoreCase(type)) {
			//TODO 暂不支持。
		} else if (DatabaseConfigUtil.MYSQL_TYPE.equalsIgnoreCase(type)) {
			str = SystemDataSetup.dbName;
			schema = str;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("server", server);
		map.put("port", port);
		map.put("user", user);
		map.put("password", password);
		map.put("schema", schema);
		map.put("schemapass", schemapass);
		map.put("linkId", linkId);
		map.put("storepath", storepath);
		return map;
	}
	
	private static String[] getGenerator() {
		StringBuffer sql = new StringBuffer(" create table pmt_tb_generator (");
		sql.append("sequence_name varchar(255) not null,");
		sql.append("sequence_next_hi_value bigint,");
		sql.append("primary key (sequence_name)");
		sql.append(") ENGINE=InnoDB ;");
		return new String[]{sql.toString()};
	}
}
