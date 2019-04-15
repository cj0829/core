package org.csr.core.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.SecurityResource;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.queue.QueueService;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.exception.SecurityExceptions;
import org.csr.core.security.message.BrowseLogMessage;
import org.csr.core.security.support.VirtualFilterChain;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.UrlUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public  class LogFilter extends BaseSecurityFilter {
	// ~ Static fields/initializers
	// =====================================================================================

	protected final Log logger = LogFactory.getLog(getClass());
	private PathMatcher matcher = new AntPathMatcher();

	// ~ Methods
	// ========================================================================================================

	/**
	 * 检验参数设置，及常规配置
	 * 
	 * @throws ServletException
	 */
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
	}

	@Override
	public void doFilter(HttpServletRequest request,HttpServletResponse response, VirtualFilterChain chain)
			throws IOException, ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (ObjUtil.isEmpty(authentication)) {
			logger.info("没有访问权限："+UrlUtil.requiresAjax(request));
			SecurityExceptions.authentication("No permission", "没有访问权限",UrlUtil.requiresAjax(request));
			return;
		}
	
		if (!Constants.USER_SUPER.equals(SecurityContextHolder.getContext().getAuthentication().getUserSession().getLoginName())) {
			// 记录登录日志。
			QueueService queueService = (QueueService) ClassBeanFactory.getBean("queueService");
			//如果，如果当前连接无法获取到功能的，则就不记日志。因为，在测试是，会全开权限点。则有些url无法对应到权限点。
			SecurityResource sr = authentication.getUserSession().getSecurityResource();
			if (ObjUtil.isNotEmpty(sr) && sr.getBrowseLogLevel() >= 2) {
				logger.info("记录操作日志："+request.getRequestURI());
				queueService.sendMessage(new BrowseLogMessage(authentication.getUserSession()));
			}
		}
		chain.doFilter(request, response);
	}
}
