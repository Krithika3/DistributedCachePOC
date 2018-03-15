package com.distributed.cache.poc.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.cache.database.repository.PersonRepositoryService;
import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.DistributedCacheImpl;

@RestController
@RequestMapping("/api/app/distributed/cache/v1")
public class CachePocController {
	/**
	 * This class is designed to show the performance of the REST service while
	 * caching data versus actually talking to the database
	 */

	@Autowired
	private PersonRepositoryService personRepositoryService;

	private DistributedCacheImpl<Map<String, Object>> distributedMapCache = new DistributedCacheImpl<Map<String, Object>>(3,
			EvictionPolicy.LRU);

	@RequestMapping("getdata/id/{id}")
	public @ResponseBody Map<String, Object> getData(@PathVariable("id") String id) throws IOException {

		if (distributedMapCache.containsKey(id)) {
			return distributedMapCache.get(id);
		}

		Map<String, Object> data = personRepositoryService.getMapData(id);

		distributedMapCache.put(id, data);

		return data;

	}

	@RequestMapping("getdata")
	public @ResponseBody void uploadData() throws IOException {

		List<Map<String, Object>> data = personRepositoryService.getListData();

		for (Map<String, Object> entry : data) {
			distributedMapCache.putIfAbsent((entry.get("ID")).toString(), entry);
		}

	}

}
