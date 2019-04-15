package org.csr.core.cloudsession;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.csr.core.cache.RedisUtil;
import org.csr.core.util.ObjUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * 
 * ClassName:RedisSessionManager.java <br/>
 * System Name：    在线学习系统  <br/>
 * Date:     2016-9-20下午4:41:54 <br/>
 * @author   Administrator <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class CloudSessionManager {
	
	
	private List<Object> applicationEventListenersList = new CopyOnWriteArrayList<>();
	
	private static final Logger log = LoggerFactory.getLogger(CloudSessionManager.class);
	
	protected int sessionTimeout = 1800;
	
	/**
	 * session 改变时保存策略
	 * 1.默认值（请求结束后保存）
	 * 2.改变立即入reids库
	 * 3.请求结束后保存
	 * */
	enum SessionPersistPolicy {
		DEFAULT, SAVE_ON_CHANGE, ALWAYS_SAVE_AFTER_REQUEST;
		static SessionPersistPolicy fromName(String name) {
			for (SessionPersistPolicy policy : SessionPersistPolicy.values()) {
				if (policy.name().equalsIgnoreCase(name)) {
					return policy;
				}
			}
			throw new IllegalArgumentException(
					"Invalid session persist policy [" + name
							+ "]. Must be one of "
							+ Arrays.asList(SessionPersistPolicy.values())
							+ ".");
		}
	}
	
	protected EnumSet<SessionPersistPolicy> sessionPersistPoliciesSet = EnumSet.of(SessionPersistPolicy.DEFAULT);
	
	protected byte[] NULL_SESSION = "null".getBytes();
	
	protected ThreadLocal<CloudSession> currentSession = new ThreadLocal<>();
	protected ThreadLocal<SessionSerializationMetadata> currentSessionSerializationMetadata = new ThreadLocal<>();
	protected ThreadLocal<String> currentSessionId = new ThreadLocal<>();
	protected ThreadLocal<Boolean> currentSessionIsPersisted = new ThreadLocal<>();
	protected Serializer serializer = new JavaSerializer();

	private static CloudSessionManager redisSessionManager = new CloudSessionManager();
	
	public static CloudSessionManager getInstance(){
		return redisSessionManager;
	}
	
	CloudSessionManager(){}
	
	
	public CloudSession createSession(String requestedSessionId,HttpServletRequest request,Integer maxInactiveInterval) {
		CloudSession session = null;
		String sessionId = null;
		if(ObjUtil.isNotEmpty(maxInactiveInterval)){
			sessionTimeout=maxInactiveInterval;
		}
		Jedis jedis=RedisUtil.getJedis();
		try {
			// Ensure generation of a unique session identifier.
			if (ObjUtil.isNotBlank(requestedSessionId)) {
				sessionId = requestedSessionId;
				if (jedis.setnx(sessionId.getBytes(), NULL_SESSION) == 0L) {
					sessionId = null;
				}
			} else {
				do {
					sessionId = generateSessionId();
				} while (jedis.setnx(sessionId.getBytes(), NULL_SESSION) == 0L);
				// 1= key set; 0 = key already existed
			}
			/*
			 * Even though the key is set in Redis, we are not going to flag the
			 * current thread as having had the session persisted since the session
			 * isn't actually serialized to Redis yet. This ensures that the
			 * save(session) at the end of the request will serialize the session
			 * into Redis with 'set' instead of 'setnx'.
			 */
			if (ObjUtil.isNotBlank(sessionId)) {
				session = new CloudSession(sessionId,request,new ConcurrentHashMap<String, Object>());
				session.setNew(true);
				session.setValid(true);
				session.setCreationTime(System.currentTimeMillis());
				session.setMaxInactiveInterval(sessionTimeout);
				session.setId(sessionId);
				session.tellNew();
			}
	
			currentSession.set(session);
			currentSessionId.set(sessionId);
			currentSessionIsPersisted.set(false);
			currentSessionSerializationMetadata.set(new SessionSerializationMetadata());
	
			if (ObjUtil.isNotBlank(sessionId)) {
				try {
					saveInternal(jedis,session, true);
				} catch (IOException ex) {
					currentSession.set(null);
					currentSessionId.set(null);
					session = null;
				}
			}
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return session;
	}

	public CloudSession findSession(String id,HttpServletRequest request,Integer maxInactiveInterval) throws IOException {
		CloudSession session = null;
		if (null == id) {
			currentSessionIsPersisted.set(false);
			currentSession.set(null);
			currentSessionSerializationMetadata.set(null);
			currentSessionId.set(null);

		} else if (id.equals(currentSessionId.get())) {
			session = currentSession.get();
		} else {

			byte[] data = loadSessionDataFromRedis(id);
			if (data != null) {
				DeserializedSessionContainer container = sessionFromSerializedData(id,request, data,maxInactiveInterval);
				session = container.session;
				currentSession.set(session);
				currentSessionSerializationMetadata.set(container.metadata);
				currentSessionIsPersisted.set(true);
				currentSessionId.set(id);
			} else {
				currentSessionIsPersisted.set(false);
				currentSession.set(null);
				currentSessionSerializationMetadata.set(null);
				currentSessionId.set(null);
			}
		}
		return session;
	}
	
	public String generateSessionId() {
		//使用uuid生成sessionId
		return UUID.randomUUID().toString();
	}
	public void save(CloudSession session) throws IOException {
		save(session, false);
	}
	
	public void save(CloudSession session, boolean forceSave) throws IOException {
		Jedis jedis = RedisUtil.getJedis();
		try {
			saveInternal(jedis, session, forceSave);
		} catch (IOException ex) {
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}
	
	
	public boolean getSaveOnChange() {
		return this.sessionPersistPoliciesSet.contains(SessionPersistPolicy.SAVE_ON_CHANGE);
	}

	public boolean getAlwaysSaveAfterRequest() {
		return this.sessionPersistPoliciesSet.contains(SessionPersistPolicy.ALWAYS_SAVE_AFTER_REQUEST);
	}
	
	public void afterRequest() {
		CloudSession redisSession = currentSession.get();
		if (redisSession != null) {
			try {
				if (redisSession.isValid()) {
					log.trace("Request with session completed, saving session "+ redisSession.getId());
					save(redisSession, getAlwaysSaveAfterRequest());
				} else {
					log.trace("HTTP Session has been invalidated, removing :" + redisSession.getId());
					remove(redisSession);
				}
			} catch (Exception e) {
				log.error("Error storing/removing session", e);
			} finally {
				log.trace("Session removed from ThreadLocal :" + redisSession.getId());
			}
		}
	}
	

	public void remove(CloudSession session) {
		Jedis jedis = RedisUtil.getJedis();
		log.trace("Removing session ID : " + session.getId());
		try {
			jedis.del(session.getId());
		} finally {
			currentSession.remove();
			currentSessionId.remove();
			currentSessionIsPersisted.remove();
			if (jedis != null) {
				RedisUtil.returnResource(jedis);
			}
		}
	}
	
	public void cleanSessionThreadLocal(){
		currentSession.remove();
		currentSessionId.remove();
		currentSessionIsPersisted.remove();
		currentSessionSerializationMetadata.remove();
	}
	
    public Object[] getApplicationEventListeners() {
        return applicationEventListenersList.toArray();
    }

    public void setApplicationEventListeners(Object listeners[]) {
        applicationEventListenersList.clear();
        if (listeners != null && listeners.length > 0) {
            applicationEventListenersList.addAll(Arrays.asList(listeners));
        }
    }
    public void addApplicationEventListener(Object listener) {
        applicationEventListenersList.add(listener);
    }
	
	
	//=========private================================================//
	
	private DeserializedSessionContainer sessionFromSerializedData(String id,HttpServletRequest request,byte[] data,Integer maxInactiveInterval) throws IOException {
		if (Arrays.equals(NULL_SESSION, data)) {
			throw new IOException("Serialized session data was equal to NULL_SESSION");
		}

		CloudSession session = null;
		SessionSerializationMetadata metadata = new SessionSerializationMetadata();
		if(ObjUtil.isNotEmpty(maxInactiveInterval)){
			sessionTimeout=maxInactiveInterval;
		}
		try {
			session = new CloudSession(id,request,new ConcurrentHashMap<String, Object>());
			serializer.deserializeInto(data, session, metadata);
			session.setId(id);
			session.setNew(false);
			session.setMaxInactiveInterval(sessionTimeout);
			session.access();
			session.setValid(true);
			session.resetDirtyTracking();

		} catch (ClassNotFoundException ex) {
			throw new IOException("Unable to deserialize into session", ex);
		}

		return new DeserializedSessionContainer(session, metadata);
	}
	
	private boolean saveInternal(Jedis jedis,CloudSession session,boolean forceSave) throws IOException {
		Boolean error = true;
		log.trace("Saving session " + session + " into Redis");
		CloudSession redisSession = session;
		if (log.isTraceEnabled()) {
			log.trace("Session Contents [" + redisSession.getId() + "]:");
			Enumeration<String> en = redisSession.getAttributeNames();
			while (en.hasMoreElements()) {
				log.trace("  " + en.nextElement());
			}
		}

		byte[] binaryId = redisSession.getId().getBytes();

		Boolean isCurrentSessionPersisted;
		SessionSerializationMetadata sessionSerializationMetadata = currentSessionSerializationMetadata.get();
		byte[] originalSessionAttributesHash = sessionSerializationMetadata.getSessionAttributesHash();
		byte[] sessionAttributesHash = null;
		if (forceSave || redisSession.isDirty()  || null == (isCurrentSessionPersisted = this.currentSessionIsPersisted.get())
				|| !isCurrentSessionPersisted
				|| !Arrays.equals(originalSessionAttributesHash,(sessionAttributesHash = serializer.attributesHashFrom(redisSession)))) {

			log.trace("Save was determined to be necessary");

			if (null == sessionAttributesHash) {
				sessionAttributesHash = serializer.attributesHashFrom(redisSession);
			}

			SessionSerializationMetadata updatedSerializationMetadata = new SessionSerializationMetadata();
			updatedSerializationMetadata.setSessionAttributesHash(sessionAttributesHash);

			jedis.set(binaryId, serializer.serializeFrom(redisSession,updatedSerializationMetadata));
			redisSession.resetDirtyTracking();
			currentSessionSerializationMetadata.set(updatedSerializationMetadata);
			currentSessionIsPersisted.set(true);
		} else {
			log.trace("Save was determined to be unnecessary");
		}

		log.trace("Setting expire timeout on session ["+ redisSession.getId() + "] to " + redisSession.getMaxInactiveInterval());
		jedis.expire(binaryId, redisSession.getMaxInactiveInterval());

		error = false;

		return error;
		
	}

	/**
	 * 从reids里面获取到session的集合
	 * @author Administrator
	 * @param id
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	private byte[] loadSessionDataFromRedis(String id) throws IOException {
		Jedis jedis = null;
		Boolean error = true;
		try {
			log.trace("Attempting to load session " + id + " from Redis");
			jedis = RedisUtil.getJedis();
			byte[] data = jedis.get(id.getBytes());
			error = false;
			if (data == null) {
				log.trace("Session " + id + " not found in Redis");
			}

			return data;
		} finally {
			if (jedis != null) {
				RedisUtil.returnResource(jedis);
			}
		}
	}
	
}

class DeserializedSessionContainer {
	public final CloudSession session;
	public final SessionSerializationMetadata metadata;

	public DeserializedSessionContainer(CloudSession session,SessionSerializationMetadata metadata) {
		this.session = session;
		this.metadata = metadata;
	}
	
}
