package org.csr.core.persistence;

import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.csr.core.Param;
import org.csr.core.page.Sort;
import org.csr.core.persistence.query.DecorateQuery;

/**
 * ClassName:Finder.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2015年5月21日下午12:27:00 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7 <br/>
 *
 * 功能描述： 查询对象，并且提供查询 并提供添加参数<br/>
 */
public interface Finder {

	/**
	 * isAutoParam: page查询的是否是否，自动把条件封装到sql中默认，为true <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public abstract boolean isAutoParam();
	
	/**
	 * 设置是否允许自动拼接由前台page对象中的parm
	 * @author caijin
	 * @param autoParam
	 * @since JDK 1.7
	 */
	public abstract void setAutoParam(boolean autoParam);
	
	public abstract Finder append(Object hql);
	
	public abstract Finder append(Object hql,String param, Object value);
	
	/**
	 * insertEnd: 插入自动拼接最后 <br/>
	 * 主要用户，order by 等关键语句的
	 * @author caijin
	 * @param hql
	 * @return
	 * @since JDK 1.7
	 */
	public abstract Finder insertEnd(Object hql);
	/**
	 * 设置采用，自己定义的别名，如果在传输的查询sql无法获取正确的别名的时候，<br>
	 * 可以使用，自己所需要的别名。
	 * @author caijin
	 * @param detectAlias
	 * @return
	 * @since JDK 1.7
	 */
	public Finder setDetectAlias(String detectAlias);
	public String getDetectAlias();
	/**
	 * 自动补全Jpdl 语句，如果没有where，自动补全<
	 * @author caijin
	 * @param field 字段需要指明自己属于哪个表的前缀
	 * @param compare
	 * @param value  比较的值：其实，也可以是 :变量
	 * @return
	 * @since JDK 1.7
	 */
	public Finder addWhereNotAliasValue(String field ,String compare, String value);
	/**
	 * 自动补全Jpdl 语句，如果没有where，自动补全
	 * @param field 要查询的字段名称 ，自己补全表的前缀
	 * @param compare 比较方式：< = > 
	 * @param value 比较的值：其实，也可以是 :变量
	 * @return
	 */
	public Finder addWhereValue(String field ,String compare, String value);
	public Finder orWhereValue(String field ,String compare, String value);
	
	/**
	 * 获得原始hql语句
	 * 
	 * @return
	 */
	public abstract String getHql();

	/**
	 * 获得查询数据库记录数的hql语句。
	 * 
	 * @return
	 */
	public abstract String getRowCountHql();

	/**
	 * 是否使用查询缓存
	 * 
	 * @return
	 */
	public abstract boolean isCacheable();

	/**
	 * 设置是否使用查询缓存
	 * 
	 * @param cacheable
	 * @see Query#setCacheable(boolean)
	 */
	public abstract void setCacheable(boolean cacheable);
	/**
	 * 设置参数排序方式
	 * @param param
	 * @param value
	 * @return
	 * @see Query#setParameter(String, Object)
	 */
	public abstract Finder setSort(Sort sort);
	/**
	 * 设置参数
	 * 
	 * @param param
	 * @param value
	 * @return
	 * @see Query#setParameter(String, Object)
	 */
	public abstract Finder setParam(Object value);
	/**
	 * 设置参数
	 * 
	 * @param param
	 * @param value
	 * @return
	 * @see Query#setParameter(String, Object)
	 */
	public abstract Finder setParam(String param, Object value);

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param paramMap
	 * @return
	 * @see Query#setProperties(Map)
	 */
	public abstract Finder setParams(Map<String, Object> paramMap);

	/**
	 * 设置，把param设置到Query查询对象中
	 * @author caijin
	 * @param query
	 * @return
	 * @since JDK 1.7
	 */
	public abstract Finder setParamsToQuery(DecorateQuery query);
	
	
	public Finder putAutoParam(String name,List<Param> autoParam);
	/**
	 * autoPackagingParams: 提供自动组装prarms <br/>
	 * @author caijin
	 * @param name
	 * @param autoParam
	 * @return
	 * @since JDK 1.7
	 */
	public Finder autoPackagingParams(String name,List<Param> autoParam);

	
	

}