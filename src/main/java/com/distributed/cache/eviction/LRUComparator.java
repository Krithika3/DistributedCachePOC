package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.result.CacheResult;

public class LRUComparator<T> implements Comparator<CacheResult<T>> {

	@Override
	public int compare(final CacheResult<T> o1, final CacheResult<T> o2) {
		if (o1.equals(o2)) {
			return 0;
		}

		final int lastHitDateComparison = Integer.compare(o1.getHitCount(), o2.getHitCount());
		return (lastHitDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : lastHitDateComparison);
	}

}
