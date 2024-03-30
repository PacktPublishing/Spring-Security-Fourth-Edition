package com.packtpub.springsecurity.configuration;

import java.time.LocalDateTime;

import org.apereo.cas.client.proxy.ProxyGrantingTicketStorage;
import org.apereo.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.apereo.cas.client.validation.Cas30ProxyTicketValidator;
import org.apereo.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.SpringCacheBasedTicketCache;
import org.springframework.security.cas.userdetails.GrantedAuthorityFromAssertionAttributesUserDetailsService;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * @author bnasslahsen
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CasConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(CasConfig.class);

	@Value("${cas.base.url}")
	private String casBaseUrl;

	@Value("${cas.login.url}")
	private String casLoginUrl;

	@Value("${service.base.url}")
	private String serviceBaseUrl;

	@Value("${cas.logout.url}")
	private String casLogoutUrl;

	@Value("${service.proxy.callback-url}")
	private String calendarServiceProxyCallbackUrl;

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(serviceBaseUrl + "/login/cas");
		serviceProperties.setAuthenticateAllArtifacts(true);
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
	public CasAuthenticationFilter casAuthenticationFilter(CasAuthenticationProvider casAuthenticationProvider,
			ProxyGrantingTicketStorage pgtStorage, ServiceProperties serviceProperties) {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setAuthenticationManager(new ProviderManager(casAuthenticationProvider));
		filter.setProxyGrantingTicketStorage(pgtStorage);
		filter.setProxyReceptorUrl("/pgtUrl");
		filter.setServiceProperties(serviceProperties);
		filter.setAuthenticationDetailsSource(new ServiceAuthenticationDetailsSource(serviceProperties));
		return filter;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider(AuthenticationUserDetailsService authenticationUserDetailsService,
			ServiceProperties serviceProperties, TicketValidator cas30ServiceTicketValidator, SpringCacheBasedTicketCache springCacheBasedTicketCache) {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setAuthenticationUserDetailsService(authenticationUserDetailsService);
		provider.setServiceProperties(serviceProperties);
		provider.setTicketValidator(cas30ServiceTicketValidator);
		provider.setKey("key");
		provider.setStatelessTicketCache(springCacheBasedTicketCache);
		return provider;
	}

	@Bean
	public SpringCacheBasedTicketCache springCacheBasedTicketCache(CacheManager cacheManager) {
		return new SpringCacheBasedTicketCache(cacheManager.getCache("castickets"));
	}

	@Bean
	public Cas30ProxyTicketValidator cas30ServiceTicketValidator(ProxyGrantingTicketStorage pgtStorage) {
		Cas30ProxyTicketValidator tv = new Cas30ProxyTicketValidator(this.casBaseUrl);
		tv.setProxyCallbackUrl(calendarServiceProxyCallbackUrl);
		tv.setProxyGrantingTicketStorage(pgtStorage);
		tv.setAcceptAnyProxy(true);
		return tv;
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(casLogoutUrl, new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl("/logout/cas");
		return logoutFilter;
	}

	@Bean
	public ProxyGrantingTicketStorage pgtStorage() {
		return new ProxyGrantingTicketStorageImpl();
	}

	@Scheduled(fixedRate = 300_000)
	public void proxyGrantingTicketStorageCleaner() {
		logger.info("Running ProxyGrantingTicketStorage#cleanup() at {}",
				LocalDateTime.now());
		pgtStorage().cleanUp();
	}

	@Bean
	public AuthenticationUserDetailsService userDetailsService() {
		return new GrantedAuthorityFromAssertionAttributesUserDetailsService(new String[] { "role" });
	}

}
