package org.csr.core.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.core.io.ClassPathResource;

/**
 * ClassName:PropertiesUtil.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:22:17 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class PropertiesUtil {
	private static final Map<String, String> exceptionMsgMap = new HashMap<String, String>();
	private static final Map<String, String> configureMap = new HashMap<String, String>();
	
	/**
	 * @description:通过一场代号得到异常信息
	 * @param: 
	 * @return: String 
	 */
	public static String getExceptionMsg(String code){
		String msg = getExceptionMsgMap().get(code);
		if(msg == null)
			msg = "操作失败";
		return msg;
	}
	
	
	/**
	 * @description:得到数据库配置文件信息
	 * @param: 
	 * @return: String 
	 */
	public static String getConfigureValue(String key){
		return getConfigureMap().get(key);
	}
	
	
	/**
	 * @description:得到异常信息map
	 * @param: 
	 * @return: Map<String,String> 
	 */
	private static Map<String, String> getExceptionMsgMap(){
		if(exceptionMsgMap.size() <= 0){
			ClassPathResource s=new ClassPathResource("/exceptionMsg.properties");
			Properties p =new Properties();
			try {
				p.load(s.getInputStream());
				Enumeration<Object> eKeys = p.keys();
				while (eKeys.hasMoreElements()) {
					Object key = eKeys.nextElement();
					String value = p.getProperty(key.toString());
					exceptionMsgMap.put(key.toString(), value);
				}
			} catch (IOException e1) {
				try {
				ResourceBundle rb=ResourceBundle.getBundle("exceptionMsg");
				Enumeration<String> e = rb.getKeys();
				while (e.hasMoreElements()) {
					String key = e.nextElement();
					String value = rb.getString(key);
					exceptionMsgMap.put(key, value);
				}
				} catch (Exception e) {e.printStackTrace();}
			}
		}
		return exceptionMsgMap;
	}
	
	/**
	 * getConfigureMap: 得到configure文件的键值，并缓存到内存中 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	private static Map<String, String> getConfigureMap(){
		if(configureMap.size() <= 0){
			ClassPathResource s=new ClassPathResource("/configure.properties");
			Properties p =new Properties();
			try {
				p.load(s.getInputStream());
				Enumeration<Object> eKeys = p.keys();
				while (eKeys.hasMoreElements()) {
					Object key = eKeys.nextElement();
					String value = p.getProperty(key.toString());
					configureMap.put(key.toString(), value);
				}
			} catch (IOException e1) {
				try {
					ResourceBundle rb=ResourceBundle.getBundle("configure");
					Enumeration<String>	eLocalKeys = rb.getKeys();
					while (eLocalKeys.hasMoreElements()) {
						String key = eLocalKeys.nextElement();
						String value = rb.getString(key);
						configureMap.put(key, value);
					}
				} catch (Exception e) {e.printStackTrace();}
			}
		}
		return configureMap;
	}
	/**
	 * setConfigureValue: 修改或增加configure.properties属性文件key-value <br/>
	 * @author yjY
	 * @param key
	 * @param value
	 * @throws IOException 
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void setConfigureValue(String key,String value) throws IOException{
		Properties props = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream("/configure.properties");
		props.load(in);
		OutputStream output = new FileOutputStream(URLDecoder.decode(PropertiesUtil.class.getResource("/configure.properties").getFile(),"utf-8"));
		props.setProperty(key, value);
		configureMap.put(key, value);
		props.store(output,"");
		output.close();
	}
}
