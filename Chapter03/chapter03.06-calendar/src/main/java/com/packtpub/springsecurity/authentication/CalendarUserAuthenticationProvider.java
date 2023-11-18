package com.packtpub.springsecurity.authentication;

import java.util.Collection;

import com.packtpub.springsecurity.core.authority.CalendarUserAuthorityUtils;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * A Spring Security {@link AuthenticationProvider} that uses our {@link CalendarService} for authentication. Compare
 * this to our  CalendarUserDetailsService which is called by Spring Security's {@link DaoAuthenticationProvider}.
 *
 *  @author bnasslahsen
 */
@Component
public class CalendarUserAuthenticationProvider implements AuthenticationProvider {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CalendarUserAuthenticationProvider.class);

	/**
	 * The Calendar service.
	 */
	private final CalendarService calendarService;

	/**
	 * Instantiates a new Calendar user authentication provider.
	 *
	 * @param calendarService the calendar service
	 */
	@Autowired
	public CalendarUserAuthenticationProvider(CalendarService calendarService) {
		if (calendarService == null) {
			throw new IllegalArgumentException("calendarService cannot be null");
		}
		this.calendarService = calendarService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		DomainUsernamePasswordAuthenticationToken token = (DomainUsernamePasswordAuthenticationToken) authentication;
		String userName = token.getName();
		String domain = token.getDomain();
		String email = userName + "@" + domain;

		CalendarUser user = calendarService.findUserByEmail(email);
		logger.info("calendarUser: {}", user);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username/password");
		}
		String password = user.getPassword();
		if (!password.equals(token.getCredentials())) {
			throw new BadCredentialsException("Invalid username/password");
		}
		Collection<? extends GrantedAuthority> authorities = CalendarUserAuthorityUtils.createAuthorities(user);
		logger.info("authorities: {}", authorities);
		return new DomainUsernamePasswordAuthenticationToken(user, password, domain, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return DomainUsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
