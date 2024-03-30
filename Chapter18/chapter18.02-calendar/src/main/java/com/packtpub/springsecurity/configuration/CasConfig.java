package com.packtpub.springsecurity.configuration;

import org.apereo.cas.client.validation.Cas30ServiceTicketValidator;
import org.apereo.cas.client.validation.TicketValidator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * @author bnasslahsen
 */
@Configuration
public class CasConfig {

	@Value("${cas.base.url}")
	private String casBaseUrl;

	@Value("${cas.login.url}")
	private String casLoginUrl;

	@Value("${service.base.url}")
	private String serviceBaseUrl;

	@Value("${cas.logout.url}")
	private String casLogoutUrl;
	
	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(serviceBaseUrl+ "/login/cas");
		return serviceProperties;
	}
	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties) {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(this.casLoginUrl);
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties);
		return casAuthenticationEntryPoint;
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter(CasAuthenticationProvider casAuthenticationProvider) {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setAuthenticationManager(new ProviderManager(casAuthenticationProvider));
		return filter;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider(UserDetailsService userDetailsService, 
			ServiceProperties serviceProperties, TicketValidator cas30ServiceTicketValidator) {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setAuthenticationUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
		provider.setServiceProperties(serviceProperties);
		provider.setTicketValidator(cas30ServiceTicketValidator);
		provider.setKey("key");
		return provider;
	}

	@Bean
	public TicketValidator cas30ServiceTicketValidator() {
		return new Cas30ServiceTicketValidator(this.casBaseUrl);
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(casLogoutUrl, new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl("/logout/cas");
		return logoutFilter;
	}
}
