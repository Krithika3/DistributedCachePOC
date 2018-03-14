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
import com.distributed.cache.record.CacheResult;

public class PocMapCache<T> implements MapCache<T> {

	private final Map<String, CacheResult<T>> cache = new HashMap<>();
	private final SortedMap<CacheResult<T>, String> sortedMap;

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();

	private final int maxSize;

	public PocMapCache(final int maxSize, final EvictionPolicy evictionPolicy) {
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
	public void putIfAbsent(final String key, final T value) {
		writeLock.lock();
		try {
			final CacheResult<T> record = cache.get(key);
			if (record == null) {
				put(key, value, record);
				return;
			}
			sortedMap.remove(record);
			record.hit();
			sortedMap.put(record, key);

		} finally {
			writeLock.unlock();
		}
	}

	private void put(final String key, final T value, final CacheResult<T> existing) {
		evict();

		if (existing != null) {
			sortedMap.remove(existing);
		}

		final CacheResult<T> record = new CacheResult<T>(key, value);
		cache.put(key, record);
		sortedMap.put(record, key);
	}

	@Override
	public void put(final String key, final T value) throws IOException {
		writeLock.lock();
		try {
			final CacheResult<T> existing = cache.get(key);
			put(key, value, existing);
			return;
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean containsKey(final String key) {
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
	public T get(final String key) {
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