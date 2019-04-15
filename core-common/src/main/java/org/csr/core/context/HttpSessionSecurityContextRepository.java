package org.csr.core.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.core.Authentication;
import org.csr.core.context.anonymous.AuthenticationTrustResolver;
import org.csr.core.context.anonymous.AuthenticationTrustResolverImpl;
import org.springframework.util.Assert;


public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
    /**
     * The default key under which the security context will be stored in the session.
     */
    public static final String PMT_SECURITY_CONTEXT_KEY = "PMT_SECURITY_CONTEXT_KEY";

    protected final Log logger = LogFactory.getLog(this.getClass());

    SecurityContextRepository securityContextRepository;
    /** SecurityContext instance used to check for equality with default (unauthenticated) content */
    private final Object contextObject = SecurityContextHolder.createEmptyContext();
    private boolean allowSessionCreation = true;
    private boolean disableUrlRewriting = false;
    private String pmtSecurityContextKey = PMT_SECURITY_CONTEXT_KEY;

    private final AuthenticationTrustResolver authenticationTrustResolver =  new AuthenticationTrustResolverImpl();

    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        HttpSession httpSession = request.getSession(false);

        SecurityContext context = readSecurityContextFromSession(httpSession);

        if (context == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No SecurityContext was available from the HttpSession: " + httpSession +". " +
                        "A new one will be created.");
            }
            context = generateNewContext();
        }
        requestResponseHolder.setResponse(new SaveToSessionResponseWrapper(response, request, httpSession != null, context));
        return context;
    }

    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        SaveContextOnUpdateOrErrorResponseWrapper responseWrapper = (SaveContextOnUpdateOrErrorResponseWrapper)response;
        if (!responseWrapper.isContextSaved() ) {
            responseWrapper.saveContext(context);
        }
    }

    public boolean containsContext(HttpServletRequest request) {
    	
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
//        System.out.println("pmtSecurity:"+session.getAttribute(pmtSecurityContextKey));
        return session.getAttribute(pmtSecurityContextKey) != null;
    }

    private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        final boolean debug = logger.isDebugEnabled();

        if (httpSession == null) {
            if (debug) {
                logger.debug("No HttpSession currently exists");
            }

            return null;
        }

        Object contextFromSession = httpSession.getAttribute(pmtSecurityContextKey);
        if (contextFromSession == null) {
            if (debug) {
                logger.debug("HttpSession returned null object for SPRING_SECURITY_CONTEXT");
            }
            return null;
        }

        if (!(contextFromSession instanceof SecurityContext)) {

            return null;
        }
        // Everything OK. The only non-null return from this method.
        return (SecurityContext) contextFromSession;
    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }

    public void setDisableUrlRewriting(boolean disableUrlRewriting) {
        this.disableUrlRewriting = disableUrlRewriting;
    }

    public void setPmtSecurityContextKey(String pmtSecurityContextKey) {
        Assert.hasText(pmtSecurityContextKey, "springSecurityContextKey cannot be empty");
        this.pmtSecurityContextKey = pmtSecurityContextKey;
    }

    //~ Inner Classes ==================================================================================================

    final class SaveToSessionResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {

        private final HttpServletRequest request;
        private final boolean httpSessionExistedAtStartOfRequest;
        private final SecurityContext contextBeforeExecution;
        private final Authentication authBeforeExecution;

        SaveToSessionResponseWrapper(HttpServletResponse response, HttpServletRequest request,boolean httpSessionExistedAtStartOfRequest,
                                                      SecurityContext context) {
            super(response, disableUrlRewriting);
            this.request = request;
            this.httpSessionExistedAtStartOfRequest = httpSessionExistedAtStartOfRequest;
            this.contextBeforeExecution = context;
            this.authBeforeExecution = context.getAuthentication();
        }

        @Override
        protected void saveContext(SecurityContext context) {
            final Authentication authentication = context.getAuthentication();
            HttpSession httpSession = request.getSession(false);
            // See SEC-776
            if (authentication == null || authenticationTrustResolver.isAnonymous(authentication) ) {
                if (logger.isDebugEnabled()) {
                    logger.debug("SecurityContext is empty or contents are anonymous - context will not be stored in HttpSession.");
                }

                if (httpSession != null && !contextObject.equals(contextBeforeExecution)) {
                    httpSession.removeAttribute(pmtSecurityContextKey);
                }
                return;
            }

            if (httpSession == null) {
                httpSession = createNewSessionIfAllowed(context);
            }

            if (httpSession != null) {
                if (contextChanged(context) || httpSession.getAttribute(pmtSecurityContextKey) == null) {
                    httpSession.setAttribute(pmtSecurityContextKey, context);

                    if (logger.isDebugEnabled()) {
                        logger.debug("SecurityContext stored to HttpSession: '" + context + "'");
                    }
                }
            }
        }

        private boolean contextChanged(SecurityContext context) {
            return context != contextBeforeExecution || context.getAuthentication() != authBeforeExecution;
        }

        private HttpSession createNewSessionIfAllowed(SecurityContext context) {
            if (httpSessionExistedAtStartOfRequest) {

                return null;
            }

            if (!allowSessionCreation) {

                return null;
            }
            // Generate a HttpSession only if we need to

            if (contextObject.equals(context)) {

                return null;
            }


            try {
                return request.getSession(true);
            } catch (IllegalStateException e) {
                logger.warn("Failed to create a session, as response has been committed. Unable to store" +
                        " SecurityContext.");
            }

            return null;
        }
    }

}
