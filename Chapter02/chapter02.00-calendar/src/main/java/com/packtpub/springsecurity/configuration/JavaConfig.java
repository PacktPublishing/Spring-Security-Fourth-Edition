package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Java config.
 */
@Configuration
@ComponentScan(basePackages =
		{
				"com.packtpub.springsecurity.dataaccess",
				"com.packtpub.springsecurity.domain",
				"com.packtpub.springsecurity.service"
		}
)
public class JavaConfig {} 
