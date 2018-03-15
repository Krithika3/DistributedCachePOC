package com.distributed.cache.result;

public interface DistributedCache<T> {
	
	/*
	 * Interface implementing basic cache methods
	 */
	void putIfAbsent(String key, T value) ;

	void put(String key, T value) ;

	boolean containsKey(String key) ;

	T get(String key) ;
	
	T remove(String key) ;

}
