package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.record.CacheRecord;

public enum EvictionPolicy {

    LFU(new LFUComparator()),
    LRU(new LRUComparator()),
    FIFO(new FIFOComparator());

    private final Comparator<CacheRecord> comparator;

    private EvictionPolicy(final Comparator<CacheRecord> comparator) {
        this.comparator = comparator;
    }

    public Comparator<CacheRecord> getComparator() {
        return comparator;
    }

    public static class LFUComparator implements Comparator<CacheRecord> {

        @Override
        public int compare(final CacheRecord o1, final CacheRecord o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            final int hitCountComparison = Integer.compare(o1.getHitCount(), o2.getHitCount());
            final int entryDateComparison = (hitCountComparison == 0) ? Long.compare(o1.getEntryDate(), o2.getEntryDate()) : hitCountComparison;
            return (entryDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : entryDateComparison);
        }
    }

    public static class LRUComparator implements Comparator<CacheRecord> {

        @Override
        public int compare(final CacheRecord o1, final CacheRecord o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            //final int lastHitDateComparison = Long.compare(o1.getLastHitDate(), o2.getLastHitDate());
            final int lastHitDateComparison =  Integer.compare(o1.getHitCount(), o2.getHitCount());
            return (lastHitDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : lastHitDateComparison);
        }
    }

    public static class FIFOComparator implements Comparator<CacheRecord> {

        @Override
        public int compare(final CacheRecord o1, final CacheRecord o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            final int entryDateComparison = Long.compare(o1.getEntryDate(), o2.getEntryDate());
            return (entryDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : entryDateComparison);
        }
    }

}