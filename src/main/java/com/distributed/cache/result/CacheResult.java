package com.distributed.cache.result;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CacheResult<T> {

	private final String key;
	private final T value;
	private static final AtomicLong idGenerator = new AtomicLong(0L);

	private final long id;
	private final long entryDate;
	private final AtomicInteger hitCount = new AtomicInteger(0);

	public CacheResult(final String key, final T value) {
		this.key = key;
		this.value = value;
		entryDate = System.currentTimeMillis();
		id = idGenerator.getAndIncrement();
	}

	public String getKey() {
		return key;
	}

	public T getValue() {
		return value;
	}

	public long getEntryDate() {
		return entryDate;
	}

	public int getHitCount() {
		return hitCount.get();
	}

	public void hit() {
		hitCount.getAndIncrement();
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] { key, value });
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof CacheResult) {
			final CacheResult<T> that = ((CacheResult) obj);
			return key.equals(that.key) && value.equals(that.value);
		}

		return false;
	}

}
