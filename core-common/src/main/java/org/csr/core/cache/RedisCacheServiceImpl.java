package org.csr.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.csr.core.util.ObjUtil;
import org.csr.core.util.SerializeUtil;

import redis.clients.jedis.Jedis;

public class RedisCacheServiceImpl implements CacheApi {

	private static RedisCacheServiceImpl instance = new RedisCacheServiceImpl();

	public static RedisCacheServiceImpl getInstance() {
		return instance;
	}

	private RedisCacheServiceImpl() {

	}

	@Override
	public void createCache(String key, Serializable value) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createCache(String key, String value, int maxInactiveInterval) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			jedis.set(bytes, SerializeUtil.serialize(value));
			jedis.expire(bytes, maxInactiveInterval);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createCache(String key, String value) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createCache(String key, Serializable value,
			int maxInactiveInterval) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			jedis.set(bytes, SerializeUtil.serialize(value));
			jedis.expire(bytes, maxInactiveInterval);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createSerializableCache(String key, Serializable value) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createSerializableCache(String key, Serializable value,
			int maxInactiveInterval) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			jedis.set(bytes, SerializeUtil.serialize(value));
			jedis.expire(bytes, maxInactiveInterval);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createDateCache(String key, Date value) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void createDateCache(String key, Date value, int maxInactiveInterval) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			jedis.set(bytes, SerializeUtil.serialize(value));
			jedis.expire(bytes, maxInactiveInterval);
		} finally {
			RedisUtil.returnResource(jedis);
		}

	}

	@Override
	public List<Serializable> findCache(String[] keys) {
		List<Serializable> backList = new ArrayList<>();
		Jedis jedis = RedisUtil.getJedis();
		try {
			for (int i = 0; i < keys.length; i++) {
				byte[] bytes = jedis.get(keys[i].getBytes());
				if (ObjUtil.isNotEmpty(bytes)) {
					Serializable p = (Serializable) SerializeUtil.unserialize(bytes);
					if (ObjUtil.isNotEmpty(p)) {
						backList.add(p);
					}
				}
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
		return backList;
	}

	@Override
	public Serializable findCache(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (ObjUtil.isNotEmpty(bytes)) {
				return (Serializable) SerializeUtil.unserialize(bytes);
			} else {
				return null;
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public String findStringCache(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (ObjUtil.isNotEmpty(bytes)) {
				return (String) SerializeUtil.unserialize(bytes);
			} else {
				return null;
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public Date findDateCache(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (ObjUtil.isNotEmpty(bytes)) {
				return (Date) SerializeUtil.unserialize(bytes);
			} else {
				return null;
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void del(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.del(key.getBytes());
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public void del(String[] keys) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			for (int i = 0; i < keys.length; i++) {
				jedis.del(keys[i].getBytes());
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public List<Serializable> findSerializableCache(String[] keys) {
		List<Serializable> backList = new ArrayList<>();
		Jedis jedis = RedisUtil.getJedis();
		try {
			for (int i = 0; i < keys.length; i++) {
				byte[] bytes = jedis.get(keys[i].getBytes());
				if (ObjUtil.isNotEmpty(bytes)) {
					Serializable p = (Serializable) SerializeUtil.unserialize(bytes);
					if (ObjUtil.isNotEmpty(p)) {
						backList.add(p);
					}
				}
			}

		} finally {
			RedisUtil.returnResource(jedis);
		}
		return backList;
	}

	@Override
	public Serializable findSerializableCache(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (ObjUtil.isNotEmpty(bytes)) {
				return (Serializable) SerializeUtil.unserialize(bytes);
			} else {
				return null;
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public Long lpush(String key, Serializable... objs) {
		if(ObjUtil.isEmpty(objs)){
			return 0l;
		}
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			byte[][] list = new byte[objs.length][];
			for (int i = 0; i < objs.length; i++) {
				byte[] serialize = SerializeUtil.serialize(objs[i]);
				if(ObjUtil.isEmpty(serialize)){
					list[i]=new byte[0];
				}else{
					list[i]=serialize;
				}
			}
			return jedis.lpush(bytes,list);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public Long sadd(String key, Serializable... objs) {
		Jedis jedis = RedisUtil.getJedis();
			try {
				byte[] bytes = key.getBytes();
				byte[][] list = new byte[objs.length][];
				for (int i = 0; i < objs.length; i++) {
					byte[] serialize = SerializeUtil.serialize(objs[i]);
					if(ObjUtil.isEmpty(serialize)){
						list[i]=new byte[0];
					}else{
						list[i]=serialize;
					}
				}
				return jedis.sadd(bytes,list);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	@Override
	public List<Serializable> lrange(String key, long start, long end) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			byte[] bytes = key.getBytes();
			List<byte[]> objs =	jedis.lrange(bytes,start,end);
			List<Serializable> arrayList = new ArrayList<>();
			for (int i = 0; i < objs.size(); i++) {
				arrayList.add((Serializable) SerializeUtil.unserialize(objs.get(i)));
			}
			return arrayList;
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}
}
