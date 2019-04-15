/**
 * Project Name:core
 * File Name:RedisUtil.java
 * Package Name:org.csr.core.cache
 * Date:2015-11-3上午10:16:50
 * Copyright (c) 2015, 版权所有 ,All rights reserved 
 */

package org.csr.core.cache;

/**
 * ClassName:RedisUtil.java <br/>
 * System Name：    基础框架 <br/>
 * Date:     2015-11-3上午10:16:50 <br/>
 * @author   Administrator <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 * redis的连接池
 */
import org.csr.core.util.ObjUtil;
import org.csr.core.util.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */

	private static JedisPool getJedisPool() {

		JedisPoolConfig config = new JedisPoolConfig();
		int maxTotal = ObjUtil.toInt(PropertiesUtil.getConfigureValue("cache.redis.maxTotal"));
		int maxIdle = ObjUtil.toInt(PropertiesUtil.getConfigureValue("cache.redis.maxIdle"));
		int waittime = ObjUtil.toInt(PropertiesUtil.getConfigureValue("cache.redis.waittime"));
		if (maxTotal > 0) {
			config.setMaxTotal(maxTotal);
		}
		if (maxIdle > 0) {
			config.setMaxIdle(maxIdle);
		}
		if (waittime > 0) {
			config.setMaxWaitMillis(waittime);
		}
//		config.setTestOnBorrow(TEST_ON_BORROW);

		String redisIp = PropertiesUtil.getConfigureValue("cache.redis.ip");
		int redisPort = Integer.parseInt(PropertiesUtil.getConfigureValue("cache.redis.port"));
		
//		String redisIp = "127.0.0.1";
//		int redisPort = 6379;
		return new JedisPool(config, redisIp, redisPort);
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		if (jedisPool == null) {
			jedisPool = getJedisPool();
		}
		Jedis resource = jedisPool.getResource();
		String auth = PropertiesUtil.getConfigureValue("cache.redis.auth");
		if(ObjUtil.isNotBlank(auth)){
			resource.auth(auth);
		}
		return resource;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedisPool == null) {
			jedisPool = getJedisPool();
		}
		jedis.close();
	}
	
	public static void main(String[] args) {
		Jedis jedis = RedisUtil.getJedis();
//		jedis.set("a55f06ff-a74a-48cd-ade5-82988af35e03".getBytes(), "a55f06ff-a74a-48cd-ade5-82988af35e03".getBytes());
//		jedis.del("a55f06ff-a74a-48cd-ade5-82988af35e03".getBytes(), "".getBytes());
		byte[] binaryId = "a55f06ff-a74a-48cd-ade5-82988af35e03".getBytes();
		byte[] data = jedis.get(binaryId);
	}
}
