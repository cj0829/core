/**
 * Project Name:core
 * File Name:ParamFactory.java
 * Package Name:org.csr.core.persistence.param
 * Date:2015年5月21日下午4:59:51
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
 */

package org.csr.core.persistence.param;

import org.csr.core.Param;
import org.csr.core.persistence.AnyType;
import org.csr.core.persistence.query.Compare;

/**
 * ClassName:ParamFactory.java <br/>
 * System Name： 基础框架 <br/>
 * Date: 2015年5月21日下午4:59:51 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class ParamFactory {
	public static Param createParam(String orWith, String compare,String key,String value,Class<?> propertyClass) {
		if(propertyClass == AnyType.class){
			if (Compare.AND.endsWith(orWith)) {
				return new AndAnyValue(key,value,compare);
			} else if (Compare.OR.endsWith(orWith)) {
				
			}
		}else{
			if (Compare.EQ.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndEqParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrEqParam(key, value,propertyClass);
				}
			} else if (Compare.NE.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndNeParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrNeParam(key, value,propertyClass);
				}
			} else if (Compare.IN.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndInParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrInParam(key, value,propertyClass);
				}
			} else if (Compare.NotIn.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndNotInParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrNotInParam(key, value,propertyClass);
				}
			}else if (Compare.LT.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndLTParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrLTParam(key, value,propertyClass);
				}
			} else if (Compare.GT.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndGTParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrGTParam(key, value,propertyClass);
				}
			} else if (Compare.LE.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndLEParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrLEParam(key, value,propertyClass);
				}
			} else if (Compare.GE.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndGEParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrGEParam(key, value,propertyClass);
				}
			} else if (Compare.IS.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndIsValue(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrIsValue(key, value,propertyClass);
				}
			}else if (Compare.NotIs.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndNotIsValue(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrNotIsValue(key, value,propertyClass);
				}
			} else if (Compare.LIKE.equals(compare)) {
				if (Compare.AND.endsWith(orWith)) {
					return new AndLikeParam(key, value,propertyClass);
				} else if (Compare.OR.endsWith(orWith)) {
					return new OrLikeParam(key, value,propertyClass);
				}
			}
		}
		return null;
	}

}
