package org.csr.core.persistence.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.csr.core.intercepter.HandlerInterceptorAdapter;
import org.csr.core.page.Direction;
import org.csr.core.page.Page;
import org.csr.core.page.QueryProperty;
import org.csr.core.page.Sort;
import org.csr.core.persistence.PageRequest;
import org.csr.core.persistence.QueryPropertyImpl;
import org.csr.core.util.ObjUtil;
import org.csr.core.web.controller.BasisAction;
import org.springframework.web.method.HandlerMethod;

/**
 * ClassName:PageInterceptor.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-19上午11:20:47 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class PageInterceptor extends HandlerInterceptorAdapter{
	/**
	 * @param auto 
	 * @description:解析查询条件
	 * @param: page：查询信息
	 * @return: void 
	 */
	private void parseQueryString(Page page, String auto){
        if ("true".equals(auto) && StringUtils.isNotEmpty(page.getQueryString()) && !"".equals(page.getQueryString()) && !"none".equals(page.getQueryString())){
            String[] parts = page.getQueryString().split(",");
            for (String queryStr : parts){
            	if(StringUtils.isNotBlank(queryStr) && !"".equals(queryStr)){
            		 String filterProperty = StringUtils.substringBefore(queryStr, ":$");
            		 String value = StringUtils.substringAfter(queryStr, ":$");
            		 //如果value值为空,则忽略此filter
            		 if (StringUtils.isNotBlank(value) && filterProperty.indexOf("auto")<0) {
            			 QueryProperty filter = new QueryPropertyImpl(filterProperty, value);
            			 page.addQueryPropertyList(filter);
            		 }
            	}
            }
        }
    }
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		if(handler instanceof HandlerMethod){
			BasisAction action = (BasisAction)((HandlerMethod) handler).getBean();
			int pageNumber=ObjUtil.toInt(request.getParameter("page"));
			int pageSize=ObjUtil.toInt(request.getParameter("rows"));
			String sort=request.getParameter("sort");
			String order=request.getParameter("order");
//			String orderBy=request.getParameter("orderBy");
//			String direction=request.getParameter("direction");
			String queryString=request.getParameter("queryString");
			String auto=request.getParameter("auto");
			if(pageSize==0){
				pageSize=20;
			}
			Page page= null;
			if(ObjUtil.isNotBlank(sort) && ObjUtil.isNotBlank(order)){
				page=new PageRequest(pageNumber, pageSize,new Sort(Direction.fromString(order), sort),queryString);
			}else{
				//默认给id排序
				page=new PageRequest(pageNumber, pageSize,queryString);
			}
			parseQueryString(page,auto);
			action.setPage(page);
		}
		if(handler instanceof BasisAction){
			BasisAction action = (BasisAction)handler;
			int pageNumber=ObjUtil.toInt(request.getParameter("page"));
			int pageSize=ObjUtil.toInt(request.getParameter("rows"));
			String sort=request.getParameter("sort");
			String order=request.getParameter("order");
			//TODO 分页查询对象需要在此重构。没有排序查询
//			String orderBy=request.getParameter("orderBy");
//			String direction=request.getParameter("direction");
//			String loginName=request.getParameter("loginName");
			String queryString=request.getParameter("queryString");
			String auto=request.getParameter("auto");
			if(pageSize==0){
				pageSize=10;
			}
			Page page= null;
			if(ObjUtil.isNotBlank(sort) && ObjUtil.isNotBlank(order)){
				page=new PageRequest(pageNumber, pageSize,new Sort(Direction.fromString(order), sort),queryString);
			}else{
				page=new PageRequest(pageNumber, pageSize,queryString);
			}
			parseQueryString(page,auto);
			action.setPage(page);
		}
		return true;
	}
}
