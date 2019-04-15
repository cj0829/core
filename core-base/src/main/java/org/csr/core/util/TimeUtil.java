/**
 * Project Name:training
 * File Name:TimeUtils.java
 * Package Name:org.csr.learning.support
 * Date:Oct 13, 201410:21:35 AM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.csr.core.exception.Exceptions;

/**
 * ClassName: TimeUtils.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: Oct 13, 201410:21:35 AM <br/>
 * 
 * @author yjY <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class TimeUtil {
	public final static String TIME_FORMAT_STRING = "HH:mm";
	private static String SHORTSYSDEF;
	private static Map<Locale, Map<String, SimpleDateFormat>> FORMATES = new HashMap<Locale, Map<String, SimpleDateFormat>>();

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param timeString
	 *            需要转换的字符串
	 * @param formatString
	 *            需要格式的目标字符串
	 * @return Time 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Time StringToTime(String timeString, String formatString) {
		String timeStr = timeString;
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		Date date = null;
		try {
			date = format.parse(timeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Time time = new Time(date.getTime());
		return time;
	}

	/**
	 * calcTimeDifference: 计算时间差 <br/>
	 * 
	 * @author yjY
	 * @param startTimeString
	 * @param endTimeString
	 * @param formatString
	 * @return
	 * @since JDK 1.7
	 */
	public static Long calcTimeDifference(String startTimeString,
			String endTimeString, String formatString) {
		String stimeStr = startTimeString;
		String etimeStr = endTimeString;
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		Date dates = null;
		Date datee = null;
		try {
			dates = format.parse(stimeStr);
			datee = format.parse(etimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long difference = datee.getTime() - dates.getTime();
		long minutes = difference / (60 * 1000);
		return minutes;
	}

	public static Long calcDateDifference(Date startDate, Date endDate) {
		if (ObjUtil.isEmpty(startDate)) {
			Exceptions.service("", "开始日期不能为空");
		}
		if (ObjUtil.isEmpty(endDate)) {
			Exceptions.service("", "结束日期不能为空");
		}
		Calendar calendar = Calendar.getInstance();
		// 开始日期天数
		calendar.setTimeInMillis(startDate.getTime());
		long before = calendar.getTimeInMillis() / (24 * 60 * 60 * 1000);
		// 结束日期天数
		calendar.setTimeInMillis(endDate.getTime());
		long after = calendar.getTimeInMillis() / (24 * 60 * 60 * 1000);
		// 相差天数
		return after - before + 1;
	}

	/**
	 * calcTimeDifference: 时间先后的比较 <br/>
	 * 
	 * @author yjY
	 * @param startTimeString
	 * @param endTimeString
	 * @param formatString
	 * @return
	 * @since JDK 1.7
	 */
	public static int compare(String startTimeString, String endTimeString,
			String formatString) {
		String stimeStr = startTimeString;
		String etimeStr = endTimeString;
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		Date dates = null;
		Date datee = null;
		try {
			dates = format.parse(stimeStr);
			datee = format.parse(etimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Time sTime = new Time(dates.getTime());
		Time eTime = new Time(datee.getTime());
		if (sTime.equals(eTime)) {
			return 0;
		}
		return sTime.after(eTime) ? 1 : -1;
	}

	/**
	 * 返回开始时间到结束时间的时间段(数轴上)集合(以分钟算)
	 * 
	 * @author yjY
	 * @param startTime
	 * @param endTime
	 * @return
	 * @since JDK 1.7
	 */
	public static List<Long> toList(Time startTime, Time endTime) {
		long sM = toMinutes(startTime);
		long eM = toMinutes(endTime);
		long totalM = eM - sM;
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i <= totalM; i++) {
			list.add(sM++);
		}
		return list;
	}

	private static long toMinutes(Time time) {
		return time.getTime() / 6000;
	}

	/**
	 * 返回开始日期到结束日期的时间段(数轴上)集合(以天算)
	 * 
	 * @author yjY
	 * @param startDate
	 * @param endDate
	 * @return
	 * @since JDK 1.7
	 */
	public static List<Long> toList(Date startDate, Date endDate) {
		long sD = toDays(startDate);
		long eD = toDays(endDate);
		long totalD = eD - sD;
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i <= totalD; i++) {
			list.add(sD++);
		}
		return list;
	}

	private static long toDays(Date date) {
		return date.getTime() / (24 * 60 * 60 * 1000);
	}

	public static String formatLongDateTime(Date date, Locale locale) {
		return formatDateTime(date, locale, SHORTSYSDEF, false);
	}

	public static String formatShortDateTime(Date date, Locale locale) {
		return formatDateTime(date, locale, SHORTSYSDEF, false);
	}

	private static String formatDateTime(Date date, Locale locale,
			String format, boolean isLong) {
		if (date == null) {
			return null;
		}
		Map<String, SimpleDateFormat> formats = getDateFormats(locale);
		String def = isLong ? "LDEF" : "SDEF";
		String fmt = format == null ? def : format;
		SimpleDateFormat df = (SimpleDateFormat) formats.get(fmt);
		if (df == null) {
			df = (SimpleDateFormat) formats.get(def);
		}
		return df.format(date);
	}

	private static Map<String, SimpleDateFormat> getDateFormats(Locale locale) {
		Map<String, SimpleDateFormat> formats = FORMATES.get(locale);
		if (formats == null) {
			formats = new LinkedHashMap<String, SimpleDateFormat>();
			formats.put("SDEF", new SimpleDateFormat("d/M", locale));
			formats.put("LDEF",
					new SimpleDateFormat("yyyy-MM-dd HH:mm", locale));
			formats.put("MDEF", new SimpleDateFormat("d/M HH:mm", locale));
			formats.put("HHMM", new SimpleDateFormat("HH:mm", locale));
			formats.put("YYMMDD", new SimpleDateFormat("yy-MM-dd", locale));
			List<String> times = new ArrayList<String>();
			times.add("ah:mm");
			times.add("HH:mm:ss");
			times.add("ahh?mm??ss??");
			times.add("ahh?mm??ss?? z");
			List<String> dates = new ArrayList<String>();
			dates.add("yy-MM-dd");
			dates.add("yyyy-MM-dd");
			dates.add("yyyy??MM??dd??");
			dates.add("yyyy??MM??dd?? EEE");
			String[] symArray = "S,M,L,F".split(",");
			for (int i = 0; i < 4; i++) {
				formats.put("T0" + symArray[i] + "0", new SimpleDateFormat(
						(String) times.get(i), locale));
				formats.put("D0" + symArray[i] + "0", new SimpleDateFormat(
						(String) dates.get(i), locale));
			}
			for (int i = 0; i < 4; i++) {
				for (int k = 0; k < 4; k++) {
					formats.put("DT" + symArray[i] + symArray[k],
							new SimpleDateFormat((String) dates.get(i) + " "
									+ (String) times.get(k), locale));
				}
			}
			FORMATES.put(locale, formats);
		}
		return formats;
	}
}
