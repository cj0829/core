package org.csr.core.jump;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.web.bean.ReturnMessage;

/**
 * 安全处理的两种方式，一直为重定向url
 * 一种是，ajax返回消息。
 * @author caijin
 *
 */
public interface RedirectStrategy {

    /**
     * 执行重定向到所提供的URL
     * @param request the current request
     * @param response the response to redirect
     * @param url the target URL to redirect to, for example "/login"
     */
    void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException;
    
    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    void ajaxMessage(HttpServletRequest request, HttpServletResponse response,ReturnMessage message) throws IOException;
}
