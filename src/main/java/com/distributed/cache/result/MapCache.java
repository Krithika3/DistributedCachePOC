package com.distributed.cache.result;

import java.io.IOException;

public interface MapCache<T> {
	
	/*
	 * Interface implementing basic cache methods
	 */

	MapCacheResult putIfAbsent(String key, T value) throws IOException;

	MapCacheResult put(String key, T value) throws IOException;

	boolean containsKey(String key) throws IOException;

	T get(String key) throws IOException;

}
