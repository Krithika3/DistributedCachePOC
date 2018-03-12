package com.distributed.cache.result;

import java.io.IOException;


public interface MapCache<T> {
	
	 	MapPutResult<T> putIfAbsent(String key, T value) throws IOException;

	 	MapPutResult<T> put(String key, T value) throws IOException;

	    boolean containsKey(String key) throws IOException;

	    T get(String key) throws IOException;

}
