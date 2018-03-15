package com.distributed.cache.database.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonRepositoryService {

	private static SqlSessionFactory sesFact = null;

	@Autowired
	private DataSourceConfigUtil dataSourceConfigUtil;

	public Map<String, Object> getMapData(String id) {

		sesFact = getSession();

		Map<String, Object> data;

		try (SqlSession session = sesFact.openSession()) {

			data = session.selectOne("getMapData", id);
		}

		return data;
	}

	public List<Map<String, Object>> getListData() {

		sesFact = getSession();

		List<Map<String, Object>> data;

		try (SqlSession session = sesFact.openSession()) {

			data = session.selectList("getListData");
		}

		return data;
	}

	private SqlSessionFactory getSession() {
		Properties prop = dataSourceConfigUtil.getProperties();
		MysqlDataSourceFactory mdsf = new MysqlDataSourceFactory();
		mdsf.setProperties(prop);
		DataSource ds = mdsf.getDataSource();

		TransactionFactory trFact = new JdbcTransactionFactory();
		Environment environment = new Environment("development", trFact, ds);
		Configuration config = new Configuration(environment);
		config.addMapper(SqlMapper.class);

		sesFact = new SqlSessionFactoryBuilder().build(config);

		return sesFact;
	}

}
