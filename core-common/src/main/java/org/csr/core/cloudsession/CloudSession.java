package org.csr.core.cloudsession;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:RedisSession.java <br/>
 * System Name： 在线学习系统 <br/>
 * Date: 2016-9-12下午3:12:16 <br/>
 * 
 * @author Administrator <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public final class CloudSession implements HttpSession {
	
	private static final Logger log = LoggerFactory.getLogger(CloudSession.class);
	CloudSessionManager manager = CloudSessionManager.getInstance();
	/**
	 * Type array.
	 */
	protected static final String EMPTY_ARRAY[] = new String[0];

	HttpServletRequest request;
	/**
	 * The collection of user data attributes associated with this Session.
	 */
	protected ConcurrentMap<String, Object> attributes;
	/**
	 * 作为存储的本地缓存session key value ，请求结束存入redis
	 */
	protected HashMap<String, Object> changedAttributes;
	protected Boolean dirty;

	public CloudSession(String sid, HttpServletRequest request,ConcurrentHashMap<String, Object> attributes) {
		this.id = sid;
		this.request = request;
		this.attributes = attributes;
		resetDirtyTracking();
	}

	// TODO 新添加以下属性
	/**
	 * Flag indicating whether this session is new or not.
	 */
	protected volatile boolean isNew = false;
	/**
	 * Flag indicating whether this session is valid or not.
	 */
	protected volatile boolean isValid = false;
	/**
	 * The time this session was created, in milliseconds since midnight,
	 * January 1, 1970 GMT.
	 */
	protected long creationTime = 0L;
	/**
	 * The last accessed time for this Session.
	 */
	protected volatile long lastAccessedTime = creationTime;
	/**
	 * The current accessed time for this session.
	 */
	protected volatile long thisAccessedTime = creationTime;

	/**
	 * The session identifier of this Session.
	 */
	protected String id = null;

	protected volatile int maxInactiveInterval = 1800;

	
	 /**
     * Inform the listeners about the new session.
     *
     */
    public void tellNew() {

        // Notify interested application event listeners
        Object listeners[] = manager.getApplicationEventListeners();
        if (listeners != null && listeners.length > 0) {
            HttpSessionEvent event = new HttpSessionEvent(this);
            for (int i = 0; i < listeners.length; i++) {
                if (!(listeners[i] instanceof HttpSessionListener))
                    continue;
                HttpSessionListener listener = (HttpSessionListener) listeners[i];
                try {
                    listener.sessionCreated(event);
                } catch (Throwable t) {
                    log.error("standardSession.sessionEvent", t);
                }
            }
        }

    }

    public void access() {
        this.thisAccessedTime = System.currentTimeMillis();
    }
	

	@Override
	public ServletContext getServletContext() {
		return request.getServletContext();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return request.getSession().getSessionContext();
	}

	@Override
	public Object getAttribute(String name) {
		if (!isValidInternal()) {
			throw new IllegalStateException("standardSession.getAttribute.ise");
		}
		if (name == null)
			return null;
		return (attributes.get(name));
	}

	@Override
	@Deprecated
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		 if (!isValidInternal())
	            throw new IllegalStateException("standardSession.getAttributeNames.ise");

		Set<String> names = new HashSet<>();
		names.addAll(attributes.keySet());
		return Collections.enumeration(names);
	}

	@Override
	@Deprecated
	public String[] getValueNames() {
		if (!isValidInternal()) {
			throw new IllegalStateException("standardSession.getValueNames.ise");
		}
		return (keys());
	}

	@Override
	public void setAttribute(String key, Object value) {
		Object oldValue = getAttribute(key);
		setAttribute(key, value, true);
		if ((value != null || oldValue != null)
				&& (value == null && oldValue != null || oldValue == null&& value != null
						|| !value.getClass().isInstance(oldValue) || !value.equals(oldValue))) {
			if (manager.getSaveOnChange()) {
				try {
					manager.save(this, true);
				} catch (IOException ex) {
					log.error("Error saving session on setAttribute (triggered by saveOnChange=true): "+ ex.getMessage());
				}
			} else {
				changedAttributes.put(key, value);
			}
		}
	}

	@Override
	@Deprecated
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		 if (!isValidInternal())
	            throw new IllegalStateException("standardSession.removeAttribute.ise");
		removeAttributeInternal(name, true);
		if (manager.getSaveOnChange()) {
			try {
				this.manager.save(this, true);
			} catch (IOException ex) {
				log.error("Error saving session on setAttribute (triggered by saveOnChange=true): "+ ex.getMessage());
			}
		} else {
			dirty = true;
		}
	}
	

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
		expire(true);
		System.out.println("销毁reids缓存中的session");
		manager.remove(this);
	}

	public Boolean isDirty() {
		return dirty || !changedAttributes.isEmpty();
	}

	public HashMap<String, Object> getChangedAttributes() {
		return changedAttributes;
	}

	public void resetDirtyTracking() {
		changedAttributes = new HashMap<>();
		dirty = false;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}


	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public long getThisAccessedTime() {
		return thisAccessedTime;
	}

	public void setThisAccessedTime(long thisAccessedTime) {
		this.thisAccessedTime = thisAccessedTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Return the <code>isValid</code> flag for this session without any
	 * expiration check.
	 */
	protected boolean isValidInternal() {
		return this.isValid;
	}

	protected String[] keys() {
		return attributes.keySet().toArray(EMPTY_ARRAY);
	}
	
	protected void doWriteObject(ObjectOutputStream stream) throws IOException {

		// Write the scalar instance variables (except Manager)
		stream.writeObject(Long.valueOf(creationTime));
		stream.writeObject(Long.valueOf(lastAccessedTime));
		stream.writeObject(Integer.valueOf(maxInactiveInterval));
		stream.writeObject(Boolean.valueOf(isNew));
		stream.writeObject(Boolean.valueOf(isValid));
		stream.writeObject(Long.valueOf(thisAccessedTime));
		stream.writeObject(id);
		log.debug("writeObject() storing session " + id);

		// Accumulate the names of serializable and non-serializable attributes
		String keys[] = keys();
		ArrayList<String> saveNames = new ArrayList<>();
		ArrayList<Object> saveValues = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) {
			Object value = attributes.get(keys[i]);
			if (value == null) {
				continue;
			} else if (isAttributeDistributable(keys[i], value)) {
				saveNames.add(keys[i]);
				saveValues.add(value);
			} else {
				removeAttributeInternal(keys[i], true);
			}
		}

		// Serialize the attribute count and the Serializable attributes
		int n = saveNames.size();
		stream.writeObject(Integer.valueOf(n));
		for (int i = 0; i < n; i++) {
			stream.writeObject(saveNames.get(i));
			try {
				stream.writeObject(saveValues.get(i));
				log.debug("storing attribute '" + saveNames.get(i)
						+ "' with value '" + saveValues.get(i) + "'");
			} catch (NotSerializableException e) {
				log.warn("standardSession.notSerializable" + saveNames.get(i)
						+ id, e);
			}
		}

	}
	
	protected void doReadObject(ObjectInputStream stream)
			throws ClassNotFoundException, IOException {

		// Deserialize the scalar instance variables (except Manager)
		// authType = null; // Transient only
		creationTime = ((Long) stream.readObject()).longValue();
		lastAccessedTime = ((Long) stream.readObject()).longValue();
		maxInactiveInterval = ((Integer) stream.readObject()).intValue();
		isNew = ((Boolean) stream.readObject()).booleanValue();
		isValid = ((Boolean) stream.readObject()).booleanValue();
		thisAccessedTime = ((Long) stream.readObject()).longValue();
		// principal = null; // Transient only
		// setId((String) stream.readObject());
		id = (String) stream.readObject();
		log.debug("readObject() loading session " + id);

		// Deserialize the attribute count and attribute values
		if (attributes == null)
			attributes = new ConcurrentHashMap<>();
		int n = ((Integer) stream.readObject()).intValue();
		boolean isValidSave = isValid;
		isValid = true;
		for (int i = 0; i < n; i++) {
			String name = (String) stream.readObject();
			final Object value;
			try {
				value = stream.readObject();
			} catch (WriteAbortedException wae) {
				if (wae.getCause() instanceof NotSerializableException) {
					// Skip non serializable attributes
					continue;
				}
				throw wae;
			}
			log.debug("  loading attribute '" + name + "' with value '" + value
					+ "'");
			attributes.put(name, value);
		}
		isValid = isValidSave;
	}

	
	private void removeAttributeInternal(String name, boolean notify) {

		// Avoid NPE
		if (name == null)
			return;

		// Remove this attribute from our collection
		Object value = attributes.remove(name);

		// Do we need to do valueUnbound() and attributeRemoved() notification?
		if (!notify || (value == null)) {
			return;
		}

		// Call the valueUnbound() method if necessary
		HttpSessionBindingEvent event = null;
		if (value instanceof HttpSessionBindingListener) {
			event = new HttpSessionBindingEvent(this, name, value);
			((HttpSessionBindingListener) value).valueUnbound(event);
		}

		// Notify interested application event listeners
		Object listeners[] = manager.getApplicationEventListeners();
		if (listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++) {
			if (!(listeners[i] instanceof HttpSessionAttributeListener))
				continue;
			HttpSessionAttributeListener listener = (HttpSessionAttributeListener) listeners[i];
			try {
				if (event == null) {
					event = new HttpSessionBindingEvent(this, name, value);
				}
				listener.attributeRemoved(event);
			} catch (Throwable t) {
				log.error("standardSession.attributeEvent", t);
			}
		}

	}
	
	private void setAttribute(String name, Object value, boolean notify) {

		// Name cannot be null
		if (name == null)
			throw new IllegalArgumentException(
					"standardSession.setAttribute.namenull");

		// Null value is the same as removeAttribute()
		if (value == null) {
			removeAttribute(name);
			return;
		}

		// Validate our current state
		if (!isValidInternal()) {
			throw new IllegalStateException("standardSession.setAttribute.ise");
		}
		// Construct an event with the new value
		HttpSessionBindingEvent event = null;

		// Call the valueBound() method if necessary
		if (notify && value instanceof HttpSessionBindingListener) {
			// Don't call any notification if replacing with the same value
			Object oldValue = attributes.get(name);
			if (value != oldValue) {
				event = new HttpSessionBindingEvent(this, name, value);
				try {
					((HttpSessionBindingListener) value).valueBound(event);
				} catch (Throwable t) {
					log.error("standardSession.bindingEvent", t);
				}
			}
		}

		// Replace or add this attribute
		Object unbound = attributes.put(name, value);

		// Call the valueUnbound() method if necessary
		if (notify && (unbound != null) && (unbound != value)
				&& (unbound instanceof HttpSessionBindingListener)) {
			try {
				((HttpSessionBindingListener) unbound).valueUnbound(new HttpSessionBindingEvent(this,name));
			} catch (Throwable t) {
	             log.error("standardSession.bindingEvent", t);
			}
		}

		if (!notify)
			return;

		// Notify interested application event listeners
		Object listeners[] = manager.getApplicationEventListeners();
		if (listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++) {
			if (!(listeners[i] instanceof HttpSessionAttributeListener))
				continue;
			HttpSessionAttributeListener listener = (HttpSessionAttributeListener) listeners[i];
			try {
				if (unbound != null) {
					if (event == null) {
						event = new HttpSessionBindingEvent(this, name,unbound);
					}
					listener.attributeReplaced(event);
				} else {
					if (event == null) {
						event = new HttpSessionBindingEvent(this, name,value);
					}
					listener.attributeAdded(event);
				}
			} catch (Throwable t) {
				log.error("standardSession.attributeEvent",t);
			}
		}

	}
	private void expire(boolean notify) {
        // Check to see if session has already been invalidated.
        // Do not check expiring at this point as expire should not return until
        // isValid is false
        if (!isValid)
            return;

        synchronized (this) {
            if (!isValid)
                return;
            if (manager == null)
                return;
            // Notify interested application event listeners
            // FIXME - Assumes we call listeners in reverse order
            // The call to expire() may not have been triggered by the webapp.
            // Make sure the webapp's class loader is set when calling the
            // listeners
            if (notify) {
                Object listeners[] = manager.getApplicationEventListeners();
                if (listeners != null && listeners.length > 0) {
                    HttpSessionEvent event =  new HttpSessionEvent(this);
                    for (int i = 0; i < listeners.length; i++) {
                        int j = (listeners.length - 1) - i;
                        if (!(listeners[j] instanceof HttpSessionListener))
                            continue;
                        HttpSessionListener listener =  (HttpSessionListener) listeners[j];
                        try {
                            listener.sessionDestroyed(event);
                        } catch (Throwable t) {
                        	 log.error("standardSession.sessionEvent", t);
                        }
                    }
                }
            }
            setValid(false);
        }

    }
	public boolean isAttributeDistributable(String name, Object value) {
        return value instanceof Serializable;
    }
}
