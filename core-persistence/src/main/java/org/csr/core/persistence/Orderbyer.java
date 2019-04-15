/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.core.persistence;

import java.io.Serializable;

/**
 * ClassName:Orderbyer.java <br/>
 * System Name：    在线学习系统 <br/>
 * Date:     2016年6月29日下午1:01:14 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface Orderbyer extends Serializable{
    /**
     * support following type: String, Integer, Long, Float, Double, Short, Date
     */
    Object getOrderby();
}
