package org.csr.core.tools.initdata.setup;
/*    */ 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnectionUtil{
	public static final String DEFAULTCONF_DIR = "dir";
	public static final String DB_DRIVER_NAME = "jdbc.driver";
	public static final String DB_CONNECTION_URL = "jdbc.driverUrl";
	public static final String DB_USER_NAME = "jdbc.user";
	public static final String DB_PASSWORD = "jdbc.password";

	public static Properties getProperties(String name)throws FileNotFoundException, IOException{
		String filename = System.getProperty("dir");
		InputStream in = new FileInputStream(filename + "/" + name);
		Properties props = new Properties();
		props.load(in);
		in.close();
		return props;
	}

	public static Connection getConnection(JdbcDomain jdbcDomain) throws SQLException {
		String driver = jdbcDomain.getDriver();
		String jdbcURL = jdbcDomain.getJdbcURL();
		String user = jdbcDomain.getUser();
		String password = jdbcDomain.getPassword();
		return getConnection(driver, jdbcURL, user, password);
	}

	public static Connection getConnection(String driver, String jdbcURL, String user, String password)throws SQLException{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return DriverManager.getConnection(jdbcURL, user, password);
	}
	public static void close(Connection conn,Statement stm,ResultSet rs){
		if(rs!=null)try{rs.close();}catch(Exception e){}
		if(stm!=null)try{stm.close();}catch(Exception e){}
		if(conn!=null)try{conn.close();}catch(Exception e){}
	}
}