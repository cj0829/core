package org.csr.core.tools.initdata.setup;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class DatabaseSetup{
	
	private static String oper="Y";
	public static void setOper(String op){
		oper=op;
	}
	public static int initDB(JdbcDomain jdbcDomain,String ddlFile)throws SQLException{
		String[] sqlStatements = loadStatements(ddlFile);
		return executeOnce(jdbcDomain, sqlStatements);
	}
	
	public static int executeOnce(JdbcDomain jdbcDomain, String[] sqlStatements)throws SQLException{
		int code = 0;
		Connection conn = null;
		PreparedStatement stmnt = null;
		String sql = null;
		try {
			conn = DBConnectionUtil.getConnection(jdbcDomain);
			conn.setAutoCommit(false);
			for (int i = 0; i < sqlStatements.length; i++) {
				sql = sqlStatements[i];
				if(sql.trim().equals("")){
					continue;
				}
				sql = sql.replaceAll("\r\n", "\n");
				String ts = sql.replaceAll("\n", "").toUpperCase();
				Statement s = null;
				try{
					if (ts.startsWith("SET QUOTED_IDENTIFIER ")) {
						s = conn.createStatement();
						s.execute(sql);
					}else {
						System.out.println(sql);
						stmnt = conn.prepareStatement(sql);
						stmnt.executeUpdate();
					}
				} finally {
					if (s != null) {
						s.close();
						s = null;
					}
					if (stmnt != null) {
						stmnt.close();
						stmnt = null;
					}
				}
			}
			conn.commit();
		} catch (SQLException sqle) {
			code = 1;
			if (conn != null) {
				conn.rollback();
			}
			throw sqle;
		} finally {
			if (stmnt != null) {
				stmnt.close();
				stmnt = null;
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
				conn = null;
			}
		}
		return code;
	}

	public static List<String[]> queryOnce(JdbcDomain jdbcDomain,String sql) throws SQLException{
		List<String[]> resultList = new ArrayList<String[]>();
		Connection conn = null;
		try {
			conn = DBConnectionUtil.getConnection(jdbcDomain);
			resultList = querydb(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		}
		return resultList;
	}

	public static List<String[]> querydb(Connection conn, String sql)throws SQLException{
		Statement stmnt = null;
		ResultSet rs = null;
		List<String[]> resultList = new ArrayList<String[]>();
		try {
			stmnt = conn.createStatement();
			rs = stmnt.executeQuery(sql);
			if (rs != null) {
				ResultSetMetaData meta = rs.getMetaData();
				int count = meta.getColumnCount();
				while (rs.next()) {
					String[] results = new String[count];
					for (int i = 1; i <= count; i++) {
						String s = rs.getString(i);
						results[(i - 1)] = (s != null ? s.trim() : s);
					}
					resultList.add(results);
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmnt != null) {
				stmnt.close();
			}
		}
		return resultList;
	}

	private static String[] loadStatements(String ddlFile) {
		FileInputStream fis = null;
		String sqls = null;
		try{
			fis = new FileInputStream(ddlFile);
			sqls = IOUtils.toString(fis, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (fis != null) {
					fis.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		String[] sqlStatements = sqls.split("GO", 0);
		if("Y".equalsIgnoreCase(oper)){
			List<String> sqlA=new ArrayList<String>();
			for(String sql:sqlStatements){
				if(selected(sql)){
					sqlA.add(sql);
				}
			}
			sqlStatements=sqlA.toArray(new String[sqlA.size()]);
		}
		return sqlStatements;
	}
	
	private static boolean selected(String sql){
		String database=DeployTool.getDatabase();
		if("MSSQL".equalsIgnoreCase(database)){
			// TODO ...
		}
		if("ORACLE".equalsIgnoreCase(database)){
			if(sql.indexOf("create table")>-1||sql.indexOf("comment on")>-1||sql.indexOf("add constraint")>-1){
				return true;
			}
		}
		if("DB2".equalsIgnoreCase(database)){
			// TODO 暂不支持
		}
		if("MYSQL".equalsIgnoreCase(database)){
			if(sql.indexOf("drop table")>-1||sql.indexOf("create table")>-1||sql.indexOf("add constraint")>-1){
				return true;
			}
		}
		return false;
	}
}
