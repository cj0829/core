package org.csr.core.security.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.UserSession;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.springframework.util.Assert;

/**
 * ClassName:SessionRegistryImpl.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午6:50:49 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述： 注册session <br/>
 * 公用方法描述：  <br/>
 */
public class SessionRegistryImpl implements SessionRegistry {

    //~ Instance fields ================================================================================================

    protected final Log logger = LogFactory.getLog(SessionRegistryImpl.class);

    /** <principal:Object,SessionIdSet> */
    private final ConcurrentMap<UserSession,Set<String>> principals = new ConcurrentHashMap<UserSession,Set<String>>();
    /** <sessionId:Object,SessionInformation> */
    private final Map<String, SessionInformation> sessionIds = new ConcurrentHashMap<String, SessionInformation>();

    //~ Methods ========================================================================================================

    @Override
    /**
     * 内存分页
     */
	public PagedInfo<UserSession> getAllUserSessions(Page page) {
		List<UserSession> list = new ArrayList<UserSession>(principals.keySet());
		int total = list.size();
		int toIndex = page.getPageNumber() * page.getPageSize();
		if (toIndex < total) {
			return new PagedInfoImpl<UserSession>(list.subList(page.getOffset(), toIndex), page, total);
		} else {
			return new PagedInfoImpl<UserSession>(list.subList(page.getOffset(), total), page, total);
		}
	}
    
    @Override
    public List<SessionInformation> getAllSessions(UserSession principal, boolean includeExpiredSessions) {
        final Set<String> sessionsUsedByPrincipal = principals.get(principal);

        if (sessionsUsedByPrincipal == null) {
            return Collections.emptyList();
        }

        List<SessionInformation> list = new ArrayList<SessionInformation>(sessionsUsedByPrincipal.size());

         for (String sessionId : sessionsUsedByPrincipal) {
            SessionInformation sessionInformation = getSessionInformation(sessionId);
            if (sessionInformation == null) {
                continue;
            }
            if (includeExpiredSessions || !sessionInformation.isExpired()) {
                list.add(sessionInformation);
            }
        }
        return list;
    }

    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        return sessionIds.get(sessionId);
    }


    /**
     * 刷新最新登录时间
     * @see org.csr.core.security.session.SessionRegistry#refreshLastRequest(java.lang.String)
     */
    @Override
    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = getSessionInformation(sessionId);
        if (info != null) {
            info.refreshLastRequest();
        }
    }

    @Override
    public void registerNewSession(String sessionId, UserSession userSession) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(userSession, "Principal required as per interface contract");

        if (logger.isDebugEnabled()) {
            logger.debug("Registering session " + sessionId +", for principal " + userSession);
        }
        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }
        sessionIds.put(sessionId, new SessionInformation(userSession, sessionId, new Date()));
        Set<String> sessionsUsedByPrincipal = principals.get(userSession);
        if (sessionsUsedByPrincipal == null) {
            sessionsUsedByPrincipal = new CopyOnWriteArraySet<String>();
            Set<String> prevSessionsUsedByPrincipal = principals.putIfAbsent(userSession,sessionsUsedByPrincipal);
            if (prevSessionsUsedByPrincipal != null) {
                sessionsUsedByPrincipal = prevSessionsUsedByPrincipal;
            }
        }

        sessionsUsedByPrincipal.add(sessionId);
        if (logger.isTraceEnabled()) {
            logger.trace("Sessions used by '" + userSession + "' : " + sessionsUsedByPrincipal);
        }
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInformation info = getSessionInformation(sessionId);
        if (info == null) {
            return;
        }

        if (logger.isTraceEnabled()) {
            logger.debug("Removing session " + sessionId + " from set of registered sessions");
        }

        sessionIds.remove(sessionId);

        Set<String> sessionsUsedByPrincipal = principals.get(info.getUserId());

        if (sessionsUsedByPrincipal == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Removing session " + sessionId + " from principal's set of registered sessions");
        }

        sessionsUsedByPrincipal.remove(sessionId);

        if (sessionsUsedByPrincipal.isEmpty()) {
            // No need to keep object in principals Map anymore
            if (logger.isDebugEnabled()) {
                logger.debug("Removing principal " + info.getUserId() + " from registry");
            }
            principals.remove(info.getUserId());
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Sessions used by '" + info.getUserId() + "' : " + sessionsUsedByPrincipal);
        }
    }


	@Override
	public void updateAllowableSessionsExceeded(List<SessionInformation> sessions,int allowedSessions, SessionRegistry sessionRegistry) {
		if ( (sessions == null)) {
			Exceptions.service("", "");
		}
		// 确定最近最少使用的会话，并将其标记为无效
		SessionInformation leastRecentlyUsed = null;
		for (SessionInformation session : sessions) {
			if ((leastRecentlyUsed == null) || session.getLastRequest().before(leastRecentlyUsed.getLastRequest())) {
				leastRecentlyUsed = session;
			}
		}
		//设置当前用户
		leastRecentlyUsed.expireNow();
	}
}
