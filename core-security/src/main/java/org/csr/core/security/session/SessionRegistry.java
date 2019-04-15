package org.csr.core.security.session;

import java.util.List;

import org.csr.core.UserSession;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;

public interface SessionRegistry {

	PagedInfo<UserSession> getAllUserSessions(Page page);

    List<SessionInformation> getAllSessions(UserSession principal, boolean includeExpiredSessions);

    SessionInformation getSessionInformation(String sessionId);

    void refreshLastRequest(String sessionId);

    void registerNewSession(String sessionId, UserSession principal);

    void removeSessionInformation(String sessionId);

	/**
	 * 将登陆用户设置为过期,队列只是存在有效的用户队列。
	 * @param sessions
	 * @param allowedSessions
	 * @param sessionRegistry
	 */
	void updateAllowableSessionsExceeded(List<SessionInformation> sessions,int allowedSessions, SessionRegistry sessionRegistry);
}
