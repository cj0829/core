/**
 * Project Name:core
 * File Name:SqlTimeFormat.java
 * Package Name:org.csr.core.util
 * Date:Oct 30, 201411:11:50 AM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.format;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csr.core.exception.Exceptions;
import org.csr.core.util.ObjUtil;
import org.springframework.format.Formatter;

/**
 * ClassName:SqlTimeFormat.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     Oct 30, 201411:11:50 AM <br/>
 * @author   yjY <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class SqlTimeFormat implements Formatter<Time> {
	Pattern pattern = Pattern.compile("^([01]\\d|2[01234]):([0-5]\\d|60)$");  
	@Override
	public String print(Time time, Locale locale) {
		if (time==null) {
			return "";
		}
		return time.toString();
	}

	@Override
	public Time parse(String text, Locale locale) throws ParseException {
		if(ObjUtil.isBlank(text)) {  
            return null;  
        }  
        Matcher matcher = pattern.matcher(text.trim());  
        if(matcher.matches()) {
            //如果匹配 进行转换  
        	Date timeDate = null;
        	DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        	timeDate = timeFormat.parse(matcher.group());
        	Time time = new Time(timeDate.getTime());
            return time;  
        } else {  
            //如果不匹配 转换失败  
//            throw new IllegalArgumentException(String.format("类型转换失败，需要格式[00:00]，但格式是[%s]",text));  
        	Exceptions.service("", String.format("类型转换失败，需要格式[00:00]，但格式是[%s]",text));
        }
		return null;  
	}

}

