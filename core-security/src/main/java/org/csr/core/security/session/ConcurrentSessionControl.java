package org.csr.core.security.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.security.exception.AuthenticationException;
import org.springframework.util.Assert;

/**
 * 策略，处理的并发会话的控制，除了通过基类所提供的功能。
 */
public class ConcurrentSessionControl extends SessionFixationProtection{
	private final SessionRegistry sessionRegistry;
	//在线存在是否有效用户队列的数量
	private int maximumSessions = 5;

	public ConcurrentSessionControl(SessionRegistry sessionRegistry) {
		Assert.notNull(sessionRegistry, "The sessionRegistry cannot be null");
		super.setAlwaysCreateSession(true);
		this.sessionRegistry = sessionRegistry;
	}

	public void onAuthentication(Authentication authentication,HttpServletRequest request, HttpServletResponse response) {
		checkAuthenticationAllowed(authentication, request);
		super.onAuthentication(authentication, request, response);
		//注册当前用户到 这里开启了新的线程
		sessionRegistry.registerNewSession(request.getSession().getId(),authentication.getUserSession());
		//并且注册，用户到session中，
		request.getSession().setAttribute(Constants.SECURITY_CONTEXT_KEY, authentication.getUserSession());
	}

	protected int getMaximumSessionsForThisUser(Authentication authentication) {
		return maximumSessions;
	}


	private void checkAuthenticationAllowed(Authentication authentication,HttpServletRequest request) throws AuthenticationException {

		final List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getUserSession(), false);

		int sessionCount = sessions.size();
		int allowedSessions = getMaximumSessionsForThisUser(authentication);

		if (sessionCount < allowedSessions) {
			//符合登陆的用户数量
			return;
		}
		if (allowedSessions == -1) {
			// 对同时登陆的用户数量没有限制
			return;
		}
		// 如果当前用户就是此用户
		if (sessionCount == allowedSessions) {
			// 如果此次sessionId已经存在则放行
			HttpSession session = request.getSession(false);
			if (session != null) {
				for (SessionInformation si : sessions) {
					if (si.getSessionId().equals(session.getId())) {
						return;
					}
				}
			}
		}
		//将最早登录用户标识为过期
		sessionRegistry.updateAllowableSessionsExceeded(sessions, allowedSessions, sessionRegistry);
	}


	public void setMaximumSessions(int maximumSessions) {
		Assert.isTrue(maximumSessions != 0,"MaximumLogins must be either -1 to allow unlimited logins, or a positive integer to specify a maximum");
		this.maximumSessions = maximumSessions;
	}
}
