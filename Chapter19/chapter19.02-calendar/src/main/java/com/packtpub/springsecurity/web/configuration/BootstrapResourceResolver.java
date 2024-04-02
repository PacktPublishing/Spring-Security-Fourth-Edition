package com.packtpub.springsecurity.web.configuration;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * The type Web jars version resource resolver.
 */
@Component
public class BootstrapResourceResolver implements ResourceResolver {

	private static final String WEBJARS_PREFIX = "/webjars/";
	private static final String BOOTSTRAP_PATH = "bootstrap/5.3.2/css/bootstrap.min.css";
	private static final String REQUESTED_CSS_PATH = "bootstrap/css/bootstrap.min.css";

	@Override
	public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		if (REQUESTED_CSS_PATH.equals(requestPath)) {
			return chain.resolveResource(request, BOOTSTRAP_PATH, locations);
		}
		return null;
	}

	@Override
	public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
		if (resourcePath.startsWith(WEBJARS_PREFIX)) {
			return chain.resolveUrlPath(resourcePath, locations);
		}
		return null;
	}
}