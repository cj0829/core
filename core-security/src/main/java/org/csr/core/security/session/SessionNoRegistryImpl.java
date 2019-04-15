package org.csr.core.security.session;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.UserSession;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.userdetails.UserSessionContext;
import org.springframework.util.Assert;

/**
 * ClassName:SessionRegistryImpl.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午6:50:49 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述： 不作为Session注册使用 <br/>
 * 公用方法描述：  <br/>
 */
public class SessionNoRegistryImpl implements SessionRegistry {

    //~ Instance fields ================================================================================================

    protected final Log logger = LogFactory.getLog(SessionNoRegistryImpl.class);

    /** <principal:Object,SessionIdSet> */

    //~ Methods ========================================================================================================

    @Override
    /**
     * 内存分页
     */
	public PagedInfo<UserSession> getAllUserSessions(Page page) {
    	return new PagedInfoImpl<UserSession>(Collections.<UserSession> emptyList(), page, 0);
	}
    
    @Override
    public List<SessionInformation> getAllSessions(UserSession principal, boolean includeExpiredSessions) {
        return Collections.<SessionInformation> emptyList();
    }

    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = new SessionInformation(UserSessionContext.getUserSession(),  sessionId, new Date());
        return info;
    }


    /**
     * 刷新最新登录时间
     * @see org.csr.core.security.session.SessionRegistry#refreshLastRequest(java.lang.String)
     */
    @Override
    public void refreshLastRequest(String sessionId) {
    }

    @Override
    public void registerNewSession(String sessionId, UserSession userSession) {
    }

    @Override
    public void removeSessionInformation(String sessionId) {
    }


	@Override
	public void updateAllowableSessionsExceeded(List<SessionInformation> sessions,int allowedSessions, SessionRegistry sessionRegistry) {
		
	}

}
