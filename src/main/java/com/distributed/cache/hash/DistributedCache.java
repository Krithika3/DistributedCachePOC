package com.distributed.cache.hash;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.SimpleMapCache;

@Component
public class DistributedCache {
	
	private SimpleMapCache <String> simpleMapCache;
	
	
	public DistributedCache(){
		 simpleMapCache = new SimpleMapCache<String>(3, EvictionPolicy.LRU);
	}
	
	
	public void setup() throws IOException {
		simpleMapCache.put("test1", "value1");
		simpleMapCache.put("test2", "value2");
		simpleMapCache.put("test3", "value3");
		
		System.out.println(simpleMapCache.get("test1"));
		System.out.println(simpleMapCache.get("test2"));
		simpleMapCache.put("test4", "value4");
		simpleMapCache.put("test5", "value5");
		
		System.out.println(simpleMapCache.get("test4"));
	}
	
	public String get(String key) {
		return simpleMapCache.get(key);
	}
	
	public void put(String key, String value) throws IOException {
		simpleMapCache.put(key, value);
	}
	

}
