package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.record.MapCacheRecord;
	
	public enum EvictionPolicy {

	    LRU(new LRUComparator()),
	    FIFO(new FIFOComparator());

	    private final Comparator<MapCacheRecord> comparator;

	    private EvictionPolicy(final Comparator<MapCacheRecord> comparator) {
	        this.comparator = comparator;
	    }

	    public Comparator<MapCacheRecord> getComparator() {
	        return comparator;
	    }
}
