package com.packtpub.springsecurity.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * The type Data source config.
 *
 *  @author bnasslahsen
 */
@Configuration
public class DataSourceConfig {


	/**
	 * Custom H2 implementation for our {@link EmbeddedDatabase}
	 *
	 * @return data source
	 */
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setName("dataSource")
				.setType(EmbeddedDatabaseType.H2)
				.addScript("/database/h2/calendar-schema.sql")
				.addScript("/database/h2/calendar-data.sql")
				.addScript("/database/h2/calendar-authorities.sql")
				.build();
	}

} 
