/*
 * Copyright (c) 2013, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.core.web.taglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * @author caijin
 *
 */
public class BeanLoaderTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * Maps lowercase JSP scope names to their PageContext integer constant values.
	 */
	private static final Map<String, Integer> scopes = new HashMap<String, Integer>();

	/**
	 * Initialize the scope names map.
	 */
	static {
		scopes.put("page", PageContext.PAGE_SCOPE);
		scopes.put("request", PageContext.REQUEST_SCOPE);
		scopes.put("session", PageContext.SESSION_SCOPE);
		scopes.put("application", PageContext.APPLICATION_SCOPE);
	}

	/**
	 * Converts the scope name into its corresponding PageContext constant value.
	 * 
	 * @param scopeName Can be "page", "request", "session", or "application" in any case.
	 * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
	 * @throws JspException if the scopeName is not a valid name.
	 */
	public int getScope(String scopeName) throws JspException {
		// Expose this value as a scripting variable
		int inScope = PageContext.PAGE_SCOPE;
		if (scopeName != null) {
			Integer scope = (Integer) scopes.get(scopeName.toLowerCase());
			if (scope == null) {
				throw new JspException(BeanLoaderTag.class.getName() + "lookup.scope");
			}
			return scope.intValue();
		}
		return inScope;
	}

	/**
	 * The name of the scripting variable that will be exposed as a page scope attribute.
	 */
	private String id = null;

	public String getId() {
		return (this.id);
	}

	public void setId(String id) {
		this.id = id;
	}

	private String className;
	private String methodName;
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	private String param6;
	private String scope;

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public void setParam6(String param6) {
		this.param6 = param6;
	}

	private String[] paramTypes;
	private String[] paramValues;

	public String[] getParamTypes() throws JspException {
		initParam();
		return paramTypes;
	}

	public String[] getParamValues() throws JspException {
		initParam();
		return paramValues;
	}

	private String findString(String str, String sign, int beginIndex, int endIndex) {
		if (StringUtils.isBlank(str))
			return null;
		String tmpStr = str.trim();
		if (endIndex > 0 && endIndex > tmpStr.length()) {
			endIndex = tmpStr.length();
		}
		if (beginIndex == -1 && null != sign && !"".equals(sign)) {
			beginIndex = tmpStr.indexOf(")") + 1;
		}
		if (endIndex == -1 && null != sign && !"".equals(sign)) {
			endIndex = tmpStr.indexOf(")");
		}
		return tmpStr.substring(beginIndex, endIndex);
	}

	private void initParam() throws JspException {
		int length = 0;
		if (paramTypes == null || paramValues == null) {
			String[] tempParamTypes = new String[6];
			String[] tempParamValues = new String[6];

			if (null != param1 && !"".equals(param1)) {
				length++;
				tempParamTypes[0] = findString(param1, ")", 1, -1);
				tempParamValues[0] = findString(param1, ")", -1, param1.length());
			}

			if (null != param2 && !"".equals(param2)) {
				length++;
				tempParamTypes[1] = findString(param2, ")", 1, -1);
				tempParamValues[1] = findString(param2, ")", -1, param2.length());
			}

			if (null != param3 && !"".equals(param3)) {
				length++;
				tempParamTypes[2] = findString(param3, ")", 1, -1);
				tempParamValues[2] = findString(param3, ")", -1, param3.length());
			}

			if (null != param4 && !"".equals(param4)) {
				length++;
				tempParamTypes[3] = findString(param4, ")", 1, -1);
				tempParamValues[3] = findString(param4, ")", -1, param4.length());
			}

			if (null != param5 && !"".equals(param5)) {
				length++;
				tempParamTypes[4] = findString(param5, ")", 1, -1);
				tempParamValues[4] = findString(param5, ")", -1, param5.length());
			}

			if (null != param6 && !"".equals(param6)) {
				length++;
				tempParamTypes[5] = findString(param6, ")", 1, -1);
				tempParamValues[5] = findString(param6, ")", -1, param6.length());
			}

			if (tempParamTypes.length > 0 && tempParamValues.length > 0) {
				paramTypes = new String[length + 1];
				paramValues = new String[length + 1];
				paramTypes[0] = "HttpServletRequest";
				paramValues[0] = "request";
				for (int i = 0; i < tempParamTypes.length; i++) {
					if (null != tempParamTypes[i] && null != tempParamValues[i]) {
						paramTypes[i + 1] = tempParamTypes[i];
						paramValues[i + 1] = tempParamValues[i];
					}
				}
			} else {
				paramTypes = new String[1];
				paramValues = new String[1];
				paramTypes[0] = "HttpServletRequest";
				paramValues[0] = "request";
			}
		}
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		try {

			Class<?> cls = Class.forName(className);
			Class<?> paramtypes[] = this.getMethodParamTypeClass(this.getParamTypes());
			Method method = cls.getMethod(methodName, paramtypes);
			Object arglist[] = this.getMethodParamValueObject(this.getParamTypes(), this.getParamValues());
			Object value = method.invoke(cls, arglist);
			pageContext.setAttribute(id, value, getScope(scope));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.paramTypes = null;
			this.paramValues = null;
		}
		return EVAL_PAGE;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getMethodParamTypeClass(String[] type) {
		Class[] cs = new Class[type.length];
		for (int i = 0; i < cs.length; i++) {
			if (type[i] != null && !type[i].trim().equals("")) {
				if (type[i].equals("HttpServletRequest")) {
					cs[i] = HttpServletRequest.class;
				} else if (type[i].equals("int") || type[i].equals("Integer")) {
					cs[i] = Integer.TYPE;
				} else if (type[i].equals("float") || type[i].equals("Float")) {
					cs[i] = Float.TYPE;
				} else if (type[i].equals("double") || type[i].equals("Double")) {
					cs[i] = Double.TYPE;
				} else if (type[i].equals("long") || type[i].equals("Long")) {
					cs[i] = Long.TYPE;
				} else if (type[i].equals("boolean") || type[i].equals("Boolean")) {
					cs[i] = Boolean.TYPE;
				} else {
					cs[i] = String.class;
				}
			}
		}
		return cs;
	}

	public Object[] getMethodParamValueObject(String[] type, String[] param) {
		Object[] obj = new Object[param.length];
		for (int i = 0; i < obj.length; i++) {
			if (param[i] != null && !param[i].trim().equals("")) {
				if (type[i].equals("HttpServletRequest")) {
					ServletRequest request = pageContext.getRequest();
					HttpServletRequest hrequest = (HttpServletRequest) request;
					obj[i] = hrequest;
				} else if (type[i].equals("int") || type[i].equals("Integer")) {
					obj[i] = new Integer(param[i]);
				} else if (type[i].equals("float") || type[i].equals("Float")) {
					obj[i] = new Float(param[i]);
				} else if (type[i].equals("double") || type[i].equals("Double")) {
					obj[i] = new Double(param[i]);
				} else if (type[i].equals("long") || type[i].equals("Long")) {
					obj[i] = new Long(param[i]);
				} else if (type[i].equals("boolean") || type[i].equals("Boolean")) {
					obj[i] = Boolean.valueOf(param[i]);
				} else {
					obj[i] = param[i];
				}
			}
		}
		return obj;
	}
}
