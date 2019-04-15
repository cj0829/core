package org.csr.core.security.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.core.io.ClassPathResource;

public class ValidationUtil {
	
	private static Map<String, String> validationMap = new HashMap<String, String>();
	/**
	 * @description:通过一场代号得到验证字段信息
	 * @param: 
	 * @return: String 
	 */
	public static String getValidationField(String code){
		validationMap = getValidationMap();
		String msg = validationMap.get(code);
		if(msg == null){
			msg = "";
		}
		return msg;
	}
	
	/**
	 * @description:得到验证字段信息map
	 * @param: 
	 * @return: Map<String,String> 
	 */
	private static Map<String, String> getValidationMap(){
		if(validationMap.size() <= 0){
			ClassPathResource s=new ClassPathResource("validation.properties");
			Properties p =new Properties();
			try {
				p.load(s.getInputStream());
				Enumeration<Object> eKeys = p.keys();
				while (eKeys.hasMoreElements()) {
					Object key = eKeys.nextElement();
					String value = p.getProperty(key.toString());
					validationMap.put(key.toString(), value);
				}
			} catch (IOException e1) {
				try {
					ResourceBundle rb=ResourceBundle.getBundle("validation");
					Enumeration<String> e = rb.getKeys();
					while (e.hasMoreElements()) {
						String key = e.nextElement();
						String value = rb.getString(key);
						validationMap.put(key, value);
					}
				}catch(Exception fe){
					
				}
			}
		}
		return validationMap;
	}
}
