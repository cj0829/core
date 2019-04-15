package org.csr.core.security.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.exception.Exceptions;
import org.csr.core.security.BaseSecurityFilter;
import org.csr.core.security.support.VirtualFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.Header;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.ResponseHeadersNotModifiableException;
import net.sf.ehcache.constructs.web.ResponseUtil;
import net.sf.ehcache.constructs.web.SerializableCookie;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;



public abstract class EhCachingFilter extends BaseSecurityFilter {

    private static final Logger LOG = LoggerFactory.getLogger(EhCachingFilter.class);
    private static final String BLOCKING_TIMEOUT_MILLIS = "blockingTimeoutMillis";
    private static final String CACHE_NAME = "cacheName";
     
    protected List<String> cacheURLs;
    
    
    Integer blockingTimeoutMillis=0;
    
    /**
     * The cache name can be set through init parameters. If it is set it is
     * stored here.
     */
    protected String cacheName="cacheName";

    /**
     * The cache holding the web pages. Ensure that all threads for a given
     * cache name are using the same instance of this.
     */
    protected BlockingCache blockingCache;

    private final VisitLog visitLog = new VisitLog();

    /**
     * Initialises blockingCache to use. The BlockingCache created by this
     * method does not have a lock timeout set.
     * <p/>
     * A timeout can be appled using
     * <code>blockingCache.setTimeoutMillis(int timeout)</code> and takes effect
     * immediately for all new requests
     * 
     * @throws CacheException
     *             The most likely cause is that a cache has not been configured
     *             in ehcache's configuration file ehcache.xml for the filter
     *             name
     * @param filterConfig
     *            this filter's configuration.
     */
    public void afterPropertiesSet() throws ServletException{
    	 synchronized (this.getClass()) {
             if (blockingCache == null) {
                 final String localCacheName = getCacheName();
                 Ehcache cache = getCacheManager().getEhcache(localCacheName);
                 if (cache == null) {
                     throw new CacheException("cache '" + localCacheName + "' not found in configuration");
                 }
                 if (!(cache instanceof BlockingCache)) {
                     // decorate and substitute
                     BlockingCache newBlockingCache = new BlockingCache(cache);
                     getCacheManager().replaceCacheWithDecoratedCache(cache, newBlockingCache);
                 }
                 blockingCache = (BlockingCache) getCacheManager().getEhcache(localCacheName);
                 if (blockingTimeoutMillis != null && blockingTimeoutMillis > 0) {
                     blockingCache.setTimeoutMillis(blockingTimeoutMillis);
                 }
             }
         }
    }

    /**
     * Destroys the filter.
     */
    protected void doDestroy() {
        // noop
    }

	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, VirtualFilterChain chain)
			throws IOException, ServletException {
		if (response.isCommitted()) {
			throw new AlreadyCommittedException(
					"Response already committed before doing buildPage.");
		}
		logRequestHeaders(request);
		PageInfo pageInfo;
		try {
			pageInfo = buildPageInfo(request, response, chain);
			if (pageInfo.isOk()) {
				if (response.isCommitted()) {
					throw new AlreadyCommittedException(
							"Response already committed after doing buildPage"
									+ " but before writing response from PageInfo.");
				}
				writeResponse(request, response, pageInfo);
			}
		} catch (Exception e) {
			Exceptions.service("", e.getMessage());

		}
	}

    /**
     * Build page info either using the cache or building the page directly.
     * <p/>
     * Some requests are for page fragments which should never be gzipped, or
     * for other pages which are not gzipped.
     */
    protected PageInfo buildPageInfo(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws Exception {
        // Look up the cached page
        final String key = calculateKey(request);
        PageInfo pageInfo = null;
        try {
            checkNoReentry(request);
            Element element = blockingCache.get(key);
            if (element == null || element.getObjectValue() == null) {
                try {
                    // Page is not cached - build the response, cache it, and
                    // send to client
                    pageInfo = buildPage(request, response, chain);
                    if (pageInfo.isOk()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("PageInfo ok. Adding to cache "
                                    + blockingCache.getName() + " with key "
                                    + key);
                        }
                        blockingCache.put(new Element(key, pageInfo));
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("PageInfo was not ok(200). Putting null into cache "
                                    + blockingCache.getName()
                                    + " with key "
                                    + key);
                        }
                        blockingCache.put(new Element(key, null));
                    }
                } catch (final Throwable throwable) {
                    // Must unlock the cache if the above fails. Will be logged
                    // at Filter
                    blockingCache.put(new Element(key, null));
                    throw new Exception(throwable);
                }
            } else {
                pageInfo = (PageInfo) element.getObjectValue();
            }
        } catch (LockTimeoutException e) {
            // do not release the lock, because you never acquired it
            throw e;
        } finally {
            // all done building page, reset the re-entrant flag
            visitLog.clear();
        }
        return pageInfo;
    }

    /**
     * Builds the PageInfo object by passing the request along the filter chain
     * 
     * @param request
     * @param response
     * @param chain
     * @return a Serializable value object for the page or page fragment
     * @throws AlreadyGzippedException
     *             if an attempt is made to double gzip the body
     * @throws Exception
     */
    protected PageInfo buildPage(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws AlreadyGzippedException, Exception {

        // Invoke the next entity in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        final GenericResponseWrapper wrapper = new GenericResponseWrapper(
                response, outstr);
        chain.doFilter(request, wrapper);
        wrapper.flush();

        long timeToLiveSeconds = blockingCache.getCacheConfiguration()
                .getTimeToLiveSeconds();

        // Return the page info
        return new PageInfo(wrapper.getStatus(), wrapper.getContentType(),
                wrapper.getCookies(), outstr.toByteArray(), true,
                timeToLiveSeconds, wrapper.getAllHeaders());
    }

    /**
     * Writes the response from a PageInfo object.
     * <p/>
     * Headers are set last so that there is an opportunity to override
     * 
     * @param request
     * @param response
     * @param pageInfo
     * @throws IOException
     * @throws DataFormatException
     * @throws ResponseHeadersNotModifiableException
     * 
     */
    protected void writeResponse(final HttpServletRequest request,
            final HttpServletResponse response, final PageInfo pageInfo)
            throws IOException, DataFormatException,
            ResponseHeadersNotModifiableException {
        boolean requestAcceptsGzipEncoding = acceptsGzipEncoding(request);

        setStatus(response, pageInfo);
        setContentType(response, pageInfo);
        setCookies(pageInfo, response);
        // do headers last so that users can override with their own header sets
        setHeaders(pageInfo, requestAcceptsGzipEncoding, response);
        writeContent(request, response, pageInfo);
    }

    /**
     * Set the content type.
     * 
     * @param response
     * @param pageInfo
     */
    protected void setContentType(final HttpServletResponse response,
            final PageInfo pageInfo) {
        String contentType = pageInfo.getContentType();
        if (contentType != null && contentType.length() > 0) {
            response.setContentType(contentType);
        }
    }

    /**
     * Set the serializableCookies
     * 
     * @param pageInfo
     * @param response
     */
    protected void setCookies(final PageInfo pageInfo,
            final HttpServletResponse response) {

        final Collection cookies = pageInfo.getSerializableCookies();
        for (Iterator iterator = cookies.iterator(); iterator.hasNext();) {
            final Cookie cookie = ((SerializableCookie) iterator.next())
                    .toCookie();
            response.addCookie(cookie);
        }
    }

    /**
     * Status code
     * 
     * @param response
     * @param pageInfo
     */
    protected void setStatus(final HttpServletResponse response,
            final PageInfo pageInfo) {
        response.setStatus(pageInfo.getStatusCode());
    }

    /**
     * Set the headers in the response object, excluding the Gzip header
     * 
     * @param pageInfo
     * @param requestAcceptsGzipEncoding
     * @param response
     */
    protected void setHeaders(final PageInfo pageInfo,
            boolean requestAcceptsGzipEncoding,
            final HttpServletResponse response) {

        final Collection<Header<? extends Serializable>> headers = pageInfo
                .getHeaders();

        // Track which headers have been set so all headers of the same name
        // after the first are added
        final TreeSet<String> setHeaders = new TreeSet<String>(
                String.CASE_INSENSITIVE_ORDER);

        for (final Header<? extends Serializable> header : headers) {
            final String name = header.getName();

            switch (header.getType()) {
            case STRING:
                if (setHeaders.contains(name)) {
                    response.addHeader(name, (String) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setHeader(name, (String) header.getValue());
                }
                break;
            case DATE:
                if (setHeaders.contains(name)) {
                    response.addDateHeader(name, (Long) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setDateHeader(name, (Long) header.getValue());
                }
                break;
            case INT:
                if (setHeaders.contains(name)) {
                    response.addIntHeader(name, (Integer) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setIntHeader(name, (Integer) header.getValue());
                }
                break;
            default:
                throw new IllegalArgumentException("No mapping for Header: "
                        + header);
            }
        }
    }

    /**
     * A meaningful name representative of the JSP page being cached.
     * <p/>
     * The <code>cacheName</code> field is be set by the <code>doInit</code>
     * method. Override to control the name used. The significance is that the
     * name is used to find a cache configuration in <code>ehcache.xml</code>
     * 
     * @return the name of the cache to use for this filter.
     */
    protected String getCacheName() {
        return cacheName;
    }

    /**
     * Gets the CacheManager for this CachingFilter. It is therefore up to
     * subclasses what CacheManager to use.
     * <p/>
     * This method was introduced in ehcache 1.2.1. Older versions used a
     * singleton CacheManager instance created with the default factory method.
     * 
     * @return the CacheManager to be used
     * @since 1.2.1
     */
    protected abstract CacheManager getCacheManager();

    /**
     * CachingFilter works off a key.
     * <p/>
     * The key should be unique. Factors to consider in generating a key are:
     * <ul>
     * <li>The various hostnames that a request could come through
     * <li>Whether additional parameters used for referral tracking e.g. google
     * should be excluded to maximise cache hits
     * <li>Additional parameters can be added to any page. The page will still
     * work but will miss the cache. Consider coding defensively around this
     * issue.
     * </ul>
     * <p/>
     * Implementers should differentiate between GET and HEAD requests otherwise
     * blank pages can result. See SimplePageCachingFilter for an example
     * implementation.
     * 
     * @param httpRequest
     * @return the key, generally the URL plus request parameters
     */
    protected abstract String calculateKey(final HttpServletRequest httpRequest);

    /**
     * Writes the response content. This will be gzipped or non gzipped
     * depending on whether the User Agent accepts GZIP encoding.
     * <p/>
     * If the body is written gzipped a gzip header is added.
     * 
     * @param response
     * @param pageInfo
     * @throws IOException
     */
    protected void writeContent(final HttpServletRequest request,
            final HttpServletResponse response, final PageInfo pageInfo)
            throws IOException, ResponseHeadersNotModifiableException {
        byte[] body;

        boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request,
                pageInfo.getStatusCode());
        if (shouldBodyBeZero) {
            body = new byte[0];
        } else if (acceptsGzipEncoding(request)) {
            body = pageInfo.getGzippedBody();
            if (ResponseUtil.shouldGzippedBodyBeZero(body, request)) {
                body = new byte[0];
            } else {
                ResponseUtil.addGzipHeader(response);
            }

        } else {
            body = pageInfo.getUngzippedBody();
        }

        response.setContentLength(body.length);
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(body);
        out.flush();
    }

    /**
     * Check that this caching filter is not being reentered by the same
     * recursively. Recursive calls will block indefinitely because the first
     * request has not yet unblocked the cache.
     * <p/>
     * This condition usually indicates an error in filter chaining or
     * RequestDispatcher dispatching.
     * 
     * @param httpRequest
     * @throws FilterNonReentrantException
     *             if reentry is detected
     */
    protected void checkNoReentry(final HttpServletRequest httpRequest)
            throws FilterNonReentrantException {
        String filterName = getClass().getName();
        if (visitLog.hasVisited()) {
            throw new FilterNonReentrantException(
                    "The request thread is attempting to reenter" + " filter "
                            + filterName + ". URL: "
                            + httpRequest.getRequestURL());
        } else {
            // mark this thread as already visited
            visitLog.markAsVisited();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread {}  has been marked as visited.", Thread
                        .currentThread().getName());
            }
        }
    }
    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
        return acceptsEncoding(request, "gzip");
    }
    /**
     * Checks if request accepts the named encoding.
     */
    protected boolean acceptsEncoding(final HttpServletRequest request, final String name) {
        final boolean accepts = headerContains(request, "Accept-Encoding", name);
        return accepts;
    }

    protected void logRequestHeaders(final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            Map headers = new HashMap();
            Enumeration enumeration = request.getHeaderNames();
            StringBuffer logLine = new StringBuffer();
            logLine.append("Request Headers");
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                String headerValue = request.getHeader(name);
                headers.put(name, headerValue);
                logLine.append(": ").append(name).append(" -> ").append(headerValue);
            }
            LOG.debug(logLine.toString());
        }
    }

    private boolean headerContains(final HttpServletRequest request, final String header, final String value) {

        logRequestHeaders(request);

        final Enumeration accepted = request.getHeaders(header);
        while (accepted.hasMoreElements()) {
            final String headerValue = (String) accepted.nextElement();
            if (headerValue.indexOf(value) != -1) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * threadlocal class to check for reentry
     * 
     * @author hhuynh
     * 
     */
    private static class VisitLog extends ThreadLocal<Boolean> {
        @Override
        protected Boolean initialValue() {
            return false;
        }

        public boolean hasVisited() {
            return get();
        }

        public void markAsVisited() {
            set(true);
        }

        public void clear() {
            super.remove();
        }
    }

	public List<String> getCacheURLs() {
		return cacheURLs;
	}

	public void setCacheURLs(List<String> cacheURLs) {
		this.cacheURLs = cacheURLs;
	}

	public Integer getBlockingTimeoutMillis() {
		return blockingTimeoutMillis;
	}

	public void setBlockingTimeoutMillis(Integer blockingTimeoutMillis) {
		this.blockingTimeoutMillis = blockingTimeoutMillis;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

}
