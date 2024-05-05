package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

@Component
public class SpringSecurityUserContext implements UserContext {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityUserContext.class);

	private final CalendarService calendarService;

	private final ReactiveUserDetailsService userDetailsService;

	public SpringSecurityUserContext(CalendarService calendarService, ReactiveUserDetailsService userDetailsService) {
		if (calendarService == null) {
			throw new IllegalArgumentException("calendarService cannot be null");
		}
		if (userDetailsService == null) {
			throw new IllegalArgumentException("userDetailsService cannot be null");
		}
		this.calendarService = calendarService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Mono<CalendarUser> getCurrentUser() {
		return ReactiveSecurityContextHolder.getContext()
				.map(context -> context.getAuthentication())
				.flatMap(authentication -> {
					if (authentication == null) {
						return Mono.empty();
					}
					String email = authentication.getName();
					if (email == null) {
						return Mono.empty();
					}
					return calendarService.findUserByEmail(email);
				})
				.switchIfEmpty(Mono.error(new IllegalStateException("Spring Security is not in sync with CalendarUsers")));
	}

	@Override
	public Mono<Void> setCurrentUser(CalendarUser user, WebSession session) {
		if (user == null) {
			return Mono.error(new IllegalArgumentException("user cannot be null"));
		}

		userDetailsService.findByUsername(user.getEmail())
				.switchIfEmpty(Mono.error(new IllegalStateException("User not found")))
				.map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()))
				.doOnNext(authentication -> {
					SecurityContextImpl securityContext = new SecurityContextImpl();
					securityContext.setAuthentication(authentication);
					session.getAttributes()
							.put(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME,
									securityContext);
				}).subscribe();

		return Mono.empty();
	}

}