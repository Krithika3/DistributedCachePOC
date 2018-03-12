package com.distributed.cache.result;

import com.distributed.cache.record.MapCacheRecord;

public class MapPutResult<T> {
	
	 private final boolean successful;
	    private final MapCacheRecord record;
	    private final MapCacheRecord existing;
	    private final MapCacheRecord evicted;

	    public MapPutResult(boolean successful, MapCacheRecord record, MapCacheRecord existing, MapCacheRecord evicted) {
	        this.successful = successful;
	        this.record = record;
	        this.existing = existing;
	        this.evicted = evicted;
	    }

	    public boolean isSuccessful() {
	        return successful;
	    }

	    public MapCacheRecord getRecord() {
	        return record;
	    }

	    public MapCacheRecord getExisting() {
	        return existing;
	    }

	    public MapCacheRecord getEvicted() {
	        return evicted;
	    }

}
