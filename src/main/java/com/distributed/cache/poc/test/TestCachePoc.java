package com.distributed.cache.poc.test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.result.DistributedCache;


public class TestCachePoc {
/**
 * A simple example to show multiple threads accessing the cache.
 * @param args
 * @throws IOException
 * @throws InterruptedException
 * @throws ExecutionException
 */


	public static void main(String[] args) throws IOException {

		DistributedCache<String> distributedCache = new DistributedCache<String>(3, EvictionPolicy.LRU);
		
		ExecutorService executor = Executors.newFixedThreadPool(3);

		executor.submit(()-> distributedCache.put("test1", "value1"));
		executor.submit(()-> distributedCache.put("test2", "value2"));
		executor.submit(()-> distributedCache.put("test3", "value3"));
		executor.submit(()-> System.out.println(distributedCache.get("test1")));
		executor.submit(()-> distributedCache.put("test2", "value5"));
		executor.submit(()-> distributedCache.put("test4", "value4"));

		executor.submit(()-> System.out.println(distributedCache.get("test2")));
		executor.submit(()-> System.out.println(distributedCache.get("test3")));
		executor.submit(()-> System.out.println(distributedCache.get("test1")));
		executor.submit(()-> System.out.println(distributedCache.get("test4")));

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
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

	
	
}
