package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.result.CacheResult;

public class FIFOComparator<T> implements Comparator<CacheResult<T>> {

	@Override
	public int compare(final CacheResult<T> o1, final CacheResult<T> o2) {
		if (o1.equals(o2)) {
			return 0;
		}

		final int entryDateComparison = Long.compare(o1.getEntryDate(), o2.getEntryDate());
		return (entryDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : entryDateComparison);
	}
}
