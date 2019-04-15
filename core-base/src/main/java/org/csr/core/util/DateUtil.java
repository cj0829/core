package org.csr.core.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * ClassName:DateUtil.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-28上午9:21:40 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： 时间工具类<br/>
 *        公用方法描述： <br/>
 */
public class DateUtil {
	private final static String Month_FORMAT = "yyyyMM";
	public final static String TIME_FORMAT_STRING = "HH:mm";
	public final static String TIME_STRING_SECONDS = "mm:ss";
	public final static String TIME_FORMAT_STRING_SECONDS = "HH:mm:ss";
	public final static String DTAE_TIME_FORMAT_12_ = "yyyy-MM-dd hh:mm";
	public final static String DTAE_TIME_FORMAT_24 = "yyyy-MM-dd HH:mm";
	public final static String DTAE_TIME_FORMAT_24_SECONDS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * @description:将时间转为字符串，并且格式为yyyy-MM-dd
	 * @param:
	 * @return: String
	 */
	public static String parseDate(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		} else {
			return null;
		}
	}
	
	public static Date formatDate(String dateStr,String format) {
		if (StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}

	public static Date formatDate(String dateStr) {
		if (StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(DTAE_TIME_FORMAT_24);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = dateFormat.parse(dateStr);
			} catch (ParseException e1) {
				dateFormat = new SimpleDateFormat("yyyyMMdd");
				try {
					date = dateFormat.parse(dateStr);
				} catch (ParseException e2) {
					e2.printStackTrace();
				}
			}
		}

		return date;
	}

	/**
	 * @description:将时间转为字符串，并且格式为yyyy-MM-dd HH:mm
	 * @param:
	 * @return: String
	 */
	public static String parseDateToString(Date date,String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return null;
		}
	}

	/**
	 * @description:将时间转为字符串，并且格式为yyyy-MM-dd HH:mm:ss
	 * @param:
	 * @return: String
	 */
	public static String parseDateTimeToSec(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DTAE_TIME_FORMAT_24);
			return sdf.format(date);
		} else {
			return null;
		}
	}

	
	
	/**
	 * @description：通过同步的方式给上传的文件重新获取名称。以时间解析得到.用于文件名的前缀名
	 * @param
	 * @return：String
	 * @方法编号：
	 */
	public synchronized static String getTimeForFileName() {
		try {
			Thread.sleep(1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(date);

	}

	/**
	 * @description：生成年月（201011）
	 * @param
	 * @return：String
	 */
	public static String getCurrYearMonth(int imonth) {
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		if (imonth != 0)
			c.add(Calendar.MONTH, imonth);
		SimpleDateFormat formatter = new SimpleDateFormat(Month_FORMAT);
		return formatter.format((c.getTime()));

	}

	/**
	 * @description：生成年月（201011）
	 * @param
	 * @return：String
	 */
	public static String getCurrYearMonth() {
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat(Month_FORMAT);
		return formatter.format((c.getTime()));

	}

	/**
	 * description:生成年
	 * 
	 * @return String
	 * @author MaoJF Jul 27, 2011
	 */
	public static String getCurrYear() {
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format((c.getTime()));

	}

	/**
	 * description:生成月
	 * 
	 * @return String
	 * @author MaoJF Jul 27, 2011
	 */
	public static String getCurrMoonth() {
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format((c.getTime()));

	}

	/**
	 * @description:字符串转为时间
	 * @param:
	 * @return: Date
	 */
	public static Date parseDateTimeToMin(String str) {
		if (str != null && !"".equals(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d = null;
			try {
				d = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}

	/**
	 * @description:字符串转为时间
	 * @param:
	 * @return: Date
	 */
	public static Date parseDate(String str, String dataFormat) {
		if (str != null && !"".equals(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
			Date d = null;
			try {
				d = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}
	
	
	/**
	 * @description:字符串转为时间
	 * @param:
	 * @return: Date
	 */
	public static Date parseDate(Date date, String dataFormat) {
		if (ObjUtil.isNotEmpty(date)) {
			
			String parseDateToString = parseDateToString(date, dataFormat);
			if(ObjUtil.isBlank(parseDateToString)){
				return null;
			}
			return formatDate(parseDateToString, dataFormat);
		} else {
			return null;
		}
	}

	/**
	 * @description:字符串转为时间
	 * @param:
	 * @return: Date
	 */
	public static Date parseDate(String str) {
		if (str != null && !"".equals(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = null;
			try {
				d = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}

	/**
	 * @description:日期加天数得到新的日期<br> 根据日历的规则，为给定的日历字段添加或减去指定的时间量。<br>
	 *                              例如，要从当前日历时间减去 5 天，可以通过调用以下方法做到这一点
	 *                              add(Calendar.DAY_OF_MONTH, -5)。
	 * @param:
	 * @return: Date
	 */
	public static Date addDate(Date d, Integer day) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, day);
			Date date = cal.getTime();
			return date;
		} else {
			return null;
		}
	}
	
	/**
	 * @return: month
	 */
	public static Date addMonth(Date d, Integer month) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MONTH, month);
			Date date = cal.getTime();
			return date;
		} else {
			return null;
		}
	}

	/**
	 * @return: year
	 */
	public static Date addYear(Date d, Integer year) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.YEAR, year);
			Date date = cal.getTime();
			return date;
		} else {
			return null;
		}
	}

	/**
	 * @description: 日期加分钟得到新的日期
	 * @param:
	 * @return: Date
	 * @author LiuHui 2011-2-22
	 * @方法编号：
	 */
	public static Date addMinutes(Date d, Integer minu) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, minu);
			Date date = cal.getTime();
			return date;
		} else {
			return null;
		}
	}

	/**
	 * @description:将时间转为字符串，并且格式为yyyy/MM/dd HH:mm:ss
	 * @param:
	 * @return: String
	 */
	public static String parseDateTimeToStr(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return sdf.format(date);
		} else {
			return null;
		}
	}

	/**
	 * @description:将时间转为字符串，并且格式为dateFormat
	 * @param: sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	 * @return: String
	 */
	public static String parseDateFormat(Date date,String dateFormat) {
		if (ObjUtil.isEmpty(date)) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (date != null) {
			return sdf.format(date);
		} else {
			return sdf.format(new Date());
		}
	}

	public static Integer getDay(Date date) {
		if (ObjUtil.isEmpty(date)) {
			return null;
		}
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static Integer getMonth(Date date) {
		if (ObjUtil.isEmpty(date)) {
			return null;
		}
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static Integer getYear(Date date) {
		if (ObjUtil.isEmpty(date)) {
			return null;
		}
		java.util.Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param timeString
	 *            需要转换的字符串(HH:MM)
	 * @param formatString
	 *            需要格式的目标字符串(HH:MM)
	 * @return Time 返回转换后的时间(java.sql.Time)
	 * @throws ParseException
	 *             转换异常
	 */
	public static Time parseTimeStrToSqlTimeByFormat(String timeString,
			String formatString) {
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
	 * calcTimeDifference: 计算时间差(返回的Long以分钟为单位) <br/>
	 * 
	 * @author yjY
	 * @param startTimeString  (HH:MM)
	 * @param endTimeString  (HH:MM)
	 * @return
	 * @since JDK 1.7
	 */
	public static Long calcTimeDifference(Date dates, Date datee) {
		long difference = datee.getTime() - dates.getTime();
		long minutes = difference / (1000);
		return minutes;
	}

	/**
	 * calcTimeDifference: 计算时间差(返回的Long以分钟为单位) <br/>
	 * 
	 * @author yjY
	 * @param 大值 
	 * @param 小值  
	 * @return
	 * @since JDK 1.7
	 */
	public static Long calcTimeDifferenceSec(Date dates, Date datee) {
		long difference = datee.getTime() - dates.getTime();
		long minutes = difference;
		return minutes;
	}
	/**
	 * calcTimeDifference: 计算时间差(返回的Long以分钟为单位) <br/>
	 * 
	 * @author yjY
	 * @param 大值 
	 * @param 小值  
	 * @return
	 * @since JDK 1.7
	 */
	public static Long calcTimeDifferenceSec(Long dates, Long datee) {
		return dates - datee;
	}

	
	/**
	 * calcTimeDifference: 计算时间差(返回的Long以分钟为单位) <br/>
	 * 
	 * @author yjY
	 * @param startTimeString
	 *            (HH:MM)
	 * @param endTimeString
	 *            (HH:MM)
	 * @param formatString
	 *            (HH:MM)
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

	/**
	 * calcTimeDifference: 时间先后的比较(推荐用java.sql.Time自己的时间比较方法) <br/>
	 * 
	 * @author yjY
	 * @param startTimeString
	 *            (HH:MM)
	 * @param endTimeString
	 *            (HH:MM)
	 * @param formatString
	 *            (HH:MM)
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
	 * calcTimeDifference: 时间先后的比较(推荐用java.sql.Time自己的时间比较方法) <br/>
	 * 
	 * @author yjY
	 * @param startTimeString
	 *            (HH:MM)
	 * @param endTimeString
	 *            (HH:MM)
	 * @param formatString
	 *            (HH:MM)
	 * @return
	 * @since JDK 1.7
	 */
	public static int compare(Date startTime, Date endTime, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		String startTimeString = format.format(startTime);
		String endTimeString = format.format(endTime);
		return compare(startTimeString, endTimeString, formatString);
	}

	/**
	 * getFirstDateFromYear:获取系统时间年份第一天的日期<br/>
	 * 
	 * @author yjY
	 * @return
	 * @since JDK 1.7
	 */
	public static Date getFirstDateFromYear() {
		Calendar firstDay = Calendar.getInstance();
		firstDay.set(Calendar.DAY_OF_YEAR, 1);
		firstDay.set(Calendar.HOUR_OF_DAY, 0);
		firstDay.set(Calendar.MINUTE, 0);
		firstDay.set(Calendar.SECOND, 0);
		return firstDay.getTime();
	}

	/**
	 * getLastDateFromYear:获取系统时间年份最后一天的日期<br/>
	 * 
	 * @author yjY
	 * @return
	 * @since JDK 1.7
	 */
	public static Date getLastDateFromYear() {
		Calendar lastDay = Calendar.getInstance();
		lastDay.add(Calendar.YEAR, 1);
		lastDay.set(Calendar.DAY_OF_YEAR, 1);
		lastDay.add(Calendar.DAY_OF_YEAR, -1);
		lastDay.set(Calendar.HOUR_OF_DAY, 0);
		lastDay.set(Calendar.MINUTE, 0);
		lastDay.set(Calendar.SECOND, 0);
		return lastDay.getTime();
	}
	  public static Date getEndDate(Date endDate) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(endDate);
	        calendar.set(Calendar.HOUR_OF_DAY, 23);
	        calendar.set(Calendar.MINUTE, 59);
	        calendar.set(Calendar.SECOND, 59);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();
	    }
	/**
	 * 计算需要提交多少日报 dateBeteen: 描述方法的作用 <br/>
	 * 
	 * @author caijin
	 * @param startDate
	 * @param endDate
	 * @return
	 * @since JDK 1.7
	 */
	public static int dateBeteen(Date startDate, Date endDate) {
		double dayBeteen = 0;
		dayBeteen = endDate.getTime() - startDate.getTime();
		if (dayBeteen < 0) {
			return 0;
		}
		int day = 0;
		double daybean = dayBeteen / (1000 * 60 * 60 * 24);
		day = dayBeteen % (1000 * 60 * 60 * 24) == 0 ? (int) daybean
				: (int) daybean + 1;
		return day;
	}

	public static String getInterval(Date createtime) { // 传入的时间格式必须类似于2012-8-21
														// 17:53:20这样的格式
		String interval = null;
		// 用现在距离1970年的时间间隔new
		// Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
		long time = new Date().getTime() - createtime.getTime();// 得出的时间间隔是毫秒

		if (time / 1000 < 10 && time / 1000 >= 0) {
			// 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
			interval = "刚刚";
		} else if (time / 1000 < 60 && time / 1000 > 0) {
			// 如果时间间隔小于60秒则显示多少秒前
			int se = (int) ((time % 60000) / 1000);
			interval = se + "秒前";

		} else if (time / 60000 < 60 && time / 60000 > 0) {
			// 如果时间间隔小于60分钟则显示多少分钟前
			int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
			interval = m + "分钟前";

		} else if (time / 3600000 < 24 && time / 3600000 >= 0) {
			// 如果时间间隔小于24小时则显示多少小时前
			int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
			interval = h + "小时前";
		} else {
			// 大于24小时，则显示正常的时间，但是不显示秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			interval = sdf.format(createtime);
		}
		return interval;
	}
	public static boolean eqNotMilliSecond(Date firstDate, Date secondDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        calendar.set(Calendar.MILLISECOND, 0);
        long lfirst = calendar.getTimeInMillis();

        calendar.setTime(secondDate);
        calendar.set(Calendar.MILLISECOND, 0);
        long lsecond = calendar.getTimeInMillis();

        return lfirst == lsecond;
    }
	
	
	
	
	
	/**
	  * 将长整型数字转换为日期格式的字符串
	  * 
	  * @param time
	  * @param format
	  * @return
	  */
	public static String convert2String(Long time, String format) {
		if (time!=null &&time > 0l) {
			if (StringUtils.isBlank(format)){
				format = DTAE_TIME_FORMAT_24;
			}
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}
	/**
	 *  时间到毫秒，如果需要到秒，自己转 /1000
	  * 将字符串格式，转换为Long型
	  * 
	  * @param time
	  * @param format
	  * @return
	  */
	public static Long convert2Long(String time, String format) {
		if(ObjUtil.isNotBlank(time)){
			if (StringUtils.isBlank(format)){
				format = DTAE_TIME_FORMAT_24;
			}
			Date date = parseDate(time, format);
			if(ObjUtil.isEmpty(date)){
				return null;
			}
			return date.getTime();
		}
		return null;
	}
	
	public static String formatSecond(Long second){
        String  html="0秒";  
        if(second!=null){  
            Double s=second.doubleValue();  
            String format;  
            Object[] array;  
            Integer hours =(int) (s/(60*60));  
            Integer minutes = (int) (s/60-hours*60);  
            Integer seconds = (int) (s-minutes*60-hours*60*60);  
            if(hours>0){  
                format="%1$,d时%2$,d分%3$,d秒";  
                array=new Object[]{hours,minutes,seconds};  
            }else if(minutes>0){  
                format="%1$,d分%2$,d秒";  
                array=new Object[]{minutes,seconds};  
            }else{  
                format="%1$,d秒";  
                array=new Object[]{seconds};  
            }  
            html= String.format(format, array);  
        }  
         
       return html;  
         
   }  
	
	public static void main(String[] args) {
		System.out.println(getYear(new Date()));
		System.out.println(getMonth(new Date()));
		System.out.println(getDay(new Date()));
		
	Date parseDate = DateUtil.parseDate("2017-1-2", "yyyy-MM-dd");
		
		System.out.println(parseDate);
	}
	public static long getGapDay(long start, long end) {
	     return (end - start) / 86400000L;
		   }
}
