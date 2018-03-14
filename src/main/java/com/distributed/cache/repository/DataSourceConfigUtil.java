package com.distributed.cache.repository;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DataSourceConfigUtil {
	
	@Autowired
	private Environment environment;

	public Properties getProperties() {
		Properties prop = new Properties();
		prop.setProperty("driver", environment.getProperty("datasource.driver"));
		prop.setProperty("url",
				environment.getProperty("datasource.url"));
		prop.setProperty("user", environment.getProperty("datasource.username"));
		prop.setProperty("password", environment.getProperty("datasource.password"));
		
		return prop;

	}

}
