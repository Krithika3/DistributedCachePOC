package com.distributed.cache.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.springframework.stereotype.Component;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.SimpleMapCache;

@Component
public class DistributedMapCache {
	
	private Map<String, SimpleMapCache<List<String>>>  serverMap;
	private SimpleMapCache <List<String>> simpleMapCache;
	
	
	public DistributedMapCache(){
		 serverMap = new HashMap<>();
		 simpleMapCache = new SimpleMapCache<List<String>>(3, EvictionPolicy.LRU);
	}

	
	public String hash (String key){
		
		byte[] bytes = key.getBytes();
        Checksum checksum = new CRC32(); 
        checksum.update(bytes, 0, bytes.length);

        long value =  checksum.getValue() % 4;
        
        return "Server" + value;
		
		
	}
	
	public List<String> get(String key){
		
		String server = hash(key);
		
		if(serverMap.containsKey(server)) {
			if(serverMap.get(server).containsKey(key)) {
				return serverMap.get(server).get(key);
			}
		}
		
		else {
			throw new RuntimeException("Server does not contain key");
		}
				
	}
	
	public void put(String key, String value) throws IOException{
		
		if(simpleMapCache.containsKey(key)) {
			simpleMapCache.get(key).add(value);
		}
		
		List<String> values = new ArrayList<>();
		
		values.add(value);
		
		simpleMapCache.put(key, values );
		
		String server = hash(key);
				
		serverMap.put(server, simpleMapCache);
	}

}
