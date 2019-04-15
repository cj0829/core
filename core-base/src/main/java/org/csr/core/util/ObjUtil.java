package org.csr.core.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.csr.core.util.support.CompareValue;
import org.csr.core.util.support.ToValue;

/**
 * 
 * ClassName:ObjUtil.java <br/>
 * System Name：  <br/>
 * Date: 2014年9月12日下午7:53:38 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public abstract class ObjUtil {
	/**
	 * 判断对象是否为null：为null返回true；
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		return value == null;
	}

	/**
	 * @param create
	 * @return
	 */
	public static boolean isNotEmpty(Object value) {
		return value != null;
	}
	
	public static boolean isSet(Object value) {
		return value != null && !value.equals("");
	}
	
	public static boolean isSet(Object value, Object compare) {
		return value != null && value.equals(compare);
	}
	/**
	 * @param create
	 * @return
	 */
	public static boolean isNotBlank(String value) {
		return StringUtils.isNotBlank(value);
	}

	/**
	 * @param create
	 * @return
	 */
	public static boolean isBlank(String value) {
		return StringUtils.isBlank(value);
	}

	/**
	 * 判断对象是否为null：为null返回true；
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection collection) {
		return ((collection == null) || collection.isEmpty());
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection collection) {
		return ((collection != null) && !(collection.isEmpty()));
	}

	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	public static boolean isNotEmpty(Object[] array) {
		return ((array != null) && (array.length > 0));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Map map) {
		return (map != null && !map.isEmpty());
	}

	/**
	 * stringIsNumber: 判断字符串是不是数字：为数字为：true <br/>
	 * 
	 * @author caijin
	 * @param sourceString
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean stringIsNumber(String sourceString) {
		return sourceString.matches("^\\d+$");
	}

	/**
	 * longNotNull: 判断字符是否大于等于0 <br/>
	 * 
	 * @author caijin
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean longNotNull(Long num) {
		return null != num && num >= 0;
	}

	/**
	 * longNotNull: 判断字符是否大于等于0 <br/>
	 * 
	 * @author caijin
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean longGTzero(Long num) {
		return null != num && num > 0;
	}
	/**
	 * longIsNull: 判断字符是否小于0 <br/>
	 * 
	 * @author caijin
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean longIsNull(Long num) {
		return !longNotNull(num);
	}

	/**
	 * toInt: 把对象强制转化为int基本类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static int toInt(Object value) {
		try {
			if (isNotEmpty(value) && StringUtils.isNotEmpty(value.toString())) {
				return Integer.parseInt(value.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 *  toInt: 把对象强制转化为Integer对象类型。如果转化异常，返回null <br/>
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static Integer toInteger(Object value){
		try {
			if (isNotEmpty(value) && StringUtils.isNotEmpty(value.toString())) {
				return Integer.parseInt(value.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 *  toCharacter: 把对象强制转化为Character对象类型。如果转化异常，返回null <br/>
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static Character toCharacter(Object value){
		try {
			if (isNotEmpty(value) && StringUtils.isNotEmpty(value.toString())) {
				return value.toString().charAt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 *  toBoolean: 把对象强制转化为Boolean对象类型。如果转化异常，返回null <br/>
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static Boolean toBoolean(Object value){
		try {
			if (isNotEmpty(value) && StringUtils.isNotEmpty(value.toString())) {
				if("true".equals(value.toString().trim())){
					return true;
				}else if("false".equals(value.toString().trim())){
					return false;
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * toLong: 把对象强制转化为long基本类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static long tolong(Object value) {
		try {
			try {
				if (isNotEmpty(value)
						&& StringUtils.isNotEmpty(value.toString())) {
					return Long.parseLong(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 把对象转换成Long对象整型，如果对象转换失败，返回null
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static Long toLong(Object value) {
		try {
			try {
				if (isNotEmpty(value)
						&& StringUtils.isNotEmpty(value.toString())) {
					return Long.parseLong(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * toFloat: 把对象强制转化为Float类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static float toFloat(Object value) {
		try {
			try {
				if (isNotEmpty(value)
						&& StringUtils.isNotEmpty(value.toString())) {
					return Float.parseFloat(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * toDouble: 把对象强制转化为Double类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static double toDouble(Object value) {
		try {
			try {
				if (isNotEmpty(value)
						&& StringUtils.isNotEmpty(value.toString())) {
					return Double.parseDouble(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * toDouble: 把对象强制转化为Double类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static Byte toByte(Object value) {
		try {
			try {
				if (isNotEmpty(value) && StringUtils.isNotEmpty(value.toString())) {
					return Byte.parseByte(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * removeNull:移除<br/>
	 * @author yjY
	 * @param arrays
	 * @return
	 * @since JDK 1.7
	 */
	public static Long[] removeNull(Long[] arrays) {
		if (ObjUtil.isNotEmpty(arrays)) {
			List<Long> list = new ArrayList<Long>(Arrays.asList(arrays));
			Iterator<Long> iter = list.iterator();
			while (iter.hasNext()){
				if(ObjUtil.isEmpty(iter.next())){
					iter.remove();
				}
			}
			return list.toArray(new Long[list.size()]);
		}
		return arrays;
	}
	/**
	 * removeDuplicate: 去除重复<br/>
	 * 
	 * @author caijin
	 * @param functionPointIds
	 * @return
	 * @since JDK 1.7
	 */
	public static Long[] removeDuplicate(Long[] arrays) {
		if (ObjUtil.isNotEmpty(arrays)) {
			Set<Long> set = new TreeSet<Long>();
			for (Long id : arrays) {
				if (ObjUtil.isNotEmpty(id)) {
					set.add(id);
				}
			}
			return set.toArray(new Long[set.size()]);
		}
		return arrays;
	}

	/**
	 * verification: 验证字符串是否为以,为分隔的数字字符串 <br/>
	 * @author caijin
	 * @param str
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean verificationIds(String str) {
		Pattern pattern = Pattern.compile("^(\\d+,)*\\d+$");
		Matcher matcher = pattern.matcher(str.trim());
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * removeDuplicate: 去除重复<br/>
	 * [1,3,5,1,4,4] [1,3,5,4]
	 * @author caijin
	 * @param functionPointIds
	 * @return
	 * @since JDK 1.7
	 */
	public static Long[] removeDuplicate(Collection<Long> arrays) {
		if (ObjUtil.isNotEmpty(arrays)) {
			Set<Long> set = new TreeSet<Long>();
			for (Long id : arrays) {
				if (ObjUtil.isNotEmpty(id)) {
					set.add(id);
				}
			}
			return set.toArray(new Long[set.size()]);
		}
		return new Long[0];
	}
	
	/**
	 * removeDuplicate: 删除相同的内容<br/>
	 * [1,3,5,1,4,4] [1,3] to [5,1,4,4]
	 * @author caijin
	 * @param functionPointIds
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> Collection<T> removeCollectionSame(Collection<T> arrays,Collection<T> dels) {
		if (ObjUtil.isNotEmpty(arrays) && ObjUtil.isNotEmpty(dels)) {
			Collection<T> newArray = new ArrayList<T>();
			for (T id : arrays) {
				boolean isAdd=true;
				for (T delId : dels) {
					if(id.equals(delId)){
						isAdd=false;
						break;
					}
				}
				if(isAdd){
					newArray.add(id);
				}
			}
			return newArray;
		}
		return arrays;
	}
	/**
	 * removeDuplicate: 删除相同的内容<br/>
	 * @author caijin
	 * @param functionPointIds
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> Collection<T> removeArraySame(T[] arrays,T[] dels) {
		if (ObjUtil.isNotEmpty(arrays)) {
			Collection<T> newArray = new ArrayList<T>();
			for (T id : arrays) {
				boolean isAdd=true;
				if(ObjUtil.isNotEmpty(dels)){
					for (T delId : dels) {
						if(id.equals(delId)){
							isAdd=false;
						}
					}
				}
				if(isAdd){
					newArray.add(id);
				}
			}
			return newArray;
		}
		return new ArrayList<T>(0);
	}
	
	
	/**
	 * toString: 把对象强制转化为String类型 <br/>
	 * @author caijin
	 * @param pId
	 * @return
	 * @since JDK 1.7
	 */
	public static String toString(Object pId) {
		if (isEmpty(pId)) {
			return "";
		}
		return pId.toString();
	}

	
	
	/**
	 * concat: 合并两个数组 <br/>
	 * @author caijin
	 * @param first
	 * @param second
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
		    
	
	/**
	 * concat: list的比较 <br/>
	 * @author caijin
	 * @param first 已经存在的数据（数据库里的数据）
	 * @param second 修改的数据（有前台提交的要修改的数据）
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable> void compareId(List<ID> first, List<ID> second,CompareValue<ID> compareValue) {
		List<ID> add = new ArrayList<ID>();
		List<ID> update = new ArrayList<ID>();
		List<ID> delete = new ArrayList<ID>();
		if(ObjUtil.isNotEmpty(second)){
			for(ID secondId : second){
				if(first.contains(secondId)){
					update.add(secondId);
					first.remove(secondId);
				}else{
					add.add(secondId);
				}
			}
			
		}else{
			if(ObjUtil.isEmpty(first)){
				return;
			}
		}
		delete = first;
		if(ObjUtil.isNotEmpty(add)){
			compareValue.add(add);
		}
		if(ObjUtil.isNotEmpty(update)){
			compareValue.update(update);
		}
		if(ObjUtil.isNotEmpty(delete)){
			compareValue.delete(delete);
		}
	}
	
	/**
	 * concat: list的比较 <br/>
	 * @author caijin
	 * @param first
	 * @param second
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable, T> T compareObject(List<T> first,ToValue<T,ID> toValue) {
		for (T t : first) {
			if(ObjUtil.isNotEmpty(toValue.getValue(t))){
				return t;
			}
		}
		return null;
	}
	
	/**
	 * concatAll: 合并多个数组 <br/>
	 * @author caijin
	 * @param first
	 * @param rest
	 * @return
	 * @since JDK 1.7
	 */
	@SafeVarargs
	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	/**
	 * checkId: 比较当前字符串是否纯正，一个已经，分割的字符中 <br/>
	 * @author caijin
	 * @param id
	 * @param idStr
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean checkId(String id ,String idStr){
		String [] strId = idStr.split(",");
		for(int i=0;i<strId.length;i++){
			if(id.trim().equals(strId[i].trim()) )
			return true;			
		}
		return false;
	} 
	static final String target[] = { "&amp;", "&lt;", "&gt;", "&quot;" };

	static final String source[] = { "&", "<", ">", "\"" };


	public static String replace(String source, String tobereplace,String usetoreplace) {
		if (source == null)
			return null;
		return replace(new StringBuffer(source), tobereplace, usetoreplace);
	}

	public static String replace(StringBuffer sourceBuffer, String tobereplace,
			String usetoreplace) {
		if (tobereplace == null)
			return sourceBuffer.toString();
		if (usetoreplace == null)
			return sourceBuffer.toString();
		if (tobereplace.equals(""))
			return sourceBuffer.toString();

		StringBuffer newStr = new StringBuffer();
		String source = sourceBuffer.toString();
		int find = 0;
		int pos = 0;
		int replaceLength = tobereplace.length();
		do {
			find = source.indexOf(tobereplace, pos);
			if (find == -1)
				return newStr.append(sourceBuffer.substring(pos)).toString();
			newStr.append(sourceBuffer.substring(pos, find));
			newStr.append(usetoreplace);
			pos = find + replaceLength;
		} while (true);
	}

	public static String HtmlDecode(String str) {
		for (int i = 0; i < source.length; i++) {
			str = replace(str, target[i], source[i]);
		}
		return str;
	}

	public static String HtmlEncode(String str) {
		for (int i = 0; i < source.length; i++) {
			str = replace(str, source[i], target[i]);
		}
		return str;
	}


	public static boolean isString(Object obj) {
		if (isNotEmpty(obj)) {
			return obj instanceof String;
		}
		return false;
	}

	public static Object[] copyArray(Collection<?> data) {
		if (isEmpty(data)) {
			return new Object[0];
		}
		Object[] array = new Object[data.size()];
		System.arraycopy(data.toArray(new Object[data.size()]), 0, array, 0,
				data.size());
		return array;
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public static String toInts(List<Integer> ids){
		if(ObjUtil.isEmpty(ids)){
			return "''";
		}
		StringBuilder idStr=new StringBuilder("");
		boolean state=true;
		for(Integer id:ids){
			if(state){
				idStr.append(id.toString());
				state=false;
			}else{
				idStr.append(",").append(id.toString());
			}
		}
		return idStr.toString();
	}
	
	
	
	/**
	 * @param ids
	 * @return
	 */
	public static String toInts(Integer[] ids){
		if(ObjUtil.isEmpty(ids)){
			return "''";
		}
		StringBuilder idStr=new StringBuilder("");
		boolean state=true;
		for(Integer id:ids){
			if(state){
				idStr.append(id.toString());
				state=false;
			}else{
				idStr.append(",").append(id.toString());
			}
		}
		return idStr.toString();
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public static String toLongs(List<Long> ids){
		if(ObjUtil.isEmpty(ids)){
			return "''";
		}
		StringBuilder idStr=new StringBuilder("");
		boolean state=true;
		for(Long id:ids){
			if(state){
				idStr.append(id.toString());
				state=false;
			}else{
				idStr.append(",").append(id.toString());
			}
		}
		return idStr.toString();
	}
	
	
	
	/**
	 * @param ids
	 * @return
	 */
	public static String toLongs(Long[] ids){
		if(ObjUtil.isEmpty(ids)){
			return "''";
		}
		StringBuilder idStr=new StringBuilder("");
		boolean state=true;
		for(Long id:ids){
			if(state){
				idStr.append(id.toString());
				state=false;
			}else{
				idStr.append(",").append(id.toString());
			}
		}
		return idStr.toString();
	}
	/**
	 * 将一行字符串的"."转为"_"返回
	 * @param key
	 * @return
	 */
	public static String underlineSeparated(String key){
		if(ObjUtil.isNotBlank(key)){
			if(key.contains(".")){
				return key.replace(".", "_");
			}
		}
		return key;
	}
	
	
	/**
	 * 把数组转换成List
	 * @author caijin
	 * @param array
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> List<T> asList(T[] array){
		if(ObjUtil.isEmpty(array)){
			return new ArrayList<T>(0);
		}else{
			return Arrays.asList(array);
		}
	}
	
	/**
	 * 获取一个新的List，并且把值存入list中
	 * @author caijin
	 * @param objs
	 * @return
	 * @since JDK 1.7
	 */
	@SafeVarargs
	public static <T> List<T>  toList(T... objs){
		if(ObjUtil.isEmpty(objs)){
			return new ArrayList<T>(0);
		}
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < objs.length; i++) {
			if(ObjUtil.isNotEmpty(objs[i])){
				list.add(objs[i]);
			}
		}
		return list;
	}
	public static <T> Set<T> toSet(List<T> objs) {
		if(ObjUtil.isEmpty(objs)){
			return new HashSet<T>(0);
		}
		Set<T> set = new HashSet<T>();
		for (int i = 0; i < objs.size(); i++) {
			if(ObjUtil.isNotEmpty(objs.get(i))){
				set.add(objs.get(i));
			}
		}
		return set;
	}
	@SafeVarargs
	public static <T> Set<T> toSet(T... objs) {
		if(ObjUtil.isEmpty(objs)){
			return new HashSet<T>(0);
		}
		Set<T> set = new HashSet<T>();
		for (int i = 0; i < objs.length; i++) {
			if(ObjUtil.isNotEmpty(objs[i])){
				set.add(objs[i]);
			}
		}
		return set;
	}
	
	/**
	 * toDoubleScale: double类型的精度转换
	 * @author huayj
	 * @param value:待转换的值
	 * @param newScale:精度
	 * @param roundingMode:模式(BigDecimal的属性)
	 * @return
	 * @since JDK 1.7
	 */
	public static double toDoubleScale(double value,int newScale, int roundingMode) {
		if(value==0){
			return value;
		}
		if(newScale<0){
			return value;
		}
		return new  BigDecimal(value).setScale(newScale,roundingMode).doubleValue();    
	}
	public static double toDoubleScale(double value,int newScale) {
		if(value==0){
			return value;
		}
		if(newScale<0){
			return value;
		}
		return new  BigDecimal(value).setScale(newScale,BigDecimal.ROUND_HALF_UP).doubleValue();    
	}
	public static float toFloatScale(float value,int newScale, int roundingMode) {
		if(value==0){
			return value;
		}
		if(newScale<0){
			return value;
		}
		return new BigDecimal(value).setScale(newScale,roundingMode).floatValue();
	}
	
	public static float toFloatScale(float value,int newScale) {
		if(value==0){
			return value;
		}
		if(newScale<0){
			return value;
		}
		return new BigDecimal(value).setScale(newScale,BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	/**
	 * 随机抽取数组中的指定个数元素。<br/>
	 * 当集合数大于<随机个数(num)>，在集合中随机抽取出<随机个数(num)>的个数。<br/>
	 * 当<随机个数(num)>大于集合长度，则集合重新随机一下顺序。<br/>
	 * 如果想给集合重新排序，则只需要<随机个数(num)>等于集合长度。
	 * @author Administrator
	 * @param array
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> List<T> randomArray(List<T> array,int num) {
		if(ObjUtil.isEmpty(array)){
			return array;
		}
		ArrayList<T> dest = new ArrayList<T>();
		dest.addAll(array);
		List<T> newList = new ArrayList<T>();
		while (num-->0) {
			int ram = (int) Math.floor(Math.random()*dest.size());
			if(dest.size()==0){
				break;
			}
			if(ram==dest.size()){
				ram--;
			}
			if(ram<0){
				ram=0;
			}
			T t=dest.remove(ram);
			newList.add(t);
		}
		return newList;
	}
	public static String spaceSizeToString(long size) {
		if (size < 1024L) {
			return formatDouble(size, 1, 1) + "KB";
		}
		double mbSize = size / 1024.0D;
		if (mbSize < 1024.0D) {
			mbSize = Math.round(mbSize * 10.0D) / 10.0D;
			return formatDouble(mbSize, 1, 1) + "MB";
		}
		double gbSize = Math.round(mbSize / 102.40000000000001D) / 10.0D;
		return formatDouble(gbSize, 1, 1) + "GB";
	}

	public static String fileSizeToString(long size) {
		if (size < 1024L) {
			return String.valueOf(size) + "B";
		}
		double kbSize = size / 1024.0D;
		if (kbSize < 1024.0D) {
			kbSize = Math.round(kbSize * 10.0D) / 10.0D;
			return formatDouble(kbSize, 1, 1) + "KB";
		}
		double mbSize = kbSize / 1024.0D;
		if (mbSize < 1024.0D) {
			mbSize = Math.round(mbSize * 10.0D) / 10.0D;
			return formatDouble(mbSize, 1, 1) + "MB";
		}
		double gbSize = Math.round(mbSize / 102.40000000000001D) / 10.0D;
		return formatDouble(gbSize, 1, 1) + "GB";
	}
	public static String formatDouble(double d, int maxFractionDigits,int minFractionDigits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(maxFractionDigits);
		nf.setMinimumFractionDigits(minFractionDigits);
		return nf.format(d);
	}
	
}







