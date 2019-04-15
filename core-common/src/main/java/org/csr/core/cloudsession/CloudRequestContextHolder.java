package org.csr.core.cloudsession;

import javax.servlet.http.HttpServletRequest;


public abstract class CloudRequestContextHolder {

	private static final ThreadLocal<HttpServletRequest> requestAttributesHolder = new ThreadLocal<HttpServletRequest>();

	/**
	 * Reset the RequestAttributes for the current thread.
	 */
	public static void resetHttpServletRequest() {
		requestAttributesHolder.remove();
	}

	public static void setRequestAttributes(HttpServletRequest attributes) {
		if (attributes == null) {
			resetHttpServletRequest();
		} else {
			requestAttributesHolder.set(attributes);
		}
	}

	public static HttpServletRequest getHttpServletRequest() {
		return requestAttributesHolder.get();
	}

	public static HttpServletRequest currentHttpServletRequest()
			throws IllegalStateException {
		HttpServletRequest attributes = getHttpServletRequest();
		if (attributes == null) {
			throw new IllegalStateException(
					"No thread-bound request found: "
							+ "Are you referring to request attributes outside of an actual web request, "
							+ "or processing a request outside of the originally receiving thread? "
							+ "If you are actually operating within a web request and still receive this message, "
							+ "your code is probably running outside of DispatcherServlet/DispatcherPortlet: "
							+ "In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
		}
		return attributes;
	}

}
