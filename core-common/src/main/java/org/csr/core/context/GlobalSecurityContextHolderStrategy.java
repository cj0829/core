package org.csr.core.context;

import org.springframework.util.Assert;


/**
 * 
 * @author caijin
 *
 */
final class GlobalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    //~ Static fields/initializers =====================================================================================

    private static SecurityContext contextHolder;

    //~ Methods ========================================================================================================

    public void clearContext() {
        contextHolder = null;
    }

    public SecurityContext getContext() {
        if (contextHolder == null) {
            contextHolder = new SecurityContextImpl();
        }

        return contextHolder;
    }

    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder = context;
    }

    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
