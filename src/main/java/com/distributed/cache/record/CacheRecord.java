package com.distributed.cache.record;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CacheRecord {
	
	private static final AtomicLong idGenerator = new AtomicLong(0L);

    private final long id;
    private final long entryDate;
    private volatile long lastHitDate;
    private final AtomicInteger hitCount = new AtomicInteger(0);
    
    //private final Integer hitCount = 0;

    public CacheRecord() {
        entryDate = System.currentTimeMillis();
        lastHitDate = entryDate;
        id = idGenerator.getAndIncrement();
    }

    public long getEntryDate() {
        return entryDate;
    }

    public long getLastHitDate() {
        return lastHitDate;
    }

    public int getHitCount() {
        return hitCount.get();
    }

    public void hit() {
        hitCount.getAndIncrement();
        lastHitDate = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

}
