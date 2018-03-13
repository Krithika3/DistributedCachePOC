package com.distributed.cache.record;

import java.util.Arrays;

public class MapCacheRecord<T> extends CacheRecord {
		
	private final String key;
    private final T value;
    private final long revision;
    
   

    public MapCacheRecord(final String key, final T value) {
        this(key, value, -1L);
    }

    public MapCacheRecord(final String key, final T value, final long revision) {
        this.key = key;
        this.value = value;
        this.revision = revision;
    }
    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
    
   

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{key, value, revision});
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof MapCacheRecord) {
            final MapCacheRecord<T> that = ((MapCacheRecord) obj);
            return key.equals(that.key) && value.equals(that.value) && revision == that.revision;
        }

        return false;
    }

    public long getRevision() {
        return revision;
    }

}
