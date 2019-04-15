package org.csr.core.persistence.query;

/**
 * ClassName:Compare.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014年3月17日下午12:24:37 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 属性比较类型. 
 * EQ:等于
 * LIKE：模糊查询
 * LT：小于
 * GT：大于
 * LE：小于等于
 * GE：大于等于
 * NE:不等于
 * IN:等于多个
 */
public interface Compare {
	/**等于 */
	public static final String EQ = "=";
	/**不等于 */
	public static final String NE = "!=";
	/**等于多个 */
	public static final String IN = "in";
	/**不等于多个 */
	public static final String NotIn = "not in";
	/**小于*/
	public static final String LT = "<";
	/**大于*/
	public static final String GT = ">";
	/**小于等于*/
	public static final String LE = "<=";
	/**大于等于*/
	public static final String GE = ">=";
	
	//======
	public static final String IS = "is";
	public static final String NotIs = "is not";
	/**LIKE：模糊查询*/
	public static final String LIKE = "like";
	
	//
	public static final String AND = "and";
	public static final String OR = "or";
	
}
