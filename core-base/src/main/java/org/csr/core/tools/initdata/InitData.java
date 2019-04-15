package org.csr.core.tools.initdata;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.csr.core.tools.initdata.setup.DBConnectionUtil;
import org.csr.core.tools.initdata.setup.DatabaseConfigUtil;
import org.csr.core.util.ExcelFileUtil;
import org.csr.core.util.JsonUtil;
import org.csr.core.util.ObjUtil;

@SuppressWarnings("resource")
public class InitData {
	private static Connection conn=null;
	private static Statement stm=null;
	
	public static void main(String[] args){
		String url=System.getProperty("url");
		String encoding=System.getProperty("encoding");
		if(ObjUtil.isNotBlank(encoding)){
			url=url+"&"+encoding;
		}
		String userName=System.getProperty("userName");
		String password=System.getProperty("password");
		String type=System.getProperty("database");
		String files=System.getProperty("files");
		System.out.println(files);
		DatabaseConfigUtil config = DatabaseConfigUtil.getInstance(type);
		try {
			conn=DBConnectionUtil.getConnection(config.getJdbcDriver(), url, userName, password);
			conn.setAutoCommit(false);
			baseData(JsonUtil.toMap(files));
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}finally{
			DBConnectionUtil.close(conn, stm, null);
		}
	}
	
	public static void baseData(Map<String, String[]> filesMap){
//		String basePath=System.getProperty("user.dir");
//		int index=basePath.lastIndexOf("\\");
		String filePath=System.getProperty("filePath");
		System.out.println("路径：="+filePath);
		Set<String> keys=filesMap.keySet();
		for(String key:keys){
			File file=new File(filePath+key);
			try{
				FileInputStream fis=new FileInputStream(file);
				Workbook workBook=new XSSFWorkbook(fis);
				for(String name:filesMap.get(key)){
					Sheet sheet=workBook.getSheet(name);
					ArrayList<ArrayList<String>> list=ExcelFileUtil.parseSheet(sheet);
					insert(list);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void insert(ArrayList<ArrayList<String>> list){
		List<Integer> sqlIndex=ExcelFileUtil.getExcelIndex(list, "sql");
		String sql="";
		try {
			stm=conn.createStatement();
			for(int i=4;i<list.size();i++){
				sql=list.get(i).get(sqlIndex.get(0));
				if(ObjUtil.isNotBlank(sql)){
					String database=System.getProperty("database");
					if("oracle".equalsIgnoreCase(database)){
						String end=sql.substring(sql.length()-1);
						if(";".equals(end)){
							sql=sql.substring(0,sql.length()-1);
						}
					}
					System.out.println(sql);
					stm.execute(sql);
				}
			}
		} catch (SQLException e1) {
			System.out.println("异常的sql=============="+sql);
			e1.printStackTrace();
			System.exit(0);
		}
	}
}
