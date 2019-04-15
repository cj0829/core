package org.csr.core.tools.initdata.setup;

public class OracleDatabaseConfig extends DatabaseConfigUtil{
	public String getDefaultAdmin(){
		return "sys";
	}
	
	public String getDefaultPort() {
		return "1521";
	}

	public String getJdbcDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	public String getAdminURL() {
		return getJdbcURL();
	}

	public String getCommonURL() {
		return getJdbcURL();
	}

	private String getJdbcURL() {
		StringBuffer sb = new StringBuffer();
		sb.append("jdbc:oracle:thin:@");
		sb.append(getServer());
		sb.append(":");
		sb.append(getPort());
		sb.append(":");
		sb.append(getLinkId());
		return sb.toString();
	}

	public String getAdminUser() {
		return getUser();
	}

	public String getAdminPassword() {
		return getPassword();
	}

	public String getDBUser() {
		return getSchema();
	}

	public String getDBPassword() {
		return getSchemapass();
	}

	public String getCheckDBExistSQL() {
		return "select USERNAME from ALL_USERS where USERNAME = '" + getSchema() + "'";
	}

	public String[] getDeleteDBSQL(){
		String schema = getSchema();
		String[] sqls = new String[8];
		sqls[0] = getConditionalSQL(schema, "USER");
		sqls[1] = getConditionalSQL(getDefaultTBS(), "TABLESPACE");
		sqls[2] = getConditionalSQL(getBlobTBS(), "TABLESPACE");
		sqls[3] = getConditionalSQL("u", "TABLE");
		sqls[4] = getConditionalSQL("w", "TABLE");
		sqls[5] = getConditionalSQL("x", "TABLE");
		sqls[6] = getConditionalSQL("y", "TABLE");
		sqls[7] = getConditionalSQL("z", "TABLE");
		return sqls;
	}
	
	private String getConditionalSQL(String object, String type) {
		String actor = null;
		String condition = null;
		if ("USER".equalsIgnoreCase(type)) {
			actor = "DROP USER " + object + " CASCADE";
			condition = "ALL_USERS where USERNAME = '" + object + "'";
		} else if ("TABLE".equalsIgnoreCase(type)) {
			actor = "DROP TABLE " + object;
			condition = "ALL_TABLES WHERE TABLE_NAME ='" + object + "'";
		} else if ("TABLESPACE".equalsIgnoreCase(type)) {
			actor = "DROP TABLESPACE " + object + " including contents";
			condition = "DBA_DATA_FILES WHERE TABLESPACE_NAME='" + object + "'";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DECLARE\r\n");
		sb.append("num NUMBER(5);\r\n");
		sb.append("BEGIN\r\n");
		sb.append("select count(*) into num from " + condition + ";\r\n");
		sb.append("if num = 1 then\r\n");
		sb.append("execute immediate('" + actor + "');\r\n");
		sb.append("end if;\r\n");
		sb.append("END;\r\n");
		return sb.toString();
	}

	public String[] getCreateDBSQL() {
		String schema = getSchema();
		String storepath = getStorepath();
		if ((storepath.length() > 0) && ((!storepath.endsWith("/")) || (!storepath.endsWith("\\")))) {
			storepath = storepath + "/";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("CREATE SMALLFILE TABLESPACE \"" + getDefaultTBS() + "\" ");
		sb.append("DATAFILE '" + storepath + schema + "_1' ");
		sb.append("SIZE 100M REUSE LOGGING EXTENT MANAGEMENT ");
		sb.append("LOCAL SEGMENT SPACE MANAGEMENT AUTO ;");

		sb.append("CREATE SMALLFILE TABLESPACE \"" + getBlobTBS() + "\" ");
		sb.append("DATAFILE '" + storepath + schema + "_2' ");
		sb.append("SIZE 100M REUSE LOGGING EXTENT MANAGEMENT ");
		sb.append("LOCAL SEGMENT SPACE MANAGEMENT AUTO ;");

		sb.append("CREATE USER \"" + schema + "\" ");
		sb.append("PROFILE \"DEFAULT\" ");
		sb.append("IDENTIFIED BY \"" + getSchemapass() + "\" ");
		sb.append("DEFAULT TABLESPACE \"" + getDefaultTBS() + "\" ");
		sb.append("TEMPORARY TABLESPACE \"TEMP\" ");
		sb.append("QUOTA UNLIMITED ON \"" + getDefaultTBS() + "\" ");
		sb.append("QUOTA UNLIMITED ON \"" + getBlobTBS() + "\" ");
		sb.append("ACCOUNT UNLOCK;");

		sb.append("GRANT ALTER ANY INDEX TO \"" + schema + "\";");
		sb.append("GRANT ALTER ANY PROCEDURE TO \"" + schema + "\";");
		sb.append("GRANT ALTER ANY TABLE TO \"" + schema + "\";");
		sb.append("GRANT CREATE ANY INDEX TO \"" + schema + "\";");
		sb.append("GRANT CREATE ANY PROCEDURE TO \"" + schema + "\";");
		sb.append("GRANT CREATE ANY TABLE TO \"" + schema + "\";");
		sb.append("GRANT DROP ANY INDEX TO \"" + schema + "\";");
		sb.append("GRANT DROP ANY PROCEDURE TO \"" + schema + "\";");
		sb.append("GRANT DROP ANY TABLE TO \"" + schema + "\";");
		sb.append("GRANT \"CONNECT\" TO \"" + schema + "\";");
		return sb.toString().split(";");
	}
	
	public String getSchemaFile() {
		return filePath+createTable;
	}
	
	public String[] getCreateBlobTableSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE TBlob (blob_key NUMBER NOT NULL, ");
		sb.append("blob_bytes BLOB DEFAULT EMPTY_BLOB() NOT NULL, ");
		sb.append("PRIMARY KEY(blob_key)) TABLESPACE ");
		sb.append(getBlobTBS());
		return new String[] { sb.toString() };
	}
	
	private String getDefaultTBS() {
		return getSchema() + "_CW";
	}
	
	private String getBlobTBS() {
		return getSchema() + "_BLOB";
	}
	
	public String getUpdateDateSQL() {
		return "update TABLEXX set COLUMNXX=to_date('DATEXX','YYYY-MM-DD')";
	}
}
