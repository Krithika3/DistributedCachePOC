package com.distributed.cache.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.SimpleMapCache;

@RestController
@RequestMapping("/api/app/distributed/cache/v1")
public class CachePocController {

	@RequestMapping("getdata/{key}")
	public String getData(@PathVariable("key")String key) throws IOException {
		
		SimpleMapCache<String> simpleMapCache = new SimpleMapCache<String>(3, EvictionPolicy.LRU);
		
		setup(simpleMapCache);	
		return simpleMapCache.get(key);
			
		
	}
	
	public void setup( SimpleMapCache<String> simpleMapCache) throws IOException {
		simpleMapCache.put("test1", "value1");
		simpleMapCache.put("test2", "value2");
		simpleMapCache.put("test3", "value3");
		
		//simpleMapCache.get("test1");
		simpleMapCache.put("test4", "value4");
		simpleMapCache.putIfAbsent("test2", "value2");

		simpleMapCache.put("test5", "value5");
		
		System.out.println(simpleMapCache);
		
	}
	

}
