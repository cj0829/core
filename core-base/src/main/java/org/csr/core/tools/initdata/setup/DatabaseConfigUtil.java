package org.csr.core.tools.initdata.setup;

public abstract class DatabaseConfigUtil{
	public static final String MSSQL_TYPE = "MSSQL";
	public static final String ORACLE_TYPE = "ORACLE";
	public static final String DB2_TYPE = "DB2";
	public static final String MYSQL_TYPE = "MYSQL";
	
	/**引用文件路径*/
	protected static String filePath="";
	/**sql文件*/
	protected static String createTable="";
	
	private String server;
	private String port;
	private String user;
	private String password;
	private String schema;
	private String schemapass;
	private String linkId;
	private String storepath;
	
	public DatabaseConfigUtil(){
		this.server = null;
		this.port = null;
		this.user = null;
		this.password = null;
		this.schema = null;
		this.schemapass = null;
		this.linkId = null;
		this.storepath = null;
	}

	
	public static String getFilePath() {
		return filePath;
	}
	public static void setFilePath(String filePath) {
		DatabaseConfigUtil.filePath = filePath;
	}
	public static String getCreateTable() {
		return createTable;
	}
	public static void setCreateTable(String createTable) {
		DatabaseConfigUtil.createTable = createTable;
	}


	public static DatabaseConfigUtil getInstance(String type){
		if("MSSQL".equalsIgnoreCase(type)){
			return new MssqlDatabaseConfig();
		}
		if ("ORACLE".equalsIgnoreCase(type)){
			return new OracleDatabaseConfig();
		}
		if ("DB2".equalsIgnoreCase(type)){
			return new OracleDatabaseConfig();
		}
		if ("MYSQL".equalsIgnoreCase(type)) {
			return new MysqlDatabaseConfig();
		}
		return null; 
	} 
	
	public abstract String getDefaultAdmin();

	public abstract String getDefaultPort();

	public abstract String getJdbcDriver();

	public abstract String getAdminURL();

	public abstract String getCommonURL();

	public abstract String getAdminUser();

	public abstract String getAdminPassword();

	public abstract String getDBUser();
	
	public abstract String getDBPassword();

	public abstract String[] getCreateDBSQL();

	public abstract String[] getDeleteDBSQL();

	public abstract String[] getCreateBlobTableSQL();

	public abstract String getSchemaFile();

	public abstract String getCheckDBExistSQL();
	
	public String getStorepath() { return this.storepath; }

	public void setStorepath(String storepath){
		this.storepath = storepath;
	}

	public String getLinkId() {
		return this.linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getSchemapass() {
		return this.schemapass;
	}

	public void setSchemapass(String schemapass) {
		this.schemapass = schemapass;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return this.user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return this.password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
}
 