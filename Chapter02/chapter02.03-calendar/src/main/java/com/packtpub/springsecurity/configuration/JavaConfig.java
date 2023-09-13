package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Java config.
 */
@Configuration
@Import({ SecurityConfig.class, DataSourceConfig.class })
@ComponentScan(basePackages =
		{
				"com.packtpub.springsecurity.dataaccess",
				"com.packtpub.springsecurity.domain",
				"com.packtpub.springsecurity.service"
		}
)
public class JavaConfig {}