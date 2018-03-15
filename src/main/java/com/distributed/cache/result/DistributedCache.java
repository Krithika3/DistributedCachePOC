package com.distributed.cache.result;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.distributed.cache.eviction.EvictionPolicy;

public class DistributedCache<T> implements MapCache<T> {
	/*
	 * A poc for a simple cache using locks to make sure it works in a distributed environment.
	 */

	private final Map<String, CacheResult<T>> cache = new HashMap<>();
	private final SortedMap<CacheResult<T>, String> sortedMap;

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();

	private final int maxSize;

	public DistributedCache(final int maxSize, final EvictionPolicy evictionPolicy) {
		sortedMap = new ConcurrentSkipListMap<>(evictionPolicy.getComparator());
		this.maxSize = maxSize;
	}

	private CacheResult<T> evict() {
		if (cache.size() < maxSize) {
			return null;
		}

		final CacheResult<T> recordToEvict = sortedMap.firstKey();
		final String valueToEvict = sortedMap.remove(recordToEvict);
		cache.remove(valueToEvict);

		return recordToEvict;
	}

	@Override
	public  MapCacheResult putIfAbsent(final String key, final T value) {
		writeLock.lock();
		try {
			final CacheResult<T> record = cache.get(key);
			if (record == null) {
				 return put(key, value, record);
			}
			sortedMap.remove(record);
			record.hit();
			sortedMap.put(record, key);
			
			return new MapCacheResult(false, record, record, null);

		} finally {
			writeLock.unlock();
		}
	}

	private MapCacheResult put(final String key, final T value, final CacheResult<T> existing) {
		
		CacheResult evicted = null;
		final long revision;
        if (existing == null) {
            revision = 0;
            evicted=evict();
        }

        else {
        	
            revision = existing.getRevision() + 1;
            sortedMap.remove(existing);
        }

		final CacheResult<T> record = new CacheResult<T>(key, value, revision);
		cache.put(key, record);
		sortedMap.put(record, key);
		
		return new MapCacheResult(true, record, existing, evicted);
	}

	@Override
	public  MapCacheResult put(final String key, final T value) {
		writeLock.lock();
		try {
			final CacheResult<T> existing = cache.get(key);
			return put(key, value, existing);
		}
				finally {
			writeLock.unlock();
		}
	}

	@Override
	public  boolean containsKey(final String key) {
		readLock.lock();
		try {
			final CacheResult<T> record = cache.get(key);
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
	public  T get(final String key) {
		readLock.lock();
		try {
			final CacheResult<T> record = cache.get(key);
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