package org.csr.core.security.session;

import java.io.Serializable;
import java.util.Date;

import org.csr.core.UserSession;
import org.csr.core.util.ObjUtil;
import org.springframework.util.Assert;


public class SessionInformation implements Serializable {

    /**
	 * 
	 */
		private static final long serialVersionUID = 7566512727411215896L;
		private Date lastRequest;
    private final UserSession userSession;
    private final String sessionId;
    private boolean expired = false;

    //~ Constructors ===================================================================================================

    public SessionInformation(UserSession userSession, String sessionId, Date lastRequest) {
        Assert.notNull(userSession, "Principal required");
        Assert.hasText(sessionId, "SessionId required");
        Assert.notNull(lastRequest, "LastRequest required");
        this.userSession = userSession;
        this.sessionId = sessionId;
        this.lastRequest = lastRequest;
    }

    //~ Methods ========================================================================================================

    public Long getUserId(){
    	if(ObjUtil.isNotEmpty(userSession)){
    		return userSession.getUserId();
    	}
    	return null;
    }
    
    public void expireNow() {
        this.expired = true;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public Object getUserSession() {
        return userSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isExpired() {
        return expired;
    }

    /**
     * Refreshes the internal lastRequest to the current date and time.
     */
    public void refreshLastRequest() {
        this.lastRequest = new Date();
    }

}
