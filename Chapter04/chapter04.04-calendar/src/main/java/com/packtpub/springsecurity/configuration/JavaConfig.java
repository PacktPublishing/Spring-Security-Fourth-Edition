package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Java config.
 *
 * @author Mick Knutson
 */
@Configuration
@Import({ SecurityConfig.class, DataSourceConfig.class })
public class JavaConfig {}
