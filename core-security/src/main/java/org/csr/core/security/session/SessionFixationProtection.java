package org.csr.core.security.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.Authentication;

/**
 * 固定session管理策略
 * @author caijin
 *
 */
public class SessionFixationProtection implements SessionAuthentication {
	
    protected final Log logger = LogFactory.getLog(this.getClass());
    private boolean migrateSessionAttributes = true;

    private List<String> retainedAttributes = null;

    private boolean alwaysCreateSession;

    /**
     * 复制session
     */
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        boolean hadSessionAlready = request.getSession(false) != null;
        if (!hadSessionAlready && !alwaysCreateSession) {
            return;
        }
        //创建一个新的session
        HttpSession session = request.getSession();

        if (hadSessionAlready && request.isRequestedSessionIdValid()) {
            String originalSessionId = session.getId();
            Map<String, Object> attributesToMigrate = extractAttributes(session);
            session.invalidate();
            session = request.getSession(true); // we now have a new session 
            transferAttributes(attributesToMigrate, session);
            onSessionChange(originalSessionId, session, authentication);
        }
    }

    protected void onSessionChange(String originalSessionId, HttpSession newSession, Authentication auth) {
    }

    protected Map<String, Object> extractAttributes(HttpSession session) {
        return createMigratedAttributeMap(session);
    }

    private void transferAttributes(Map<String, Object> attributes, HttpSession newSession) {
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                newSession.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    private HashMap<String, Object> createMigratedAttributeMap(HttpSession session) {
        HashMap<String, Object> attributesToMigrate = null;

        if (migrateSessionAttributes || retainedAttributes == null) {
            attributesToMigrate = new HashMap<String, Object>();

            @SuppressWarnings("rawtypes")
			Enumeration enumer = session.getAttributeNames();

            while (enumer.hasMoreElements()) {
                String key = (String) enumer.nextElement();
                if (!migrateSessionAttributes && !key.startsWith("SPRING_SECURITY_")) {
                    // Only retain Spring Security attributes
                    continue;
                }
                attributesToMigrate.put(key, session.getAttribute(key));
            }
        } else {
            // Only retain the attributes which have been specified in the retainAttributes list
            if (!retainedAttributes.isEmpty()) {
                attributesToMigrate = new HashMap<String, Object>();
                for (String name : retainedAttributes) {
                    Object value = session.getAttribute(name);

                    if (value != null) {
                        attributesToMigrate.put(name, value);
                    }
                }
            }
        }
        return attributesToMigrate;
    }

    public void setMigrateSessionAttributes(boolean migrateSessionAttributes) {
        this.migrateSessionAttributes = migrateSessionAttributes;
    }

    public void setAlwaysCreateSession(boolean alwaysCreateSession) {
        this.alwaysCreateSession = alwaysCreateSession;
    }
}
