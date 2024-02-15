package com.packtpub.springsecurity.intercept;

/**
 *
 */

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * FilterInvocationServiceSecurityMetadataSource
 * Requires {@link FilterInvocation}
 */
@Component
public class FilterInvocationServiceSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private FilterInvocationSecurityMetadataSource delegate;

	private final RequestConfigMappingService requestConfigMappingService;

	public FilterInvocationServiceSecurityMetadataSource(RequestConfigMappingService filterInvocationService) {
		this.requestConfigMappingService = filterInvocationService;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return this.delegate.getAllConfigAttributes();
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		if (delegate == null)
			getDelegate();
		return this.delegate.getAttributes(object);
	}

	public boolean supports(Class<?> clazz) {
		return this.delegate.supports(clazz);
	}

	public void getDelegate() {
		List<RequestConfigMapping> requestConfigMappings = requestConfigMappingService.getRequestConfigMappings();
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>(requestConfigMappings.size());
		for (RequestConfigMapping requestConfigMapping : requestConfigMappings) {
			RequestMatcher matcher = requestConfigMapping.getMatcher();
			requestMap.put(matcher, requestConfigMapping.getAttributes());
		}
		this.delegate = new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, new DefaultWebSecurityExpressionHandler());
	}
}