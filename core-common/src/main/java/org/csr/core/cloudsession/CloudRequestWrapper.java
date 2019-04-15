package org.csr.core.cloudsession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csr.core.cache.RedisUtil;
import org.csr.core.util.ObjUtil;

import redis.clients.jedis.Jedis;

public class CloudRequestWrapper extends HttpServletRequestWrapper {

	public static final String tokenSession="tokenSession";
	CloudSessionManager rm = CloudSessionManager.getInstance();
	private Map<String, String[]> params = new HashMap<String, String[]>();
	HttpServletRequest request;
	HttpServletResponse response;
	private String sessionManagerMode="localSession";
	private int maxInactiveInterval=1800;
	private String sessionName="jsessionid";
	
	public CloudRequestWrapper(HttpServletRequest request,HttpServletResponse response,String sessionManagerMode,Integer maxInactiveInterval,String sessionName) {
		// 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
		super(request);
		this.request =  request;
		this.response=response;
		// 将参数表，赋予给当前的Map以便于持有request中的参数
		this.params.putAll(request.getParameterMap());
		this.modifyParameterValues();
		//
		if(ObjUtil.isNotBlank(sessionManagerMode)){
			this.sessionManagerMode=sessionManagerMode;
		}
		if(ObjUtil.isNotEmpty(maxInactiveInterval)){
			this.maxInactiveInterval=maxInactiveInterval;
		}
		if(ObjUtil.isNotBlank(sessionName)){
			this.sessionName=sessionName;
		}
	}


	public void modifyParameterValues() {// 将parameter的值去除空格后重写回去
		Set<String> set = params.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] values = params.get(key);
			if(ObjUtil.isNotEmpty(values)){
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
			}
			params.put(key, values);
		}
	}

	@Override
	public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取
		String[] values = params.get(name);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}
	
	@Override
	public HttpSession getSession(boolean create) {
		if(tokenSession.equals(sessionManagerMode)){
			//先暂时获取第一个cookies的jsessionId
			String cookieJsessionId = readCookieSessionId();
			//sessionid 不存在
			if(ObjUtil.isBlank(cookieJsessionId)){
				//创建RedisSession并将sessionId写入cookies中。
				CloudSession session = rm.createSession(cookieJsessionId,request,maxInactiveInterval);
				writeCookie(session.getId());
				return session;
			}else{
				try {
					CloudSession session = rm.findSession(cookieJsessionId,request,maxInactiveInterval);
					if( ObjUtil.isEmpty(session)){
						//如果
						if(create){
							session = rm.createSession(cookieJsessionId,request,maxInactiveInterval);
							writeCookie(session.getId());
							return session;
						}else{
							//删除cookie中的jsession以及jsession值 不能删除
//							clearSessionCookie();
							return null;
						}
					}else{
						return session;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		}else{
			return request.getSession(create);
		}
	}
	
	/**
	 * clearSessionCookie:清空cookie中的键值 <br/>
	 * @author Administrator
	 * @since JDK 1.7
	 */
	private void clearSessionCookie() {
		Cookie[] cookies=getCookies();
		if(ObjUtil.isNotEmpty(cookies)){
			for (Cookie cookie : cookies) {
				cookie.setValue("");
			}
		}
	}

	/**
	 * 去读cookie 中的jsessionId并返回
	 * @author Administrator
	 * @param create
	 * @return
	 * @since JDK 1.7
	 */
	private String readCookieSessionId() {
		Cookie[] cookies=request.getCookies();
		if(ObjUtil.isNotEmpty(cookies)){
			for (Cookie cookie : cookies) {
				if(sessionName.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return ObjUtil.toString(request.getAttribute("___"+sessionName));
	}

	/**
	 * sessionId写入cookie中。
	 * @author Administrator
	 * @param cookieJsessionId
	 * @since JDK 1.7
	 */
	private void writeCookie(String cookieJsessionId) {
		Cookie[] cookies=getCookies();
		boolean isadd=true;
		if(ObjUtil.isNotEmpty(cookies)){
			for (Cookie cookie : cookies) {
				if(sessionName.equals(cookie.getName())){
					if(ObjUtil.isNotBlank(cookieJsessionId)){
						if(!cookieJsessionId.equals(cookie.getValue())){
							cookie.setValue(cookieJsessionId);
						}
						isadd=false;
					}
				}
			}
		}
		if(isadd){
			request.setAttribute("___"+sessionName,cookieJsessionId);
			Cookie cookie = new Cookie(sessionName,cookieJsessionId);
			response.addCookie(cookie);
		}
	}

	public String[] getParameterValues(String name) {// 同上
		return params.get(name);
	}

	public void addAllParameters(Map<String, Object> otherParams) {// 增加多个参数
		for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {// 增加参数
		if (value != null) {
			if (value instanceof String[]) {
				params.put(name, (String[]) value);
			} else if (value instanceof String) {
				params.put(name, new String[] { (String) value });
			} else {
				params.put(name, new String[] { String.valueOf(value) });
			}
		}
	}
	
	protected Jedis acquireConnection() {
		Jedis jedis=RedisUtil.getJedis();
		return jedis;
	}
	@Override
	public void logout() throws ServletException {
		super.logout();
		System.out.println("结束清空");
	}

	public void commitSession() {
		if(tokenSession.equals(sessionManagerMode)){
			rm.afterRequest();
		}
	}


	public void cleanSessionThreadLocal() {
		if(tokenSession.equals(sessionManagerMode)){
			rm.cleanSessionThreadLocal();
		}
	}
}