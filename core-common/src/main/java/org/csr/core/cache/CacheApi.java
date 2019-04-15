package org.csr.core.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接口的定义，该接口可以通过简单工厂来定义
 * @author Administrator
 */
public interface CacheApi {
	
	/**
	 * 写缓存
	 * @param cacheType:缓存类型
	 * @param userId：用户Id
	 * @param objectId：这儿的objectId目前只是  userExaminationId【用户考试ID】
	 * @param object：需要缓存的对象
	 */
	public void createCache(String key,Serializable value);
	public void createCache(String key,Serializable value,int maxInactiveInterval);
	/**
	 * 写缓存
	 * @param cacheType:缓存类型
	 * @param userId：用户Id
	 * @param objectId：这儿的objectId目前只是  userExaminationId【用户考试ID】
	 * @param value：需要缓存的数据
	 */
	public void createCache(String key,String value);
	public void createCache(String key,String value,int maxInactiveInterval);
	
	/**
	 * 写缓存
	 * createDateCache:创建时间缓存 <br/>
	 * @author Administrator
	 * @param key
	 * @param value
	 * @since JDK 1.7
	 */
	void createDateCache(String key, Date value);
	void createDateCache(String key, Date value,int maxInactiveInterval);
	
	/**
	 *  写缓存
	 * @author caijin
	 * @param key
	 * @param object
	 * @since JDK 1.7
	 */
	public void createSerializableCache(String key,Serializable value);
	public void createSerializableCache(String key,Serializable value,int maxInactiveInterval);
	/**
	 * 读缓存
	 */
	public List<Serializable> findCache(String[] keys);
	
	/**
	 * 读缓存
	 */
	public Serializable findCache(String key);
	
	/**
	 * 读缓存
	 */
	public List<Serializable> findSerializableCache(String[] keys);
	
	/**
	 * 读缓存
	 */
	public Serializable findSerializableCache(String key);
	
	/**
	 * 读缓存
	 */
	public String findStringCache(String key);

	/**
	 * 读时间缓存
	 * findDateCache: 描述方法的作用 <br/>
	 * @author Administrator
	 * @param key
	 * @return
	 * @since JDK 1.7
	 */
	public Date findDateCache(String key);
	
	/**
	 * del: 移除 <br/>
	 * @author Administrator
	 * @param key
	 * @since JDK 1.7
	 */
	public void del(String key);
	
	/**
	 * del: 移除 <br/>
	 * @author Administrator
	 * @param keys
	 * @since JDK 1.7
	 */
	public void del(String[] keys);
	
	
	public Long lpush(final String key, final Serializable... strings);
	
	public List<Serializable> lrange(final String key, final long start, final long end) ;
	
	public Long sadd(final String key, final Serializable... members);

}
