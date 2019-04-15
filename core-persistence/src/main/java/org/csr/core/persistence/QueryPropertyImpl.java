package org.csr.core.persistence;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.csr.core.Param;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.QueryProperty;
import org.csr.core.persistence.param.ParamFactory;
import org.csr.core.persistence.query.Compare;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:PropertyFilter.java <br/>
 * System Name：  <br/>
 * Date: 2014年3月17日上午11:45:54 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class QueryPropertyImpl implements QueryProperty{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6119089590423103572L;
	/**
	 * 属性的比较类型
	 */
	private String compare = null;
	/**
	 * 用来判断是否用and或用or
	 */
	private String orWith = Compare.AND;
	/**
	 * 属性的值
	 */
	private String value = null;
	/**
	 * 属性名称
	 */
	private String name = null;
	/**
	 * 属性的数据类型
	 */
	private Class<?> propertyClass;


	public QueryPropertyImpl() {
		super();
	}

	public QueryPropertyImpl(final String propertyName, final String propertyValue) {
		// 分析名称
		parsePropertyName(propertyName);
		// 分析值
		parsePropertyValue(propertyValue);
	}
	
	public Param toParam() {
		if(ObjUtil.isBlank(value) || ObjUtil.isBlank(name)){
			return null;
		}
		return ParamFactory.createParam(orWith,compare,name,value,propertyClass);
	}

	/**
	 * 解析属性名称
	 * 
	 * @author caijin
	 * @param propertyName
	 * @return
	 * @since JDK 1.7
	 */
	private void parsePropertyName(String propertyName) {
		if (StringUtils.isNotEmpty(propertyName) && StringUtils.isNotBlank(propertyName)) {
			String firstPart = StringUtils.substringBefore(propertyName, ":");
			// 分割firstPart，以便于处理日期类型的数据，及其他数据类型
			boolean isContain = firstPart.contains("!");
			if (isContain) {
				String[] subParts = firstPart.split("!");
				boolean format = subParts.length > 1 ? true : false;
				if (format) {
					this.name = subParts[0];
					propertyClass = PropertyClass.getClassType(subParts[1]);
				} else {
					Exceptions.service("", "filter属性" + name + "没有按规则编写,无法得到属性比较类型.");
				}
			} else {
				this.name = firstPart;
				//默认设置为String类型
				propertyClass=String.class;
				//暂时不支持默认的全部类型，还是需要声明具体类型，也是为了安全考虑
//				propertyClass=AnyType.class;
			}
			// 如果有数据类型的处理，处理失败，this.filterProperty 为 null
			if (ObjUtil.isBlank(this.name)) {
				Exceptions.service("", "filter属性" + name + "没有按规则编写,无法得到属性比较类型.");
			}
			compare = verificationCompare(StringUtils.substringAfter(propertyName, ":"));
		} else {
			Exceptions.service("", "filter属性" + name + "没有按规则编写,无法得到属性值类型.");
		}
	}

	/**
	 * 解析属性值，包括
	 * 
	 * @author caijin
	 * @param propertyValue
	 * @return
	 * @since JDK 1.7
	 */
	private void parsePropertyValue(String propertyValue) {
		if (StringUtils.isNotEmpty(propertyValue) && StringUtils.isNotBlank(propertyValue)) {
			String[] pvalue=propertyValue.split("!");
			String matchValue = "";
			boolean format = pvalue.length > 1 ? true : false;
			//如果当format为true时，说明有!分割符
			if(format){
				if (Compare.OR.toLowerCase().equals(pvalue[0].toLowerCase()) || Compare.AND.toLowerCase().equals(pvalue[0].toLowerCase())) {
					 this.orWith = pvalue[0];
					 matchValue = pvalue[1];
				 }else{
					 Exceptions.service("", "compare属性" + name + "没有按规则编写,无法得到属性比较类型.");
				 }
			}else{
				//判断是否有分割符，如果有分隔符。
				if(propertyValue.indexOf('!')>0){
					if (Compare.OR.toLowerCase().equals(pvalue[0].toLowerCase()) || Compare.AND.toLowerCase().equals(pvalue[0].toLowerCase())) {
						 this.orWith = pvalue[0];
					 }else{
						 Exceptions.service("", "compare属性" + name + "没有按规则编写,无法得到属性比较类型.");
					 }
				}else{
					this.orWith = Compare.AND;
					matchValue=pvalue[0];
				}
			}
			// 判断转换类型，如果时间类型调用自己的方法
			if (propertyClass == Date.class) {
				this.value =  matchValue;
			} else {
				this.value = matchValue;
			}
		} else {
			Exceptions.service("", "filter属性" + this.value + "没有按规则编写,无法得到属性值类型.");
		}
	}

	/**
	 * 验证，比较，符号是否正确
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public static String verificationCompare(String compare) {
		if (!(Compare.EQ.equals(compare) || Compare.NE.equals(compare)
				|| Compare.IN.equals(compare) || Compare.LT.equals(compare)
				|| Compare.GT.equals(compare) || Compare.LE.equals(compare)
				|| Compare.GE.equals(compare) || Compare.IS.equals(compare)
				|| Compare.LIKE.equals(compare))) {
			Exceptions.service("", compare+"比较类型，不再规定范围中");
		}
		return compare;
	}
	
}
