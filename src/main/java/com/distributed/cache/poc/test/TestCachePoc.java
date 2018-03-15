package com.distributed.cache.poc.test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.DistributedCache;
import com.distributed.cache.result.DistributedCacheImpl;

public class TestCachePoc {
	/**
	 * A simple example to show multiple threads accessing the cache.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	public static void main(String[] args) throws IOException {

		DistributedCache<String> distributedCache = new DistributedCacheImpl<String>(3, EvictionPolicy.LRU);

		ExecutorService executor = Executors.newFixedThreadPool(3);

		executor.submit(() -> distributedCache.put("test1", "value1"));
		executor.submit(() -> distributedCache.put("test2", "value2"));
		executor.submit(() -> distributedCache.put("test3", "value3"));

		executor.submit(() -> distributedCache.putIfAbsent("test2", "value5"));
		executor.submit(() -> distributedCache.remove("test2"));
		executor.submit(() -> distributedCache.put("test4", "value4"));

		executor.submit(() -> System.out.println("Value of key test2:" + distributedCache.get("test2")));
		executor.submit(() -> System.out.println("Value of key test1:" + distributedCache.get("test1")));
		executor.submit(() -> System.out.println("Value of key test3:" + distributedCache.get("test3")));

		stop(executor);

	}

	public static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void stop(ExecutorService executor) {
		try {
			executor.shutdown();
			executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("termination interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("killing non-finished tasks");
			}
			executor.shutdownNow();
		}
	}

}
