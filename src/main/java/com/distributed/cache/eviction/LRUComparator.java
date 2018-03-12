package com.distributed.cache.eviction;

import java.util.Comparator;

import com.distributed.cache.record.MapCacheRecord;

public class LRUComparator<T> implements Comparator<MapCacheRecord<T>> {

        @Override
        public int compare(final MapCacheRecord<T> o1, final MapCacheRecord<T> o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            final int lastHitDateComparison = Long.compare(o1.getLastHitDate(), o2.getLastHitDate());
            return (lastHitDateComparison == 0 ? Long.compare(o1.getId(), o2.getId()) : lastHitDateComparison);
        }

}
