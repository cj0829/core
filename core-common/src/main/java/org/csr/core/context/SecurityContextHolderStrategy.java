package org.csr.core.context;

public interface SecurityContextHolderStrategy {

    void clearContext();

    SecurityContext getContext();
    void setContext(SecurityContext context);
    SecurityContext createEmptyContext();
}
