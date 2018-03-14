package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.record.CacheResult;


public enum EvictionPolicy {

    //LFU(new LFUComparator()),
    LRU(new LRUComparator()),
    FIFO(new FIFOComparator());

    private final Comparator<CacheResult> comparator;

    private EvictionPolicy(final Comparator<CacheResult> comparator) {
        this.comparator = comparator;
    }

    public Comparator<CacheResult> getComparator() {
        return comparator;
    }

}