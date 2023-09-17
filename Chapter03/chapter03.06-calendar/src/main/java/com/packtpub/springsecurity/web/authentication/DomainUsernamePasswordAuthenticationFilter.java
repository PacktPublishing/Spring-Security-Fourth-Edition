package com.packtpub.springsecurity.web.authentication;


import com.packtpub.springsecurity.authentication.DomainUsernamePasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * An extension to the existing {@link UsernamePasswordAuthenticationFilter} that
 * obtains a domain parameter and then creates a {@link DomainUsernamePasswordAuthenticationToken}.
 *
 * @author mickknutson
 * @since chapter03.06 Created Class
 */
public final class DomainUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	/**
	 * Instantiates a new Domain username password authentication filter.
	 *
	 * @param authenticationManager the authentication manager
	 */
	public DomainUsernamePasswordAuthenticationFilter(final AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	public Authentication attemptAuthentication
			(HttpServletRequest request, HttpServletResponse response) throws
			AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException
					("Authentication method not supported: "
							+ request.getMethod());
		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		String domain = request.getParameter("domain");
		// authRequest.isAuthenticated() = false since no
		//authorities are specified
		DomainUsernamePasswordAuthenticationToken authRequest
				= new DomainUsernamePasswordAuthenticationToken(username,
				password, domain);
		setDetails(request, authRequest);
		return this.getAuthenticationManager()
				.authenticate(authRequest);
	}
}