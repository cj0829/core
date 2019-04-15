/**
 * Project Name:core
 * File Name:ComputingSymbol.java
 * Package Name:org.csr.core.persistence
 * Date:2014年3月27日下午3:38:23
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence;
/**
 * ClassName:ComputingSymbol.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2014年3月27日下午3:38:23 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface Operators{
	/**AVG 函数返回数值列的平均值。NULL 值不包括在计算中*/
	int AVG=1;
	/**COUNT(*) 函数返回表中的记录数*/
	int COUNT=2;
	/**FIRST() 函数返回指定的字段中第一个记录的值*/
	int FIRST=3;
	/**LAST() 函数返回指定的字段中最后一个记录的值*/
	int LAST=4;
	/**MAX 函数返回一列中的最大值。NULL 值不包括在计算中*/
	int MAX=5;
	/**MIN 函数返回一列中的最小值。NULL 值不包括在计算中*/
	int MIN=6;
	/**SUM 函数返回数值列的总数（总额）*/
	int SUM=7;
}

