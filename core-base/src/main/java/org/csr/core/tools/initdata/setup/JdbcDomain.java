package org.csr.core.tools.initdata.setup;

public class JdbcDomain {
	String driver;
	String jdbcURL;
	String user;
	String password;

	public JdbcDomain(String driver, String jdbcURL, String user,String password) {
		super();
		this.driver = driver;
		this.jdbcURL = jdbcURL;
		this.user = user;
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getJdbcURL() {
		return jdbcURL;
	}

	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
