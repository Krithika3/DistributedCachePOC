package com.distributed.cache.result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.distributed.cache.eviction.EvictionPolicy;
import com.distributed.cache.record.MapCacheRecord;

public class SimpleMapCache<T> implements MapCache<T> {

    private final Map<String, MapCacheRecord<T>> cache = new HashMap<>();
    private final SortedMap<MapCacheRecord<T>, String> sortedMap;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();


    private final int maxSize;

    public SimpleMapCache(final int maxSize, final EvictionPolicy evictionPolicy) {
        // need to change to ConcurrentMap as this is modified when only the readLock is held
        sortedMap = new ConcurrentSkipListMap<>(evictionPolicy.getComparator());
        this.maxSize = maxSize;
    } 

    private MapCacheRecord<T> evict() {
        if (cache.size() < maxSize) {
            throw new RuntimeException("Cache size is less than max size");
        }

        final MapCacheRecord<T> recordToEvict = sortedMap.firstKey();
        final String valueToEvict = sortedMap.remove(recordToEvict);
        cache.remove(valueToEvict);

        return recordToEvict;
    }

    @Override
    public MapPutResult<T> putIfAbsent(final String key, final T value) {
        writeLock.lock();
        try {
            final MapCacheRecord<T> record = cache.get(key);
            if (record == null) {
                put(key, value, record);
            }

            // Record is not null. Increment hit count and return result indicating that record was not added.
            sortedMap.remove(record);
            record.hit();
            sortedMap.put(record, key);

            return new MapPutResult<T>(false, record, record, null);
        } finally {
            writeLock.unlock();
        }
    }

    private MapPutResult<T> put(final String key, final T value, final MapCacheRecord<T> existing) {
        // evict if we need to in order to make room for a new entry.
       MapCacheRecord<T> evicted =  evict();

        final long revision;
        if (existing == null) {
            revision = 0;
        } else {
            revision = existing.getRevision() + 1;
            sortedMap.remove(existing);
        }

        final MapCacheRecord<T> record = new MapCacheRecord<T>(key, value, revision);
        cache.put(key, record);
        sortedMap.put(record, key);

        return new MapPutResult<T>(true, record, existing, evicted);
    }

    @Override
    public MapPutResult<T> put(final String key, final T value) throws IOException {
        writeLock.lock();
        try {
            final MapCacheRecord<T> existing = cache.get(key);
            return put(key, value, existing);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean containsKey(final String key) {
        readLock.lock();
        try {
            final MapCacheRecord<T> record = cache.get(key);
            if (record == null) {
                return false;
            }

            sortedMap.remove(record);
            record.hit();
            sortedMap.put(record, key);

            return true;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public T get(final String key) {
        readLock.lock();
        try {
            final MapCacheRecord<T> record = cache.get(key);
            if (record == null) {
                return null;
            }

            sortedMap.remove(record);
            record.hit();
            sortedMap.put(record, key);

            return record.getValue();
        } finally {
            readLock.unlock();
        }
    }

   
}