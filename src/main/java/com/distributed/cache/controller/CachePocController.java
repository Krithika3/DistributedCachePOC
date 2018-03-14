package com.distributed.cache.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.repository.PersonRepositoryService;
import com.distributed.cache.result.PocMapCache;

@RestController
@RequestMapping("/api/app/distributed/cache/v1")
public class CachePocController {
	
	@Autowired
	private PersonRepositoryService personRepositoryService;
	
	private PocMapCache<Map<String,Object>> simpleMapCache = new PocMapCache<Map<String,Object>>(3, EvictionPolicy.LRU);


	@RequestMapping("getdata/id/{id}")
	public @ResponseBody Map<String,Object> getData(@PathVariable("id")String id) throws IOException {
		
		if(simpleMapCache.containsKey(id)) {
			return simpleMapCache.get(id);
		}		
		
		return personRepositoryService.getMapData(id);		
		
    }
	
	@RequestMapping("getdata")
	public @ResponseBody void uploadData() throws IOException {
		
		List<Map<String,Object>> data = personRepositoryService.getListData();
		
		
		for(Map<String,Object> entry: data) {
			simpleMapCache.putIfAbsent((entry.get("ID")).toString(), entry);
		}
		
	}
		
	
	
	

}
