package org.csr.core.persistence.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.csr.core.Persistable;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.persistence.PageRequest;
import org.csr.core.persistence.service.SetBean;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.support.ToString;
import org.csr.core.util.support.ToValue;

public abstract class PersistableUtil {
	
	
	/**
	 * arrayTransforObject: 拼接制定对象属性，转化为Collection <br/>
	 * @author caijin
	 * @param paged
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable, T> Collection<ID> arrayTransforCollection(Collection<T> array, ToValue<T, ID> value) {
		if (ObjUtil.isEmpty(array)) {
			return new ArrayList<>(0);
		}
		Map<ID, ID> set = new LinkedHashMap<ID, ID>();
		for (T item:array) {
			ID id = value.getValue(item);
			if (ObjUtil.isNotEmpty(id)) {
				set.put(id, id);
			}
		}
		return set.values();
	}
	
	/**
	 * toLong: 把对象强制转化为Long类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static <T extends Persistable<ID>, ID extends Serializable> Map<ID, T> toMap(
			List<T> list) {
		return toMap(list, new ToValue<T, ID>() {
			@Override
			public ID getValue(T obj) {
				return obj.getId();
			}
		});
	}
	
	/**
	 * toPagedInfoBeans: 把domain 对象 转换为vo 对象<br/>
	 * @author caijin
	 * @param paged
	 * @param setBean
	 * @return
	 * @since JDK 1.7
	 */
	@SuppressWarnings("rawtypes")
	public static <B extends Persistable,P> List<B> toListBeans(List<P> paged,SetBean<P> setBean) {
		List<B> items=new ArrayList<B>();
		if(ObjUtil.isNotEmpty(paged)){
			Iterator<P> itpaged=paged.iterator();
			 while (itpaged.hasNext()) {
				P type =  itpaged.next();
				@SuppressWarnings("unchecked")
				B value =  (B) setBean.setValue(type);
				if(ObjUtil.isNotEmpty(value)){
					items.add(value);
				}
			}
		}
		return items;
	}
	
	
	/**
	 * toPagedInfoBeans: 把domain 对象 转换为vo 对象<br/>
	 * @author caijin
	 * @param paged
	 * @param setBean
	 * @return
	 * @since JDK 1.7
	 */
	@SuppressWarnings("rawtypes")
	public static <B extends Persistable,P> PagedInfo<B> toPagedInfoBeans(PagedInfo<P> paged,SetBean<P> setBean) {
		List<B> items=new ArrayList<B>();
		if(paged.hasRows()){
			Iterator<P> itpaged=paged.iterator();
			 while (itpaged.hasNext()) {
				P type =  itpaged.next();
				@SuppressWarnings("unchecked")
				B value =  (B) setBean.setValue(type);
				if(ObjUtil.isNotEmpty(value)){
					items.add(value);
				}
			}
		}
		return createPagedInfo(paged.getTotal(), new PageRequest(paged.getPageNumber(), paged.getPageSize()), items);
	}
	
	/**
	 * 创建一个分页对象
	 * @param paramValues
	 * @return
	 */
	public static <O> PagedInfo<O> createPagedInfo(long total,Page page,List<O> items){
		return new PagedInfoImpl<O>(items, page, total);
	}
	
	/**
	 * arrayTransforList: <br/>
	 * @author caijin
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <T extends Persistable<ID>, ID extends Serializable> List<ID> arrayTransforList(List<T> idArr) {
		if (ObjUtil.isEmpty(idArr)) {
			return new ArrayList<ID>(0);
		}
		List<ID> list = new ArrayList<ID>();
		for (Persistable<ID> obj : idArr) {
			list.add(obj.getId());
		}
		return list;
	}
	
	/**
	 * arrayTransforObject: 拼接制定对象属性，转化为Collection <br/>
	 * @author caijin
	 * @param paged
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable, T> List<ID> arrayTransforList(PagedInfo<T> paged, ToValue<T, ID> value) {
		if (!paged.hasRows()) {
			return new ArrayList<>(0);
		}
		Set<ID> set = new LinkedHashSet<ID>();
		List<T> list = paged.getRows();
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			ID id = value.getValue(t);
			if (ObjUtil.isNotEmpty(id)) {
				set.add(id);
			}
		}
		return new ArrayList<>(set);
	}
	
	/**
	 * toLong: 把对象强制转化为Long类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static <T, ID extends Serializable> Map<ID,T> toMap(List<T> list,ToValue<T, ID> toValue) {
		Map<ID,T> maps=new LinkedHashMap<>();
		try {
			if (ObjUtil.isNotEmpty(toValue)&&ObjUtil.isNotEmpty(list)) {
				for (T t : list) {
					maps.put(toValue.getValue(t), t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	
	
	/**
	 * toLong: 把对象强制转化为Long类型。如果转化异常，返回0 <br/>
	 * 
	 * @author caijin
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static <T, ID extends Serializable> Map<ID, List<T>> toMapList(List<T> list, ToValue<T, ID> toValue) {
		Map<ID, List<T>> maps = new LinkedHashMap<ID, List<T>>();
		if (ObjUtil.isEmpty(list)) {
			return maps;
		}
		try {
			if (ObjUtil.isNotEmpty(toValue)) {
				for (T t : list) {
					ID id = toValue.getValue(t);
					List<T> newList = maps.get(id);
					if (ObjUtil.isEmpty(newList)) {
						newList = new ArrayList<T>();
						maps.put(id, newList);
					}
					newList.add(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	/**
	 * arrayTransforString: 把对象数组转化为字符数组 <br/>
	 * @author caijin
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static String arrayTransforString(Object[] idArr) {
		String arrStr = "";
		if (ObjUtil.isEmpty(idArr)) {
			return arrStr;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (int i = 0; i < idArr.length; i++) {
			String id = ObjUtil.toString(idArr[i]);
			if (ObjUtil.isNotBlank(id)) {
				set.add(id);
			}
		}
		boolean b=true;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			if (b) {
				arrStr += iterator.next();
				b=false;
			} else {
				arrStr += "," + iterator.next();
			}
		}
		return arrStr;
	}
	
	/**
	 * arrayTransforString: 把对象数组转化为字符数组 <br/>
	 * @author caijin
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> String arrayTransforString(List<T> idArr) {
		String arrStr = "";
		if (ObjUtil.isEmpty(idArr)) {
			return arrStr;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (int i = 0; i < idArr.size(); i++) {
			String id = ObjUtil.toString(idArr.get(i));
			if (ObjUtil.isNotBlank(id)) {
				set.add(id);
			}
		}
		boolean b=true;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			if (b) {
				arrStr += iterator.next();
				b=false;
			} else {
				arrStr += "," + iterator.next();
			}
		}
		return arrStr;
	}
	

	/**
	 * arrayTransforString: 描述方法的作用 <br/>
	 * @author caijin
	 * @param idArr
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> String arrayTransforString(T[] idArr,ToString<T> value) {
		String arrStr = "";
		if (ObjUtil.isEmpty(idArr)) {
			return arrStr;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (int i = 0; i < idArr.length; i++) {
			T t = idArr[i];
			String id = value.getValue(t);
			if (ObjUtil.isNotEmpty(id)) {
				set.add(id);
			}
		}
		boolean b=true;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			if (b) {
				arrStr += iterator.next();
				b=false;
			} else {
				arrStr += "," + iterator.next();
			}
		}
		return arrStr;
	}
	
	
	
	/**
	 * arrayTransforString: 拼接制定对象属性，转化为String  <br/>
	 * @author caijin
	 * @param idArr
	 * @param value
	 * @return
	 * @since JDK 1.7
	 */
	public static <T> String arrayTransforString(Collection<T> list,ToString<T> value) {
		String arrStr = "";
		if (ObjUtil.isEmpty(list)) {
			return arrStr;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (T item:list) {
			String id = value.getValue(item);
			if (ObjUtil.isNotEmpty(id)) {
				set.add(id);
			}
		}
		boolean b=true;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			if (b) {
				arrStr += iterator.next();
				b=false;
			} else {
				arrStr += "," + iterator.next();
			}
		}
		return arrStr;
	}
	/**
	 * arrayTransforObject: 拼接制定对象属性，转化为Collection <br/>
	 * @author caijin
	 * @param paged
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable, T> List<ID> arrayTransforList(T[] array, ToValue<T, ID> value) {
		if (ObjUtil.isEmpty(array)) {
			return new ArrayList<>(0);
		}
		Set<ID> set = new LinkedHashSet<ID>();
		for (int i = 0; i < array.length; i++) {
			T t = array[i];
			ID id = value.getValue(t);
			if (ObjUtil.isNotEmpty(id)) {
				set.add(id);
			}
		}
		return new ArrayList<>(set);
	}
	/**
	 * arrayTransforObject: 拼接制定对象属性，转化为Collection <br/>
	 * 自带去重功能
	 * @author caijin
	 * @param paged
	 * @param idArr
	 * @return
	 * @since JDK 1.7
	 */
	public static <ID extends Serializable, T> List<ID> arrayTransforList(Collection<T> array, ToValue<T, ID> value) {
		if (ObjUtil.isEmpty(array)) {
			return new ArrayList<>(0);
		}
		Set<ID> set = new LinkedHashSet<ID>();
		for (T item:array) {
			ID id = value.getValue(item);
			if (ObjUtil.isNotEmpty(id)) {
				set.add(id);
			}
		}
		return new ArrayList<>(set);
	}
	
	/**
	 * stringToArray: 将带格式的字符串，转化为Long数组 <br/>
	 * @author caijin
	 * @param ids 带格式的：123,1233,4
	 * @return
	 * @since JDK 1.7
	 */
	public static Long[] stringToArray(String ids) {
		if(ObjUtil.isNotBlank(ids)){
			String[] idsArray=ids.split(",");
			Collection<Long> idL=arrayTransforList(idsArray, new ToValue<String,Long>(){
				public Long getValue(String obj) {
					if(ObjUtil.isNotBlank(obj)){
						try {
							return Long.parseLong(obj.trim());
						} catch (NumberFormatException e) {}
					}
					return null;
				}
			});
			return idL.toArray(new Long[idL.size()]);
		}
		return new Long[0];
	}
	/**
	 * stringToArray: 将带格式的字符串，转化为Long数组 <br/>
	 * @author caijin
	 * @param ids 带格式的：123,1233,4
	 * @return
	 * @since JDK 1.7
	 */
	public static List<Long> stringToList(String ids) {
		if(ObjUtil.isNotBlank(ids)){
			String[] idsArray=ids.split(",");
			Collection<Long> idL=arrayTransforList(idsArray, new ToValue<String,Long>(){
				public Long getValue(String obj) {
					if(ObjUtil.isNotBlank(obj)){
						try {
							return Long.parseLong(obj.trim());
						} catch (NumberFormatException e) {}
					}
					return null;
				}
			});
			return new ArrayList<Long>(idL);
		}
		return new ArrayList<Long>(0);
	}
	
	public static List<Integer> stringToIntegerList(String ids) {
		if(ObjUtil.isNotBlank(ids)){
			String[] idsArray=ids.split(",");
			Collection<Integer> idL=arrayTransforList(idsArray, new ToValue<String,Integer>(){
				public Integer getValue(String obj) {
					if(ObjUtil.isNotBlank(obj)){
						try {
							return Integer.parseInt(obj.trim());
						} catch (NumberFormatException e) {}
					}
					return null;
				}
			});
			return new ArrayList<Integer>(idL);
		}
		return new ArrayList<Integer>(0);
	}
	/**
	 * 内存分页方法
	 * @author yjY
	 * @param page
	 * @param list
	 * @return 
	 * @return
	 * @since JDK 1.7
	 */
	public static <T extends Serializable> List<T> memoryPage(Page page,List<T> list){
		int toIndex = page.getOffset()+page.getPageSize();
		if(toIndex>list.size()){
			toIndex = list.size();
		}
		return list.subList(page.getOffset(), toIndex);
	}
	
	public static Page createNewPage(Integer page, Integer rows) {
		if(ObjUtil.isEmpty(page)){
			page=1;
		}
		if(ObjUtil.isEmpty(rows)){
			rows=10;
		}
		return new PageRequest(page, rows);
	}
	
}
