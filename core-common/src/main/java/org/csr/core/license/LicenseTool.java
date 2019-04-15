/**
 * Project Name:core
 * File Name:LicenseTool.java
 * Package Name:org.csr.core.license
 * Date:2015-10-10上午11:14:56
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
*/

package org.csr.core.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.csr.core.util.ObjUtil;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * ClassName:LicenseTool.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2015-10-10上午11:14:56 <br/>
 * @author   liurui <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class LicenseTool {
	public static Integer toInt(char c){
		return ciMap().get(c);
	}
	public static String toInt(String str){
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<str.length();i++){
			buffer.append(toInt(str.charAt(i)));
		}
		return buffer.toString();
	}
	public static Map<Character,Integer> ciMap(){
		Map<Character, Integer> map=new LinkedHashMap<Character, Integer>();
		map.put('D', 0);
		map.put('l', 1);
		map.put('R', 2);
		map.put('E', 3);
		map.put('A', 4);
		map.put('S', 5);
		map.put('b', 6);
		map.put('q', 7);
		map.put('B', 8);
		map.put('g', 9);
		return map;
	}
	public static Map<Integer, Character> icMap(){
		Map<Integer, Character> map=new LinkedHashMap<Integer, Character>();
		map.put(0, 'D');
		map.put(1, 'l');
		map.put(2, 'R');
		map.put(3, 'E');
		map.put(4, 'A');
		map.put(5, 'S');
		map.put(6, 'b');
		map.put(7, 'q');
		map.put(8, 'B');
		map.put(9, 'g');
		return map;
	}
	public static String toChar(String digit){
		Map<Integer, Character> map=icMap();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<digit.length();i++){
			Character v=map.get(Integer.parseInt(digit.charAt(i)+""));
			if(ObjUtil.isNotEmpty(v)){
				sb.append(v);
			}
		}
		return sb.toString();
	}
	public static String encode(String str){
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<str.length();i++){
			buffer.append(ecBook().get(str.charAt(i)+""));
		}
		return buffer.toString();
	}
	public static String decode(String str){
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<str.length();){
			buffer.append(dcBook().get(str.substring(i, i=i+4)));
		}
		return buffer.toString();
	}
	public static Map<String, String> ecBook(){
		Map<String, String> map=new LinkedHashMap<String, String>();
		map.put("0", "MSDI");
		map.put("1", "MIGL");
		map.put("2", "LSDG");
		map.put("3", "JGSL");
		map.put("4", "UWOR");
		map.put("5", "MOWE");
		map.put("6", "ZXFB");
		map.put("7", "PEOR");
		map.put("8", "LAHG");
		map.put("9", "AETA");		
		map.put("A", "JIGE");
		map.put("B", "987J");
		map.put("D", "234T");
		map.put("E", "JFI8");
		map.put("R", "67WE");
		map.put("S", "WEO2");
		map.put("X", "IEGJ");
		map.put("Y", "K398");
		map.put("b", "8R4B");
		map.put("g", "YJDF");
		map.put("l", "7GTF");
		map.put("q", "HFIU");
		return map;
	}
	public static Map<String, String> dcBook(){
		Map<String, String> map=new LinkedHashMap<String, String>();
		map.put("MSDI", "0");
		map.put("MIGL", "1");
		map.put("LSDG", "2");
		map.put("JGSL", "3");
		map.put("UWOR", "4");
		map.put("MOWE", "5");
		map.put("ZXFB", "6");
		map.put("PEOR", "7");
		map.put("LAHG", "8");
		map.put("AETA", "9");
		map.put("JIGE", "A");
		map.put("987J", "B");
		map.put("234T", "D");
		map.put("JFI8", "E");
		map.put("67WE", "R");
		map.put("WEO2", "S");
		map.put("IEGJ", "X");
		map.put("K398", "Y");
		map.put("8R4B", "b");
		map.put("YJDF", "g");
		map.put("7GTF", "l");
		map.put("HFIU", "q");
		return map;
	}
	public static void reVal(String realPath,String val){
		try {
			File file=new File(realPath+"WEB-INF\\classes\\com\\pmt\\exam\\action\\ValAction.class");
			FileInputStream fis=new FileInputStream(file);
			ClassReader classReader=new ClassReader(fis);
			ClassWriter classWriter=new ClassWriter(ClassWriter.COMPUTE_MAXS);// 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小  
			ClassAdapter classAdapter=new GeneralClassAdapter(classWriter,val);  
			classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);  
			byte[] classFile=classWriter.toByteArray();  
			FileOutputStream fos=new FileOutputStream(file);  
			fos.write(classFile);  
			fos.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public static String getActivated(String snId){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://192.168.10.120:3306/prodata?useUnicode=true&characterEncoding=utf-8";
			conn=DriverManager.getConnection(url,"root","root");
			PreparedStatement pst=conn.prepareStatement("select passphrase from sn where snId=?");
			pst.setString(1,snId);
			ResultSet rs=pst.executeQuery();
			String activated=null;
			if(rs.next()){
				activated=rs.getString("passphrase");
			}
			return activated;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}

