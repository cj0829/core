package org.csr.core.tools.initdata.setup;

public class MssqlDatabaseConfig  extends DatabaseConfigUtil{
	public String getDefaultAdmin(){
		return "sa";
	}

	public String getDefaultPort() {
		return "1433";
	}

	public String getJdbcDriver() {
		return "net.sourceforge.jtds.jdbc.Driver";
	}

	public String getAdminURL() {
		return getJdbcURL("master");
	}

	public String getCommonURL() {
		return getJdbcURL(getSchema());
	}

	private String getJdbcURL(String schema) {
		String port = getPort();
		String sp = new StringBuilder().append(getServer()).append("1433".equals(port) ? "" : new StringBuilder().append(":").append(port).toString()).toString();
		StringBuffer sb = new StringBuffer();
		sb.append("jdbc:jtds:sqlserver://");
		sb.append(sp);
		sb.append(";DatabaseName=");
		sb.append(schema);
		sb.append(";SelectMethod=cursor");
		sb.append(";sendStringParametersAsUnicode=true");
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
		return new StringBuilder().append("select name from sysdatabases where name='").append(getSchema()).append("'").toString();
	}
	
	public String[] getDeleteDBSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("if exists (select * from sysdatabases where name='");
		sb.append(getSchema());
		sb.append("')\r\n");
		sb.append("begin\r\n");
		sb.append(new StringBuilder().append("drop database ").append(getSchema()).append("\r\n").toString());
		sb.append("end");
		return new String[] { sb.toString() };
	}

	public String[] getCreateDBSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("create database ");
		sb.append(getSchema());
		sb.append(" COLLATE Chinese_PRC_CS_AS");
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

