package com.packtpub.springsecurity.core.authority;

import java.util.Collection;
import java.util.List;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * A utility class used for creating the {@link GrantedAuthority}'s given a {@link CalendarUser}. In a real solution
 * this would be looked up in the existing system, but for simplicity our original system had no notion of authorities.
 *
 *  @author bnasslahsen
 */
public final class CalendarUserAuthorityUtils {
	/**
	 * The constant ADMIN_ROLES.
	 */
	private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN",
			"ROLE_USER");

	/**
	 * The constant USER_ROLES.
	 */
	private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

	/**
	 * Instantiates a new Calendar user authority utils.
	 */
	private CalendarUserAuthorityUtils() {
	}

	/**
	 * Create authorities collection.
	 *
	 * @param calendarUser the calendar user
	 * @return the collection
	 */
	public static Collection<? extends GrantedAuthority> createAuthorities(CalendarUser calendarUser) {
		String username = calendarUser.getEmail();
		if (username.startsWith("admin")) {
			return ADMIN_ROLES;
		}
		return USER_ROLES;
	}

}
