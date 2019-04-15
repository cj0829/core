package org.csr.core.tools.initdata.setup;

public class MysqlDatabaseConfig extends DatabaseConfigUtil{
	
	public String getDefaultAdmin(){
		return "root";
	}
	public String getDefaultPort() {
		return "3306";
	}
	public String getJdbcDriver() {
		return "com.mysql.jdbc.Driver";
	}
	public String getAdminURL() {
		return getJdbcURL("");
	}
	public String getCommonURL() {
		return getJdbcURL(getSchema());
	}
	private String getJdbcURL(String schema) {
		String port = getPort();
		String sp = new StringBuilder().append(getServer()).append("3306".equals(port) ? "" : new StringBuilder().append(":").append(port).toString()).toString();
		StringBuffer sb = new StringBuffer();
		sb.append("jdbc:mysql://");
		sb.append(sp);
		if (!schema.equals("")) {
			sb.append("/");
			sb.append(schema);
			sb.append("?useUnicode=true&characterEncoding=UTF-8");
		}
		return sb.toString();
	}
	public String getAdminUser() {
		return getUser();
	}
	public String getAdminPassword() {
		return getPassword();
	}
	public String getDBUser() {
		return getUser();
	}
	public String getDBPassword() {
		return getPassword();
	}
	public String getCheckDBExistSQL() {
		return new StringBuilder().append("select schema_name from information_schema.schemata where schema_name='").append(getSchema()).append("'").toString();
	}
	public String[] getDeleteDBSQL(){
		StringBuffer sb = new StringBuffer();
		sb.append("Drop schema if exists ");
		sb.append(getSchema());
		return new String[] { sb.toString() };
	}
	public String[] getCreateDBSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("create schema ");
		sb.append(getSchema());
		sb.append(" COLLATE utf8_general_ci");
		return new String[] { sb.toString() };
	}
	public String getSchemaFile() {
		return filePath+createTable;
	}
	public String[] getCreateBlobTableSQL() {
		return null;
	}
	public String getUpdateDateSQL() {
		return "update TABLEXX set COLUMNXX='DATEXX'";
	}
}
