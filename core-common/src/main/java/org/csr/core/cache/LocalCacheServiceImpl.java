package org.csr.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.csr.core.util.ObjUtil;

public class LocalCacheServiceImpl implements CacheApi {

	private static LocalCacheServiceImpl instance = new LocalCacheServiceImpl();

	/** 本地缓存常量 */
	private final static Map<String, LocalCache> cacheMap = new HashMap<String, LocalCache>();

	
	public static LocalCacheServiceImpl getInstance() {
		return instance;
	}

	private LocalCacheServiceImpl() {

	}

	/**
	 * 写缓存
	 */
	@Override
	public void createCache(String key, Serializable p) {
		cacheMap.put(key, new LocalCache(p, -1));
	}

	@Override
	public void createCache(String key, String value) {
		cacheMap.put(key, new LocalCache(value, -1));
	}

	/**
	 * 读取缓存
	 */
	@Override
	public List<Serializable> findCache(String[] keys) {
		List<Serializable> backList = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) {
			LocalCache localCache = cacheMap.get(keys[i]);
			Serializable p = (Serializable) verifyValidity(keys[i], localCache);
			if (ObjUtil.isNotEmpty(p)) {
				backList.add(p);
			}
		}
		return backList;
	}

	@Override
	public Serializable findCache(String key) {
		LocalCache localCache = cacheMap.get(key);
		return (Serializable) verifyValidity(key, localCache);
	}

	@Override
	public String findStringCache(String key) {
		LocalCache localCache = cacheMap.get(key);

		return (String) verifyValidity(key, localCache);
	}

	@Override
	public void createDateCache(String key, Date value) {
		cacheMap.put(key, new LocalCache(value, -1));
	}

	@Override
	public Date findDateCache(String key) {
		LocalCache localCache = cacheMap.get(key);
		return (Date) verifyValidity(key, localCache);
	}

	@Override
	public void del(String key) {
		cacheMap.remove(key);
	}

	@Override
	public void del(String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			cacheMap.remove(keys[i]);
		}
	}

	@Override
	public void createSerializableCache(String key, Serializable object) {
		cacheMap.put(key, new LocalCache(object, -1));
	}

	@Override
	public List<Serializable> findSerializableCache(String[] keys) {
		List<Serializable> backList = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) {
			LocalCache localCache = cacheMap.get(keys[i]);
			Serializable p = (Serializable) verifyValidity(keys[i], localCache);
			if (ObjUtil.isNotEmpty(p)) {
				backList.add(p);
			}
		}
		return backList;
	}

	@Override
	public Serializable findSerializableCache(String key) {
		LocalCache object = cacheMap.get(key);
		return (Serializable) verifyValidity(key, object);
	}

	@Override
	public void createCache(String key, Serializable value,
			int maxInactiveInterval) {
		cacheMap.put(key, new LocalCache(value, maxInactiveInterval));
	}

	@Override
	public void createCache(String key, String value, int maxInactiveInterval) {
		cacheMap.put(key, new LocalCache(value, maxInactiveInterval));
	}

	@Override
	public void createDateCache(String key, Date value, int maxInactiveInterval) {
		cacheMap.put(key, new LocalCache(value, maxInactiveInterval));
	}

	@Override
	public void createSerializableCache(String key, Serializable value,
			int maxInactiveInterval) {
		cacheMap.put(key, new LocalCache(value, maxInactiveInterval));
	}

	
	
	

	@Override
	public Long lpush(String key, Serializable... strings) {
		LocalCache object = cacheMap.get(key);
		Object verifyValidity = verifyValidity(key, object);
		if(ObjUtil.isEmpty(verifyValidity)){
			object =	new LocalCache(new ArrayList<>(), -1);
			cacheMap.put(key, object);
		}
		List<Serializable> valueList=(List) object.value;
		for (Serializable serializable : strings) {
			valueList.add(serializable);
		}
		return (long) valueList.size();
	}

	@Override
	public List<Serializable> lrange(String key, long start, long end) {
		if(start>end && end!=-1){
			return null;
		}
		LocalCache object = cacheMap.get(key);
		Object verifyValidity = verifyValidity(key, object);
		if(ObjUtil.isEmpty(verifyValidity)){
			object =	new LocalCache(new ArrayList<>(), -1);
			cacheMap.put(key, object);
		}
		List<Serializable> valueList=(List) object.value;
		if(start>valueList.size()){
			return null;
		}else if(end==-1){
			end=valueList.size();
		}else if(valueList.size()<end){
			end=valueList.size();
		}
		return valueList.subList((int)start, (int)end);
	}

	@Override
	public Long sadd(String key, Serializable... members) {
		LocalCache object = cacheMap.get(key);
		Object verifyValidity = verifyValidity(key, object);
		if(ObjUtil.isEmpty(verifyValidity)){
			object =	new LocalCache(new HashSet<>(), -1);
			cacheMap.put(key, object);
		}
		HashSet<Serializable> valueList=(HashSet) object.value;
		for (Serializable serializable : members) {
			valueList.add(serializable);
		}
		return (long) valueList.size();
	}
	
	private Object verifyValidity(String key, LocalCache localCache) {
		if(ObjUtil.isEmpty(localCache)){
			return null;
		}
		Object value = localCache.getValue();
		if (ObjUtil.isEmpty(value)) {
			cacheMap.remove(key);
			return null;
		} else {
			return value;
		}
	}
	
	
	

	private class LocalCache implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 813341634305878536L;
		private Object value;
		private int inactiveInterval = -1;
		private final long updateTime;

		public LocalCache(Serializable value, int inactiveInterval) {
			this.value = value;
			this.inactiveInterval = inactiveInterval;
			this.updateTime = System.currentTimeMillis();
		}

		public Object getValue() {
			if (inactiveInterval <= 0) {
				return value;
			} else {
				long newTime = inactiveInterval * 1000 + updateTime;
				if (newTime >= System.currentTimeMillis()) {
					return value;
				} else {
					return null;
				}
			}
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getInactiveInterval() {
			return inactiveInterval;
		}

		public void setInactiveInterval(int inactiveInterval) {
			this.inactiveInterval = inactiveInterval;
		}

	}


}
