package org.csr.core.context.anonymous;

import org.csr.core.HttpAuthentication;
import org.csr.core.userdetails.UserSessionBasics;
import org.csr.core.util.ObjUtil;


public class AnonymousToken extends HttpAuthentication {
    //~ Instance fields ================================================================================================

    private static final long serialVersionUID = 1L;
    private final Object principal;
    private final int keyHash;

    //~ Constructors ===================================================================================================

    public AnonymousToken(String key, Object principal) {
        if ((key == null) || ("".equals(key)) || (principal == null) || "".equals(principal)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        this.keyHash = key.hashCode();
        this.principal = principal;
        userSession = new UserSessionBasics("游客");
        UserSessionBasics  userSessionBasics = (UserSessionBasics) userSession;
        userSessionBasics.setUserId(ObjUtil.toLong(key));
        userSessionBasics.setPhotoUrl("游客");
        userSessionBasics.setMenuStyle("right");
    }

    //~ Methods ========================================================================================================

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof AnonymousToken) {
        	AnonymousToken test = (AnonymousToken) obj;
            if (this.getKeyHash() != test.getKeyHash()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Always returns an empty <code>String</code>
     *
     * @return an empty String
     */
    public Object getCredentials() {
        return "";
    }

    public int getKeyHash() {
        return this.keyHash;
    }

    public Object getPrincipal() {
        return this.principal;
    }

	@Override
	public Object getSystemConfig() {
		
		// Auto-generated method stub
		return null;
	}

	@Override
	public Object getRequestConfig() {
		
		// Auto-generated method stub
		return null;
	}

	@Override
	public Object getResponseResult() {
		
		// Auto-generated method stub
		return null;
	}

	@Override
	public Object getSessionConfig() {
		
		// Auto-generated method stub
		return null;
	}

}
