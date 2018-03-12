package com.distributed.cache.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.hash.DistributedMapCache;

@RestController
@RequestMapping("/api/app/distributed/cache/v1")
public class CachePocController {
	
	@Autowired
	private DistributedMapCache distributedMapCache;
	
	@RequestMapping("getdata")
	public String getData() throws IOException {
		
				
		distributedMapCache.put("test1", "value1");
		distributedMapCache.put("test2", "value2");
		distributedMapCache.put("test3", "value3");
		System.out.println(distributedMapCache.get("test1"));
		System.out.println(distributedMapCache.get("test2"));
		distributedMapCache.put("test4", "value4");
		distributedMapCache.put("test5", "value5");
		
		System.out.println(distributedMapCache.get("test4"));
		
		return distributedMapCache.get("test2");
		
		
		
		
		
	}

}
