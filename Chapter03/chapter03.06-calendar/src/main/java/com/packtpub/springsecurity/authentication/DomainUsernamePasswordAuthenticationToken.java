package com.packtpub.springsecurity.authentication;

import java.util.Collection;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The DomainUsernamePasswordAuthenticationToken class
 *
 * @author mickknutson
 * @since chapter03.06 Created Class
 */
public final class DomainUsernamePasswordAuthenticationToken extends
		UsernamePasswordAuthenticationToken {
	/**
	 * The Domain.
	 */
	private final String domain;

	/**
	 * Instantiates a new Domain username password authentication token.
	 *
	 * @param principal   the principal
	 * @param credentials the credentials
	 * @param domain      the domain
	 */
// used for attempting authentication
	public DomainUsernamePasswordAuthenticationToken(String
			principal, String credentials, String domain) {
		super(principal, credentials);
		this.domain = domain;
	}

	/**
	 * Instantiates a new Domain username password authentication token.
	 *
	 * @param principal   the principal
	 * @param credentials the credentials
	 * @param domain      the domain
	 * @param authorities the authorities
	 */
	// used for returning to Spring Security after being authenticated
	public DomainUsernamePasswordAuthenticationToken(CalendarUser
			principal, String credentials, String domain,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.domain = domain;
	}

	/**
	 * Gets domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
}