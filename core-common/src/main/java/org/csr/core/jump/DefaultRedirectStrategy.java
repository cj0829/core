package org.csr.core.jump;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.util.UrlUtil;
import org.csr.core.web.bean.ReturnMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class DefaultRedirectStrategy implements RedirectStrategy {

    protected final Log logger = LogFactory.getLog(getClass());

    private boolean contextRelative;

    /**
     * 重定向到指定的url资源
     */
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
    	response.reset();
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to '" + redirectUrl + "'");
        }

        response.sendRedirect(redirectUrl);
    }
    @Override
	public void ajaxMessage(HttpServletRequest request,HttpServletResponse response,ReturnMessage message) throws IOException{
    		try{
    			if(!response.isCommitted()){
    				response.setContentType("text/json; charset=UTF-8");
    				// 获取原始数据
    				SerializeConfig jsonConfig = new SerializeConfig(); 
    				String dateFormat = "yyyy-MM-dd HH:mm";  
    				jsonConfig.put(Date.class, new SimpleDateFormatSerializer(dateFormat));  
    		//		这种写法，有可能会让浏览器等待(必须	response.setContentType("text/json; charset=UTF-8");不然)
    				response.getWriter().print(JSON.toJSONString(message,jsonConfig, SerializerFeature.DisableCircularReferenceDetect));
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
		
	}
    private String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtil.isAbsoluteUrl(url)) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://

        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualified URL, minus the scheme and base context.
        url = url.substring(url.indexOf("://") + 3); // strip off scheme
        url = url.substring(url.indexOf(contextPath) + contextPath.length());

        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    /**
     * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
     * and context path (defaults to <tt>false</tt>).
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }

	

}
