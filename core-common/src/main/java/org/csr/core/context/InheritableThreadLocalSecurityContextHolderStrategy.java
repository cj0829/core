package org.csr.core.context;

import org.springframework.util.Assert;


/**
 * 
 * @author caijin
 *
 */
final class InheritableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    //~ Static fields/initializers =====================================================================================

    private static final ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<SecurityContext>();

    //~ Methods ========================================================================================================

    public void clearContext() {
        contextHolder.remove();
    }

    public SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
