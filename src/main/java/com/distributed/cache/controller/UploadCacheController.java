package com.distributed.cache.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.repository.PersonRepositoryService;
import com.distributed.cache.result.PocMapCache;

@RestController
@RequestMapping("/api/app/upload/cache/v1")
public class UploadCacheController {
		
		@Autowired
		private PersonRepositoryService personRepositoryService;
		



//		@RequestMapping("getdata")
//		public @ResponseBody void uploadData() throws IOException {
//			
//			List<Map<String,Object>> data = personRepositoryService.getListData();
//			
//			SimpleMapCache<Map<String,Object>> simpleMapCache = new SimpleMapCache<Map<String,Object>>(4, EvictionPolicy.LRU);
//			
//			for(Map<String,Object> entry: data) {
//				simpleMapCache.putIfAbsent((entry.get("ID")).toString(), entry);
//			}
//			
//		}
		
		public static void main(String[] args) throws IOException {
			
			PocMapCache<String> simpleMapCache = new PocMapCache<String>(3, EvictionPolicy.LRU);

			
				simpleMapCache.put("test1", "value1");
				simpleMapCache.put("test2", "value2");
				simpleMapCache.put("test3", "value3");
				
				//simpleMapCache.get("test1");
				simpleMapCache.put("test4", "value4");
				simpleMapCache.putIfAbsent("test1", "value1a");

				simpleMapCache.put("test5", "value5");
				
				System.out.println(simpleMapCache.get("test1"));
				
				}

}
