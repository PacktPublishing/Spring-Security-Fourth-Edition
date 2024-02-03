package com.packtpub.springsecurity.service;

import java.util.Collection;

import com.packtpub.springsecurity.core.authority.CalendarUserAuthorityUtils;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.repository.CalendarUserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final CalendarUserRepository userRepository;

	public UserDetailsServiceImpl(CalendarUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		CalendarUser user = userRepository.findByEmail(username);
		if (user == null) throw new UsernameNotFoundException(username);
		return new CalendarUserDetails(user);
	}

	private final class CalendarUserDetails extends CalendarUser implements UserDetails {

		private static final long serialVersionUID = 3384436451564509032L;

		CalendarUserDetails(CalendarUser user) {
			setId(user.getId()); setEmail(user.getEmail());
			setFirstName(user.getFirstName()); setLastName(user.getLastName());
			setPassword(user.getPassword());
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return CalendarUserAuthorityUtils.createAuthorities(this);
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

	}
}
