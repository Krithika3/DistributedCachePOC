package com.distributed.cache.database.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface SqlMapper {

	@Select("SELECT * FROM Persons where id=#{id}")
	public Map<String, Object> getMapData();

	@Select("SELECT * FROM Persons")
	public Map<String, Object> getListData();

}
