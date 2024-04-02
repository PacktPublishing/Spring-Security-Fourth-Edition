package com.packtpub.springsecurity.service;


import java.util.Collection;

import com.packtpub.springsecurity.core.authority.CalendarUserAuthorityUtils;
import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * There are advantages to creating a class that extends {@link CalendarUser}, our domain notion of a user, and
 * implements {@link UserDetails}, Spring Security's notion of a user.
 * <ul>
 * <li>First we can obtain all the custom information in the {@link CalendarUser}</li>
 * <li>Second, we can use this service to integrate with Spring Security in other ways (i.e. when implementing
 * Spring Security's <a
 * href="http://static.springsource.org/spring-security/site/docs/current/reference/remember-me.html">Remember-Me
 * Authentication</a></li>
 * </ul>
 *
 * @author bnasslahsen
 *
 */
public class CalendarUserDetails extends CalendarUser implements UserDetails {
	CalendarUserDetails(CalendarUser user) {
		setId(user.getId());
		setEmail(user.getEmail());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setPassword(user.getPassword());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return CalendarUserAuthorityUtils.createAuthorities(this);
	}

	public boolean hasAdminRole() {
		return getUsername().startsWith("admin");
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static final long serialVersionUID = 3384436451564509032L;
}