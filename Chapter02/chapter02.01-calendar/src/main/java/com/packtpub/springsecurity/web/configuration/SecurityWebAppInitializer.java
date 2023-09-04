package com.packtpub.springsecurity.web.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;


/**
 * Registers the {@link DelegatingFilterProxy} to use the springSecurityFilterChain before
 */
@Order(1)
public class SecurityWebAppInitializer
		extends AbstractSecurityWebApplicationInitializer {

} 
