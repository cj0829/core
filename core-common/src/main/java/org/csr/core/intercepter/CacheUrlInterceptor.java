package org.csr.core.intercepter;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csr.core.page.Page;
import org.csr.core.web.controller.BasisAction;
import org.springframework.web.method.HandlerMethod;
/**
 * ClassName:CacheUrlInterceptor.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-19上午11:20:36 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class CacheUrlInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		cacheUrl(request,response,handler);
		return true;
	}
	
	/**
	 * @param handler 
	 * @description: 缓存请求连接
	 * @param: page：查询信息
	 * @return: void 
	 */
	private void cacheUrl(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
        String url = request.getRequestURI();
        String preURL = request.getHeader("referer");  
        if(url.indexOf("person!backtrack")>0){
        	return;
        }
        HttpSession session=request.getSession();
		Stack<HistoryUrl> stack=(Stack<HistoryUrl>) session.getAttribute("history_url");
		if(stack==null){
			stack=new Stack<HistoryUrl>();
			session.setAttribute("history_url", stack);
		}
		if(handler instanceof HandlerMethod){
			BasisAction action = (BasisAction)((HandlerMethod) handler).getBean();
			if(request.getParameter("backtrack")!=null){
				markChildUrlBack(stack);
			}else if(request.getParameter("exception")!=null){
				markChildUrlBack(stack);
			}else{
				buildCache(stack,url,preURL,action);
			}
		}
		if(handler instanceof BasisAction){
			BasisAction action = (BasisAction)handler;
			if(request.getParameter("backtrack")!=null){
				markChildUrlBack(stack);
			}else if(request.getParameter("exception")!=null){
				markChildUrlBack(stack);
			}else{
				buildCache(stack,url,preURL,action);
			}
		}
	}
	
	/**
	 * buildCache: 构建url缓存 <br/>
	 * @author caijin
	 * @param stack
	 * @param url
	 * @param preURL
	 * @param action
	 * @since JDK 1.7
	 */
	@SuppressWarnings("unused")
	private void buildCache(Stack<HistoryUrl> stack,String url,String preURL,BasisAction action){
		 if(url.indexOf("!pre")>0){
	        	boolean pop=false;
	    		for (HistoryUrl historyUrl : stack) {
	    			if(historyUrl.getUrl().equals(url)){
	    				pop=true;
	    				break;
	    			}
	    		}
	    		HistoryUrl history=null;
	    		if(pop){
	    			while(stack.isEmpty() || pop){
	    				HistoryUrl hurl=stack.pop();
	    				if(hurl.getUrl().equals(url)){
	    					history=hurl;
	    					break;
	    				}
	    			}
	    		}

	    		if(history==null){
	    			history=new HistoryUrl();
	    			history.setUrl(url);
	    		}
	    		history.setNew(0);
	    		stack.push(history);
        }else{
        	if(!stack.isEmpty()){
	        	HistoryUrl historyUrl = stack.pop();
	        	HistoryUrl callback=checkCallbackUrl(historyUrl, url,action);
	        	//不是回调url
	        	if(callback==null){
	        		HistoryUrl newUrl=checkBackUrl(historyUrl, url);
	        		historyUrl.getChild().push(newChildUrl(action,url));
        			//把当前链接入栈
	        	}
	        	stack.push(historyUrl);
        	}
        }
	}
	
	/**
	 * 创建新的url入栈
	 */
	private HistoryUrl newChildUrl(BasisAction action,String url){
		HistoryUrl childHistory=new HistoryUrl();
//		//若有查询条件则进行解析
		if (action.getPage() != null){
            childHistory.setPage(action.getPage());
        }
		childHistory.setUrl(url);
		return childHistory;
	}
	
	/**
	 * 检查是否为回调刷新的页面
	 * @param history
	 * @param url
	 * @return
	 */
	private HistoryUrl checkCallbackUrl(HistoryUrl history,String url,BasisAction action){
		for (HistoryUrl child : history.getChild()) {
			if(child.getUrl().equals(url) && child.isBacktrack()){
				child.setBacktrack(false);
				if(child.getPage()!=null){
					action.setPage(child.getPage());
				}
				return child;
			}
		}
		return null;
	}
	
	/**
	 * 检验是否为点击回退
	 * @param history
	 * @param url
	 * @return
	 */
	private HistoryUrl checkBackUrl(HistoryUrl history,String url){
		for (HistoryUrl child : history.getChild()) {
			if(child.getUrl().equals(url)){
				history.getChild().remove(child);
				return child;
			}
		}
		return null;
	}
	
	/**
	 * 回退分析url
	 */
	@SuppressWarnings("unused")
	private Page resolveCache(Stack<HistoryUrl> stack,String url,Page page){
		if(!stack.isEmpty()){
			HistoryUrl historyUrl=stack.pop();
			for (HistoryUrl hiUrl : historyUrl.getChild()) {
    			if(hiUrl.getUrl().equals(url)){
    				if(hiUrl.getPage()!=null){
    					page=hiUrl.getPage();
    				}
    			}
    		}
		}
		return page;
	}
	
	/**
	 * 标记，当前的标签为,返回链接
	 */
	private void markChildUrlBack(Stack<HistoryUrl> stack){
		if(!stack.isEmpty()){
			HistoryUrl historyUrl=stack.peek();
			for (HistoryUrl hiUrl : historyUrl.getChild()) {
				hiUrl.setBacktrack(true);
    		}
		}
	}
	public void backtrack(HttpServletResponse response,HistoryUrl url){
		try {
			response.sendRedirect(url.getUrl());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return;
	}
}
