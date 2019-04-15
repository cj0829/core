package org.csr.core.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.csr.core.cloudsession.CloudRequestContextHolder;
import org.csr.core.i18n.CoreAcceptHeaderLocaleResolver;
import org.csr.core.page.Page;
import org.csr.core.util.JsonUtil;
import org.csr.core.util.json.ExcludePropertyPreFilter;
import org.csr.core.web.bean.ResultJson;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

public abstract class BasisAction {
	/**
	 * 分页对象
	 */
	protected Page page;

	protected String ERROR = "/error";

	protected String NoPermission = "/403";

	protected String NoOrganization = "/organization";

	public static HttpSession getSession() {
		HttpSession session = null;
		try {
			session = getRequest().getSession();
		} catch (Exception e) {
		}
		return session;
	}

	public static HttpServletRequest getRequest() {
		return CloudRequestContextHolder.currentHttpServletRequest();
	}
	
	public static String getMessage(HttpServletRequest request,String msgKey) {
		return getMessage(request, msgKey,null);
	}
	
	public static String getMessage(HttpServletRequest request,String msgKey, String[] args) {
		Locale locale = (Locale) getRequest().getSession().getAttribute(CoreAcceptHeaderLocaleResolver.LOCALE_KEY);
		RequestContext requestContext = new RequestContext(request);
		locale = locale == null ? getRequest().getLocale() : locale;
		requestContext.changeLocale(locale);
		return requestContext.getMessage(msgKey, args);
	}
	
	
	public void setRequest(String name, Object value) {
		getRequest().setAttribute(name, value);
	}
	
	public Object getRequest(String name) {
		return getRequest().getAttribute(name);
	}
	
	
	
	/**
	 * 获得response
	 */
	public HttpServletResponse getResponse() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs.getResponse();
	}
	

	public String getParameterValue(String name) {
		return getRequest().getParameter(name);
	}

	protected String getCxt() {
		String cxt = (String) getRequest().getAttribute("cxt");
		return cxt;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected String viewName(String name) {
		String viewName = "";
		try {
			viewName = ServletRequestUtils.getStringParameter(getRequest(), "viewName");
		} catch (ServletRequestBindingException e) {}
		if (StringUtils.isBlank(viewName)) {
			viewName = name;
		}
		return viewName;
	}

	protected void setSession(String name, Object value) {
		getSession().setAttribute(name, value);
	}

	protected Object getSession(String name) {
		return getSession().getAttribute(name);
	}

	/**
	 * 返回一个 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param obj
	 *            返回的对象，可以是对象，集合，数组。
	 * @param excludes
	 *            不包含的属性
	 * @return
	 */
	protected ModelAndView resultExcludeJson(Object obj, String... excludes) {
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", excludeJson(obj, excludes));
		return view;
	}
	

	/**
	 * 返回一个 String,内容自定义
	 * @param context 返回页面的内容
	 * @return
	 */
	protected ModelAndView resultString(String context) {
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", context);
		return view;
	}
	
	/**
	 * 返回一个 错误 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param msg
	 *            提示消息
	 * @return
	 */
	protected ModelAndView errorMsgJson(String msg) {
		ResultJson success = new ResultJson(ResultJson.FAIL, msg);
		return resultExcludeJson(success);
	}
	/**
	 * 返回一个 错误 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param msg 提示消息
	 * @param data    需要返回的数据
	 * @param excludes 返回的数据不包含属性
	 * @return
	 */
	protected ModelAndView errorMsgJson(String msg, Object data, String... excludes) {
		ResultJson success = new ResultJson(ResultJson.FAIL, msg);
		success.setData(data);
		ExcludePropertyPreFilter[] filters = new ExcludePropertyPreFilter[2];
		filters[0] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		//filters[1] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", JsonUtil.excludeJson(success,filters));		
		return resultExcludeJson(success);
	}
	
	/**
	 * 返回一个 错误 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param msg 提示消息
	 * @param code 消息编码
	 * @param data    需要返回的数据
	 * @param excludes 返回的数据不包含属性
	 * @return
	 */
	protected ModelAndView errorMsgJson(String msg,String code, Object data, String... excludes) {
		ResultJson success = new ResultJson(ResultJson.FAIL, msg);
		success.setData(data);
		success.setCode(code);
		ExcludePropertyPreFilter[] filters = new ExcludePropertyPreFilter[2];
		filters[0] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		//filters[1] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", JsonUtil.excludeJson(success,filters));		
		return resultExcludeJson(success);
	}

	/**
	 * 返回一个 成功 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param msg 提示消息
	 * @param data    需要返回的数据
	 * @param excludes 返回的数据不包含属性
	 * @return
	 */
	protected ModelAndView successMsgJson(String msg, Object data, String... excludes) {
		ResultJson success = new ResultJson(ResultJson.SUCCESS, msg);
		success.setData(data);
		ExcludePropertyPreFilter[] filters = new ExcludePropertyPreFilter[2];
		filters[0] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		//filters[1] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", JsonUtil.excludeJson(success,filters));		
		return view;
	}

	/**
	 * 返回一个 成功 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param msg 提示消息
	 * @param code 消息编码
	 * @param data    需要返回的数据
	 * @param excludes 返回的数据不包含属性
	 * @return
	 */
	protected ModelAndView successMsgJson(String msg,String code, Object data, String... excludes) {
		ResultJson success = new ResultJson(ResultJson.SUCCESS, msg);
		success.setData(data);
		success.setCode(code);
		ExcludePropertyPreFilter[] filters = new ExcludePropertyPreFilter[2];
		filters[0] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		//filters[1] = new ExcludePropertyPreFilter(data.getClass(),excludes);
		ModelAndView view = new ModelAndView("jsonView");
		view.addObject("jsonData", JsonUtil.excludeJson(success,filters));		
		return view;
	}
	/**
	 * 返回一个 成功 提示 json 对象。可以过滤掉不需要的属性
	 * 
	 * @param obj
	 * @param excludes
	 * @return
	 **/
	protected ModelAndView successMsgJson(String msg) {
		ResultJson error = new ResultJson(ResultJson.SUCCESS, msg);
		return resultExcludeJson(error);
	}

	protected String excludeJson(Object obj, String... params) {
		return JsonUtil.excludeJson(obj, params);
	}
	
	
	
	public void setPage(Page page) {
		this.page = page;
	}

	public Page getPage() {
		return page;
	}
	
}
