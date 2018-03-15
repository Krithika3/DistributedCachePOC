package com.distributed.cache.result;


public class MapCacheResult {
	
	 private final boolean successful;
	    private final CacheResult record;
	    private final CacheResult existing;
	    private final CacheResult evicted;

	    public MapCacheResult(boolean successful, CacheResult record, CacheResult existing, CacheResult evicted) {
	        this.successful = successful;
	        this.record = record;
	        this.existing = existing;
	        this.evicted = evicted;
	    }

	    public boolean isSuccessful() {
	        return successful;
	    }

	    public CacheResult getRecord() {
	        return record;
	    }

	    public CacheResult getExisting() {
	        return existing;
	    }

	    public CacheResult getEvicted() {
	        return evicted;
	    }

}
