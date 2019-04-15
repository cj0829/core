/**
 * Project Name:common
 * File Name:StatisticalParam.java
 * Package Name:org.csr.supplies.result
 * Date:2014年3月26日下午3:34:22
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.statistical;

import java.util.ArrayList;
import java.util.List;

import org.csr.core.util.ObjUtil;

/**
 * ClassName: StatisticalParam.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014年3月26日下午3:34:22 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class StatisticalParam{
	private List<Grouping> groupings;
	private YCoordinate y;
	private List<Target> targets;
	private List<Where> wheres;
	
	private String sql="";
	

	public YCoordinate getY(){
		return y;
	}

	public void setY(YCoordinate y){
		this.y=y;
	}
	public List<Grouping> getGrouping(){
		return groupings;
	}

	public StatisticalParam addGrouping(Grouping grouping){
		if(ObjUtil.isEmpty(groupings)){
			groupings=new ArrayList<Grouping>();
		}
		if(ObjUtil.isNotEmpty(grouping)){
			this.groupings.add(grouping);
		}
		return this;
	}
	
	public StatisticalParam addTargets(Target target){
		if(ObjUtil.isEmpty(targets)){
			targets=new ArrayList<Target>();
		}
		if(ObjUtil.isNotEmpty(target)){
			this.targets.add(target);
		}
		return this;
	}

	public List<Target> getTargets(){
		return this.targets;
	}
	
	public StatisticalParam addWheres(Where where){
		if(ObjUtil.isEmpty(wheres)){
			wheres=new ArrayList<Where>();
		}
		if(ObjUtil.isNotEmpty(where)){
			this.wheres.add(where);
		}
		return this;
	}

	public List<Where> getWheres(){
		return this.wheres;
	}

	public String getSql(){
		return sql;
	}

	public void setSql(String sql){
		this.sql=sql;
	}
	
}

