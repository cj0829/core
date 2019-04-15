package org.csr.core.cache;

import java.util.List;

import org.csr.core.util.SerializeUtil;
import org.csr.core.web.bean.ResultJson;


/**
 * 工厂类，用于创建【CacheApi】对象
 * @author Administrator
 *
 */
public class CacheFactory {

	private CacheFactory(){}
	
	public static CacheApi createApi(String name){
		CacheApi cacheApi = null;
		//local表示本地缓存  redies表示缓存到redies
		if("local".equals(name)){
			cacheApi = LocalCacheServiceImpl.getInstance();
		}else if("redis".equals(name)){
			cacheApi = RedisCacheServiceImpl.getInstance();
		}else{
			cacheApi = LocalCacheServiceImpl.getInstance();
		}
		return cacheApi;
	} 
	
	public static void main(String[] args) {
		LocalCacheServiceImpl.getInstance().createCache("test", "1");;
		LocalCacheServiceImpl.getInstance().createCache("test", "1");;
		System.out.println(SerializeUtil.serialize(new ResultJson()).length);
		
		LocalCacheServiceImpl.getInstance().lpush("12321", new ResultJson());
		List ss= LocalCacheServiceImpl.getInstance().lrange("12321", 0, -1);
		System.out.println(ss);
		for (int i = 0; i < args.length; i++) {
			
		}
	}
}
